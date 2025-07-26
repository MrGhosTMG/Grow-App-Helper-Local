package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Models.Model;
import org.jdta.growapp.Utils.DialogUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    public ListView<Cycle> hist_list_view;
    public Label error_lbl;
    public ScrollPane scrl_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hist_list_view.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Cycle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " | Yield: " + item.getYieldGrams() + "g");
                }
            }
        });

        hist_list_view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Cycle selected = hist_list_view.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openCycleInfo(selected);
                }
            }
        });
    }
    public void setCycles(List<Cycle> finishedCycles) {
        if (finishedCycles == null || finishedCycles.isEmpty()) {
            error_lbl.setVisible(true);
            DialogUtils.setErrorMessage(error_lbl,"No finished cycles found.");
            return;
        }
        else {
            error_lbl.setVisible(false);
        }
        hist_list_view.getItems().setAll(finishedCycles);
    }

    private void openCycleInfo(Cycle cycle) {
        // Сохраняем выбранный цикл в Model
        Model.getInstance().setSelectedCycle(cycle);
        Model.getInstance().setFinishedCycle(true); // новый флаг

        // Показываем CycleLoadController
        Model.getInstance().getView().showSelectedCycleWindow(); // Переход

        // Закрываем текущее окно, если надо
        Stage stage = (Stage) hist_list_view.getScene().getWindow();
        stage.close();
    }

}
