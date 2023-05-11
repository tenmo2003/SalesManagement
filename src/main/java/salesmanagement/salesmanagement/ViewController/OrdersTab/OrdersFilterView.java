
package salesmanagement.salesmanagement.ViewController.OrdersTab;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
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
    private StackPane centerBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        orderStatus.setItems(FXCollections.observableList(Order.getorderStatusList()));
        orderType.setItems(FXCollections.observableList(Order.getorderTypeList()));
    }

    @Override
    public void show() {
        super.show();
        Rectangle rect = new Rectangle(centerBox.getWidth(), centerBox.getHeight());
        rect.setArcHeight(15.0);
        rect.setArcWidth(15.0);
        centerBox.setClip(rect);
    }

    @Override
    public void applyFilter() {
        close();
        filteredList.setPredicate(order -> {
            boolean contactMatch = order.getContact().toLowerCase().contains(contactTextField.getText().toLowerCase());
            boolean shippedDateMatch = order.getShippedDate().equals(shippedDatePicker.getValue());
            if (shippedDatePicker.getValue() == null) shippedDateMatch = true;
            boolean requiredDateMatch = order.getRequiredDate().equals(requiredDatePicker.getValue());
            if (requiredDatePicker.getValue() == null) requiredDateMatch = true;
            boolean orderedDateMatch = order.getOrderDate().equals(orderedDatePicker.getValue());
            if (orderedDatePicker.getValue() == null) orderedDateMatch = true;
            boolean destinationMatch = order.getDestination().toLowerCase().contains(destinationTextField.getText().toLowerCase());
            boolean commentMatch = order.getComments().toLowerCase().contains(commentTextField.getText().toLowerCase());
            boolean statusMatch = order.getStatus().equals(orderStatus.getValue());
            if (orderStatus.getValue() == null) statusMatch = true;
            boolean typeMatch = order.getType().equals(orderType.getValue());
            if (orderType.getValue() == null) typeMatch = true;
            boolean customerMatch = order.getCustomerName().toLowerCase().contains(customerNameTextField.getText().toLowerCase());
            System.out.println(order.getCustomerName() + "   " + customerNameTextField.getText().toLowerCase());
            boolean employeeMatch = order.getEmployeeName().toLowerCase().contains(employeeNameTextField.getText().toLowerCase());
            return contactMatch && orderedDateMatch && customerMatch && shippedDateMatch && employeeMatch
                    && requiredDateMatch && destinationMatch && commentMatch && statusMatch && typeMatch;
        });
    }
}
