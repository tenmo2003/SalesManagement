package salesmanagement.salesmanagement.ViewController.ProductsTab;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.ExportViewController;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.Utils.exportToExcel;

public class ProductsExportViewController extends ExportViewController implements ProductsTabController{
    @FXML
    private JFXCheckBox buyPrice;

    @FXML
    private JFXCheckBox description;

    @FXML
    private JFXCheckBox inStock;

    @FXML
    private JFXCheckBox productCode;

    @FXML
    private JFXCheckBox productName;

    @FXML
    private JFXCheckBox productVendor;

    @FXML
    private JFXCheckBox productLine;

    @FXML
    private JFXCheckBox sellPrice;

    @FXML
    public File export() {
        setExportedFileName("products_list");
        File file = super.export();
        if (file != null)
            runTask(() -> {
                close();
                List<String> selectedColumns = new ArrayList<>();
                if (productName.isSelected()) selectedColumns.add("productName");
                if (productLine.isSelected()) selectedColumns.add("productLine");
                if (productCode.isSelected()) selectedColumns.add("productCode");
                if (inStock.isSelected()) selectedColumns.add("quantityInStock");
                if (productVendor.isSelected()) selectedColumns.add("productVendor");
                if (description.isSelected()) selectedColumns.add("productDescription");
                if (buyPrice.isSelected()) selectedColumns.add("buyPrice");
                if (sellPrice.isSelected()) selectedColumns.add("sellPrice");

                String query = "SELECT * FROM products";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    exportToExcel(resultSet, file, selectedColumns);
                } catch (Exception e) {
                    Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.ERROR_EXPORTING, stage));
                }
            }, () -> {
                NotificationSystem.throwNotification(NotificationCode.SUCCEED_EXPORTING, stage);
            }, parentController.getLoadingIndicator(), null);
        return file;
    }
}
