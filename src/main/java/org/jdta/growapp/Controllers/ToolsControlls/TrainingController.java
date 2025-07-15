package org.jdta.growapp.Controllers.ToolsControlls;


import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.jdta.growapp.Enums.TrainingType;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class TrainingController implements Initializable {
    public RadioButton LST_rb;
    public RadioButton DEF_rb;
    public RadioButton SCRG_rb;
    public RadioButton TOP_rb;
    public RadioButton FIM_rb;
    public RadioButton ML_rb;
    public RadioButton other_rb;
    public Label delay_lbl;
    public TextField days_in_fld;
    public Button add_days_btn;
    public RadioButton ml_main_step_rb;
    public RadioButton adit_step_rb;
    public Button set_step_btn;
    public Button set_train_btn;
    public AnchorPane info_top_anchor;
    public BorderPane parent_border_pane;
    public Label error_lbl;

    private boolean readOnlyMode = false;
    private boolean mainStepApplied  = false;
    private Runnable onSaveCallback;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setStep();

        //For only one RadioButton selected
        ToggleGroup trainingToggleGroup = new ToggleGroup();
        LST_rb.setToggleGroup(trainingToggleGroup);
        DEF_rb.setToggleGroup(trainingToggleGroup);
        SCRG_rb.setToggleGroup(trainingToggleGroup);
        TOP_rb.setToggleGroup(trainingToggleGroup);
        FIM_rb.setToggleGroup(trainingToggleGroup);
        ML_rb.setToggleGroup(trainingToggleGroup);
        other_rb.setToggleGroup(trainingToggleGroup);

        //binding to R.buttons
        Map<RadioButton, TrainingType> trainingTypeMap = Map.of(
                LST_rb, TrainingType.LST,
                DEF_rb, TrainingType.DEFOLIATION,
                SCRG_rb, TrainingType.SCROG,
                TOP_rb, TrainingType.TOP,
                FIM_rb, TrainingType.FIM,
                ML_rb, TrainingType.MAINLINING,
                other_rb, TrainingType.OTHER
        );

        trainingToggleGroup.selectedToggleProperty().addListener((observable, oldToggle , newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                TrainingType type = trainingTypeMap.getOrDefault(selected, TrainingType.OTHER);
                showTrainingInfo(type);
                updateUIBasedOnTrainingType(type);
            }
        });
        //Default screen
        other_rb.setSelected(true);

        if (readOnlyMode) {
            disableEditableControls();
        }

        add_days_btn.setOnAction(actionEvent -> onSetClicked());

        filterInputField(days_in_fld);
    }

    private void onSetClicked() {
        if (isDaysInputInvalid()) {
            return;
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
            error_lbl.setText("");
        }
        setDaysMessage("Saved " + getSelectedDays());
    }

    private boolean isDaysInputInvalid() {
        String day = days_in_fld.getText().trim();
        if (day.isEmpty()) {
            DialogUtils.warning("Training Stage", "Please enter number of days");
            return true;
        }

        try {
            int dayVal = Integer.parseInt(day);
            if (dayVal > 30) {
                DialogUtils.warning("Warning", "There is no Stress type more than 30 days");
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            DialogUtils.warning("Training", "Please enter valid number for days");
            return true;
        }
    }

    public void setOnSave(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public String getSelectedDays() {
        String day = days_in_fld.getText().trim();

        try {
            int d = Integer.parseInt(day);

            return d + " days stress set " ;
        } catch (NumberFormatException e) {
            return "Invalid format";
        }
    }

    public void setDaysMessage(String msg) {
        error_lbl.setText(msg);
        DialogUtils.hideErrorMessage(error_lbl, 3);
    }

    private void disableEditableControls() {
        days_in_fld.setVisible(false);
        add_days_btn.setVisible(false);
        ml_main_step_rb.setVisible(false);
        adit_step_rb.setVisible(false);
        set_step_btn.setVisible(false);
        set_train_btn.setVisible(false);
        delay_lbl.setVisible(false);
    }

    private void showTrainingInfo(TrainingType type) {
        info_top_anchor.getChildren().clear();

        Node content = FXMLUtils.loadFXML(type.getFxmlPath()); // например, "/FXML/tips/LST.fxml"
        if (content != null) {
            info_top_anchor.getChildren().setAll(content);
        }
    }

    private void loadTip(TrainingType type) {
        Node tipNode = FXMLUtils.loadFXMLTips(type.getFxmlPath());
        if (tipNode != null) {
            info_top_anchor.getChildren().setAll(tipNode);
        }else {
            System.out.println(" failed to load Tip for " + type.name());
        }
    }
    public void setReadOnlyMode(boolean readOnly) {
        this.readOnlyMode = readOnly;
    }

    private void filterInputField(TextField daysInFld) {
        daysInFld.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                error_lbl.setText("");
                return; // If empty field
            }

            // Проверяем, что ввод состоит только из цифр
            if (!newVal.matches("\\d*")) {
                daysInFld.setText(oldVal != null ? oldVal : "");
                setDaysMessage("Enter digits only (1-30)");
                return;
            }

            // Check range
            if (!newVal.isEmpty()) {
                try {
                    int day = Integer.parseInt(newVal);
                    if (day < 1 || day > 30) {
                        daysInFld.setText(oldVal != null ? oldVal : "");
                        DialogUtils.setErrorMessage(error_lbl,"Enter from 1 to 30");
                    } else {
                        error_lbl.setText("");
                    }
                } catch (NumberFormatException e) {
                    daysInFld.setText(oldVal != null ? oldVal : "");
                    DialogUtils.setErrorMessage(error_lbl,"Invalid number");
                }
            }
        });
    }

    private void setStep() {
        set_step_btn.setOnAction(actionEvent -> {
            if (ml_main_step_rb.isSelected()) {
                if (!mainStepApplied) {
                    DialogUtils.setErrorMessage(error_lbl,"Main step set!");
                    mainStepApplied = true;
                    //Clean error label in 3 sec

                    // блокируем Main step после применения
                    ml_main_step_rb.setDisable(true);
                    ml_main_step_rb.setSelected(false);

                    // разблокируем Additional step
                    adit_step_rb.setDisable(false);

                } else {
                    DialogUtils.warning("Attention", "Not allowed");
                    DialogUtils.setErrorMessage(error_lbl,"Not allowed");
                }

            } else if (adit_step_rb.isSelected()) {
                if (mainStepApplied) {
                    DialogUtils.setErrorMessage(error_lbl,"New step added!");
                } else {

                    DialogUtils.setErrorMessage(error_lbl,"Apply Main step first!");
                }
            } else {
                DialogUtils.warning(" MainLining method needs to ", "Apply Main step first! then Select a step.");
                setDaysMessage("Select a step.");
            }
        });
    }


    private void updateUIBasedOnTrainingType(TrainingType type) {
        boolean isOtherStressActive = type == TrainingType.OTHER;
        boolean isMainLining = type == TrainingType.MAINLINING;

        // enable / disable other stress
        days_in_fld.setDisable(!isOtherStressActive);
        add_days_btn.setDisable(!isOtherStressActive);

        // enable / disable steps MineLining
        ml_main_step_rb.setDisable(!isMainLining || mainStepApplied); // if applied set disable
        adit_step_rb.setDisable(!isMainLining || !mainStepApplied);
        set_step_btn.setDisable(!isMainLining);

        if (!isMainLining) {
            // Сброс выбора RadioButtons, если переключились с MainLining
            ml_main_step_rb.setSelected(false);
            adit_step_rb.setSelected(false);
        }
    }
}
