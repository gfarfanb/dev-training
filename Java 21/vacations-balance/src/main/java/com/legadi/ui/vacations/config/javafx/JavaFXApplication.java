package com.legadi.ui.vacations.config.javafx;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.legadi.ui.vacations.VacationsBalanceApplication;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
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
            .sources(VacationsBalanceApplication.class)
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
}
