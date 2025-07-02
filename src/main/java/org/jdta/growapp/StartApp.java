package org.jdta.growapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.PreferencesUtils;


// Gендальф©
public class StartApp extends Application {

    @Override
    public void start(Stage stage) {
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
