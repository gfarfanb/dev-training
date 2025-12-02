package com.legadi.ui.vacations.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Employee {

    private String companyName;
    private String name;
    private LocalDate startDate;
    private int previousVacationDays;
    private int balanceDays;
    private int ratioDays;
    private Map<Integer, Integer> allowedByYear = new HashMap<>();
    private Map<Integer, Integer> takenByYear = new HashMap<>();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getPreviousVacationDays() {
        return previousVacationDays;
    }

    public void setPreviousVacationDays(int previousVacationDays) {
        this.previousVacationDays = previousVacationDays;
    }

    public int getBalanceDays() {
        return balanceDays;
    }

    public void setBalanceDays(int balanceDays) {
        this.balanceDays = balanceDays;
    }

    public int getRatioDays() {
        return ratioDays;
    }

    public void setRatioDays(int ratioDays) {
        this.ratioDays = ratioDays;
    }

    public Map<Integer, Integer> getAllowedByYear() {
        return allowedByYear;
    }

    public void setAllowedByYear(Map<Integer, Integer> allowedByYear) {
        this.allowedByYear = allowedByYear;
    }

    public Map<Integer, Integer> getTakenByYear() {
        return takenByYear;
    }

    public void setTakenByYear(Map<Integer, Integer> takenByYear) {
        this.takenByYear = takenByYear;
    }
}
