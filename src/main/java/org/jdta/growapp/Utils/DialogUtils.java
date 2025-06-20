package org.jdta.growapp.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
}
