package salesmanagement.salesmanagement.ViewController.ProductLinesTab;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.ExportView;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.Utils.exportToExcel;

public class ProductLinesExportView  extends ExportView implements ProductLinesTab{
    @FXML
    private JFXCheckBox description;

    @FXML
    private JFXCheckBox mainProductVendor;

    @FXML
    private JFXCheckBox productLine;

    @FXML
    private JFXCheckBox quantityInStock;

    @FXML
    private JFXCheckBox totalRevenue;

    @Override
    protected File export() {
        setExportedFileName("productlines_list");
        File file = super.export();
        if (file != null)
            runTask(() -> {
                close();
                List<String> selectedColumns = new ArrayList<>();
                if (productLine.isSelected()) selectedColumns.add("productLine");
                if (description.isSelected()) selectedColumns.add("textDescription");
                if (totalRevenue.isSelected()) selectedColumns.add("totalRevenue");
                if (quantityInStock.isSelected()) selectedColumns.add("number");
                if (mainProductVendor.isSelected()) selectedColumns.add("productVendor");

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
                        "right join productlines on totalRevenueTable.productLine = productlines.productLine;\n";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    exportToExcel(resultSet, file, selectedColumns);
                } catch (Exception e) {
                    Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.ERROR_EXPORTING, stage));
                }
            }, () -> NotificationSystem.throwNotification(NotificationCode.SUCCEED_EXPORTING, stage), parentController.getLoadingIndicator(), null);
        return file;
    }
}
