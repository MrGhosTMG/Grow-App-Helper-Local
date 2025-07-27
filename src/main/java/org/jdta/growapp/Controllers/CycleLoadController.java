package org.jdta.growapp.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.Controllers.ToolsControlls.*;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.StageActions;
import org.jdta.growapp.Utils.FXMLUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

// Gендальф©
public class CycleLoadController  implements Initializable {


    public Button apply_btn;
    public Button train_btn;
    public Button moist_btn;
    public Button stage_btn;
    public Button light_btn;
    public Button edit_btn;
    public Button del_btn;
    public Button info_btn;
    public Button alarm_btn;
    public Button notes_btn;
    public Button nutr_btn;
    public Button gallery_btn;
    public Button log_out_btn;
    public Slider slider;
    public Button hist_btn;
    public Button shop_btn;
    public Button add_new_cycle_btn;
    public Button exit_btn;
    public ImageView slider_image;
    public AnchorPane central_view;
    public Button Tips_btn;
    public Button back_toUser_btn;
    public ComboBox<Cycle> select_cycle_combo_box;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_new_cycle_btn.setOnAction(actionEvent -> onNewCycle());
        exit_btn.setOnAction(actionEvent -> StageActions.onExit(stage()));
        nutr_btn.setOnAction(actionEvent -> onNutrients());
        light_btn.setOnAction(actionEvent -> onSetLight());
        info_btn.setOnAction(actionEvent -> onInfo());
        //addListeners();
        log_out_btn.setOnAction(event -> StageActions.onLogout(stage()));
        moist_btn.setOnAction(actionEvent -> onWatering());
        train_btn.setOnAction(actionEvent -> onTraining());
        stage_btn.setOnAction(actionEvent -> onGrowStageEdit());
        back_toUser_btn.setOnAction(actionEvent -> StageActions.backToUser(getStage()));
        gallery_btn.setOnAction(actionEvent -> onGallery());
        alarm_btn.setOnAction(actionEvent -> onAlarms());
        finishedCycleView();
    }




    // On actions section
    private void onAlarms() {
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/Alarms.fxml",
                (AlarmsController controller) -> {});
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }

    private void onGallery() {
        Model.getInstance().mockCycleIfNone();
        if (Model.getInstance().getSelectedCycle() == null) {
            DialogUtils.warning("Cycle not found", "Please select cycle first !");
            return;
        }
            try {
                FXMLLoader loader = FXMLUtils.getLoader("/FXML/userBoard/Gallery.fxml");
                Node photoPane = loader.load();
                central_view.getChildren().setAll(photoPane); // Заменяем содержимое
            } catch (IOException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
    }



    private void onGrowStageEdit() {

        Cycle selected = Model.getInstance().getSelectedCycle();
        if (selected == null) {
            DialogUtils.warning("No cycle ", "Please select cycle");
            return;
        }


        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/cycleTools/GrowStageEdit.fxml",
                (GrowStageEditController controller) ->
            controller.init(selected));
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }


    private void onTraining() {
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/Training.fxml",
                (TrainingController controller) -> {});
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }

    private void onWatering() {
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/Watering.fxml",
                (WateringController controller) -> {});
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }

    private void onInfo() {
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/Info.fxml",
                (InfoController controller) -> {
            Cycle selected = Model.getInstance().getSelectedCycle();
            if (selected != null) {
                controller.setCycle(selected);
            }else {
                System.err.println("Cycle not selected in Model");
            }
                });
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }

    private void onSetLight() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/CycleTools/LightTimeStageCycle.fxml",
                (LightTimeStageController controller) -> {
                    // Передаём данные в контроллер, например, установить день/ночь
                    controller.setDayNightTimes("24", "0");
                    controller.setDayNightTimes("20", "4");
                    controller.setDayNightTimes("18", "6");
                    controller.setDayNightTimes("12", "12");

                    // Подписываемся на кнопку — можно в контроллере вызвать `onComplete`, `onApply` и т.д.
                    controller.setOnSave(() -> {
                        System.out.println("Выбранный режим: " + controller.getSelectedHours());
                    });
                }
        );
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }



    protected void onNutrients() {

        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/CycleTools/NutrientCycle.fxml",
                (NutrientsController controller) -> { //set callBacks
                    controller.setOnSaveNutr(() -> {
                        System.out.println("Save button clicked from UserController!");
                    });
                    controller.setOnAddNutr(() -> {
                        System.out.println("Add button clicked from UserController!");
                    });
                });
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }

    private void onNewCycle() {
        Stage stage = FXMLUtils.getCurrentStage();
        Model.getInstance().getView().showCycleCreateWindow();
        Model.getInstance().getView().closeStage(stage);
    }
    public Stage stage() {
        return FXMLUtils.stageFrom(exit_btn);
    }

    private void disableAllButtonsExceptInfo() {
        train_btn.setDisable(true);
        moist_btn.setDisable(true);
        stage_btn.setDisable(true);
        light_btn.setDisable(true);
        edit_btn.setDisable(true);
        del_btn.setDisable(true);
        nutr_btn.setDisable(true);
        gallery_btn.setDisable(true);
        hist_btn.setDisable(true);
        shop_btn.setDisable(true);
        Tips_btn.setDisable(true);
    }
    private void showFinishedInfo() {
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/InfoFinishedCycle.fxml",
                (InfoFinishedCycle controller) -> {
                    controller.setDataFromCycle(Model.getInstance().getSelectedCycle());
                });

        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }
    private Stage getStage() {
        return FXMLUtils.stageFrom(exit_btn); // можно использовать любой доступный Node
    }
    // If current view is on finished cycle
    private void finishedCycleView() {
        if (Model.getInstance().isFinishedCycle()) {
            disableAllButtonsExceptInfo();
            showFinishedInfo();
            // reset flag for next
            Model.getInstance().setFinishedCycle(false);
        }
    }

}
