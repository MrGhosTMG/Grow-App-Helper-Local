package org.jdta.growapp.Controllers;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;

import java.io.IOException;
import java.net.URL;
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
        exit_btn.setOnAction(actionEvent -> onExit());
        change_user_btn.setOnAction(actionEvent -> onLogin());
        add_new_cycle_btn.setOnAction(actionEvent -> onNewCycle());
        photos_btn.setOnAction(actionEvent -> onPhoto());
        history_btn.setOnAction(actionEvent -> onHistory());
        nutr_btn.setOnAction(actionEvent -> onNutrients());
        addListeners();
    }




    public void addListeners() {
        Model.getInstance().getView().getUserSelectedButton().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "Photo" -> Model.getInstance().getView().getPhotoView();// .showPhotoFieldInPane(down_border_pane_top);
                case "History" -> Model.getInstance().getView().getHistoryView();// .showPhotoFieldInPane(down_border_pane_top);
                case "Nutrients" -> Model.getInstance().getView().getNutrientsView();// .showPhotoFieldInPane(down_border_pane_top);
                default -> {
                    Model.getInstance().getView().getUserView();
                }
            }
        });
    }


    // On actions section
    private void onNutrients() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/userBoard/Nutrients.fxml"));
        try {
            down_border_pane_top.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e); //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void onHistory() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/userBoard/History.fxml"));
        try {
            down_border_pane_top.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
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
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        Model.getInstance().getView().showSelectedCycleWindow();
        Model.getInstance().getView().closeStage(stage);
    }


    private void onNewCycle() {
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        Model.getInstance().getView().showCycleCreateWindow();
        Model.getInstance().getView().closeStage(stage);
    }


    private void onLogin() {
        DialogUtils.confirm("Logout from application?", () -> {
            Stage stage = (Stage) change_user_btn.getScene().getWindow();
            Model.getInstance().getView().showLoginWindow();
            stage.close();
        });

//        Stage stage = (Stage) exit_btn.getScene().getWindow();
//        Model.getInstance().getView().showLoginWindow();
//        Model.getInstance().getView().closeStage(stage);
    }
    private void onExit() {
//            Stage stage = (Stage) exit_btn.getScene().getWindow();
//            Model.getInstance().getView().closeStage(stage);
        DialogUtils.confirm("Do you really want to exit?", () -> {
            Stage stage = (Stage) exit_btn.getScene().getWindow();
            stage.close();
        });

    }

}
