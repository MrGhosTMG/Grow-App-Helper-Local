package org.jdta.growapp.Utils;

import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;


public class StageActions {


    public static void onLogout(Stage stage) {
        DialogUtils.confirm(stage," Logout ? Come back soon :)", () -> {
            Model.getInstance().getView().showLoginWindow();
            stage.close();
        });
    }

    public static void onExit(Stage stage) {
        DialogUtils.confirm(stage, "It is 4:20 ? Want to exit?", stage::close);
    }

    public static void backToUser(Stage stage) {
        Model.getInstance().getView().showUserWindow();
        stage.close();
    }
}
