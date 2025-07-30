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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

// Gендальф©
public class CycleLoadController  implements Initializable {


    public Button apply_btn, train_btn, moist_btn, stage_btn, light_btn;
    public Button edit_btn, del_btn, info_btn, alarm_btn,notes_btn;
    public Button nutr_btn, gallery_btn, log_out_btn, hist_btn, shop_btn;
    public Button add_new_cycle_btn, exit_btn, tipsBtn, back_toUser_btn;
    public Slider slider;
    public ImageView slider_image;
    public AnchorPane central_view;
    public ComboBox<Cycle> select_cycle_combo_box;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCyclesComboBox();
        apply_btn.setOnAction(actionEvent -> onApplyCycle());
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



    private void initCyclesComboBox() {
        int userId = Model.getInstance().getCurrentUser().getId();
        try {
            var cycles = Model.getInstance().getCycleDAO().findAllByUserId(userId);
            if (cycles != null && !cycles.isEmpty()) {
                select_cycle_combo_box.getItems().setAll(cycles);

                Cycle selectedCycle = Model.getInstance().getSelectedCycle();
                if (selectedCycle != null && cycles.contains(selectedCycle)) {
                    select_cycle_combo_box.setValue(selectedCycle);
                   if (selectedCycle.getGrowStage() != null) {
                       slider.setValue(selectedCycle.getGrowStage().ordinal());
                   }
                }
                else {
                    select_cycle_combo_box.setValue(cycles.get(0));
                    Model.getInstance().setSelectedCycle(cycles.get(0));
                    if (cycles.get(0).getGrowStage() != null) {
                        slider.setValue(selectedCycle.getGrowStage().ordinal());
                    }
                    slider.setValue(cycles.get(0).getGrowStage().ordinal());
                }
            }
        } catch (SQLException e) {
            System.out.println("No cycle to set" + e.getMessage());
            e.printStackTrace();
        }
    }


    // On actions section

    private void onApplyCycle() {
        Cycle selected = select_cycle_combo_box.getValue();
        if (selected == null) {
            DialogUtils.warning("No Cycle", "Select cycle to show");
            return;
        }
        Model.getInstance().setSelectedCycle(selected);
        if (selected.getGrowStage() != null) {
            slider.setValue(selected.getGrowStage().ordinal());
        }
        slider.setValue(selected.getGrowStage().ordinal());
        DialogUtils.info("Cycle Applied", "Cycle '" + selected.getName() + "' is shown");
        central_view.getChildren().clear();
    }

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
            controller.init(selected, slider));
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
        tipsBtn.setDisable(true);
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
