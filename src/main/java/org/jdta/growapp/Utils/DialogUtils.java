package org.jdta.growapp.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public class DialogUtils {

    public static void confirm(String message, Runnable onConfirm) {
        confirm(getCurrentStage(), message, onConfirm);//🥚
    }

    private static Stage getCurrentStage() {
        return (Stage) Window.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);
    }


    public static void confirm(Window owner, String message, Runnable onConfirm) {

        try {
            FXMLLoader loader = new FXMLLoader(DialogUtils.class.getResource("/FXML/Utils/ConfirmDialogWindow.fxml"));
            BorderPane root = loader.load();

            ConfirmDialogController controller = loader.getController();
            controller.alert_lbl.setText(message);



            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            if (owner != null) dialogStage.initOwner(owner);
            dialogStage.setTitle("Confirmation");
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
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
//переделаю на кастом фхмл
    public static void info(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void delete(String title, String message, Runnable onDeleteConfirm) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogUtils.class.getResource("/FXML/Utils/Delete.fxml"));
            AnchorPane root = loader.load();

            DeleteDialogController controller = loader.getController();
            controller.setMessage(message);
            controller.setOnDeleteConfirmed(onDeleteConfirm);


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void warning(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogUtils.class.getResource("/FXML/Utils/WarningDialog.fxml"));
            AnchorPane root = loader.load();

            WarningDialogController controller = loader.getController();
            controller.setMessage(title, message);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Warning");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void error(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogUtils.class.getResource("/FXML/Utils/Error.fxml"));
            AnchorPane root = loader.load();

            ErrorDialogController controller = loader.getController();
            controller.setMessage(title, message);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Oops,.. Something went wrong.. ");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setErrorMessage(Label label, String message) {
        if (label != null) {
            label.setText(message);
            hideErrorMessage(label, 3); // Автоочистка через 3 секунды
        }
    }

    /**
     * Hide Label text in X - time
     * @param label Clean Label
     * @param delaySeconds Dely in X - seconds
     *
     * logging
     hideErrorMessage(error_lbl, 3, () -> log("Сообщение скрыто"));
     * Update UI:
     * hideErrorMessage(error_lbl, 2, () -> updateUIState());
     * Actions chain
     * hideErrorMessage(status_lbl, 1, () -> startNextAnimation());
     */
    public static void hideErrorMessage(Label label, int delaySeconds) {
        if (label == null) return;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(delaySeconds),
                        e -> {
                            if (label != null) {
                                label.setText("");
                            }
                        }
                )
        );
        timeline.play();
    }

    // for 3 sec delay
    public static void hideErrorMessage(Label label) {
        hideErrorMessage(label, 3);
    }

    // with callback after clean text
    public static void hideErrorMessage(Label label, int delaySeconds, Runnable onComplete) {
        Timeline clearMsg = new Timeline(
                new KeyFrame(Duration.seconds(delaySeconds),
                        event -> {
                            label.setText("");
                            onComplete.run();
                        }
                ));
        clearMsg.play();
    }

    public static void showDialogStage(Node root, String title, Stage stageOwner) {
        Stage dialog = new Stage();
        dialog.initOwner(stageOwner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);
        Scene scene = new Scene((Parent) root);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }
}
