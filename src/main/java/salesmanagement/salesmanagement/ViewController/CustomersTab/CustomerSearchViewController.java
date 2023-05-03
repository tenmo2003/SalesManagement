package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.ViewController.ViewController;

public class CustomerSearchViewController extends ViewController {

    @FXML
    private TableColumn<?, ?> SSNColumn;

    @FXML
    private TextField SSNTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField contactTextField;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private TableColumn<?, ?> customerNumberColumn;

    @FXML
    private TextField customerNumberTextField;

    @FXML
    private VBox employeeFilterBox;

    @FXML
    private StackPane root;

    @FXML
    private FilteredTableView<?> searchTable;

    @FXML
    void apply(MouseEvent event) {

    }

    @FXML
    void clearAll(MouseEvent event) {

    }

    @FXML
    void close(MouseEvent event) {

    }

}
