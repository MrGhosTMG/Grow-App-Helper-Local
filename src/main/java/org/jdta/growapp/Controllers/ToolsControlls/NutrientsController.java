package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class NutrientsController implements Initializable {


    public CheckBox add_new_activ;
    public TextField N_fld;
    public TextField P_fld;
    public TextField K_fld;
    private Runnable onSaveCallback;
    private Runnable onAddCallback;

    public AnchorPane parent_nutr;
    public Label header_lbl;
    public TextField nut_name_fld;
    public ChoiceBox choose_nuts;
    public TextArea nutr_add_desc_text;
    public ListView desc_picked_nut_text;
    public Button save_to_list_btn;
    public Button add_nut_in_cycle_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Подгрузка данных
        save_to_list_btn.setOnAction(actionEvent -> onSaveClicked());
        add_nut_in_cycle_btn.setOnAction(actionEvent -> onAddClicked());
    }

    public void setOnSaveNutr(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public void setOnAddNutr(Runnable callback) {
        this.onAddCallback = callback;
    }

    // check actions
    private void onSaveClicked() {
        if (onSaveCallback != null) {
            onSaveCallback.run();
            System.out.println("SAVE new Nutrient button Action");
        }
        //((Stage) save_to_list_btn.getScene().getWindow()).close();
    }

    private void onAddClicked() {
        if (onAddCallback != null) {
            onAddCallback.run();
            System.out.println("Add to  button action");
        }
        //((Stage) add_nut_in_cycle_btn.getScene().getWindow()).close();
    }
}
