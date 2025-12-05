package com.legadi.ui.vacations.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "component.texts")
@Getter @Setter
public class ComponentText {

    private String employeeNameFirstCellHelp;
    private String totalTakenDaysColumnHelp;
    private String companyNameCellHelp;
    private String startDateCellHelp;
    private String balanceDaysCellHelp;
    private String previousVacationsDaysCellHelp;
    private String ratioDaysCellHelp;
    private String allowedDaysYearFirstCellHelp;
    private String allowedDaysValueColumnHelp;
    private String allowedDaysValueRowHelp;
    private String acceptButton;
}
