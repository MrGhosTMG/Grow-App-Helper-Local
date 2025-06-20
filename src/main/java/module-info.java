module org.jdta.growapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;


    opens org.jdta.growapp to javafx.fxml;
    exports org.jdta.growapp;
    exports org.jdta.growapp.Controllers;
    exports org.jdta.growapp.Models;
    exports org.jdta.growapp.Views;
    exports org.jdta.growapp.Utils;
    exports org.jdta.growapp.Controllers.ToolsControlls;
}