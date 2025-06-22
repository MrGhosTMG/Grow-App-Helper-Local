package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class CycleCreateController implements Initializable {
    public Button web_cat_btn;
    public Button add_note_btn;
    public TextArea text_aea_fld;
    public Button delete_img_btn;
    public Button import_btn;
    public Button back_btn;
    public Button log_out_btn;
    public Button exit_btn;
    public TextField txt_sort_fld;
    public SplitMenuButton in_out_fld;
    public Label pot_capa_lbl;
    public CheckBox check_pot_1;
    public CheckBox check_pot_2;
    public CheckBox check_pot_3;
    public CheckBox check_pot_4;
    public TextField text_pot_fld;
    public Button soil_info_btn;
    public Button stage_btn;
    public Button add_img_btn;
    public Button save_btn;
    public SplitMenuButton split_menu_typeSort_btn;
    public Button light_stage_btn;
    public TextField start_date_fld;
    public TextField eta_date_fld;
    public Button set_date_btn;
    public TableView formed_info_table_view;
    public Label cycle_reg_lbl;
    public ImageView img_reg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit_btn.setOnAction(actionEvent -> onExit());
        log_out_btn.setOnAction(actionEvent -> onLogin());
        back_btn.setOnAction(actionEvent -> onUser());
        save_btn.setOnAction(actionEvent -> onSelectedCycle());
        light_stage_btn.setOnAction(actionEvent -> onSetLight());
    }



    //on actions section


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


    private void onUser() {
        Stage stage = (Stage) back_btn.getScene().getWindow();
        Model.getInstance().getView().showUserWindow();
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
    }

    private void onSelectedCycle() {
        Stage stage = (Stage) exit_btn.getScene().getWindow();
        Model.getInstance().getView().showSelectedCycleWindow();
        Model.getInstance().getView().closeStage(stage);
    }
}
