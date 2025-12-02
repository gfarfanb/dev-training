package com.legadi.ui.vacations.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeYear extends Employee {

    protected Map<Integer, YearRecord> yearRecords;
}
