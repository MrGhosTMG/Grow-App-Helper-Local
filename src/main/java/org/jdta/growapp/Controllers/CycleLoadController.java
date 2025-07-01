package org.jdta.growapp.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.Controllers.ToolsControlls.*;
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
    public ImageView slider_image;
    public AnchorPane central_view;
    public Button Tips_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Model.getInstance().getView().
        add_new_cycle_btn.setOnAction(actionEvent -> onNewCycle());
        exit_btn.setOnAction(actionEvent -> onExit());
        nutr_btn.setOnAction(actionEvent -> onNutrients());
        light_btn.setOnAction(actionEvent -> onSetLight());
        info_btn.setOnAction(actionEvent -> onInfo());
        //addListeners();
        log_out_btn.setOnAction(event -> onLogin());
        moist_btn.setOnAction(actionEvent -> onWatering());
        train_btn.setOnAction(actionEvent -> onTraining());
    }
    // On actions section

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
                (InfoController controller) -> {});
        if (root != null) {
            central_view.getChildren().setAll(root);
        }
    }

    private void onSetLight() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/tools/LightTimeStageCycle.fxml",
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

        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/tools/NutrientCycle.fxml",
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
    }

    private void onExit() {
        DialogUtils.confirm("Do you really want to exit?", () -> {
            Stage stage = (Stage) exit_btn.getScene().getWindow();
            stage.close();
        });

//        Stage stage = (Stage) exit_btn.getScene().getWindow();
//        Model.getInstance().getView().closeStage(stage);
    }
//    public void addListeners() {
//        Model.getInstance().getView().getUserSelectedButton().addListener((observableValue, oldVal, newVal) -> {
//            switch (newVal) {
//                case "Photo" ->
//                        Model.getInstance().getView().getPhotoView();// .showPhotoFieldInPane(down_border_pane_top);
//                case "History" ->
//                        Model.getInstance().getView().getHistoryView();
//                case "Nutrients" ->
//                        Model.getInstance().getView().getNutrientsView();
//                case "Info" ->
//                    Model.getInstance().getView().getInfoView();
//                default -> {
//                    Model.getInstance().getView().getUserView();
//                }
//            }
//        });
//    }
}
