package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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



    /*
on actions section
    private void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(add_img_btn.getScene().getWindow());
        if (selectedFile != null) {
            try {
                File destDir = new File("Photos");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                File destFile = new File(destDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                loadImagesFromDisk(); // перезагрузка галереи
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadImagesFromDisk() {
        File photoDir = new File("Photos");
        if (!photoDir.exists() || !photoDir.isDirectory()) {
            return;
        }

        File[] imageFiles = photoDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png") ||
                        name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".jpeg")
        );

        grid_pane.getChildren().clear();

        if (imageFiles == null) return;

        int column = 0;
        int row = 0;

        for (File imgFile : imageFiles) {
            Image image = new Image(imgFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(135);
            imageView.setPreserveRatio(true);

            grid_pane.add(imageView, column, row);
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }
    ещё кнопку «Удалить все», превью при клике, или диалог редактирования.
*/

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
