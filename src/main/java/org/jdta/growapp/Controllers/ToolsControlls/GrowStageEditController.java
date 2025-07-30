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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GrowStageEditController implements Initializable {
    private final ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();// забыл где-то передать

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
    private Slider slider;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCycle();
    }

    private void initializeCycle() {
        cycle = Model.getInstance().getSelectedCycle();
        if (cycle == null) {
            DialogUtils.warning("No Cycle", "Cycle wasn't introduced");
            return;
        }
        stage_select_choice_box.getItems().addAll(GrowStages.values());
        stage_select_choice_box.setValue(cycle.getGrowStage());
        updateUiFromCycle();
        set_stage.setOnAction(actionEvent -> applyEditStage());
    }

    private void applyEditStage() {
        GrowStages newStage = stage_select_choice_box.getValue();
        GrowStages currentStage = cycle.getGrowStage();

        if (cycle.getStageStartDates() != null) {
            cycle.getStageStartDates().put(newStage, LocalDate.now());
        }

        if (cycle.getStageStartDates().containsKey(currentStage)) {
            LocalDate stageDate = cycle.getStageStartDates().get(currentStage);
            long days = ChronoUnit.DAYS.between(stageDate, LocalDate.now());
            cycle.getStageDurationDays().put(currentStage, (int) days);
        }

        cycle.setGrowStage(newStage);
        try {
            Model.getInstance().getCycleDAO().update(cycle);
        } catch (SQLException e) {
            System.err.println("Cycle wasn't updated" + e.getMessage());
            e.printStackTrace();
        }
        updateUiFromCycle();
        DialogUtils.info("Updated", "Stage was successfully updated.");
    }

    public void init(Cycle cycle, Slider slider) {
        this.cycle = cycle;
        this.slider = slider;
        setupStageSelection();
        setupYieldAndDryingControls();
        updateStageDurationsUI();
    }

    private void setupStageSelection() {
        stage_select_choice_box.getItems().clear();

        GrowStages currentStage = cycle.getGrowStage();
        if (currentStage == null) {
            DialogUtils.warning("Current stage", "Please set Grow stage.");
            return;
        }

        List<GrowStages> allowedStages = Arrays.stream(GrowStages.values()).filter(s -> s.ordinal() >= currentStage.ordinal())
                        .collect(Collectors.toList());

        stage_select_choice_box.setItems(FXCollections.observableArrayList(allowedStages));
        stage_select_choice_box.setValue(currentStage);

        set_stage.setOnAction(event -> {
            GrowStages nowSelected = stage_select_choice_box.getValue();

            if (nowSelected == null) {
                DialogUtils.warning("Current stage", "Please set Grow stage.");
                return;
            }

            if (nowSelected.ordinal() < currentStage.ordinal()) {
                DialogUtils.warning("Blocked", "You can't go to earlier stage!");
                return;
            }

            LocalDate now = LocalDate.now();
            Map<GrowStages, Integer> stageDurations = cycle.getStageDurationDays();
            Map<GrowStages, LocalDate> stageStartDates = cycle.getStageStartDates();

            if (!stageStartDates.containsKey(currentStage)) {
                stageStartDates.put(currentStage, cycle.getStartDateTime().toLocalDate());
            }

            LocalDate stageStartDate = stageStartDates.get(currentStage);
            int daysSpent = (int) ChronoUnit.DAYS.between(stageStartDate, now);
            stageDurations.put(currentStage, daysSpent);

            cycle.setGrowStage(nowSelected);
            stageStartDates.put(nowSelected, now);
            try {
                Model.getInstance().getCycleDAO().update(cycle);
                if (slider != null) slider.setValue(nowSelected.ordinal());
                updateInfoLabel();
                DialogUtils.info("Saved", "New grow stage was applied");
            } catch (SQLException e) {
                DialogUtils.error("Database error", "Failed to apply new grow stage");
                e.printStackTrace();
                System.out.println("Error with updating in Database grow stage");
            }

            if (nowSelected == GrowStages.YIELD) {
                showInfoFinishedCycle();
            }
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
        GrowStages current = cycle.getGrowStage();
        current_stage_name_days_lbl.setText("Current: " + current.name());
    }

    private void showInfoFinishedCycle() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/InfoFinishedCycle.fxml",
                (InfoFinishedCycle controller) -> controller.setDataFromCycle(cycle));

        if (root != null) {
            parent_anchor.getChildren().setAll(root);
        }
    }

    private void updateStageDurationsUI() {
        Map<GrowStages, Integer> durations = cycle.getStageDurationDays();

        start_stage_days_lbl.setText(durations.getOrDefault(GrowStages.START_PLANTING, 0) + " days");
        germinated_days_lbl.setText(durations.getOrDefault(GrowStages.GERMINATED, 0) + " days");
        vegetation_days_lbl.setText(durations.getOrDefault(GrowStages.VEGETATION, 0) + " days");
        pre_flow_days_lbl.setText(durations.getOrDefault(GrowStages.PRE_FLOWERING, 0) + " days");
        flow_days_lbl.setText(durations.getOrDefault(GrowStages.FLOWERING, 0) + " days");
    }
    private void updateUiFromCycle() {
        if (cycle == null || cycle.getStageStartDates() == null) return;

        for (GrowStages stage : GrowStages.values()) {
            if (cycle.getStageStartDates().containsKey(stage)) {
                LocalDate date = cycle.getStageStartDates().get(stage);
                int days = (int) ChronoUnit.DAYS.between(date, LocalDate.now());
                cycle.getStageDurationDays().put(stage,days);
                switch (stage) {
                    case START_PLANTING -> start_stage_days_lbl.setText("Planted - " + days + "days");
                    case GERMINATED -> germinated_days_lbl.setText("First Leafs - " + days + "days");
                    case VEGETATION -> vegetation_days_lbl.setText("Vegetation - " + days + "days");
                    case PRE_FLOWERING -> pre_flow_days_lbl.setText("Starting Flow - " + days + "days");
                    case FLOWERING -> flow_days_lbl.setText("Flowering - " + days + "days");
                    case CLEANING -> set_edit_error_lbl.setText("Cleaning stage - " + days + "days");
                    case HARVEST -> set_edit_error_lbl.setText("Plant cut - " + days + "days");
                    case DRYING -> set_edit_error_lbl.setText("Drying process - " + days + "days");
                    default -> {}
                }
            }
        }
    }
}
