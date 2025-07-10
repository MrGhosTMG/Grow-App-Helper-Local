package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jdta.growapp.Enums.GrowStages;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class GrowStageCreateController implements Initializable {
    private final ObservableList<GrowStages> pickedStages = FXCollections.observableArrayList();
    private boolean isEditMode = false;

    // FXML элементы
    public AnchorPane parent_GR_create_anchor;
    public Label new_started_lbl;
    public Label days_of_stage_lbl;
    public CheckBox new_check;
    public CheckBox started_check;
    public ChoiceBox<GrowStages> stage_picker;
    public DatePicker start_date_picker;
    public Button apply_btn;
    public ProgressBar grow_progress_bar;
    public ChoiceBox<GrowStages> stage_select_choice_box;
    public Button set_stage;
    public TextField days_grams_in_fld;
    public RadioButton Drying_rb;
    public RadioButton yield_rb;

    public Button add_btn;
    public AnchorPane parent_anchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initProgressBar();
        setupEditModeToggle();
    }

    private void setupEditModeToggle() {
        new_check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            setEditMode(!newVal); // Если new_check активен - режим создания (isEditMode = false)
        });
    }

    private void initProgressBar() {
        initialGrowStages();
        setupProgressBarByStages();

        stage_picker.setOnAction(actionEvent -> {
            GrowStages picked = stage_picker.getValue();
            if (picked != null) {
                if (!isEditMode) {
                    // Режим создания: свободное изменение
                    updateCurrentStage(picked);
                } else {
                    // Режим редактирования: проверка ограничений
                    if (canSelectStage(picked)) {
                        updateCurrentStage(picked);
                    } else {
                        DialogUtils.warning("Ошибка", "Нельзя выбрать стадию раньше уже применённой.");
                    }
                }
            }
        });
    }

    private boolean canSelectStage(GrowStages newStage) {
        return pickedStages.stream()
                .noneMatch(existing -> existing.ordinal() > newStage.ordinal());
    }

    private void updateCurrentStage(GrowStages stage) {
        if (!pickedStages.contains(stage)) {
            pickedStages.add(stage);
        }
        double progress = (double) (stage.ordinal() + 1) / GrowStages.values().length;
        grow_progress_bar.setProgress(progress);
    }

    private void setupProgressBarByStages() {
        grow_progress_bar.setProgress(0.0);
        grow_progress_bar.progressProperty().addListener((obs, oldVal, newVal) -> {
            double progress = newVal.doubleValue();
            if (progress < 0.1) {
                grow_progress_bar.setStyle("-fx-accent: #eef906;");
            } else if (progress < 0.2) {
                grow_progress_bar.setStyle("-fx-accent: #2cb20b;");
            } else if (progress < 0.3) {
                grow_progress_bar.setStyle("-fx-accent: #259509;");
            } else if (progress < 0.4) {
                grow_progress_bar.setStyle("-fx-accent: #1b7305;");
            } else if (progress < 0.5) {
                grow_progress_bar.setStyle("-fx-accent: #087c1d;");
            } else if (progress < 0.6) {
                grow_progress_bar.setStyle("-fx-accent: #0fd9ea;");
            } else if (progress < 0.7) {
                grow_progress_bar.setStyle("-fx-accent: #ef230f;");
            } else {
                grow_progress_bar.setStyle("-fx-accent: #ec0fef;");
            }
        });
    }

    // Оригинальный метод без изменений
    private void initialGrowStages() {
        if (stage_picker != null) {
            stage_picker.getItems().addAll(GrowStages.values());
            stage_picker.setOnAction(actionEvent -> {
                GrowStages picked = stage_picker.getValue();
                if (picked != null && !pickedStages.contains(picked)) {
                    pickedStages.add(picked);
                }
            });
        }

        if (stage_select_choice_box != null) {
            stage_select_choice_box.getItems().addAll(GrowStages.values());
        }

        if (add_btn != null) {
            add_btn.setOnAction(event -> {
                if (yield_rb != null && yield_rb.isSelected()) {
                    String input = days_grams_in_fld.getText();
                    if (input != null && !input.isEmpty()) {
                        Model.getInstance().getCurrentCycle().setYieldGrams(Integer.parseInt(input));
                        System.out.println("Yield set: " + input + " grams");
                    }
                }
            });
        }

        if (set_stage != null) {
            set_stage.setOnAction(event -> {
                if (yield_rb != null && yield_rb.isSelected() &&
                        days_grams_in_fld != null && days_grams_in_fld.getText().matches("\\d+")) {
                    showInfoFinishedCycle();
                } else {
                    DialogUtils.warning("Missing data", "Enter yield in grams to finish stage.");
                }
            });
        }
    }

    private void showInfoFinishedCycle() {
        Node root = FXMLUtils.loadWithControllerCallBackActions(
                "/FXML/userBoard/InfoFinishedCycle.fxml",
                (InfoFinishedCycle controller) -> {
                    controller.setDataFromCycle(Model.getInstance().getCurrentCycle());
                });

        if (root != null) {
            parent_anchor.getChildren().setAll(root);
        }
    }

    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
    }
}


