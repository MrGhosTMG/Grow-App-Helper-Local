package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {


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
}
