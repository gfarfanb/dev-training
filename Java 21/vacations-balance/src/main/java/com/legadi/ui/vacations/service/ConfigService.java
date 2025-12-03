package com.legadi.ui.vacations.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legadi.ui.vacations.common.CellRef;
import com.legadi.ui.vacations.common.ErrorMessage;

@Service
public class ConfigService {

    private final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String DEFAULT_SETTINGS = "settings.json";

    private final Map<String, Object> defaultProperties;
    private final Map<String, Object> configProperties;

    private final String configLocation;
    private final AlertService alertService;
    private final ErrorMessage errorMessage;

    public ConfigService(
            @Value("${app.config.location}") String configLocation,
            AlertService alertService,
            ErrorMessage errorMessage) {
        this.defaultProperties = loadJsonInternal(DEFAULT_SETTINGS);
        this.configProperties = loadInitialConfig(configLocation, this.defaultProperties);
        this.configLocation = configLocation;
        this.alertService = alertService;
        this.errorMessage = errorMessage;
    }

    private Map<String, Object> loadInitialConfig(String configLocation, Map<String, Object> defaultProperties) {
        File configFile = new File(configLocation);
        if(configFile.exists()) {
            return loadJsonFile(configLocation);
        }

        writeJsonFile(configLocation, defaultProperties);
        return new HashMap<>(defaultProperties);
    }

    private Map<String, Object> loadJsonInternal(String location) {
        try {
            logger.info("Loading internal JSON file: {}", location);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream jsonInputStream = classLoader.getResource(location).openStream();

            try(Reader reader = new InputStreamReader(jsonInputStream)) {
                return OBJECT_MAPPER.readValue(jsonInputStream, new TypeReference<>() {});
            }
        } catch(Exception ex) {
            String message = String.format("%s: %s", errorMessage.getLoadJsonInternal(), location);
            logger.error(message, ex);
            return alertService.error(null, message);
        }
    }

    private Map<String, Object> loadJsonFile(String location) {
        try(Reader reader = Files.newBufferedReader(Paths.get(location))) {
            logger.info("Loading JSON file: {}", location);
            return OBJECT_MAPPER.readValue(reader, new TypeReference<>() {});
        } catch(Exception ex) {
            String message = String.format("%s: %s", errorMessage.getLoadJsonFile(), location);
            logger.error(message, ex);
            return alertService.error(null, message);
        }
    }

    private void writeJsonFile(String location, Object content) {
        try(Writer writer = Files.newBufferedWriter(getPathAndCreateDirs(location))) {
            OBJECT_MAPPER.writeValue(writer, content);
            logger.info("JSON file saved: {}", location);
        } catch(IOException ex) {
            String message = String.format("%s: %s", errorMessage.getWriteJsonFile(), location);
            logger.error(message, ex);
            alertService.error(null, message);
        }
    }

    private Path getPathAndCreateDirs(String location) {
        try {
            Path filePath = Paths.get(location);
            Files.createDirectories(filePath.getParent());
            return filePath;
        } catch(IOException ex) {
            String message = String.format("%s: %s", errorMessage.getCreateDirs(), location);
            logger.error(message, ex);
            return alertService.error(null, message);
        }
    }

    public void override(String property, Object value) {
        override(property, value, configProperties.get(property));
        writeJsonFile(configLocation, configProperties);
    }

    public void override(Map<String, Object> values) {
        values.forEach((k, v) -> override(k, v, configProperties.get(k)));
        writeJsonFile(configLocation, configProperties);
    }

    public void override(String property, Object value, Object previous) {
        if(previous == null || value == null) {
            configProperties.put(property, value);
            return;
        }
        if(previous.getClass() == value.getClass()) {
            configProperties.put(property, value);
            return;
        }
        if(previous instanceof Boolean) {
            configProperties.put(property, Boolean.parseBoolean(value.toString()));
            return;
        }
        try {
            if(previous instanceof Integer) {
                configProperties.put(property, Integer.parseInt(value.toString()));
                return;
            }
            if(previous instanceof Double) {
                configProperties.put(property, Double.parseDouble(value.toString()));
                return;
            }
        } catch(Exception ex) {
            String message = String.format("%s [%s]: %s", errorMessage.getInvalidPropertyData(), property, value);
            alertService.warn(null, message);
            return;
        }
        if(previous instanceof String) {
            configProperties.put(property, value.toString());
        }
    }

    public String get(String property) {
        return getValue(property, v -> v instanceof String);
    }

    public int getInt(String property) {
        return getValue(property, v -> v instanceof Integer);
    }

    public boolean getBoolean(String property) {
        return getValue(property, v -> v instanceof Boolean);
    }

    public CellRef getCell(String property) {
        return new CellRef(get(property));
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(String propertyName, Predicate<Object> typeValidation) {
        Object value = configProperties.getOrDefault(propertyName, defaultProperties.get(propertyName));
        if(value == null) {
            String message = String.format("%s: %s", errorMessage.getPropertyNotFound(), propertyName);
            return alertService.error(null, message);
        }
        if(typeValidation.test(value)) {
            return (T) value;
        } else {
            String message = String.format("%s: [%s] %s", errorMessage.getInvalidDataType(),
                (value != null ? value.getClass() : "null"), value);
            return alertService.error(null, message);
        }
    }
}
