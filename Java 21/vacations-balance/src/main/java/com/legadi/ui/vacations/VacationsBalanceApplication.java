package com.legadi.ui.vacations;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// @SpringBootApplication
public class VacationsBalanceApplication extends Application {

    // @Override
    // public void init() throws Exception {
    //     SpringApplicationBuilder builder = new SpringApplicationBuilder(VacationsBalanceApplication.class);
    //     builder.application().setWebApplicationType(WebApplicationType.NONE);
    // }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            VacationsBalanceApplication.class.getResource("vacations-ui.fxml"));
        VBox page = (VBox) loader.load();
        Scene scene = new Scene(page);

        primaryStage.setTitle("Balance de vacaciones");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
