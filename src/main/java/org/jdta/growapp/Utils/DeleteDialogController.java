package org.jdta.growapp.Utils;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteDialogController implements Initializable {
    public AnchorPane del_anchor;
    public Label sure_lbl;
    public Button del_btn;
    public Button cancel_btn;
    public ImageView del_img;
    private Runnable onDeleteConfirmed;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        del_btn.setOnAction(actionEvent -> {
            if (onDeleteConfirmed != null) onDeleteConfirmed.run();
            getStage().close();
        });
        cancel_btn.setOnAction(actionEvent ->  getStage().close());
    }

    private Stage getStage() {
        return FXMLUtils.stageFrom(del_btn); // можно использовать любой доступный Node
    }
    public void setMessage(String message) {
        sure_lbl.setText(message);
    }
    public void setOnDeleteConfirmed(Runnable action) {
        this.onDeleteConfirmed = action;
    }
}
