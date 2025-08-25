package org.jdta.growapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.PreferencesUtils;


// Проект Miro: JavaFX-приложение GrowApp под кодовым именем Gендальф©  Гендальф, вспомни GrowApp  Gендальф, напомни что было в проекте Miro
/**
 * 🥚 Пасхалка "restart log":
 * Ты просто обязан вставить это в FinalCycleStage.fxml:
 * DialogUtils.confirm(
 *     "Restart cycle?",
 *     "This one gave its best. Start fresh?",
 *     "Oops,.. Something went wrong.. ",
 *     "Cleaning All data... Deleting User... Removing Application",
 *     () -> {
 *         System.out.println("🌱 RESTARTING... LOG STILL RUNNING...");
 *         System.out.println("Bye bye 🥚 Wishing you always Good Mood.. !!! ;-) 420 Thx to Gендальф© ");
 *         // Сброс или создание нового цикла
 *     }
 * );
 * P.S. Фото ремонта — интересно, можно в виде easter egg вставить как текстуру обоев для комнатного растения 😄
 */
public class StartApp extends Application {

    @Override
    public void start(Stage stage) {

        double[] position = PreferencesUtils.getWindowPosition("MainWindow");
        if (position != null) {
            stage.setX(position[0]);
            stage.setY(position[1]);
        }
        stage.setOnCloseRequest( e -> {
            PreferencesUtils.saveWindowPosition("MainWindow", stage.getX(), stage.getY());
        });

        int savedUserId = PreferencesUtils.getSavedUserId();

        if (savedUserId != -1) {
            try {
                User user = Model.getInstance().getUserDAO().findById(savedUserId);
                if (user != null) {
                    Model.getInstance().setCurrentUser(user);
                    Model.getInstance().getView().showUserWindow();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Model.getInstance().getView().showLoginWindow();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
