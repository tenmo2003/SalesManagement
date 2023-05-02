package salesmanagement.salesmanagement.ViewController.OrdersTab;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.ViewController.ViewController;

public class OrderInfoViewController extends ViewController implements OrdersTabController {

    @FXML
    private JFXButton add;

    @FXML
    private TextField commentsTextField;

    @FXML
    private TextField customerNumberTextField;

    @FXML
    private TextField deliverTo;

    @FXML
    private VBox detailsInfoBox;

    @FXML
    private ScrollPane employeeInfoBox;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private DatePicker orderedDatePicker;

    @FXML
    private FilteredTableView<?> ordersTable;

    @FXML
    private ComboBox<?> paymentMethod;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TextField priceEachTextField;

    @FXML
    private JFXButton print;

    @FXML
    private TableColumn<?, ?> productCodeColumn;

    @FXML
    private TextField productCodeTextField;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TextField quantityTextField;

    @FXML
    private DatePicker requiredDatePicker;

    @FXML
    private DatePicker shippedDatePicker;

    @FXML
    private ComboBox<?> status;

    @FXML
    private TextField totalAmountTextField;

    @FXML
    private TableColumn<?, ?> totalColumn;

    @FXML
    private TextField totalTextField;


    @FXML
    void searchCustomer(MouseEvent event) {

    }
}
