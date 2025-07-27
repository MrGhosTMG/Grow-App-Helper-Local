package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Enums.GrowStages;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GrowStageEditController implements Initializable {
    private final ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();
    public AnchorPane parent_anchor;
    public ChoiceBox<GrowStages> stage_select_choice_box;
    public Button set_stage;
    public Button add_btn;
    public CheckBox clean_check;
    public CheckBox harvest_check;
    public TextField days_grams_in_fld;
    public RadioButton drying_rb;
    public RadioButton yield_rb;
    public Label set_edit_error_lbl;
    public Label current_stage_name_days_lbl;
    public Label drying_or_yield_lbl;
    public Label total_days_lbl;
    public Label start_stage_days_lbl;
    public Label germinated_days_lbl;
    public Label vegetation_days_lbl;
    public Label pre_flow_days_lbl;
    public Label flow_days_lbl;

    private Cycle cycle;

//    public Cycle getCycle() {
//        return cycle;
//    }
//
//    public void setCycle(Cycle cycle) {
//        this.cycle = cycle;
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //init(cycle);
    }

    public void init(Cycle cycle) {
        this.cycle = cycle;
        setupStageSelection();
        setupYieldAndDryingControls();
        updateStageDurationsUI();
    }

    private void setupStageSelection() {

        stage_select_choice_box.getItems().addAll(GrowStages.values());

        GrowStages selected = stage_select_choice_box.getValue();
        GrowStages current = cycle.getGrowStage();

        if (current == null) {
            DialogUtils.warning("Current stage", "Please set Grow stage.");
            return;
        }

        stage_select_choice_box.setOnAction(event -> {
            GrowStages nowSelected = stage_select_choice_box.getValue();

            if (nowSelected.ordinal() < current.ordinal()) {
                DialogUtils.warning("Error", "Cannot downgrade stage!");
                stage_select_choice_box.setValue(current);
                return;
            }

            pickedStages.add(selected);
        });

        set_stage.setOnAction(event -> {

            GrowStages nowSelected = stage_select_choice_box.getValue();


            if (nowSelected == null) {
                DialogUtils.warning("Current stage", "Please set Grow stage.");
                return;
            }


            if (nowSelected.ordinal() < current.ordinal()) {
                DialogUtils.warning("Blocked", "You can't go to earlier stage!");
                return;
            }

            cycle.setGrowStage(nowSelected);
            try {
                Model.getInstance().getCycleDAO().update(cycle);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error with updating cycle Database");
            }

            if (nowSelected == GrowStages.YIELD) {
                showInfoFinishedCycle();
            }
            updateInfoLabel();
        });
    }

    private void setupYieldAndDryingControls() {

        add_btn.setDisable(true);
        days_grams_in_fld.setDisable(true);

        clean_check.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                stage_select_choice_box.setValue(GrowStages.CLEANING);
                harvest_check.setDisable(false);
            } else {
                harvest_check.setDisable(true);
                drying_rb.setDisable(true);
            }
        });

        harvest_check.setDisable(true);
        harvest_check.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV && clean_check.isSelected()) {
                stage_select_choice_box.setValue(GrowStages.HARVEST);
                drying_rb.setDisable(false);
            } else {
                drying_rb.setDisable(true);
                yield_rb.setDisable(true);
            }
        });

        drying_rb.setDisable(true);
        drying_rb.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV && harvest_check.isSelected()) {
                stage_select_choice_box.setValue(GrowStages.DRYING);
                yield_rb.setDisable(false);
                days_grams_in_fld.setPromptText("Drying Days");
                drying_or_yield_lbl.setText("Days");
                days_grams_in_fld.setDisable(false);
                add_btn.setDisable(true);
            } else {
                drying_or_yield_lbl.setText("");
                yield_rb.setDisable(true);
                days_grams_in_fld.setDisable(true);
                add_btn.setDisable(true);
            }
        });

        yield_rb.setDisable(true);
        yield_rb.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV && drying_rb.isSelected()) {
                stage_select_choice_box.setValue(GrowStages.YIELD);
                days_grams_in_fld.setPromptText("Yield (grams)");
                drying_or_yield_lbl.setText("Grams");
                days_grams_in_fld.setDisable(false);
                add_btn.setDisable(false);
            }else {
                drying_or_yield_lbl.setText("");
                add_btn.setDisable(true);
                days_grams_in_fld.setDisable(true);
            }
        });

        add_btn.setOnAction(event -> {
            if (yield_rb.isSelected()) {
                String input = days_grams_in_fld.getText();
                if (input != null && input.matches("\\d+")) {
                    Model.getInstance().getSelectedCycle().setYieldGrams(Integer.parseInt(input));
                    DialogUtils.info("Yield Saved", input + " grams");
                } else {
                    DialogUtils.warning("Input Error", "Enter valid number of grams.");
                }
            }
        });
    }

    private void updateInfoLabel() {
        GrowStages current = Model.getInstance().getSelectedCycle().getGrowStage();
        current_stage_name_days_lbl.setText("Current: " + current.name());
    }

    private void showInfoFinishedCycle() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/InfoFinishedCycle.fxml",
                (InfoFinishedCycle controller) -> controller.setDataFromCycle(Model.getInstance().getSelectedCycle()));

        if (root != null) {
            parent_anchor.getChildren().setAll(root);
        }
    }

    private void updateStageDurationsUI() {
        var durations = Model.getInstance().getSelectedCycle().getStageDurationDays();

        start_stage_days_lbl.setText(durations.getOrDefault(GrowStages.START_PLANTING, 0) + " days");
        germinated_days_lbl.setText(durations.getOrDefault(GrowStages.GERMINATED, 0) + " days");
        vegetation_days_lbl.setText(durations.getOrDefault(GrowStages.VEGETATION, 0) + " days");
        pre_flow_days_lbl.setText(durations.getOrDefault(GrowStages.PRE_FLOWERING, 0) + " days");
        flow_days_lbl.setText(durations.getOrDefault(GrowStages.FLOWERING, 0) + " days");
    }

}
