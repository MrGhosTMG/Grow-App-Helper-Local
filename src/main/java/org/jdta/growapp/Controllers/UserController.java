package org.jdta.growapp.Controllers;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.Controllers.ToolsControlls.HistoryController;
import org.jdta.growapp.Controllers.ToolsControlls.NutrientsController;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.StageActions;
import org.jdta.growapp.Utils.FXMLUtils;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController implements Initializable {


    public AnchorPane down_border_pane_top;
    public ImageView image_view_board;
    public Button select_cycle_btn;
    public Button add_new_cycle_btn;
    public ListView list_view_cycle_info;
    public Button photos_btn;
    public Button history_btn;
    public Button change_user_btn;
    public Button exit_btn;
    public Button nutr_btn;
    public Button shop_btn;
    public Label select_lbl;
    public ComboBox select_cycle_combo_box;
    public Label selected_user_name;
    public Label cycle_name_info_lbl;
    public ScrollPane top_scroll_pane;
    public ScrollBar scrl_bar;
    public TextArea info_text_fld;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        select_cycle_btn.setOnAction(actionEvent -> onSelectedCycle());
        exit_btn.setOnAction(actionEvent -> StageActions.onExit(stage()));
        change_user_btn.setOnAction(event -> StageActions.onLogout(stage()));
        add_new_cycle_btn.setOnAction(actionEvent -> onNewCycle());
        photos_btn.setOnAction(actionEvent -> onPhoto());
        history_btn.setOnAction(actionEvent -> onHistory());
        nutr_btn.setOnAction(actionEvent -> onNutrients());
        addListeners();

// Добавляем
        if (Model.getInstance().getFinishedCycles().isEmpty()) {
            //test MOCk
            Cycle test = new Cycle();
            test.setName("TestCycle 001");
            test.setStartDateTime(LocalDateTime.now().minusDays(50));
            test.setEtaDateTime(LocalDateTime.now());
            test.setYieldGrams(320);
            test.setFinished(true);
            test.setSortType("AK-47");
            test.setPotCapacity(5.0);
            test.setIndoorOutdoor("Indoor");

// Доп. стадии
            test.setVegetationDays(15);
            test.setFloweringDays(20);
            test.setPreFloweringDays(5);
            test.setDryingDays(10);
            test.setTotalGrowDays(50);
            Model.getInstance().getFinishedCycles().add(test);
            //Model.getInstance().getFinishedCycles().clear();
        }
    }


    public void addListeners() {
        Model.getInstance().getView().getUserSelectedButton().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Photo" ->
                        Model.getInstance().getView().getPhotoView();// .showPhotoFieldInPane(down_border_pane_top);
                case "History" ->
                        Model.getInstance().getView().getHistoryView();
                case "Nutrients" ->
                        Model.getInstance().getView().getNutrientsView();
                default -> {
                    Model.getInstance().getView().getUserView();
                }
            }
        });
    }

    // On actions section
    protected void onNutrients() {

        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/Nutrients.fxml",
                (NutrientsController controller) -> { //set callBacks
                    controller.setOnSaveNutr(() -> {
                        System.out.println("Save button clicked from UserController!");
                    });
                    controller.setOnAddNutr(() -> {
                        System.out.println("Add button clicked from UserController!");
                    });
                });
        if (root != null) {
            down_border_pane_top.getChildren().setAll(root);
        }
    }

    private void onHistory() {
        Node root = FXMLUtils.loadWithControllerCallBackActions("/FXML/userBoard/History.fxml",
                (HistoryController controller) -> {
                    controller.setCycles(Model.getInstance().getFinishedCycles());
                });

        if (root != null) {
            down_border_pane_top.getChildren().setAll(root);
        }
    }

    private void onPhoto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/userBoard/Photo.fxml"));
            ScrollPane photoPane = loader.load();
            down_border_pane_top.getChildren().setAll(photoPane); // Заменяем содержимое
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void onSelectedCycle() {
        Stage stage = FXMLUtils.stageFrom(exit_btn);
        Model.getInstance().getView().showSelectedCycleWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    private void onNewCycle() {
        Stage stage = FXMLUtils.stageFrom(exit_btn);
        Model.getInstance().getView().showCycleCreateWindow();
        Model.getInstance().getView().closeStage(stage);
    }
    public Stage stage() {
        return FXMLUtils.stageFrom(exit_btn);
    }
}
