package org.jdta.growapp.Views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jdta.growapp.Utils.ConfirmDialogController;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class View {

    private final StringProperty userSelectedButton;

    private AnchorPane loginView;
    private AnchorPane nutrientsView;
    private AnchorPane historyView;
    private AnchorPane photoView;
    private AnchorPane userView;
    private AnchorPane regView;
    private AnchorPane cycleCreateView;
    private BorderPane selectedCycleView;

    public View() {
        this.userSelectedButton = new SimpleStringProperty("");
    }

    //getters

    public StringProperty getUserSelectedButton() {
        return userSelectedButton;
    }



    // FXML read section

    public AnchorPane getNutrientsView() {
        if (nutrientsView == null) {
            try {
                nutrientsView = new FXMLLoader(getClass().getResource("/FXML/userBoard/Nutrients.fxml")).load();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return nutrientsView;
    }

    public AnchorPane getHistoryView() {
        if (historyView == null) {
            try {
                historyView = new  FXMLLoader(getClass().getResource("/FXML/userBoard/History.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return historyView;
    }

    public AnchorPane getPhotoView() {
        if (photoView == null) {
            try {
                photoView = new FXMLLoader(getClass().getResource("/FXML/userBoard/Photo.fxml")).load();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return photoView;
    }

    public BorderPane getSelectedCycleView() {
        if (selectedCycleView == null) {
            try {
                selectedCycleView = new FXMLLoader(getClass().getResource("/FXML/Cycle.fxml")).load();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return selectedCycleView;
    }

    public AnchorPane getLoginView() {
        if (loginView == null) {
            try {
                loginView = new FXMLLoader(getClass().getResource("/FXML/Login.fxml")).load();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return loginView;
    }

    public AnchorPane getUserView() {
        if (userView == null) {
            try {
                userView = new FXMLLoader(getClass().getResource("/FXML/User.fxml")).load();
            }catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
            return userView;
    }

    public AnchorPane getRegView() {
        if (regView == null) {
            try {
                regView = new FXMLLoader(getClass().getResource("/FXML/Reg.fxml")).load();
            }catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return regView;
    }

    public AnchorPane getCycleCreateView() {
        if (cycleCreateView == null) {
            try {
                cycleCreateView = new FXMLLoader(getClass().getResource("/FXML/tools/CycleCreate.fxml")).load();
            }catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        return cycleCreateView;
    }

    //show window section

    public void showPhotoFieldInPane(AnchorPane parentPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/userBoard/Photo.fxml"));
            AnchorPane photoPane = loader.load();
            parentPane.getChildren().setAll(photoPane); // заменяет все узлы
            userSelectedButton.set("Photo");
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }



    public void showSelectedCycleWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Cycle.fxml"));
        createStage(loader);
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        createStage(loader);
    }

    public void showCycleCreateWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/tools/CycleCreate.fxml"));
        createStage(loader);
    }

    public void showRegWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Reg.fxml"));
        createStage(loader);
    }

    public void showUserWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User.fxml"));
        createStage(loader);
    }





    //stage create method for show methods only
    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        }catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Grow App Helper");
        stage.setResizable(false);
        stage.show();
    }

    // stage close method
    public void closeStage(Stage stage) {
        stage.close();
    }


}
