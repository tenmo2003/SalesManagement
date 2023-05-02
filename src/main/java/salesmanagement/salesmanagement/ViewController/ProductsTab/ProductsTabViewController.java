package salesmanagement.salesmanagement.ViewController.ProductsTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Product;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class ProductsTabViewController extends ViewController implements ProductsTabController {

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> productCodeColumn;

    @FXML
    private TableColumn<?, ?> productLineColumn;

    @FXML
    private TableColumn<?, ?> productVendorColumn;

    @FXML
    private FilteredTableView<Product> productsTable;

    @FXML
    private StackPane root;

    @FXML
    void addFilter() {
        productsFilterViewController.show();
    }

    @FXML
    void addNewProduct() {
        productInfoViewController.show();
    }

    public ProgressIndicator getLoadingIndicator() {
        return loadingIndicator;
    }

    @FXML
    void openExportProductsBox() {
        productsExportViewController.show();
    }

    SortedList<Product> sortedAndFilteredProducts;

    // View inside.
    ProductInfoViewController productInfoViewController;
    ViewController productsExportViewController;
    ProductsFilterViewController productsFilterViewController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productLineColumn.setCellValueFactory(new PropertyValueFactory<>("productLine"));
        productVendorColumn.setCellValueFactory(new PropertyValueFactory<>("productVendor"));

        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/product-info-view.fxml"));
            loader.load();
            productInfoViewController = loader.getController();
            root.getChildren().add(productInfoViewController.getRoot());
            productInfoViewController.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-export-view.fxml"));
            loader.load();
            productsExportViewController = loader.getController();
            root.getChildren().add(productsExportViewController.getRoot());
            productsExportViewController.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-filter-view.fxml"));
            loader.load();
            productsFilterViewController = loader.getController();
            root.getChildren().add(productsFilterViewController.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                productInfoViewController.show(productsTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    @Override
    public void show() {
        super.show();
        runTask(() -> {
            productInfoViewController.loadProductLine();
            List<Product> products = new ArrayList<>();
            try {
                String query = "SELECT productCode, productName, productLine, productVendor, productDescription, quantityInStock, buyPrice, sellPrice FROM products";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getDouble(7),
                            resultSet.getDouble(8)
                    );
                    products.add(product);
                }

                productsFilterViewController.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(products)));
                sortedAndFilteredProducts = new SortedList<>(productsFilterViewController.getFilteredList());
                productsTable.setItems(sortedAndFilteredProducts);
                sortedAndFilteredProducts.comparatorProperty().bind(productsTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, loadingIndicator, null);
    }
}
