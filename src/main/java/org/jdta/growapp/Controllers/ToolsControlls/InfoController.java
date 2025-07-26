package org.jdta.growapp.Controllers.ToolsControlls;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.LightStageUtils;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    public AnchorPane parent_anchor;
    public Label sort_name;
    public Label start_date_lbl;
    public Label ETA_date_lbl;
    public ImageView image_cycle;
    public Label light_on_hour;
    public Label light_of_hour;
    public Label light_stage;
    public Label moist_date;
    public Label nutr_lbl;
    public Label clean_lbl;
    public Label moist_remind_lbl;
    public Label training_lbl;
    public Label training_remind_lbl;
    public Label grow_stage_and_days_lbl;
    public Label total_grow_days_lbl;
    private Cycle cycle;

    private Timeline updateLightTimeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    private void loadInfoLabels() {
        sort_name.setText(cycle.getName());
        start_date_lbl.setText(cycle.getStartDateTime().toString());
        ETA_date_lbl.setText(cycle.getEtaDateTime().toString());
        //grow_stage_and_days_lbl.setText(cycle.getGrowStage().name());
        light_stage.setText(LightStageUtils.getLightStatusLabel(cycle));
        light_on_hour.setText("Light " + cycle.getLightDayHours() + "h");
        light_of_hour.setText("Dark " + cycle.getLightNightHours() + "h");
        //training_lbl.setText(cycle.getTrainingType().name());

        if (cycle.getImagePath() != null) {
            try {
                File photoFile = new File(cycle.getImagePath());
                if (photoFile.exists()) {
                    image_cycle.setImage(new Image(photoFile.toURI().toString()));
                }
            }catch (Exception e) {
                System.err.println("Image load error " + e.getMessage());
            }
        }
    }

    public void setCycle(Cycle selected) {
        this.cycle = selected;
        loadInfoLabels();
        updateLightPhase();
        updateLightStatus();
    }

    private void updateLightStatus() {
        updateLightTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateLightPhase()),
                new KeyFrame(Duration.seconds(60)));
        updateLightTimeline.setCycleCount(Timeline.INDEFINITE);
        updateLightTimeline.play();
    }

    private void updateLightPhase() {
        if (cycle != null) {
            light_stage.setText(LightStageUtils.getLightStatusLabel(cycle));
        }
    }
}
