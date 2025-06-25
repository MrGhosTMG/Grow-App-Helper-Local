package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Service.UserService;
import org.jdta.growapp.Utils.DialogUtils;

import java.net.URL;
import java.time.LocalDate;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(actionEvent -> onLogin());
        exit_btn.setOnAction(actionEvent -> onExit());
        reg_btn.setOnAction(actionEvent -> onRegister());
    }

    private void onRegister() {
        clearLabels(); // Очистка старых сообщений

        String email = e_mail_fld.getText().trim();
        String username = user_name_fld.getText().trim();
        String password = password_fld.getText();
        String confirmPassword = password_check_fld.getText();

        boolean hasError = false;

        if (!isValidEmail(email)) {
            mail_lbl.setText(" Введите корректный email");
            hasError = true;
        }

        if (username.length() < 3) {
            user_lbl.setText(" Имя пользователя от 3 символов");
            hasError = true;
        }

        if (password.length() < 8) {
            pass_lbl.setText(" Пароль от 8 символов");
            hasError = true;
        }

        if (!password.equals(confirmPassword)) {
            pass_check_lbl.setText(" Пароли не совпадают");
            hasError = true;
        }

        if (!accept_terms_ch_box.isSelected()) {
            DialogUtils.warning("Внимание", "Вы должны принять условия использования.");
            hasError = true;
        }

        if (hasError) return;

        try {
            org.jdta.growapp.DTO.User user = new org.jdta.growapp.DTO.User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password); // можно захешировать позже
            user.setCreatedAt(java.time.LocalDate.now());

            userService.registerUser(user); //  метод должен проверить, не существует ли пользователь

            DialogUtils.info("Успешно", "Пользователь зарегистрирован!");
            onLogin(); // Вернуться к окну логина
        } catch (Exception e) {
            DialogUtils.error("Ошибка регистрации", "Такой пользователь уже существует или произошла ошибка.");
            e.printStackTrace();
        }
    }



// On actions section

    private void onLogin() {
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        //Model.getInstance().getView().showConfirmDialogWindow();
        Model.getInstance().getView().showLoginWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    private void onExit() {
//        Stage stage = (Stage) exit_btn.getScene().getWindow();
//        Model.getInstance().getView().closeStage(stage);
        DialogUtils.confirm("Do you really want to exit?", () -> {
            Stage stage = (Stage) exit_btn.getScene().getWindow();
            stage.close();
        });
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void onRegisterClicked() {
        String email = e_mail_fld.getText().trim();
        String username = user_name_fld.getText().trim();
        String password = password_fld.getText();
        String confirmPassword = password_check_fld.getText();

        mail_lbl.setText("");
        user_lbl.setText("");
        pass_lbl.setText("");
        pass_check_lbl.setText("");

        boolean hasError = false;

        if (email.isEmpty() || !isValidEmail(email)) {
            mail_lbl.setText(" Введите корректный email");
            hasError = true;
        }

        if (username.length() < 3) {
            user_lbl.setText(" Имя пользователя должно быть от 3 символов");
            hasError = true;
        }

        if (password.length() < 8) {
            pass_lbl.setText(" Пароль должен быть не менее 8 символов");
            hasError = true;
        }

        if (!password.equals(confirmPassword)) {
            pass_check_lbl.setText(" Пароли не совпадают");
            hasError = true;
        }

        if (!accept_terms_ch_box.isSelected()) {
            pass_check_lbl.setText("Примите условия");
            hasError = true;
        }

        if (hasError) return;

        // Если всё ок — вызываем сервис
        try {
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password); // TODO: Пароль лучше хешировать (SHA-256 например) — можно потом внедрить
            user.setCreatedAt(LocalDate.now());


            userService.registerUser(user);

            // показать сообщение, перейти к логину и т.д.
        } catch (Exception e) {
            user_lbl.setText(" Пользователь с таким email уже существует");
        }
    }

    private void clearLabels() {
        mail_lbl.setText("");
        user_lbl.setText("");
        pass_lbl.setText("");
        pass_check_lbl.setText("");
    }


}
