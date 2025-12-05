package com.legadi.ui.vacations.model;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode
public class YearRecord {

    private int year;
    @EqualsAndHashCode.Exclude
    private int allowedByYear;
    @EqualsAndHashCode.Exclude
    private int takenByYear;
    @EqualsAndHashCode.Exclude
    private LocalDate expiration;
}
