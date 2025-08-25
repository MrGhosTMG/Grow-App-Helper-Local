package org.jdta.growapp.Controllers.ToolsControlls;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.LightStageUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    public Label potinfo_soilmix;
    public TextArea notes_area;
    public Label seed_door_info_lbl;
    private Cycle cycle;

    private Timeline updateLightTimeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    private void loadInfoLabels() {
        sort_name.setText(cycle.getName());
        start_date_lbl.setText(cycle.getStartDateTime().toString());
        ETA_date_lbl.setText(cycle.getEtaDateTime().toString());
        grow_stage_and_days_lbl.setText(cycle.getGrowStage().name() + " - " +
                cycle.getStageDurationDays().getOrDefault(cycle.getGrowStage(), 0) + "days");
        long totalDays = ChronoUnit.DAYS.between(cycle.getStartDateTime().toLocalDate(), LocalDate.now());
        total_grow_days_lbl.setText(totalDays + " total grow days");
        light_stage.setText(LightStageUtils.getLightStatusLabel(cycle));
        light_on_hour.setText("Light " + cycle.getLightDayHours() + "h");
        light_of_hour.setText("Dark " + cycle.getLightNightHours() + "h");
        //training_lbl.setText(cycle.getTrainingType().name());

        try {
            Path previewPath = Paths.get("Photos", "Cycle_" + cycle.getId(), "preview.JPG");
            File file = previewPath.toFile();
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                image_cycle.setPreserveRatio(false);
                image_cycle.setFitWidth(image_cycle.getBoundsInParent().getWidth());
                image_cycle.setFitHeight(image_cycle.getBoundsInParent().getHeight());
                image_cycle.setImage(image);
            } else {
                System.err.println("Файл не существует: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке preview: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setCycle(Cycle selected) {
        this.cycle = selected;
        Model.getInstance().loadCycleTransitions(cycle);
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
