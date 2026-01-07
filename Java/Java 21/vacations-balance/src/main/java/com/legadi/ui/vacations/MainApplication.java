package com.legadi.ui.vacations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import com.legadi.ui.vacations.JavaFXApplication.StageListener;

import javafx.application.Application;

@SpringBootApplication
public class MainApplication {

    @Value("${vacations.application.ui.resource}")
    private Resource fxml;

    @Value("${vacations.application.ui.title}")
    private String applicationTitle;

    @Bean
    public StageListener stageListener(ApplicationContext context) {
        return new StageListener(context, fxml, applicationTitle);
    }

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
}
