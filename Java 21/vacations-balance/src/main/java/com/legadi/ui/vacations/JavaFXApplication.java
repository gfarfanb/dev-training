package com.legadi.ui.vacations;

import java.net.URL;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
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
        this.context = new SpringApplicationBuilder().sources(VacationsBalanceApplication.class)
            .initializers(initializer)
            .build()
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(this.context != null) {
            this.context.publishEvent(new StageReadyEvent(primaryStage));
        }

        URL fxmlUrl = Thread.currentThread().getContextClassLoader().getResource("vacations-ui.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        VBox page = (VBox) loader.load();
        Scene scene = new Scene(page);

        primaryStage.setTitle("Balance de vacaciones");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if(this.context != null) {
            this.context.close();
        }
        Platform.exit();
    }

    class StageReadyEvent extends ApplicationEvent {

        public Stage getStage() {
            return Stage.class.cast(getSource());
        }

        public StageReadyEvent(Object source) {
            super(source);
        }
    }
}
