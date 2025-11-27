package com.legadi.ui.vacations;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.legadi.ui.vacations.config.javafx.JavaFXApplication;

import javafx.application.Application;

@SpringBootApplication
public class VacationsBalanceApplication {

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
}
