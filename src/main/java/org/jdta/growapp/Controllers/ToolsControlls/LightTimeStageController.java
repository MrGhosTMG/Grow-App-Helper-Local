package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LightTimeStageController implements Initializable {
    public AnchorPane parent_Anchor_pane;
    public Label Hours_lbl;
    public CheckBox check_box_manually;
    public TextField night_input_fld;
    public TextField day_input_fld;
    public Button set_btn;
    public RadioButton select_1;
    public RadioButton select_2;
    public RadioButton select_3;
    public RadioButton select_4;
    public TextArea text_info_area;
    public Label error_lbl;

    private Runnable onSaveCallback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterInputField(day_input_fld, night_input_fld);
        setupManualInput();

        ToggleGroup group = new ToggleGroup();
        setupRadioButtons(group);

        select_2.fire(); // дефолт

        set_btn.setOnAction(actionEvent -> onSetClicked());

        // Автовключение полей при ручном вводе
        day_input_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            if (check_box_manually.isSelected()) {
                if (newVal.matches("\\d{1,2}")) {
                    try {
                        int day = Integer.parseInt(newVal);
                        if (day >= 0 && day <= 24) {
                            night_input_fld.setText(String.valueOf(24 - day));
                        } else {
                            night_input_fld.setText("");
                        }
                    } catch (NumberFormatException e) {
                        night_input_fld.setText("");
                    }
                } else {
                    night_input_fld.setText("");
                }
            }
        });
    }

    public String setErrorMessage(String msg) {
        error_lbl.setText(msg);
        return msg;
    }

    private void setupRadioButtons(ToggleGroup group) {
        select_1.setToggleGroup(group);
        select_2.setToggleGroup(group);
        select_3.setToggleGroup(group);
        select_4.setToggleGroup(group);

        select_1.setOnAction(e -> applyPreset(24, 0));
        select_2.setOnAction(e -> applyPreset(20, 4));
        select_3.setOnAction(e -> applyPreset(18, 6));
        select_4.setOnAction(e -> applyPreset(12, 12));
    }

    private void setupManualInput() {
        check_box_manually.setOnAction(e -> onManualMode(check_box_manually.isSelected()));
    }

    private void onManualMode(boolean selected) {
        day_input_fld.setDisable(!selected);
        night_input_fld.setDisable(!selected);

        if (selected) {
            enableAllRadioButtons(false);
            select_1.getToggleGroup().selectToggle(null);
            day_input_fld.setText("");
            night_input_fld.setText("");
        } else {
            enableAllRadioButtons(true);
        }
    }

    private void applyPreset(int day, int night) {
        check_box_manually.setSelected(false);
        day_input_fld.setText(String.valueOf(day));
        night_input_fld.setText(String.valueOf(night));
        day_input_fld.setDisable(true);
        night_input_fld.setDisable(true);
        enableAllRadioButtons(true);
    }

    private RadioButton getRadioButtonByValue(String value) {
        return switch (value) {
            case "24 / 0" -> select_1;
            case "20 / 4" -> select_2;
            case "18 / 6" -> select_3;
            case "12 / 12" -> select_4;
            default -> null;
        };
    }

    private void enableAllRadioButtons(boolean enable) {
        for (RadioButton rb : List.of(select_1, select_2, select_3, select_4)) {
            rb.setDisable(!enable);
        }
    }

    private void disableOtherRadioButtons(RadioButton selected) {
        for (RadioButton rb : List.of(select_1, select_2, select_3, select_4)) {
            rb.setDisable(rb != selected);
        }
    }

    private void filterInputField(TextField day, TextField night) {
        day.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,2}")) {
                day.setText(oldVal);
            } else if (!newVal.isEmpty()) {
                try {
                    int val = Integer.parseInt(newVal);
                    if (val > 24) {
                        day.setText(oldVal);
                    } else {
                        night.setText(String.valueOf(24 - val));
                    }
                } catch (NumberFormatException e) {
                    day.setText(oldVal);
                }
            } else {
                night.setText("");
            }
        });
    }

    // check actions
    private void onSetClicked() {
        String day = day_input_fld.getText().trim();
        String night = night_input_fld.getText().trim();

        if (day.isEmpty() || night.isEmpty()) {
            setErrorMessage("Invalid Input -> Enter Digits only in 24 Hours range");
            return;
        }

        try {
            int dayVal = Integer.parseInt(day);
            int nightVal = Integer.parseInt(night);
            if (dayVal + nightVal != 24) {
                setErrorMessage("Sum of Day and Night hours must be 24.");
                return;
            }
        } catch (NumberFormatException e) {
            setErrorMessage("Please enter valid numbers for time.");
            return;
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
            error_lbl.setText("");
        }
        ((Stage) set_btn.getScene().getWindow()).close();
    }


    public void setOnSave(Runnable callback) {
        this.onSaveCallback = callback;
    }

    public void setDayNightTimes(String day, String night) {
        day_input_fld.setText(day);
        night_input_fld.setText(night);
    }

    public String getSelectedHours() {
        return day_input_fld.getText() + " / " + night_input_fld.getText();
    }
}
