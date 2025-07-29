package org.jdta.growapp.Service;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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

    public boolean areDatesValid(LocalDate start, Label errorLabel) {
        if (start == null ) {
            DialogUtils.setErrorMessage(errorLabel,"Please select both start and estimated end date.");
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

    public boolean isInOutdoorSelected(RadioButton in, RadioButton out) {
        return in.isSelected() || out.isSelected();
    }

public int potCapacityCheck(CheckBox pot1, CheckBox pot2, CheckBox pot3, CheckBox pot4,
                            CheckBox hydroCheck, TextField customField, boolean isIndoor, Label errorLabel) {

    if (hydroCheck.isSelected()) {
        // Only custom capacity allowed
        return parseCustomPotLitres(customField, errorLabel);
    }

    if (isIndoor) {
        if (pot1.isSelected()) return 10;
        if (pot2.isSelected()) return 18;
        if (pot3.isSelected()) return 24;
    }

    if (pot4.isSelected()) {
        return parseCustomPotLitres(customField, errorLabel);
    }

    DialogUtils.setErrorMessage(errorLabel, "Select pot size or enter your litres.");
    return 0;
}

    private int parseCustomPotLitres(TextField field, Label errorLabel) {
        String val = field.getText().trim();
        if (val.isEmpty()) {
            DialogUtils.setErrorMessage(errorLabel, "Enter custom pot size.");
            return 0;
        }
        try {
            int litres = Integer.parseInt(val);
            if (litres < 1 || litres > 300) {
                DialogUtils.setErrorMessage(errorLabel, "Pot size must be between 1 and 300 litres.");
                return 0;
            }
            return litres;
        } catch (NumberFormatException e) {
            DialogUtils.setErrorMessage(errorLabel, "Invalid number for pot size.");
            return 0;
        }
    }

    public boolean componentCapacityCheck(String inputLitres, double totalLitres, int maxLitres, Label errorLabel) {
        try {
            double litres = Double.parseDouble(inputLitres.trim());
            if (litres <=0) {
                DialogUtils.setErrorMessage(errorLabel, "More than 0 litres");
                return false;
            }
            if (totalLitres + litres > maxLitres) {
                DialogUtils.setErrorMessage(errorLabel, "Max pot is(" + maxLitres + "L)");
                return false;
            }
            return true;
        }catch (NumberFormatException e) {
            DialogUtils.warning("Component data is empty", "Invalid number format");
            return false;
        }
    }
}
