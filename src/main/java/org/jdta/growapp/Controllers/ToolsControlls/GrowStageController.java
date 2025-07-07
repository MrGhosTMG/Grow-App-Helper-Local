package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jdta.growapp.Enums.GrowStages;

import java.net.URL;
import java.util.ResourceBundle;

public class GrowStageController implements Initializable {
    public AnchorPane parent_GR_create_anchor;
    public Label new_started_lbl;
    public Label days_of_stage_lbl;
    public CheckBox new_check;
    public CheckBox started_check;
    public ChoiceBox stage_picker;
    public DatePicker start_date_picker;
    public Button apply_btn;
    public ProgressBar grow_progress_bar;
    private ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stage_picker = new ChoiceBox<GrowStages>();
        stage_picker.getItems().addAll(GrowStages.values());


        stage_picker.setOnAction(actionEvent -> {
            GrowStages picked = (GrowStages) stage_picker.getValue();
            if (picked != null && !pickedStages.contains(picked)) {
                pickedStages.add(picked);
            }
        });
    }

    //тут пока бросил Нужно под первые 5 стадий подогнать
//    ProgressBar dynamicBar = new ProgressBar(0.5);
//dynamicBar.progressProperty().
//
//    addListener((obs, oldVal, newVal) ->
//
//    {
//        double progress = newVal.doubleValue();
//        if (progress < 0.3) {
//            dynamicBar.setStyle("-fx-accent: #FF5252;"); // Красный
//        } else if (progress < 0.7) {
//            dynamicBar.setStyle("-fx-accent: #FFD740;"); // Жёлтый
//        } else {
//            dynamicBar.setStyle("-fx-accent: #69F0AE;"); // Зелёный
//        }
//    });

}
