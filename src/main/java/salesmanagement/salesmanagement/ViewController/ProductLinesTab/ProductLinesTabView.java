package salesmanagement.salesmanagement.ViewController.ProductLinesTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Product;
import salesmanagement.salesmanagement.SalesComponent.ProductLine;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class ProductLinesTabView extends TabView implements Initializable, ProductLinesTab {
    @FXML
    private TableColumn<?, ?> descriptionColumn;
    @FXML
    private TableColumn<?, ?> mainProductVendorColumn;
    @FXML
    private TableColumn<?, ?> numberOfProductsColumn;
    @FXML
    private TableColumn<?, ?> productLineColumn;
    @FXML
    private FilteredTableView<ProductLine> productLinesTable;
    @FXML
    private TableColumn<?, ?> totalRevenueColumn;

    private SortedList<ProductLine> sortedAndFilteredProductLines;

    ProductLineInfoView productLineInfoView;
    ProductLinesFilterView productLinesFilterView;
    ProductLinesExportView productLinesExportView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productLineColumn.setCellValueFactory(new PropertyValueFactory<>("productLine"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        mainProductVendorColumn.setCellValueFactory(new PropertyValueFactory<>("mainProductVendor"));
        numberOfProductsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfProducts"));

        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/productlines-tab/productline-info-view.fxml"));
            loader.load();
            productLineInfoView = loader.getController();
            root.getChildren().add(productLineInfoView.getRoot());
            productLineInfoView.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/productlines-tab/productlines-export-view.fxml"));
            loader.load();
            productLinesExportView = loader.getController();
            root.getChildren().add(productLinesExportView.getRoot());
            productLinesExportView.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/productlines-tab/productlines-filter-view.fxml"));
            loader.load();
            productLinesFilterView = loader.getController();
            root.getChildren().add(productLinesFilterView.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        productLinesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                productLineInfoView.show(productLinesTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    void addFilter() {
        productLinesFilterView.show();
    }

    @FXML
    void addNewProductLine() {
        productLineInfoView.show();
    }

    @FXML
    void openExportProductLinesBox() {
        productLinesExportView.show();
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        runTask(() -> {
            List<ProductLine> productLines = new ArrayList<>();
            try {
                String query = "SELECT productlines.*,\n" +
                        "       totalRevenueTable.totalRevenue as 'totalRevenue',\n" +
                        "       numberofproducts.number as 'numberOfProducts',\n" +
                        "       mainvendor.productVendor as 'mainProductVendor'\n" +
                        "from (SELECT SUM(priceEach * quantityOrdered) AS totalRevenue, products.productLine\n" +
                        "      FROM orderdetails\n" +
                        "               INNER JOIN products ON orderdetails.productCode = products.productCode\n" +
                        "               INNER JOIN productlines ON products.productLine = productlines.productLine\n" +
                        "      GROUP BY products.productLine) as totalRevenueTable\n" +
                        "         inner join (select sum(quantityInStock) as 'number', products.productLine\n" +
                        "                     from products\n" +
                        "                     GROUP BY products.productLine) as numberofproducts\n" +
                        "                    on totalRevenueTable.productLine = numberofproducts.productLine\n" +
                        "         inner join (select max(t.number), productVendor, productLine\n" +
                        "                     from (SELECT products.productLine, productVendor, sum(quantityInStock) as 'number'\n" +
                        "                           FROM products\n" +
                        "                                    INNER JOIN productlines ON products.productLine = productlines.productLine\n" +
                        "                           GROUP BY productlines.productLine, productVendor) as t\n" +
                        "                     group by t.productLine) as mainvendor on mainvendor.productLine = totalRevenueTable.productLine\n" +
                        "inner join productlines on totalRevenueTable.productLine = productlines.productLine;\n";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    ProductLine productLine = new ProductLine(resultSet);
                    productLines.add(productLine);
                }

                productLinesFilterView.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(productLines)));
                sortedAndFilteredProductLines = new SortedList<>(productLinesFilterView.getFilteredList());
                productLinesTable.setItems(sortedAndFilteredProductLines);
                sortedAndFilteredProductLines.comparatorProperty().bind(productLinesTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, () -> isShowing = false, loadingIndicator, null);
    }
}
