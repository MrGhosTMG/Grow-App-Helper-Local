package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Enums.ComponentType;
import org.jdta.growapp.Enums.LightStages;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Service.CycleCreateService;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.PreferencesUtils;
import org.jdta.growapp.Utils.StageActions;
import org.jdta.growapp.Utils.FXMLUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CycleCreateController implements Initializable {

    private static final File TEMP_PHOTO_DIR = new File("Photos/__temp_cycle__");
    private final CycleCreateService cycleService = new CycleCreateService();
    private final Map<ComponentType, Double> addedComponents = new HashMap<>();
    private Cycle draftCycle = new Cycle();
    private boolean isLightSet = false;
    private boolean isGrowSet = false;
    private int potCapacity = 0;
    private LightStages selectedLightStage;
    private static long sortTypeAverageDays;

    public TextArea text_area_fld;
    public TextField txt_sort_fld, text_pot_fld, component_litres_fld;
    public Label eet_date_lbl;
    public Label pot_capa_lbl, cycle_reg_lbl, soil_components_lbl, error_lbl;
    public Button pot_litres_set_btn, web_cat_btn, add_note_btn, delete_img_btn, import_btn;
    public Button back_btn, log_out_btn, exit_btn, add_img_btn, stage_btn;
    public Button save_btn, light_stage_btn, add_component_btn, set_date_btn, add_soil_info_btn;
    public DatePicker start_date;
    public CheckBox check_pot_1, check_pot_2, check_pot_3, check_pot_4, hydroponic_check;
    public ChoiceBox<ComponentType> component_choice_box;
    public RadioButton indoor_rb, outdoor_rb, auto_fem_btn ,photo_fem_btn, reg_btn, photo_fast_fem_btn;
    public ColumnConstraints photo_grid_pane;
    public ImageView img_reg;
    public ImageView img_view;
    public GridPane grid_pane;
    public BorderPane cycle_create_anchor;
    public AnchorPane soil_anchor, scene_anchor, photo_anchor;
    public AnchorPane central_anchor;

    public static long getSortTypeAverageDays() {
        return sortTypeAverageDays;
    }

    public static void setSortTypeAverageDays(long sortTypeAverageDays) {
        CycleCreateController.sortTypeAverageDays = sortTypeAverageDays;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_soil_info_btn.setDisable(true);
        disableButtons(true);
        setRadioGroup();
        setIndoorOutdoorListeners();
        setOutdoorOrHydroponicDisable();
        setupButtonActions();
        component_choice_box.getItems().setAll(ComponentType.values());

        loadImagesFromTemp();
        clearTempFolder();
        filterInputField(text_pot_fld);
        filterInputField(component_litres_fld);
        initializeWindowPosition();
    }

    private void initializeWindowPosition() {
        Platform.runLater(() -> {
            Stage stage = (Stage) cycle_create_anchor.getScene().getWindow();
            double[] pos = PreferencesUtils.getWindowPosition(this.getClass().getSimpleName());
            if (pos != null) {
                stage.setX(pos[0]);
                stage.setY(pos[1]);
            }
            stage.setOnCloseRequest(e -> {
                PreferencesUtils.saveWindowPosition(this.getClass().getSimpleName(), stage.getX(), stage.getY());
            });
        });
    }


    private void setOutdoorOrHydroponicDisable() {
        outdoor_rb.setOnAction(actionEvent -> {
            if (outdoor_rb.isSelected()) {
                hydroponic_check.setSelected(false);
                hydroponic_check.setDisable(true);
                setIndoorPotFieldsEnabled(false);
            }else {
                hydroponic_check.setDisable(false);
            }
        });
        hydroponic_check.setOnAction(actionEvent -> {
            if (hydroponic_check.isSelected()) {
                outdoor_rb.setSelected(false);
                outdoor_rb.setDisable(true);
                setIndoorPotFieldsEnabled(false);
            }
            else {
                outdoor_rb.setDisable(false);
            }
        });
    }

    private void setIndoorOutdoorListeners() {
        indoor_rb.setOnAction(actionEvent -> {
            outdoor_rb.setSelected(false);
            if (!hydroponic_check.isSelected()) setIndoorPotFieldsEnabled(true);
        });
        outdoor_rb.setOnAction(actionEvent -> {
            indoor_rb.setSelected(false);
            setIndoorPotFieldsEnabled(false);
        });

        hydroponic_check.setOnAction(actionEvent -> {
            if (hydroponic_check.isSelected()) {
                setIndoorPotFieldsEnabled(false);
            }
            else {
                setIndoorPotFieldsEnabled(!indoor_rb.isSelected());
            }
        });

        List<CheckBox> pots = List.of(check_pot_1, check_pot_2, check_pot_3, check_pot_4);
        for (CheckBox checkBox : pots) {
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    pots.stream().filter(other -> other != checkBox).forEach(c -> c.setSelected(false));
                    text_pot_fld.setDisable(checkBox != check_pot_4);
                } else {
                    text_pot_fld.setDisable(true);
                }
            });
        }
    }

    private void setIndoorPotFieldsEnabled(boolean isIndoor) {
        check_pot_1.setDisable(!isIndoor);
        check_pot_2.setDisable(!isIndoor);
        check_pot_3.setDisable(!isIndoor);

        if (!isIndoor) {
            check_pot_1.setSelected(false);
            check_pot_2.setSelected(false);
            check_pot_3.setSelected(false);
        }
    }

    private void setRadioGroup() {
        ToggleGroup sortTypeGroup = new ToggleGroup();
        auto_fem_btn.setToggleGroup(sortTypeGroup);
        photo_fem_btn.setToggleGroup(sortTypeGroup);
        photo_fast_fem_btn.setToggleGroup(sortTypeGroup);
        reg_btn.setToggleGroup(sortTypeGroup);
    }

    private void setupButtonActions() {
        exit_btn.setOnAction(actionEvent -> StageActions.onExit(getStage()));
        log_out_btn.setOnAction(event -> StageActions.onLogout(getStage()));
        back_btn.setOnAction(actionEvent -> StageActions.backToUser(getStage()));
        add_img_btn.setOnAction(actionEvent -> onAddPhoto());
        delete_img_btn.setOnAction(actionEvent -> deletePreviewPhoto());
        add_component_btn.setOnAction(actionEvent -> onComponents());// check needed
        light_stage_btn.setOnAction(actionEvent -> onSetLight());// check needed
        stage_btn.setOnAction(actionEvent -> onCreateGrowStage());// check needed
        //save_btn.setOnAction(actionEvent -> onSelectedCycle());// check needed
        set_date_btn.setOnAction(actionEvent -> validDates());
        save_btn.setOnAction(actionEvent -> onSaveAndStartCycle());// check needed
        add_soil_info_btn.setOnAction(actionEvent -> unlockStagesButtons());
    }

    private void unlockStagesButtons() {

        System.out.println("Add soil info button was pressed");
        disableButtons(false);
    }

    private void disableButtons(boolean disable) {
        add_img_btn.setDisable(disable);
        add_note_btn.setDisable(disable);
        light_stage_btn.setDisable(disable);
        stage_btn.setDisable(disable);
    }

    private boolean validDates() {
        LocalDate start = start_date.getValue();
        if (!cycleService.areDatesValid(start, error_lbl)) {
            start_date.setPromptText("enter date");
            return false;
        }
        add_soil_info_btn.setDisable(false);
        System.out.println("Set date btn was pressed");
        return true;
    }

    private void onSaveAndStartCycle() {

        if (!validDates()) return;

        String name = cycleService.pickSortCycleName(txt_sort_fld.getText(), cycle_reg_lbl);
        if (name == null) return;

        LocalDate start = start_date.getValue();
        LocalDateTime startDate = start.atStartOfDay();


        if (!cycleService.isInOutdoorSelected(indoor_rb , outdoor_rb)) {
            DialogUtils.setErrorMessage(error_lbl, "Select Indoor or Outdoor");
            return;
        }

        if (!cycleService.isSortTypeSelected(auto_fem_btn, photo_fem_btn, photo_fast_fem_btn, reg_btn)) {
            DialogUtils.setErrorMessage(error_lbl, "Select a sort type");
            return;
        }
        
        potCapacity = cycleService.potCapacityCheck(check_pot_1, check_pot_2, check_pot_3, check_pot_4, hydroponic_check,
                text_pot_fld, indoor_rb.isSelected(), error_lbl);
        
        if (potCapacity == 0) return;
        if (!hydroponic_check.isSelected() && addedComponents.isEmpty()) {
            DialogUtils.setErrorMessage(soil_components_lbl, "Add at last one soil component");
            return;
        }
        
        if (!isLightSet || !isGrowSet) {
            DialogUtils.setErrorMessage(error_lbl, "Set Light and Grow stages");
            return;
        }

        try {
            draftCycle.setName(name);
            draftCycle.setStartDateTime(startDate);

            long days = getSortTypeAverageDays();
            LocalDateTime eta = startDate.plus(days, ChronoUnit.DAYS);
            eet_date_lbl.setText(eta.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            draftCycle.setEtaDateTime(eta);

            draftCycle.setIndoorOutdoor(indoor_rb.isSelected() ? "Indoor" : "Outdoor");
            draftCycle.setSortType(getSelectedSortType());
            draftCycle.setPotCapacity(potCapacity);
            draftCycle.setNotes(text_area_fld.getText().trim());
            draftCycle.setUserId(Model.getInstance().getCurrentUser().getId());

            if (selectedLightStage != null) {
                draftCycle.setLightDayHours(selectedLightStage.getDayHours());
                draftCycle.setLightNightHours(selectedLightStage.getNightHours());
                draftCycle.setLightSetTime(LocalDateTime.now());
            }

            int generatedId = Model.getInstance().getCycleDAO().insert(draftCycle);
            if (generatedId != -1) {
                draftCycle.setId(generatedId);
                Model.getInstance().setSelectedCycle(draftCycle);

                Path tempDir = Paths.get("Photos", "__temp_cycle__");
                Path newDir = Paths.get("Photos", "Cycle_" + generatedId);

                try {
                    if (Files.exists(tempDir)) {
                        Files.move(tempDir, newDir, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("No photo folder to move.");
                    }
                } catch (IOException e) {
                    DialogUtils.warning("Photo error", "Unable to move photo folder.");
                }

                //Успешно — переходим на выбранный цикл
                Model.getInstance().getView().showSelectedCycleWindow();
                getStage().close();
            } else {
                DialogUtils.warning("DB Error", "Failed to save the cycle to DB.");
            }

        } catch (Exception e) {
            DialogUtils.error("Exception", "Something went wrong while saving.");
            e.printStackTrace();
        }

    }


    //on Stages
    private void onSetLight() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/LightTimeStageCreate.fxml",
                (LightTimeStageController controller) -> {
                    controller.setOnSave(() -> {
                        isLightSet = true;
                        selectedLightStage = controller.getSelectedStage();
                        System.out.println("Light stage applied: " + selectedLightStage);
                    });
                }
        );
        if (root != null) scene_anchor.getChildren().setAll(root);

    }
    private void onCreateGrowStage() {
        draftCycle.setStartDateTime(start_date.getValue().atStartOfDay());
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/GrowStageCreate.fxml",
                (GrowStageCreateController controller) -> {
                    controller.setDraftCycle(draftCycle);
                    isGrowSet = true;
                });
        if (root != null) scene_anchor.getChildren().setAll(root);
    }


    //Photo logics
    private void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Browse photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            try {
                if (!TEMP_PHOTO_DIR.exists()) TEMP_PHOTO_DIR.mkdirs();
                //clearTempFolder();
                Files.copy(selectedFile.toPath(), new File(TEMP_PHOTO_DIR, "preview.jpg").toPath(), StandardCopyOption.REPLACE_EXISTING);
                loadImagesFromTemp(); // previewRefresh
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void deletePreviewPhoto() {
        File[] files = TEMP_PHOTO_DIR.listFiles(f -> f.getName().toLowerCase().matches(".*\\.(png|jpg|jpeg)$"));
        if (files != null) {
            for (File f : files) f.delete();
        }
        // refresh preview
        grid_pane.getChildren().clear();
        loadImagesFromTemp();
    }

    //Internal methods
    private void clearTempFolder() {
        if (TEMP_PHOTO_DIR.exists() && TEMP_PHOTO_DIR.isDirectory()) {
            File[] files = TEMP_PHOTO_DIR.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        Files.deleteIfExists(file.toPath());
                    } catch (IOException e) {
                        System.out.println("Failed to delete file" + file.getName());
                        e.printStackTrace();
                    }
                }
            }
            loadImagesFromTemp();
        }
    }

    private void loadImagesFromTemp() {
        File[] imageFiles = TEMP_PHOTO_DIR.listFiles(f -> f.getName().toLowerCase().matches(".*\\.(png|jpg|jpeg)$"));
        grid_pane.getChildren().clear();

        if (imageFiles == null) return;
        int column = 0, row = 0;
        for (File img : imageFiles) {
            ImageView imageView = new ImageView(new Image(img.toURI().toString()));
            imageView.setFitWidth(150);
            imageView.setFitHeight(135);
            imageView.setPreserveRatio(false);
            grid_pane.add(imageView, column++, row);
            if (column == 2) {column = 0; row ++; }
        }
    }

    private void onComponents() {
        if (component_choice_box.getValue() == null) {
            DialogUtils.warning("No component", "Please select soil component");
            return;
        }
        ComponentType selectedComponent = component_choice_box.getValue();
        if (selectedComponent == null) {
            DialogUtils.setErrorMessage(soil_components_lbl, "Choose soil component");
            return;
        }
        String litresEntered = component_litres_fld.getText().trim();
        double totalLitres = addedComponents.values().stream().mapToDouble(Double::doubleValue).sum();
        if (!cycleService.componentCapacityCheck(litresEntered,totalLitres,potCapacity ,soil_components_lbl )) return;
        double litres = Double.parseDouble(litresEntered);
        addedComponents.merge(selectedComponent, litres, Double::sum);
        component_litres_fld.clear();
        component_choice_box.setValue(null);
        updateSoilInfoLabel();
    }

    private void updateSoilInfoLabel() {
        StringBuilder sb = new StringBuilder("Mix:");
        addedComponents.forEach((type, litres) ->
                sb.append(litres).append("L"));
        sb.append("Total: ").append(addedComponents.values().stream().mapToDouble(Double::doubleValue).sum())
                .append(" / ").append(potCapacity).append(" L");
        soil_components_lbl.setText(sb.toString());
    }

    private String getSelectedSortType () {
        if (auto_fem_btn.isSelected()) {
            setSortTypeAverageDays(75);
            return "Auto";
        }
        if (photo_fem_btn.isSelected()) {
            setSortTypeAverageDays(165);
            return "Photo";
        }
        if (photo_fast_fem_btn.isSelected()) {
            setSortTypeAverageDays(120);
            return "Fast";
        }
        if (reg_btn.isSelected()) {
            setSortTypeAverageDays(180);
            return "Regular";
        }
        return "Unknown";
    }

    private Stage getStage() {
        return FXMLUtils.stageFrom(exit_btn); // можно использовать любой доступный Node
    }

    private void filterInputField(TextField capacity) {
        capacity.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                soil_components_lbl.setText("");
                return; // If empty field
            }
            // Digits check
            if (!newVal.matches("\\d*")) {
                capacity.setText(oldVal != null ? oldVal : "");
                DialogUtils.setErrorMessage(soil_components_lbl, "Enter digits only");
            }
        });
    }

    //    private void onSelectedCycle() {
//        Stage stage = FXMLUtils.getCurrentStage();
//        Model.getInstance().getView().showSelectedCycleWindow();
//        Model.getInstance().getView().closeStage(stage);
//    }
}
