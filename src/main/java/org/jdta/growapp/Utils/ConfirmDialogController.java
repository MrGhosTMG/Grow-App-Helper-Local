package org.jdta.growapp.Utils;


import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmDialogController implements Initializable {


    public BorderPane parent_bord_pane;
    public HBox btn_box;
    public Button ok_btn;
    public Button cancel_btn;
    public Label alert_lbl;
    public ImageView icon_view;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
