package com.legadi.ui.vacations;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageListener implements ApplicationListener<JavaFXApplication.StageReadyEvent> {

    private final ApplicationContext applicationContext;
    private final String applicationTitle;
    private final Resource fxml;

    public StageListener(ApplicationContext applicationContext,
            @Value("${spring.application.ui.resource}") Resource fxml,
            @Value("${spring.application.ui.title}") String applicationTitle) {
        this.applicationContext = applicationContext;
        this.fxml = fxml;
        this.applicationTitle = applicationTitle;
    }

    @Override
    public void onApplicationEvent(JavaFXApplication.StageReadyEvent stageReadyEvent) {
        try {
            Stage stage = stageReadyEvent.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(fxml.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load scene", ex);
        }
    }
}
