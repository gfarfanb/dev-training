package com.legadi.ui.vacations.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeBalance extends EmployeeYear {

    private String companyName;
    private LocalDate startDate;
    private int previousVacationDays;
    private int balanceDays;
    private int ratioDays;
}
