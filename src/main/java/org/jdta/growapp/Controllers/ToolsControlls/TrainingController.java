package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.jdta.growapp.Enums.TrainingType;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class TrainingController implements Initializable {
    public RadioButton LST_rb;
    public RadioButton DEF_rb;
    public RadioButton SCRG_rb;
    public RadioButton TOP_rb;
    public RadioButton FIM_rb;
    public RadioButton ML_rb;
    public RadioButton other_rb;
    public Label delay_lbl;
    public TextField days_in_fld;
    public Button add_days_btn;
    public RadioButton ml_main_step_rb;
    public RadioButton adit_step_rb;
    public Button set_step_btn;
    public Button set_train_btn;
    public AnchorPane info_top_anchor;
    public BorderPane parent_border_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<RadioButton, TrainingType> trainingMap = Map.of(
                LST_rb, TrainingType.LST,
                DEF_rb, TrainingType.DEF,
                SCRG_rb, TrainingType.SCRG,
                TOP_rb, TrainingType.TOP,
                FIM_rb, TrainingType.FIM,
                ML_rb, TrainingType.ML,
                other_rb, TrainingType.OTHER
        );

        trainingMap.forEach((button, type) -> {
            button.setOnAction(e -> showTrainingInfo(type));
        });

    }


    private void showTrainingInfo(TrainingType type) {
        info_top_anchor.getChildren().clear();

        Label title = new Label(type.getTitle());
        title.setTextFill(type.getColor());
        title.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        Label desc = new Label(type.getDescription());
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: white;");

        VBox box = new VBox(5, title, desc);
        box.setPadding(new Insets(10));
        info_top_anchor.getChildren().add(box);
    }
}
