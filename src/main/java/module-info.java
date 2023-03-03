module salesmanagement.salesmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires fontawesomefx;
    requires com.jfoenix;


    opens salesmanagement.salesmanagement to javafx.fxml;
    exports salesmanagement.salesmanagement;
}