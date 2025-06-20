package org.jdta.growapp.Views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jdta.growapp.Utils.FXMLUtils;


import java.io.IOException;
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
    private AnchorPane lightStageView;
    private BorderPane selectedCycleView;

    public View() {
        this.userSelectedButton = new SimpleStringProperty("");
    }

    //getters

    public StringProperty getUserSelectedButton() {
        return userSelectedButton;
    }



    // FXML read section
    public AnchorPane getLightStageView() {
        if (lightStageView == null) {
            lightStageView = FXMLUtils.loadFXML("/FXML/tools/cycleCreateTools/LightTimeStage.fxml");
        }
        return lightStageView;
    }


    public AnchorPane getNutrientsView() {
        if (nutrientsView == null) {
            nutrientsView = FXMLUtils.loadFXML("/FXML/userBoard/Nutrients.fxml");
        }
        return nutrientsView;
    }

    public AnchorPane getHistoryView() {
        if (historyView == null) {
            FXMLUtils.loadFXML("/FXML/userBoard/History.fxml");
        }
        return historyView;
    }

    public AnchorPane getPhotoView() {
        if (photoView == null) {
            FXMLUtils.loadFXML("/FXML/userBoard/Photo.fxml");
        }
        return photoView;
    }

    public BorderPane getSelectedCycleView() {
        if (selectedCycleView == null) {
            FXMLUtils.loadFXML("/FXML/Cycle.fxml");
        }
        return selectedCycleView;
    }

    public AnchorPane getLoginView() {
        if (loginView == null) {
            FXMLUtils.loadFXML("/FXML/Login.fxml");
        }
        return loginView;
    }

    public AnchorPane getUserView() {
        if (userView == null) {
            FXMLUtils.loadFXML("/FXML/User.fxml");

        }
            return userView;
    }

    public AnchorPane getRegView() {
        if (regView == null) {
            FXMLUtils.loadFXML("/FXML/Reg.fxml");
        }
        return regView;
    }

    public AnchorPane getCycleCreateView() {
        if (cycleCreateView == null) {
            FXMLUtils.loadFXML("/FXML/tools/CycleCreate.fxml");
        }
        return cycleCreateView;
    }

    //show window section
    public void showLightStageWindow() {
        FXMLUtils.openModalWindow("/FXML/tools/cycleCreateTools/LightTimeStage.fxml", "Set Light Time");
    }

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
        FXMLUtils.openModalWindow("/FXML/Cycle.fxml", "Cycle");
    }

    public void showLoginWindow() {
        FXMLUtils.openModalWindow("/FXML/Login.fxml", "Login");
    }

    public void showCycleCreateWindow() {
        FXMLUtils.openModalWindow("/FXML/tools/CycleCreate.fxml", "CycleCreate");
    }

    public void showRegWindow() {
        FXMLUtils.openModalWindow("/FXML/Reg.fxml", "Reg");
    }

    public void showUserWindow() {
        FXMLUtils.openModalWindow("/FXML/User.fxml", "User");
    }


    // stage close method
    public void closeStage(Stage stage) {
        stage.close();
    }
}
