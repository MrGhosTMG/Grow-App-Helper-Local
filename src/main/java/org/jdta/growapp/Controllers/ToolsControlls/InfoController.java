package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    public AnchorPane parent_anchor;
    public Label sort_name;
    public Label start_date_lbl;
    public Label ETA_date_lbl;
    public Label grow_stage_lbl;
    public ImageView image_cycle;
    public Label light_on_hour;
    public Label light_of_hour;
    public Label light_stage;
    public Label moist_date;
    public Label nutr_lbl;
    public Label clean_lbl;
    public Label moist_remind_lbl;
    public Label training_lbl;
    public Label training_remind_lbl;
    public ScrollPane note_scrl;
    public TextArea note_txt_area;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
