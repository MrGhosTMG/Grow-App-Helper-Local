package org.jdta.growapp;


import javafx.application.Application;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;


public class StartApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        Model.getInstance().getView().showLoginWindow();

    }
}
