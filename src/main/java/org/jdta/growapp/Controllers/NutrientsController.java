package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NutrientsController implements Initializable {

    private Runnable onSaveCallback;
    private Runnable onAddCallback;

    public AnchorPane parent_nutr;
    public Label header_lbl;
    public TextField nut_name_fld;
    public ChoiceBox choise_nutr;
    public TextArea nutr_add_desc_text;
    public ListView desc_picked_nut_text;
    public Button save_to_list_btn;
    public Button add_nut_in_cycle_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Подгрузка данных
        save_to_list_btn.setOnAction(actionEvent -> onSetClicked());
        add_nut_in_cycle_btn.setOnAction(actionEvent -> onAddClicked());
    }

    public void setOnSave(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public void setOnSet(Runnable callback) {
        this.onAddCallback = callback;
    }

    private void onSetClicked() {
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
        ((Stage) save_to_list_btn.getScene().getWindow()).close();
    }

    private void onAddClicked() {
        if (onAddCallback != null) {
            onAddCallback.run();
        }
        ((Stage) add_nut_in_cycle_btn.getScene().getWindow()).close();
    }
}
