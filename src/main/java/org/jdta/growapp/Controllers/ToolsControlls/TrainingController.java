package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.jdta.growapp.Enums.TrainingType;
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
                DEF_rb, TrainingType.DEF,
                SCRG_rb, TrainingType.SCRG,
                TOP_rb, TrainingType.TOP,
                FIM_rb, TrainingType.FIM,
                ML_rb, TrainingType.ML,
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

    private void setStep() {
        set_step_btn.setOnAction(actionEvent -> {

            //Clean error label in 3 sec
            Timeline clearMsg = new Timeline(new KeyFrame(Duration.seconds(3),event -> error_lbl.setText("")));
            clearMsg.play();

            if (ml_main_step_rb.isSelected()) {
                if (!mainStepApplied) {
                    error_lbl.setText("Main step set!");
                    mainStepApplied = true;

                    // блокируем Main step после применения
                    ml_main_step_rb.setDisable(true);
                    ml_main_step_rb.setSelected(false);

                    // разблокируем Additional step
                    adit_step_rb.setDisable(false);

                } else {
                    error_lbl.setText("Not allowed");
                }

            } else if (adit_step_rb.isSelected()) {
                if (mainStepApplied) {
                    error_lbl.setText("New step added!");
                } else {
                    error_lbl.setText("Apply Main step first!");
                }

            } else {
                error_lbl.setText("Select a step.");
            }
        });
    }


    private void updateUIBasedOnTrainingType(TrainingType type) {
        boolean isOtherStressActive = type == TrainingType.OTHER;
        boolean isMainLining = type == TrainingType.ML;

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
