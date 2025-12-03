package com.legadi.ui.vacations.service;

import static com.legadi.ui.vacations.common.ConfigConstants.FILE_TO_ANALYZE_LOCATION;

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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legadi.ui.vacations.common.CellRef;
import com.legadi.ui.vacations.common.ErrorMessage;
import com.legadi.ui.vacations.common.functions.ParseFunction;
import com.legadi.ui.vacations.common.functions.ToBoolean;
import com.legadi.ui.vacations.common.functions.ToNumber;
import com.legadi.ui.vacations.common.functions.ToString;

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
            String message = String.format(errorMessage.getLoadJsonInternal(), location);
            logger.error(message, ex);
            return alertService.error(null, message);
        }
    }

    private Map<String, Object> loadJsonFile(String location) {
        try(Reader reader = Files.newBufferedReader(Paths.get(location))) {
            logger.info("Loading JSON file: {}", location);
            return OBJECT_MAPPER.readValue(reader, new TypeReference<>() {});
        } catch(Exception ex) {
            String message = String.format(errorMessage.getLoadJsonFile(), location);
            logger.error(message, ex);
            return alertService.error(null, message);
        }
    }

    private void writeJsonFile(String location, Object content) {
        try(Writer writer = Files.newBufferedWriter(getPathAndCreateDirs(location))) {
            OBJECT_MAPPER.writeValue(writer, content);
            logger.info("JSON file saved: {}", location);
        } catch(IOException ex) {
            String message = String.format(errorMessage.getWriteJsonFile(), location);
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
            String message = String.format(errorMessage.getCreateDirs(), location);
            logger.error(message, ex);
            return alertService.error(null, message);
        }
    }

    public void resetToDefaultConfig() {
        String balanceFile = get(FILE_TO_ANALYZE_LOCATION);
        try {
            Files.deleteIfExists(Paths.get(configLocation));

            Map<String, Object> properties = new HashMap<>(defaultProperties);
            properties.put(FILE_TO_ANALYZE_LOCATION, balanceFile);

            configProperties.clear();
            configProperties.putAll(properties);
            writeJsonFile(configLocation, configProperties);
        } catch(IOException ex) {
            logger.error("Unable to delete previous config - {}", ex.getMessage(), ex);
        }
    }

    public void override(String property, Object value) {
        convertAndOverride(property, value);
        writeJsonFile(configLocation, configProperties);
    }

    public void override(Map<String, Object> values) {
        values.forEach((k, v) -> convertAndOverride(k, v));
        writeJsonFile(configLocation, configProperties);
    }

    public void convertAndOverride(String property, Object value) {
        if(value == null) {
            configProperties.put(property, value);
            return;
        }

        Class<?> expectedType = expectedType(property);

        if(Integer.class == expectedType) {
            configProperties.put(property,
                Optional.ofNullable(new ToNumber().apply(value))
                    .map(Number::intValue)
                    .orElse(null)
            );
            return;
        }

        if(Boolean.class == expectedType) {
            configProperties.put(property, new ToBoolean().apply(value));
            return;
        }

        configProperties.put(property, new ToString().apply(value));
    }

    public String get(String property) {
        return getValue(property, new ToString());
    }

    public int getInt(String property) {
        return getValue(property, new ToNumber()).intValue();
    }

    public boolean getBoolean(String property) {
        return getValue(property, new ToBoolean());
    }

    public CellRef getCell(String property) {
        return new CellRef(get(property));
    }

    private <T> T getValue(String propertyName, ParseFunction<T> transformer) {
        Object value = configProperties.getOrDefault(propertyName, defaultProperties.get(propertyName));
        if(value == null) {
            return alertService.error(null,
                String.format(errorMessage.getPropertyNotFound(), propertyName));
        }
        try {
            T typedValue = transformer.apply(value);
            if(typedValue == null) {
                return alertService.error(null,
                    String.format(errorMessage.getInvalidDataType(), transformer.type(), value));
            }
            return typedValue;
        } catch(Exception ex) {
            return alertService.error(null,
                String.format(errorMessage.getInvalidDataType(), transformer.type(), value));
        }
    }

    private Class<?> expectedType(String property) {
        String definedType = configProperties
            .getOrDefault(property + ".class", String.class.getName())
            .toString();
        try {
            return Class.forName(definedType);
        } catch(ClassNotFoundException ex) {
            return String.class;
        }
    }
}
