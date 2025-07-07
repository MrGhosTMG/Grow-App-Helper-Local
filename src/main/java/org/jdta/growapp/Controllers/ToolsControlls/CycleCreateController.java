package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.StageActions;
import org.jdta.growapp.Utils.FXMLUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class CycleCreateController implements Initializable {
    public Button web_cat_btn;
    public Button add_note_btn;
    public TextArea text_area_fld;
    public Button delete_img_btn;
    public Button import_btn;
    public Button back_btn;
    public Button log_out_btn;
    public Button exit_btn;
    public TextField txt_sort_fld;
    public Label pot_capa_lbl;
    public CheckBox check_pot_1;
    public CheckBox check_pot_2;
    public CheckBox check_pot_3;
    public CheckBox check_pot_4;
    public TextField text_pot_fld;
    public Button stage_btn;
    public Button add_img_btn;
    public Button save_btn;
    public Button light_stage_btn;
    public Button set_date_btn;
    public Label cycle_reg_lbl;
    public ImageView img_reg;
    public DatePicker start_date;
    public DatePicker EET_date;
    public RadioButton indoor_rb;
    public RadioButton outdoor_rb;
    public RadioButton auto_fem_btn;
    public RadioButton photo_fem_btn;
    public RadioButton reg_btn;
    public RadioButton photo_fast_fem_btn;
    public AnchorPane scene_anchor;
    public Label error_lbl;
    public GridPane grid_pane;
    private static final File TEMP_PHOTO_DIR = new File("Photos/__temp_cycle__");
    public ImageView photo_view;
    public AnchorPane soil_anchor;
    public BorderPane parent_border_pane;
    public Button add_soil_info_btn;
    public AnchorPane light_grow_scene;
    public ChoiceBox component_choice_box;
    public Label soil_components_lbl;
    public TextField component_litres_fld;
    public Button add_component_btn;
    public Label components_set_lbl;
    public ImageView img_view;
    public AnchorPane image_preview_anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ToggleGroup sortTypeGroup = new ToggleGroup();

        auto_fem_btn.setToggleGroup(sortTypeGroup);
        photo_fem_btn.setToggleGroup(sortTypeGroup);
        photo_fast_fem_btn.setToggleGroup(sortTypeGroup);
        reg_btn.setToggleGroup(sortTypeGroup);


        exit_btn.setOnAction(actionEvent -> StageActions.onExit(getStage()));
        log_out_btn.setOnAction(event -> StageActions.onLogout(getStage()));
        back_btn.setOnAction(actionEvent -> StageActions.backToUser(getStage()));
        save_btn.setOnAction(actionEvent -> onSelectedCycle());
        light_stage_btn.setOnAction(actionEvent -> onSetLight());
        stage_btn.setOnAction(actionEvent -> onCreateGrowStage());

        loadImagesFromTemp();
        clearTempFolder();
        add_img_btn.setOnAction(actionEvent -> onAddPhoto());
        delete_img_btn.setOnAction(actionEvent -> deletePreviewPhoto());

        save_btn.setOnAction(actionEvent -> onSaveAndStartCycle());
    }



    private void onSaveAndStartCycle() {
        if (!areDatesValid()) return;

        if (!isSortTypeSelected()) {
            DialogUtils.warning("Missing Sort", "Please select sort type (Auto, Photo...)");
            return;
        }

        try {
            Cycle cycle = new Cycle();
            cycle.setUserId(Model.getInstance().getCurrentUser().getId());
            cycle.setName(txt_sort_fld.getText().trim());
            cycle.setIndoorOutdoor(indoor_rb.isSelected() ? "Indoor" : "Outdoor");
            cycle.setSortType(getSelectedSortType()); // метод ниже
            cycle.setStartDateTime(start_date.getValue().atStartOfDay());
            cycle.setEtaDateTime(EET_date.getValue().atStartOfDay());
            cycle.setPotCapacity(parsePotCapacity()); // метод ниже
            cycle.setNotes(text_area_fld.getText().trim());
            cycle.setLightDayHours(20);  // пока жёстко, позже возьми из LightStageController
            cycle.setLightNightHours(4); // тоже

            // Временное изображение (если добавлялось)
            cycle.setImagePath("Photos/__temp_cycle__/preview.jpg"); // опционально

            int generatedId = Model.getInstance().getCycleDAO().insert(cycle);
            if (generatedId != -1) {
                // Переименовать папку:
                Path tempDir = Paths.get("Photos", "__temp_cycle__");
                Path newDir = Paths.get("Photos", "Cycle_" + generatedId);
                Files.move(tempDir, newDir, StandardCopyOption.REPLACE_EXISTING);

                // Открыть окно CycleLoad или Dashboard:
                Model.getInstance().getView().showSelectedCycleWindow();
                FXMLUtils.getCurrentStage().close();
            } else {
                DialogUtils.warning("Ошибка", "Не удалось сохранить цикл в базу данных.");
            }
        } catch (Exception e) {
            DialogUtils.error("Ошибка", "Ошибка при сохранении цикла.");
            e.printStackTrace();
        }
    }


    private void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Browse photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            try {
                if (!TEMP_PHOTO_DIR.exists()) {
                    TEMP_PHOTO_DIR.mkdirs();
                }
                File destinationFile = new File(TEMP_PHOTO_DIR, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loadImagesFromTemp(); // previewRefresh
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onDeleteAllPhoto() {
        if (TEMP_PHOTO_DIR.exists()) {
            for (File file : TEMP_PHOTO_DIR.listFiles()) {
                file.delete();
            }
        }
    }

    private void deletePreviewPhoto() {
        File[] files = TEMP_PHOTO_DIR.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png") ||
                        name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".jpeg")
        );

        if (files != null && files.length > 0) {
            // clear picked preview image from CycleCreate
            File last = files[files.length - 1];
            if (last.delete()) {
                System.out.println(" Deleted: " + last.getName());
            } else {
                System.out.println(" Failed to delete: " + last.getName());
            }
        }

        // refresh preview
        grid_pane.getChildren().clear();
        loadImagesFromTemp();
    }


    private void clearTempFolder() {
        if (TEMP_PHOTO_DIR.exists()) {
            for (File file : TEMP_PHOTO_DIR.listFiles()) {
                file.delete();
            }
            loadImagesFromTemp();
        }
    }

    private void onCreateGrowStage() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/GrowStageCreate.fxml",
                (GrowStageController controller) -> {

                });
        if (root != null) {
            light_grow_scene.getChildren().setAll(root);
        }
    }

    private void onSetLight() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/LightTimeStageCreate.fxml",
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
            light_grow_scene.getChildren().setAll(root);
        }
    }


    private Stage getStage() {
        return FXMLUtils.stageFrom(exit_btn); // можно использовать любой доступный Node
    }

    private void onSelectedCycle() {
        Stage stage = FXMLUtils.getCurrentStage();
        Model.getInstance().getView().showSelectedCycleWindow();
        Model.getInstance().getView().closeStage(stage);
    }

    private void loadImagesFromTemp() {
        File[] imageFiles = TEMP_PHOTO_DIR.listFiles(((dir, name) ->
        name.toLowerCase().endsWith(".png") ||
        name.toLowerCase().endsWith(".jpg") ||
        name.toLowerCase().endsWith(".jpeg")));
        grid_pane.getChildren().clear();

        if (imageFiles == null) return;
        int column = 0;
        int row = 0;

        for (File img : imageFiles) {
            Image image = new Image(img.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(135);
            imageView.setPreserveRatio(true);

            grid_pane.add(imageView, column, row);
            column++;
            if (column == 2) {
                column = 0;
                row ++;
            }
        }
    }

    private String getSelectedSortType () {
        if (auto_fem_btn.isSelected()) return "Auto";
        if (photo_fem_btn.isSelected()) return "Photo";
        if (photo_fast_fem_btn.isSelected()) return "Fast";
        if (reg_btn.isSelected()) return "Regular";
        return "Unknown";
    }

    private double parsePotCapacity() {
        try {
            return Double.parseDouble(text_pot_fld.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }


    private boolean areDatesValid() { // пока не применил
        if (start_date.getValue() == null || EET_date.getValue() == null) {
            DialogUtils.warning("Missing Dates", "Please select both start and estimated end date.");
            return false;
        }

        if (EET_date.getValue().isBefore(start_date.getValue())) {
            DialogUtils.warning("Invalid Dates", "End date cannot be before start date.");
            return false;
        }

        return true;
    }

    private boolean isSortTypeSelected() {
        return auto_fem_btn.isSelected() || photo_fem_btn.isSelected()
                || photo_fast_fem_btn.isSelected() || reg_btn.isSelected();
    }


}
