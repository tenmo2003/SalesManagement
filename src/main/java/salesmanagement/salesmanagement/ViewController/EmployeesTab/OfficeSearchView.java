package salesmanagement.salesmanagement.ViewController.EmployeesTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import salesmanagement.salesmanagement.SalesComponent.Office;
import salesmanagement.salesmanagement.SalesComponent.Office;
import salesmanagement.salesmanagement.Utils.Utils;
import salesmanagement.salesmanagement.ViewController.SearchView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;


public class OfficeSearchView extends SearchView<Office> {
    @FXML
    private TextField addressLineTextField;
    @FXML
    private TextField officeCodeTextField;
    @FXML
    private TextField phoneCodeTextField;
    @FXML
    private TableColumn<?, ?> officeCodeColumn;
    @FXML
    private TableColumn<?, ?> phoneColumn;
    @FXML
    private TableColumn<?, ?> addressLineColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        officeCodeColumn.setCellValueFactory(new PropertyValueFactory<>("officeCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneCode"));
        addressLineColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine"));

        Utils.adjustTableColumnWidths(searchTable, Arrays.asList(0.3, 0.2, 0.5));
    }

    @Override
    public void show() {
        super.show();
        search();
    }

    @Override
    protected void search() {
        runTask(() -> {
            String query = "select * from offices";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            List<Office> officeList = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    officeList.add(new Office(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            searchList = new FilteredList<>(FXCollections.observableArrayList(officeList));
            searchList.setPredicate(office -> {
                boolean officeCodeMatch = String.valueOf(office.getOfficeCode()).contains(officeCodeTextField.getText().toLowerCase());
                boolean addressMatch = String.valueOf(office.getAddressLine()).contains(addressLineTextField.getText().toLowerCase());
                boolean phoneMatch = String.valueOf(office.getPhone()).contains(phoneCodeTextField.getText().toLowerCase());
                return officeCodeMatch && addressMatch && phoneMatch;
            });
        }, () -> searchTable.setItems(searchList), loadingIndicator, null);
    }
}
