package org.jdta.growapp.Utils;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoDialogController implements Initializable {
    public Label info_lbl;
    public Label msg_lbl;
    public Button ok_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ok_btn.setOnAction(event -> ((Stage) ok_btn.getScene().getWindow()).close());
    }

    public void setMessage(String title, String message) {
        info_lbl.setText(title);
        msg_lbl.setText(message);
    }
}
