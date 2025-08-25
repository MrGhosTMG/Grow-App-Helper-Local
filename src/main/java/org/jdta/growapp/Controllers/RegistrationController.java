package org.jdta.growapp.Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Service.UserService;
import org.jdta.growapp.Utils.PreferencesUtils;
import org.jdta.growapp.Utils.StageActions;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    private final UserService userService = new UserService(Model.getInstance().getUserDAO());


    public TextField e_mail_fld;
    public Label mail_lbl;
    public TextField user_name_fld;
    public Label user_lbl;
    public PasswordField password_fld;
    public Label pass_lbl;
    public PasswordField password_check_fld;
    public Label pass_check_lbl;
    public ScrollPane scrll_pane;
    public CheckBox accept_terms_ch_box;
    public Button reg_btn;
    public Button login_btn;
    public Button exit_btn;
    public ScrollPane scrl_pane;
    public AnchorPane text_anchor_pane;
    public TextArea text_area_terms;
    public AnchorPane reg_anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(actionEvent -> onLogin());
        exit_btn.setOnAction(actionEvent -> StageActions.onExit(getStage()));
        reg_btn.setOnAction(actionEvent -> onRegister());
        initializeWindowPosition();
    }

    private void initializeWindowPosition() {
        Platform.runLater(() -> {
            Stage stage = (Stage) reg_anchor.getScene().getWindow();
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
    private Stage getStage() {
        return FXMLUtils.stageFrom(exit_btn); // можно использовать любой доступный Node
    }

    // On actions section
    private void onRegister() {
        clearLabels(); // Очистка старых сообщений

        String email = e_mail_fld.getText().trim();
        String username = user_name_fld.getText().trim();
        String password = password_fld.getText();
        String confirmPassword = password_check_fld.getText();

        boolean hasError = false;

        if (!isValidEmail(email)) {
            DialogUtils.setErrorMessage(mail_lbl," Incorrect email form");
            hasError = true;
        }

        if (username.length() < 3) {
            DialogUtils.setErrorMessage(user_lbl," user name must be longer than 3 symbols");
            hasError = true;
        }

        if (!isValidPassword(password)) {
            DialogUtils.setErrorMessage(pass_lbl,"minimum 1 digit 1 special from [@#$%^&+=!?*()_-] 1 from [a-z] + [A-Z]");
            hasError = true;
        }

        if (!password.equals(confirmPassword)) {
            DialogUtils.setErrorMessage(pass_check_lbl, " Passwords do not match");
            hasError = true;
        }

        if (!accept_terms_ch_box.isSelected()) {
            DialogUtils.setErrorMessage(pass_check_lbl,"Accept terms for complete registration");
            hasError = true;
        }

        if (hasError) {
            if (!accept_terms_ch_box.isSelected()) {
                DialogUtils.warning("Oops!", "You forgot to fill in all fields!");

            }
            return;
        }

        try {
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setCreatedAt(LocalDateTime.now());

            userService.registerUser(user); //  метод должен проверить, не существует ли пользователь

            DialogUtils.info("success", "New user created!");

            // Вернуться к окну логина
            onLogin();

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Email")) {
                DialogUtils.setErrorMessage(mail_lbl," Email already used");
            } else if (e.getMessage().contains("User name")) {
                DialogUtils.setErrorMessage(user_lbl," Name already used");
            } else {
                DialogUtils.error("Registration Error", e.getMessage());
            }
        } catch (Exception e) {
            DialogUtils.error("Registration Error", "Try to restart application and try again");
            e.printStackTrace();
        }
    }

    private void onLogin() {
        Stage stage = FXMLUtils.stageFrom(exit_btn);
        Model.getInstance().getView().showLoginWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    //Fields service tools
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=\\S*[0-9])(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[@#$%^&+=!?*()_-])\\S{8,}$");
    }

    private void clearLabels() {
        mail_lbl.setText("");
        user_lbl.setText("");
        pass_lbl.setText("");
        pass_check_lbl.setText("");
    }

}
