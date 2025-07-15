package org.jdta.growapp.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLUtils {


    // Загрузка AnchorPane без Stage (например, для setCenter или setChildren)
    public static Parent loadAnchorPane(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Parent> T loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(path));
            return loader.load();
        } catch (IOException e) {
            Logger.getLogger(FXMLUtils.class.getName()).log(Level.SEVERE, "Error loading FXML: " + path, e);
            return null;
        }
    }

    public static FXMLLoader getLoader(String path) {
        return new FXMLLoader(FXMLUtils.class.getResource(path));
    }

    // Открытие нового окна
    public static void openModalWindow(String fxmlPath, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает FXML и передаёт контроллер через callback.
     *
     * @param fxmlPath путь до FXML
     * @param controllerCallback действие над контроллером (установка колбэков и т.д.)
     * @return Node для отображения в Scene
     */
    public static <T> Node loadWithControllerCallBackActions(String fxmlPath, Consumer<T> controllerCallback) {

        try {
            FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            //Get controller
            T controller = loader.getController();
            if (controllerCallback != null) {
                controllerCallback.accept(controller);
            }
            return root;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void openModalWithCallback(
            String fxmlPath,
            String title,
            Consumer<T> controllerConsumer // лямбда-функция, в которую ты передаёшь действия над контроллером
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Получаем контроллер после загрузки
            T controller = loader.getController();

            // Применяем callback — можно установить данные, подписки и т.д.
            controllerConsumer.accept(controller);

            // Настраиваем и показываем окно
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            Logger.getLogger(FXMLUtils.class.getName()).log(Level.SEVERE, "Ошибка открытия модального окна с callback", e);
        }
    }
    public static Node loadFXMLTips(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(fxmlPath));
            return loader.load();
        }catch (IOException | NullPointerException e) {
            System.out.println(" Error loading FXML Tip: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static Stage getCurrentStage() {
        return (Stage) javafx.stage.Window.getWindows().stream()
                .filter(javafx.stage.Window::isShowing)
                .findFirst()
                .orElse(null);
    }


    public static Stage stageFrom(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public static void showDialogStage(Node root, String confirmDelete, Stage stage) {
        if (root == null) return;
        Stage dialogStage = new Stage();
        dialogStage.setTitle(confirmDelete);
        dialogStage.initOwner(stage);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);

        Scene scene = new Scene((Parent) root);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
