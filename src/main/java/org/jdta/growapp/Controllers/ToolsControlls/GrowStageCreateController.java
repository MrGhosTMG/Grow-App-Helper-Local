package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Enums.GrowStages;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class GrowStageCreateController implements Initializable {
    private final ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();
    private Cycle draftCycle;

    public AnchorPane parent_GR_create_anchor;
    public Label new_started_lbl;
    public Label days_of_stage_lbl;
    public CheckBox new_check;
    public CheckBox started_check;
    public ChoiceBox<GrowStages> stage_picker;
    public DatePicker start_date_picker;
    public Button apply_btn;
    public ProgressBar grow_progress_bar;
    public Button add_btn;

    public void setDraftCycle(Cycle cycle) {
        this.draftCycle = cycle;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureModeSwitching();
        configureStagePicker();
        setupProgressBar();
        apply_btn.setOnAction(e -> applyStage());
    }

    private void configureModeSwitching() {
        // Toggle между new_check и started_check
        new_check.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                started_check.setSelected(false);
                start_date_picker.setDisable(true);
                stage_picker.getItems().setAll(GrowStages.START_PLANTING, GrowStages.GERMINATED);
                stage_picker.setValue(GrowStages.START_PLANTING);
                updateProgressAndLabel(GrowStages.START_PLANTING);
                DialogUtils.setErrorMessage(new_started_lbl, "Start from new");
            }
        });

        started_check.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                new_check.setSelected(false);
                start_date_picker.setDisable(false);
                stage_picker.getItems().setAll(GrowStages.values());
                stage_picker.getItems().removeAll(GrowStages.START_PLANTING, GrowStages.GERMINATED);
                stage_picker.setValue(GrowStages.VEGETATION);
                updateProgressAndLabel(GrowStages.VEGETATION);
                DialogUtils.setErrorMessage(new_started_lbl, "Was started");
            }
        });

        start_date_picker.valueProperty().addListener((obs, oldV, newV) -> updateDaysOfStageLabel());
    }

    private void configureStagePicker() {
        stage_picker.setOnAction(event -> {
            GrowStages picked = stage_picker.getValue();
            if (picked != null) {
                updateProgressAndLabel(picked);
            }
        });
    }

    private void updateProgressAndLabel(GrowStages stage) {
        if (!pickedStages.contains(stage)) pickedStages.add(stage);

        double progress = (double) (stage.ordinal() + 1) / GrowStages.values().length;
        grow_progress_bar.setProgress(progress);

        days_of_stage_lbl.setText("~" + stage.getDaysOfStage() + " Days");
        updateProgressColor(progress);
    }

    private void updateDaysOfStageLabel() {
        LocalDate selected = start_date_picker.getValue();
        if (selected != null) {
            long days = ChronoUnit.DAYS.between(selected, LocalDate.now());
            days_of_stage_lbl.setText(days + " days");
        } else {
            days_of_stage_lbl.setText("0 days");
        }
    }

    private void setupProgressBar() {
        grow_progress_bar.setProgress(0.0);
    }

    private void updateProgressColor(double progress) {
        String color;
        if (progress < 0.2) color = "#eef906";
        else if (progress < 0.4) color = "#2cb20b";
        else if (progress < 0.6) color = "#0fd9ea";
        else if (progress < 0.8) color = "#ef230f";
        else color = "#ec0fef";

        grow_progress_bar.setStyle("-fx-accent: " + color + ";");
    }

    private void applyStage() {
        cycleSelectCheck();
        GrowStages stage = stage_picker.getValue();
        LocalDate pickedDate = start_date_picker.getValue();
        if (draftCycle == null || draftCycle.getStartDateTime() == null) {
            DialogUtils.warning("Start error", "Start date not set");
            return;
        }
        LocalDateTime cycleDateOfStart = draftCycle.getStartDateTime();


        if (started_check.isSelected()) {
            if (pickedDate == null || !pickedDate.isBefore(cycleDateOfStart.toLocalDate())) {
                DialogUtils.warning("Invalid Date", "Pick earlier date than newCycle start.");
                return;
            }
        }

        if (stage == null) {
            DialogUtils.warning("Missing data", "Select a growth stage");
            return;
        }

        long days = ChronoUnit.DAYS.between(
                started_check.isSelected() ? pickedDate : cycleDateOfStart.toLocalDate(),
                LocalDate.now()
        );

        draftCycle.setGrowStage(stage);
        draftCycle.getStageDurationDays().put(stage, (int) days);
        DialogUtils.info("Stage Set", "Stage " + stage + " applied with " + days + " days");
    }

    private void cycleSelectCheck() {
        if (draftCycle == null) {
            DialogUtils.warning("No cycle", "Please make sure cycle is set");
            apply_btn.setDisable(true);
            return;
        }
    }

}


