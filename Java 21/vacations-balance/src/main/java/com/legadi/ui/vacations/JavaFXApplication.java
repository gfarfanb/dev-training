package com.legadi.ui.vacations;

import java.io.IOException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer = new ApplicationContextInitializer<>() {
            @Override
            public void initialize(GenericApplicationContext genericApplicationContext) {
                genericApplicationContext.registerBean(Application.class, () -> JavaFXApplication.this);
                genericApplicationContext.registerBean(Parameters.class, () -> getParameters());
                genericApplicationContext.registerBean(HostServices.class, () -> getHostServices());
            }
        };
        this.context = new SpringApplicationBuilder()
            .sources(MainApplication.class)
            .initializers(initializer)
            .build()
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(this.context != null) {
            this.context.publishEvent(new StageReadyEvent(primaryStage));
        } else {
            throw new IllegalStateException("[start] Spring context not initialized yet");
        }
    }

    @Override
    public void stop() throws Exception {
        if(this.context != null) {
            this.context.close();
        }
        Platform.exit();
    }

    public static class StageListener implements ApplicationListener<StageReadyEvent> {

        private final ApplicationContext applicationContext;
        private final Resource fxml;
        private final String applicationTitle;

        public StageListener(ApplicationContext applicationContext, Resource fxml, String applicationTitle) {
            this.applicationContext = applicationContext;
            this.fxml = fxml;
            this.applicationTitle = applicationTitle;
        }

        @Override
        public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
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

    public static class StageReadyEvent extends ApplicationEvent {

        public Stage getStage() {
            return Stage.class.cast(getSource());
        }

        public StageReadyEvent(Object source) {
            super(source);
        }
    }
}
