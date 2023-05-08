package salesmanagement.salesmanagement.ViewController.ProductLinesTab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.ProductLine;
import salesmanagement.salesmanagement.ViewController.InfoView;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductLineInfoView extends InfoView<ProductLine> implements ProductLinesTab {
    @FXML
    private Label boxLabel;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField mainProductVendorTextField;
    @FXML
    private TextField productLineTextField;
    @FXML
    private TextField quantityInStockTextField;
    @FXML
    private TextField totalRevenueTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

    }

    @Override
    protected void save() {

    }

    @Override
    protected void add() {

    }

    public void show(ProductLine productLine) {
        super.show();
        saveButton.setVisible(true);
        quantityInStockTextField.setText(String.valueOf(productLine.getNumberOfProducts()));
        productLineTextField.setText(productLine.getProductLine());
        mainProductVendorTextField.setText(productLine.getMainProductVendor());
        totalRevenueTextField.setText(productLine.getMainProductVendor());
        descriptionTextField.setText(productLine.getDescription());
    }

    @Override
    protected void resetData() {
        super.resetData();
        totalRevenueTextField.setText("");
        productLineTextField.setText("");
        descriptionTextField.setText("");
        mainProductVendorTextField.setText("");
        quantityInStockTextField.setText("");
    }
}
