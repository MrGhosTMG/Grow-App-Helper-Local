package org.jdta.growapp.Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Service.UserService;
import org.jdta.growapp.StartApp;
import org.jdta.growapp.Utils.FXMLUtils;
import org.jdta.growapp.Utils.StageActions;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.PreferencesUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {
    private final UserService userService = new UserService(Model.getInstance().getUserDAO());

    public TextField user_log_fld;
    public PasswordField pass_fld;
    public CheckBox stay_in_check;
    public Button enter_button;
    public Label err_lbl;
    public Button reg_btn;
    public Button exit_btn;
    public AnchorPane login_anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enter_button.setOnAction(actionEvent -> onLogin());
        reg_btn.setOnAction(actionEvent -> onReg());
        exit_btn.setOnAction(actionEvent -> StageActions.onExit(getStage()));

        // Подгрузим сохранённый логин (если есть)
        String savedUsername = PreferencesUtils.getSavedUsername();
        if (!savedUsername.isEmpty()) {
            user_log_fld.setText(savedUsername);
            stay_in_check.setSelected(true);
        }
        initializeWindowPosition();
    }

    private void initializeWindowPosition() {
        Platform.runLater(() -> {
            Stage stage = (Stage) login_anchor.getScene().getWindow();
            double[] pos = PreferencesUtils.getWindowPosition(this.getClass().getSimpleName());
            if (pos != null) {
                stage.setX(pos[0]);
                stage.setY(pos[1]);
            }
            stage.setOnCloseRequest(e -> {
                PreferencesUtils.saveWindowPosition(this.getClass().getSimpleName(), stage.getX(), stage.getY());
            });
        });
    }

    private void onLogin() {
        String username = user_log_fld.getText().trim();
        String password = pass_fld.getText().trim();

        err_lbl.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            err_lbl.setText("Enter your username and password");
            DialogUtils.hideErrorMessage(err_lbl, 5);
            return;
        }

        try {
            User user = userService.login(username, password);
            if (user != null) {
                Model.getInstance().setCurrentUser(user);

                if (stay_in_check.isSelected()) {
                    PreferencesUtils.saveUser(user.getId(), user.getUsername());
                } else {
                    PreferencesUtils.clearUser();
                }

                Stage stage = FXMLUtils.getCurrentStage();
                Model.getInstance().getView().showUserWindow();
                Model.getInstance().getView().closeStage(stage);
            } else {
                err_lbl.setText("Incorrect login or password");
                DialogUtils.hideErrorMessage(err_lbl, 5);
            }
        } catch (Exception e) {
            DialogUtils.error("Login Error", "An error occurred while trying to log in.");
            e.printStackTrace();
        }
    }


    private void onReg() {
        Stage stage = FXMLUtils.getCurrentStage();
        Model.getInstance().getView().showRegWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    private Stage getStage() {
        return FXMLUtils.stageFrom(exit_btn); // можно использовать любой доступный Node
    }
}
