module com.gui.programinventaris {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens com.gui.programinventaris to javafx.fxml;
    exports com.gui.programinventaris;
}
