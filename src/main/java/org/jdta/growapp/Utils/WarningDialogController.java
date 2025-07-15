package org.jdta.growapp.Utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WarningDialogController {

    public Label title_lbl;
    public Label warn_msg;
    public Button ok_btn;
    public AnchorPane parent_anchor;

    public void initialize() {
        ok_btn.setOnAction(event -> ((Stage) ok_btn.getScene().getWindow()).close());
    }

    public void setMessage(String title, String message) {
        title_lbl.setText(title);
        warn_msg.setText(message);
    }
}
