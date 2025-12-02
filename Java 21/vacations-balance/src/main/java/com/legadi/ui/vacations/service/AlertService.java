package com.legadi.ui.vacations.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javafx.application.Platform;
import javafx.scene.control.Alert;

@Service
public class AlertService {

    @Value("${vacations.application.ui.title}")
    private String title;

    public void info(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public void warn(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public <T> T error(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        alert.show();
        return null;
    }
}
