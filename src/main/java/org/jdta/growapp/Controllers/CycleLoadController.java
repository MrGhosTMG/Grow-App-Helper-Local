package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jdta.growapp.Controllers.ToolsControlls.LightTimeStageController;
import org.jdta.growapp.Controllers.ToolsControlls.NutrientsController;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class CycleLoadController  implements Initializable {

    public UserController userController;


    public Label cycle_name_lbl;
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
    public ComboBox select_cycle_combo_box;
    public ImageView slider_image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Model.getInstance().getView().
        add_new_cycle_btn.setOnAction(actionEvent -> onNewCycle());
        exit_btn.setOnAction(actionEvent -> onExit());
        log_out_btn.setOnAction(actionEvent -> onLogin());
        nutr_btn.setOnAction(actionEvent -> onNutrients());
        light_btn.setOnAction(actionEvent -> onSetLight());
    }




    // On actions section

    private void onSetLight() {
        FXMLUtils.openModalWithCallback(
                "/FXML/tools/LightTimeStage.fxml",
                "Set Light Time",
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
    }

    private void onNutrients() {
        FXMLUtils.openModalWithCallback("/FXML/userBoard/Nutrients.fxml", "Nutrients",
                (NutrientsController controller) -> {
            controller.setOnSaveNutr(() -> {System.out.println("Save button clicked from CycleLoadController!");});
            controller.setOnAddNutr(() -> {System.out.println("Add button clicked from CycleLoadController!");});
        });
    }

    private void onNewCycle() {
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        Model.getInstance().getView().showCycleCreateWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    private void onLogin() {
        DialogUtils.confirm("Logout from application?", () -> {
            Stage stage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getView().showLoginWindow();
            stage.close();
        });

//        Stage stage = (Stage) exit_btn.getScene().getWindow();
//        Model.getInstance().getView().showLoginWindow();
//        Model.getInstance().getView().closeStage(stage);
    }

    private void onExit() {
        DialogUtils.confirm("Do you really want to exit?", () -> {
            Stage stage = (Stage) exit_btn.getScene().getWindow();
            stage.close();
        });

//        Stage stage = (Stage) exit_btn.getScene().getWindow();
//        Model.getInstance().getView().closeStage(stage);
    }
}
