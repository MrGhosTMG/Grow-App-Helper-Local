package org.jdta.growapp.Utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;


public class StageActions {


    public static void onLogout(Stage stage) {
        DialogUtils.confirm(stage," Logout ? Come back soon :)", () -> {
            Model.getInstance().logout();
            stage.close();
        });
    }

    public static void onExit(Stage stage) {
        DialogUtils.confirm(stage, "It is 4:20 ? Want to exit?", stage::close);
    }

    public static void backToUser(Stage stage) {
        Model.getInstance().getView().showUserWindow();
        stage.close();
    }

    public static boolean isTimePairValid(TextField dayField, TextField nightField,
                                          Label errorLabel, int total) {
        if (isNumberInvalid(dayField, errorLabel, 0, total) ||
                isNumberInvalid(nightField, errorLabel, 0, total)) {
            return false;
        }

        int day = Integer.parseInt(dayField.getText());
        int night = Integer.parseInt(nightField.getText());

        if (day + night != total) {
            DialogUtils.setErrorMessage(errorLabel,
                    String.format("Sum must be %d (current: %d)", total, day + night));
            return false;
        }
        return true;
    }

    public static boolean isNumberInvalid(TextField field, Label errorLabel, int min, int max) {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            DialogUtils.setErrorMessage(errorLabel, "Field cannot be empty");
            return true;
        }

        try {
            int value = Integer.parseInt(text);
            if (value < min || value > max) {
                DialogUtils.setErrorMessage(errorLabel,
                        String.format("Value must be between %d and %d", min, max));
                return true;
            }
            errorLabel.setText(""); // Очищаем ошибку при успешной валидации
            return false;
        } catch (NumberFormatException e) {
            DialogUtils.setErrorMessage(errorLabel, "Must be a valid number");
            return true;
        }
    }
}
