package salesmanagement.salesmanagement.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class EmployeesFilterViewController extends ViewController{

    @FXML
    private ComboBox<?> accessibilityComboBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private VBox employeeFilterBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private AnchorPane root;

    @FXML
    private ComboBox<?> statusComboBox;

    @FXML
    void applyFilter(MouseEvent event) {

    }

    @FXML
    void clearFilter(MouseEvent event) {

    }

}
