package org.jdta.growapp.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdta.growapp.Controllers.ToolsControlls.ConfirmDialogController;

import java.io.IOException;

public class DialogUtils {

    public static void confirm(String message, Runnable onConfirm) {

        try {
            FXMLLoader loader = new FXMLLoader(DialogUtils.class.getResource("/FXML/tools/CustomConfirmDialogWindow.fxml"));
            BorderPane root = loader.load();

            ConfirmDialogController controller = loader.getController();
            controller.alert_lbl.setText(message);



            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Confirmation");
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            //dialogStage.getIcons().add(new Image(DialogUtils.class.getResource("src/main/resources/Images/cannabis.png").toString()));
            dialogStage.centerOnScreen();

            // Поведение кнопок
            controller.ok_btn.setOnAction(e -> {
                onConfirm.run();
                dialogStage.close();
            });

            controller.cancel_btn.setOnAction(e -> dialogStage.close());

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void info(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void warning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void error(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
