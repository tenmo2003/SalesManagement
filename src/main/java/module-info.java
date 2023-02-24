module salesmanagement.salesmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens salesmanagement.salesmanagement to javafx.fxml;
    exports salesmanagement.salesmanagement;
}