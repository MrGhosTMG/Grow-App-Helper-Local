package org.jdta.growapp.Controllers;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.Controllers.ToolsControlls.HistoryController;
import org.jdta.growapp.Controllers.ToolsControlls.NutrientsController;
import org.jdta.growapp.Controllers.ToolsControlls.PhotoController;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.*;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController implements Initializable {


    private static final File PHOTO_DIR = new File("Photos/cycle_photos");
    public AnchorPane down_border_pane_top;
    public TextArea info_text_fld;
    public Button select_cycle_btn;
    public Button add_new_cycle_btn;
    public Button del_cycle_btn;
    public Button photos_btn;
    public Button history_btn;
    public Button change_user_btn;
    public Button exit_btn;
    public Button del_all_photo_btn;
    public Button nutr_btn;
    public Button shop_btn;
    public ComboBox<Cycle> select_cycle_combo_box;
    public Label selected_user_name;
    public Label select_lbl;
    public Label cycle_name_info_lbl;



    public Label status_lbl;
    public ImageView image_view_board;
    public ScrollBar scrl_bar;
    public ListView<String> list_view_cycle_info;
    public ProgressIndicator spinner;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit_btn.setOnAction(actionEvent -> StageActions.onExit(getStage()));
        change_user_btn.setOnAction(event -> StageActions.onLogout(getStage()));
        add_new_cycle_btn.setOnAction(actionEvent -> onNewCycle());
        photos_btn.setOnAction(actionEvent -> onPhoto());
        history_btn.setOnAction(actionEvent -> onHistory());
        nutr_btn.setOnAction(actionEvent -> onNutrients());
        addListeners();
        del_all_photo_btn.setOnAction(actionEvent -> onDeleteAllPhoto());
        select_cycle_combo_box.setOnAction(actionEvent -> showSelectedCycleInfo());
        select_cycle_btn.setOnAction(actionEvent -> onSelectedCycle());
        loadUserCycle();
        initMOCK();
        del_cycle_btn.setOnAction(actionEvent -> deleteSelectedCycle());
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

    private void loadUserCycle() {
        int userId = Model.getInstance().getCurrentUser().getId();
        try {
            List<Cycle> cycles = Model.getInstance().getCycleDAO().findAllByUserId(userId);
            select_cycle_combo_box.getItems().setAll(cycles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedCycle() {
        Cycle selected = select_cycle_combo_box.getValue();
        if (selected != null) {
            try {
                Model.getInstance().getCycleDAO().deleteById(selected.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void onSelectedCycle() {
        Cycle selected = select_cycle_combo_box.getValue();
        if (selected != null) {
            Model.getInstance().setSelectedCycle(selected);
            Stage stage = FXMLUtils.stageFrom(select_cycle_btn);
            Model.getInstance().getView().showSelectedCycleWindow();
            Model.getInstance().getView().closeStage(stage);
        }
//        Stage stage = FXMLUtils.stageFrom(exit_btn);
//        Model.getInstance().getView().showSelectedCycleWindow();
//        Model.getInstance().getView().closeStage(stage);
    }

    private void showSelectedCycleInfo() {
        Cycle selected = select_cycle_combo_box.getValue();
        if (selected != null) {
            displayCycleInfo(selected);
        }
    }

    private void displayCycleInfo(Cycle cycle) {
        list_view_cycle_info.getItems().clear();

        list_view_cycle_info.getItems().addAll(
                "Name: " + cycle.getName(),
                "Sort: " + cycle.getSortType(),
                "Start: " + cycle.getStartDateTime(),
                "Growing in : " + cycle.getIndoorOutdoor(),
                "Pot: " + cycle.getPotCapacity() + "L",
                "Light: " + cycle.getLightDayHours() + " / " + cycle.getLightNightHours() + "h",
                "Estimated End: " + cycle.getEtaDateTime()
        );

        // Image preview
        if (cycle.getImagePath() != null) {
            try {
                System.out.println("cycle.getImagePath() = " + cycle.getImagePath());
                File file = new File(cycle.getImagePath());
                System.out.println("Absolute path: " + file.getAbsolutePath());
                System.out.println("Exists? " + file.exists());
                if (file.exists()) {
                    image_view_board.setImage(new Image(file.toURI().toString()));
                }
            } catch (Exception e) {
                System.err.println("Ошибка при загрузке preview: " + e.getMessage());
            }
        }
    }

    private void onNewCycle() {

        Stage stage = FXMLUtils.stageFrom(exit_btn);
        Model.getInstance().getView().showCycleCreateWindow();
        Model.getInstance().getView().closeStage(stage);
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

    private void onDeleteAllPhoto() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/userBoard/Photo.fxml"));
            ScrollPane photoPane = loader.load();
            PhotoController controller = loader.getController();

            down_border_pane_top.getChildren().setAll(photoPane); // Обновим UI
            controller.setParentStage(getStage());
            javafx.application.Platform.runLater(controller::deleteAllPhotosWithConfirm);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Stage getStage() {
        return FXMLUtils.stageFrom(exit_btn);
    }

    private void initMOCK() {
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
}
