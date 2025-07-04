package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WateringController implements Initializable {
    public AnchorPane parent_anchorpane;
    public Label water_lbl;
    public TextField water_txt_fld;
    public TextField temp_txt_fld;
    public TextField humi_txt_fld;
    public TextField PH_txt_fld;
    public TextField TDS_txt_fld;
    public CheckBox check_water;
    public CheckBox check_clean;
    public TextField clean_name_fld;
    public TextField clean_capa_fld;
    public CheckBox check_hydro_aero;
    public ChoiceBox nutr_choice;
    public Button add_nutr_btn;
    public ListView nutr_added_list;
    public ScrollBar scrl_bar;
    public TextField nutr_capa_fld;
    public Button del_nutr_btn;
    public Button set_alarm_btn;
    public Label fill_all_fields_lbl;
    public Button apply_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
