package org.jdta.growapp.Utils;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorDialogController implements Initializable {
    
    public AnchorPane parent_anchor;
    public Label title_lbl;
    public Label error_lbl;
    public Button ok_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ok_btn.setOnAction(actionEvent -> StageActions.backToUser(getStage()));
    }
    private Stage getStage() {
        return FXMLUtils.stageFrom(ok_btn); // можно использовать любой доступный Node
    }
    public void setMessage(String title, String message) {
        title_lbl.setText(title);
        error_lbl.setText(message);
    }
}
