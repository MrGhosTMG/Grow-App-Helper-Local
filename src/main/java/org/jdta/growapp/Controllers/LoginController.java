package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField user_log_fld;
    public PasswordField pass_fld;
    public CheckBox stay_in_check;
    public Button enter_button;
    public Label err_lbl;
    public Button reg_btn;
    public Button exit_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enter_button.setOnAction(actionEvent -> onLogin());
        reg_btn.setOnAction(actionEvent -> onReg());
        exit_btn.setOnAction(actionEvent -> onExit());
    }


    // On actions section
    private void onReg() {
        Stage stage = (Stage) reg_btn.getScene().getWindow();
        Model.getInstance().getView().showRegWindow();
        Model.getInstance().getView().closeStage(stage);
    }


    private void onLogin() {
        Stage stage = (Stage) enter_button.getScene().getWindow();
        Model.getInstance().getView().showUserWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    private void onExit() {
        DialogUtils.confirm("Do you really want to exit?", () -> {
            Stage stage = (Stage) exit_btn.getScene().getWindow();
            stage.close();
        });

//        Stage stage = (Stage) exit_btn.getScene().getWindow();
//        Model.getInstance().getView().closeStage(stage);

    }
}
