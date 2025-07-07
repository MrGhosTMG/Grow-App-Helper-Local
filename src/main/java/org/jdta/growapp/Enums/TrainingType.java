package org.jdta.growapp.Enums;



public enum TrainingType {
    LST("/FXML/TipTools/LSTtip.fxml"),
    DEFOLIATION("/FXML/TipTools/DefoliationTip.fxml"),
    SCROG("/FXML/TipTools/ScrogTip.fxml"),
    TOP("/FXML/TipTools/ToppingTip.fxml"),
    FIM("/FXML/TipTools/FimTip.fxml"),
    MAINLINING("/FXML/TipTools/MainLiningTip.fxml"),
    OTHER("/FXML/TipTools/MainTip.fxml");

    private final String fxmlPath;
    //private final String showDescription;

    TrainingType(String fxmlPath) {
        this.fxmlPath = fxmlPath;
        //this.showDescription = showDescription;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}


