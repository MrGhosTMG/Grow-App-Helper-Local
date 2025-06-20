package org.jdta.growapp.Utils;

import javafx.fxml.FXMLLoader;
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

}
