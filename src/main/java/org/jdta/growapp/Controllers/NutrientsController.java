package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class NutrientsController implements Initializable {
    public AnchorPane parent_nutr;
    public Label header_lbl;
    public TextField nut_name_fld;
    public ChoiceBox choise_nutr;
    public TextArea nutr_add_desc_text;
    public ListView desc_picked_nut_text;
    public Button save_to_list_btn;
    public Button add_nut_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Подгрузка данных или изображений
    }
}
