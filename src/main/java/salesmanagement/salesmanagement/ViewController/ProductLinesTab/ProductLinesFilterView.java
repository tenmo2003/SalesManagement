package salesmanagement.salesmanagement.ViewController.ProductLinesTab;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.ProductLine;
import salesmanagement.salesmanagement.ViewController.FilterView;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ProductLinesFilterView extends FilterView<ProductLine> implements ProductLinesTab {
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField productLineTextField;

    @Override
    protected void applyFilter() {
        filteredList.setPredicate(productLine -> {
            boolean nameMatch = productLine.getProductLine().toLowerCase().contains(productLineTextField.getText().toLowerCase());
            boolean descriptionMatch = productLine.getDescription().toLowerCase().contains(descriptionTextField.getText().toLowerCase());
            return nameMatch && descriptionMatch;
        });
        close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
}
