package salesmanagement.salesmanagement.ViewController.ProductsTab;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Product;
import salesmanagement.salesmanagement.ViewController.FilterView;

public class ProductsFilterView extends FilterView<Product> implements ProductsTab {
    @FXML
    private TextField productCodeTextField;

    @FXML
    private TextField productLineTextField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField productVendorTextField;

    @FXML
    public void applyFilter() {
        filteredList.setPredicate(product -> {
            boolean nameMatch = product.getProductName().toLowerCase().contains(productNameTextField.getText().toLowerCase());
            boolean productLineMatch = product.getProductLine().toLowerCase().contains(productLineTextField.getText().toLowerCase());
            boolean productCodeMatch = product.getProductCode().toLowerCase().contains(productCodeTextField.getText().toLowerCase());
            boolean productVendorMatch = product.getProductVendor().contains(productVendorTextField.getText());
            return nameMatch && productCodeMatch && productLineMatch && productVendorMatch;
        });
        close();
    }
}
