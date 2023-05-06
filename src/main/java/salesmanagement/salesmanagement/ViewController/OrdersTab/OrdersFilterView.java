
package salesmanagement.salesmanagement.ViewController.OrdersTab;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Order;
import salesmanagement.salesmanagement.ViewController.FilterView;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersFilterView extends FilterView<Order> implements OrdersTab {

    @FXML
    private TextField commentTextField;

    @FXML
    private TextField contactTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField destinationTextField;

    @FXML
    private TextField employeeNameTextField;

    @FXML
    private ComboBox<String> orderStatus;
    @FXML
    private ComboBox<String> orderType;

    @FXML
    private DatePicker orderedDatePicker;

    @FXML
    private DatePicker requiredDatePicker;

    @FXML
    private DatePicker shippedDatePicker;

    @FXML
    public void applyFilter() {
        close();
        filteredList.setPredicate(order -> {
            boolean contactMatch = order.getContact().toLowerCase().contains(contactTextField.getText());
            boolean shippedDateMatch = order.getShippedDate().equals(shippedDatePicker.getValue());
            if (shippedDatePicker.getValue() == null) shippedDateMatch = true;
            boolean requiredDateMatch = order.getRequiredDate().equals(requiredDatePicker.getValue());
            if (requiredDatePicker.getValue() == null) requiredDateMatch = true;
            boolean orderedDateMatch = order.getOrderDate().equals(orderedDatePicker.getValue());
            if (orderedDatePicker.getValue() == null) orderedDateMatch = true;
            boolean destinationMatch = order.getDestination().toLowerCase().contains(destinationTextField.getText());
            boolean commentMatch = order.getComments().toLowerCase().contains(commentTextField.getText());
            boolean statusMatch = order.getStatus().equals(orderStatus.getValue());
            if (orderStatus.getValue() == null) statusMatch = true;
            boolean typeMatch = order.getType().equals(orderType.getValue());
            if (orderType.getValue() == null) typeMatch = true;
            boolean customerMatch = order.getCustomerName().toLowerCase().contains(customerNameTextField.getText());
            System.out.println(order.getCustomerName() + "   " + customerNameTextField.getText());
            boolean employeeMatch = order.getEmployeeName().toLowerCase().contains(employeeNameTextField.getText());
            return contactMatch && orderedDateMatch && customerMatch && shippedDateMatch && employeeMatch
                    && requiredDateMatch && destinationMatch && commentMatch && statusMatch && typeMatch;
        });
    }

    @FXML
    public void clearFilter() {
        commentTextField.setText("");
        contactTextField.setText("");
        customerNameTextField.setText("");
        destinationTextField.setText("");
        employeeNameTextField.setText("");
        orderStatus.setValue(null);
        orderType.setValue(null);
        orderedDatePicker.setValue(null);
        requiredDatePicker.setValue(null);
        shippedDatePicker.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        orderStatus.setItems(FXCollections.observableList(Order.getorderStatusList()));
        orderType.setItems(FXCollections.observableList(Order.getorderTypeList()));
    }
}
