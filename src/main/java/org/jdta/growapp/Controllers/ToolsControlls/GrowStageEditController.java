package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jdta.growapp.Enums.GrowStages;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class GrowStageEditController implements Initializable {
    private final ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();
    public AnchorPane parent_anchor;
    public Label set_edit_error_lbl;
    public Label current_stage_name_days_lbl;
    public ChoiceBox<GrowStages> stage_select_choice_box;
    public Button set_stage;
    public Label start_stage_days_lbl;
    public Label germinated_days_lbl;
    public Label vegetation_days_lbl;
    public Label pre_flow_days_lbl;
    public Label flow_days_lbl;
    public CheckBox clean_check;
    public CheckBox harvest_check;
    public TextField days_grams_in_fld;
    public RadioButton Drying_rb;
    public RadioButton yield_rb;
    public Label drying_or_yield_lbl;
    public Label total_days_lbl;
    public Button add_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialGrowStages();
    }


    private void showInfoFinishedCycle() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/InfoFinishedCycle.fxml",
                (InfoFinishedCycle controller) -> {
                    // передаём туда данные при необходимости
                    controller.setDataFromCycle(Model.getInstance().getCurrentCycle());
                });

        if (root != null) {
            parent_anchor.getChildren().setAll(root);
        }
    }
    private void initialGrowStages() {
        if (stage_select_choice_box != null) {
            stage_select_choice_box.getItems().addAll(GrowStages.values());
            stage_select_choice_box.setOnAction(actionEvent -> {
                GrowStages picked = stage_select_choice_box.getValue();
                if (picked != null && !pickedStages.contains(picked)) {
                    pickedStages.add(picked);
                }
            });
        }

        if (stage_select_choice_box != null) {
            stage_select_choice_box.getItems().addAll(GrowStages.values());
        }

        if (add_btn != null) {
            add_btn.setOnAction(event -> {
                if (yield_rb != null && yield_rb.isSelected()) {
                    String input = days_grams_in_fld.getText();
                    if (input != null && !input.isEmpty()) {
                        Model.getInstance().getCurrentCycle().setYieldGrams(Integer.parseInt(input));
                        System.out.println("Yield set: " + input + " grams");
                    }
                }
            });
        }

        if (set_stage != null) {
            set_stage.setOnAction(event -> {
                if (yield_rb != null && yield_rb.isSelected() &&
                        days_grams_in_fld != null && days_grams_in_fld.getText().matches("\\d+")) {
                    showInfoFinishedCycle();
                } else {
                    DialogUtils.warning("Missing data", "Enter yield in grams to finish stage.");
                }
            });
        }
    }
}
