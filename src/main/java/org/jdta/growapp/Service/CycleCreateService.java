package org.jdta.growapp.Service;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.jdta.growapp.Utils.DialogUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CycleCreateService {


    public String pickSortCycleName(String name, Label errorLabel) {
        if (name == null || name.trim().isEmpty()) {
            DialogUtils.setErrorMessage(errorLabel, "Enter name for new cycle");
            return null;
        }
        return name.trim();
    }

    public boolean isInOutdoorSelected(RadioButton in, RadioButton out, Label errorLabel, CheckBox... ifOutDisable) {

        if (in.isSelected()) {
            out.setDisable(true);//нужное правило
            return true;
        }
        if (out.isSelected()) {
            in.setDisable(true);//нужное правило
            for (CheckBox checkBox : ifOutDisable) {
                checkBox.setDisable(true);
            }
            return true;
        }
        else if (!in.isSelected() && !out.isSelected()) {
            DialogUtils.setErrorMessage(errorLabel,"Select Indoor or Outdoor");
            return false;
        }
        else {
            System.out.println("Some error in IN/Out selecting");
            return false;
        }
    }

    public boolean areDatesValid(LocalDate start, LocalDate EET, Label errorLabel) {
        if (start == null || EET == null) {
            DialogUtils.setErrorMessage(errorLabel,"Please select both start and estimated end date.");
            return false;
        }

        if (EET.isBefore(start)) {
            DialogUtils.setErrorMessage(errorLabel,"End date cannot be before start date.");
            return false;
        }
        return true;
    }

    public boolean isSortTypeSelected(RadioButton... buttons) {
        for (RadioButton button : buttons) {
            if (button.isSelected()) return true;
        }
        return false;
    }

    public double parsePotCapacity(String capacity) {
        try {
            return Double.parseDouble(capacity);
        }catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
