module salesmanagement.salesmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires java.mail;
    requires javafx.swing;
    requires javafx.web;
    requires libphonenumber;
    requires jasperreports;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.burningwave.core;
    opens salesmanagement.salesmanagement to javafx.fxml;
    exports salesmanagement.salesmanagement;
    exports salesmanagement.salesmanagement.SalesComponent;
    opens salesmanagement.salesmanagement.SalesComponent to javafx.fxml;
    exports salesmanagement.salesmanagement.scenecontrollers;
    opens salesmanagement.salesmanagement.scenecontrollers to javafx.fxml;
}