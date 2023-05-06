package salesmanagement.salesmanagement.ViewController.ProductsTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Product;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.TabView;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class ProductsTabView extends TabView implements ProductsTab {

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
        productsFilterView.show();
    }

    @FXML
    void addNewProduct() {
        productInfoView.show();
    }

    @FXML
    void openExportProductsBox() {
        productsExportViewController.show();
    }

    SortedList<Product> sortedAndFilteredProducts;

    ProductInfoView productInfoView;
    ViewController productsExportViewController;
    ProductsFilterView productsFilterView;

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
            productInfoView = loader.getController();
            root.getChildren().add(productInfoView.getRoot());
            productInfoView.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-export-view.fxml"));
            loader.load();
            productsExportViewController = loader.getController();
            root.getChildren().add(productsExportViewController.getRoot());
            productsExportViewController.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-filter-view.fxml"));
            loader.load();
            productsFilterView = loader.getController();
            root.getChildren().add(productsFilterView.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                productInfoView.show(productsTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        runTask(() -> {
            productInfoView.loadProductLine();
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

                productsFilterView.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(products)));
                sortedAndFilteredProducts = new SortedList<>(productsFilterView.getFilteredList());
                productsTable.setItems(sortedAndFilteredProducts);
                sortedAndFilteredProducts.comparatorProperty().bind(productsTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, () -> isShowing = false, loadingIndicator, null);
    }
}
