package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jdta.growapp.DAO.StageTransitionDAO;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.DTO.StageTransition;
import org.jdta.growapp.Database.DBConnection;
import org.jdta.growapp.Enums.GrowStages;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GrowStageCreateController implements Initializable {
    private final ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();
    private Cycle draftCycle;

    public Spinner<GrowStages> past_stages_spinner;
    public Button set_days_btn;
    public TextField days_in;
    public Label stage_lbl;
    public AnchorPane parent_GR_create_anchor;
    public Label new_started_lbl;
    public Label days_of_stage_lbl;
    public CheckBox new_check;
    public CheckBox started_check;
    public ChoiceBox<GrowStages> stage_picker;
    public DatePicker start_date_picker;
    public Button apply_btn;
    public ProgressBar grow_progress_bar;

    public void setDraftCycle(Cycle cycle) {
        this.draftCycle = cycle;
        updateUiFromCycle();
    }
    public Cycle getDraftCycle() {
        return draftCycle;
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
                past_stages_spinner.setDisable(true);
                days_in.setDisable(true);
                set_days_btn.setDisable(true);
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
                past_stages_spinner.setDisable(false);
                days_in.setDisable(false);
                set_days_btn.setDisable(false);
                setupPastStageEditor();
            }
        });

        start_date_picker.valueProperty().addListener((obs, oldV, newV) -> updateDaysOfStageLabel());
    }

    private void configureStagePicker() {
        stage_picker.setOnAction(event -> {
            GrowStages picked = stage_picker.getValue();
            if (picked != null) {
                updateProgressAndLabel(picked);
                if (started_check.isSelected()) setupPastStageEditor();
            }
        });
    }

    private void updateProgressAndLabel(GrowStages stage) {
        if (!pickedStages.contains(stage)) pickedStages.add(stage);

        double progress = (double) (stage.ordinal() + 1) / GrowStages.values().length;
        grow_progress_bar.setProgress(progress);

        days_of_stage_lbl.setText("~" + stage.calculateDaysSince(draftCycle.getStartDateTime().toLocalDate()) + " Days");
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

    private void updateUiFromCycle() {
        if (draftCycle == null) return;

        draftCycle.getStageStartDates().clear();
        draftCycle.getStageDurationDays().clear();

        List<StageTransition> transitions = Model.getInstance().getStageTransitionDAO().findAllByCycleId(draftCycle.getId());
        for (StageTransition stageTransition : transitions) {
            draftCycle.getStageStartDates().put(stageTransition.getGrowStages(), stageTransition.getStartDate());
            draftCycle.getStageDurationDays().put(stageTransition.getGrowStages(), stageTransition.getDurationsDays());
        }
        GrowStages currentStage = draftCycle.getGrowStage();
        if (currentStage != null) {
            stage_picker.setValue(currentStage);
            updateProgressAndLabel(currentStage);
        }
        setupPastStageEditor();
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
        draftCycle.getStageStartDates().put(stage, started_check.isSelected() ?
                pickedDate : draftCycle.getStartDateTime().toLocalDate());

        draftCycle.getStageDurationDays().put(stage, (int) days);
        if (draftCycle.getEtaDateTime() == null && draftCycle.getStartDateTime() != null) {
            draftCycle.setEtaDateTime(draftCycle.getStartDateTime().plusDays(90));
        }
        try {

            Model.getInstance().getCycleDAO().update(draftCycle);

            if (draftCycle.getId() == 0) {
                Model.getInstance().getCycleDAO().insert(draftCycle);
            }
            saveStageTransitions(draftCycle);

            Model.getInstance().loadCycleTransitions(draftCycle);

//            StageTransition transition = new StageTransition(
//                    draftCycle.getId(), stage, started_check.isSelected() ? pickedDate : draftCycle.getStartDateTime().toLocalDate(), (int) days);
//
//            Model.getInstance().getStageTransitionDAO().insert(transition);
//            Model.getInstance().loadCycleTransitions(draftCycle);

            DialogUtils.info("Stage Set", "Stage " + stage + " applied with " + days + " days from: " +
                    (started_check.isSelected() ? pickedDate : draftCycle.getStartDateTime().toLocalDate()));
        }catch (SQLException e) {
            DialogUtils.error("DB Error" , "Failed to update GrowStage");
            e.printStackTrace();
        }
    }

    private void cycleSelectCheck() {
        if (draftCycle == null) {
            DialogUtils.warning("No cycle", "Please make sure cycle is set");
            apply_btn.setDisable(true);
        }
    }

    private void setupPastStageEditor() {
        GrowStages current = stage_picker.getValue();
        if (current == null) return;

        // List stages till selected
        List<GrowStages> editableStages = Arrays.stream(GrowStages.values()).filter(s -> s.ordinal() <= current.ordinal())
                .collect(Collectors.toList());
                SpinnerValueFactory<GrowStages> factory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(editableStages));
                past_stages_spinner.setValueFactory(factory);

                past_stages_spinner.valueProperty().addListener((obs, oldV, newV)-> {
                    //GrowStages selectedStage = editableStages.get(newV);
                    stage_lbl.setText("Set days" + newV.name());
                    days_in.setText(String.valueOf(draftCycle.getStageDurationDays().getOrDefault(newV, 0)));
                });

                set_days_btn.setOnAction(actionEvent -> {
                    try {
                        int days = Integer.parseInt(days_in.getText().trim());
                        GrowStages selectedStage = past_stages_spinner.getValue();
                        draftCycle.getStageDurationDays().put(selectedStage, days);
                        DialogUtils.info("Updated", "Stage " + selectedStage + " set to " + days + "days");
                    }catch (NumberFormatException e) {
                        DialogUtils.warning("Invalid input", " Please enter valid number");
                    }
                });
                past_stages_spinner.getValueFactory().setValue(editableStages.get(0));
                stage_lbl.setText("Days for " + editableStages.get(0).name());
    }

    private void saveStageTransitions(Cycle cycle) {

        try {
            StageTransitionDAO dao = new StageTransitionDAO(DBConnection.getConnection());

            dao.deleteByCycleId(cycle.getId());
            System.out.println("Saving transitions:");
            for (Map.Entry<GrowStages, Integer> entry : cycle.getStageDurationDays().entrySet()) {
                GrowStages stage = entry.getKey();
                Integer duration = entry.getValue();
                LocalDate startDate = cycle.getStageStartDates().get(stage);
                System.out.println("Stage: " + stage + ", start: " + startDate + ", duration: " + duration);

                if (startDate != null && duration!= null) {
                    StageTransition transition = new StageTransition();
                    transition.setCycleId(cycle.getId());
                    transition.setGrowStages(stage);
                    transition.setStartDate(startDate);
                    transition.setDurationsDays(duration);
                    dao.insert(transition);
                    System.out.println("⏺ Saving stage transitions for cycle ID = " + cycle.getId());
                }else {
                    System.out.println("❌ Skipping stage: " + stage + " (startDate or duration is null)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


