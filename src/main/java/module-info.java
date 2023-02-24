module salesmanagement.salesmanagement {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens salesmanagement.salesmanagement to javafx.fxml;
    exports salesmanagement.salesmanagement;
}