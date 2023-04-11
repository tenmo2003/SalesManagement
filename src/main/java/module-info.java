module salesmanagement.salesmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires java.mail;
    requires javafx.web;
    requires de.jensd.fx.glyphs.fontawesome;
    opens salesmanagement.salesmanagement to javafx.fxml;
    exports salesmanagement.salesmanagement;
    exports salesmanagement.salesmanagement.SalesComponent;
    opens salesmanagement.salesmanagement.SalesComponent to javafx.fxml;
    exports salesmanagement.salesmanagement.scenecontrollers;
    opens salesmanagement.salesmanagement.scenecontrollers to javafx.fxml;
}