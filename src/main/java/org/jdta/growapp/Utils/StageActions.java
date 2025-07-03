package org.jdta.growapp.Utils;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class DTOUtils implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

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
