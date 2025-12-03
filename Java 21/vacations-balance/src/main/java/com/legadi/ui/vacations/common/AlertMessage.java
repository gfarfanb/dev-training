package com.legadi.ui.vacations.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "alert.messages")
@Getter @Setter
public class AlertMessage {

    private String loadJsonInternal;
    private String loadJsonFile;
    private String writeJsonFile;
    private String createDirs;
    private String propertyNotFound;
    private String invalidDataType;
    private String readBalanceFile;
    private String writeBalanceFile;
    private String balanceNotFound;
    private String employeeSaved;
}
