package org.jdta.growapp.Controllers.ToolsControlls;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Utils.DialogUtils;
import org.jdta.growapp.Utils.FXMLUtils;
import org.jdta.growapp.Utils.StageActions;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoFinishedCycle implements Initializable {
    public AnchorPane parent_anchor;
    public Label total_days_lbl;
    public Label training_lbl;
    public Label yield_lbl;
    public Button back_to_user_btn;
    public Button ester_egg_btn;
    public Label cycle_name_lbl;
    public Label start_date;
    public Label end_date;
    public Label germ_days_lbl;
    public Label first_days_lbl;
    public Label vegetation_days_lbl;
    public Label pre_flow_days_lbl;
    public Label flow_days_lbl;
    public Label in_out_lbl;
    public Label seed_type;
    public Label pot_lbl;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back_to_user_btn.setOnAction(actionEvent -> StageActions.backToUser(getStage()));
        ester_egg_btn.setOnAction(actionEvent -> onRestartCycle());
    }


    private void onRestartCycle() {
        StageActions.onEasterEgg(getStage());
    }


    private Stage getStage() {
        return (Stage) back_to_user_btn.getScene().getWindow();
    }
    public void setDataFromCycle(Cycle cycle) {

        cycle_name_lbl.setText(cycle.getName());
        start_date.setText("Started at " + cycle.getStartDateTime().toLocalDate());
        end_date.setText("Finished at " + cycle.getEtaDateTime().toLocalDate());
        germ_days_lbl.setText("Waiting germination  " + cycle.getGerminationDays() + "days");
        first_days_lbl.setText("First Leafs " + cycle.getFirstLeafDays() + "days");

        vegetation_days_lbl.setText("Vegetation " + cycle.getVegetationDays() + "days");
        pre_flow_days_lbl.setText("Pre Flowering " + cycle.getPreFloweringDays() + "days");
        flow_days_lbl.setText("Flowering " + cycle.getFloweringDays() + "days");
        total_days_lbl.setText("Total growing " + cycle.getTotalGrowDays() + "days");
        pot_lbl.setText("Pot " + cycle.getPotCapacity() + "Litres");
        in_out_lbl.setText("" + cycle.getIndoorOutdoor());
//        seed_type.setText("" + cycle.getSortType());
//        training_lbl.setText("" + cycle.);


        yield_lbl.setText("Your resulting YIELD is " + cycle.getYieldGrams() + " grams");

    }

}
