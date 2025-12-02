package com.legadi.ui.vacations.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "error.messages")
public class ErrorMessage {

    private String loadJsonInternal;
    private String loadJsonFile;
    private String writeJsonFile;
    private String createDirs;
    private String propertyNotFound;
    private String invalidDataType;
    private String readBalanceFile;

    public String getLoadJsonInternal() {
        return loadJsonInternal;
    }

    public void setLoadJsonInternal(String loadJsonInternal) {
        this.loadJsonInternal = loadJsonInternal;
    }

    public String getLoadJsonFile() {
        return loadJsonFile;
    }

    public void setLoadJsonFile(String loadJsonFile) {
        this.loadJsonFile = loadJsonFile;
    }

    public String getWriteJsonFile() {
        return writeJsonFile;
    }

    public void setWriteJsonFile(String writeJsonFile) {
        this.writeJsonFile = writeJsonFile;
    }

    public String getCreateDirs() {
        return createDirs;
    }

    public void setCreateDirs(String createDirs) {
        this.createDirs = createDirs;
    }

    public String getPropertyNotFound() {
        return propertyNotFound;
    }

    public void setPropertyNotFound(String propertyNotFound) {
        this.propertyNotFound = propertyNotFound;
    }

    public String getInvalidDataType() {
        return invalidDataType;
    }

    public void setInvalidDataType(String invalidDataType) {
        this.invalidDataType = invalidDataType;
    }

    public String getReadBalanceFile() {
        return readBalanceFile;
    }

    public void setReadBalanceFile(String readBalanceFile) {
        this.readBalanceFile = readBalanceFile;
    }
}
