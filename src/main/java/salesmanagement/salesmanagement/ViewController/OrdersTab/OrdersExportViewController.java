package salesmanagement.salesmanagement.ViewController.OrdersTab;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.ExportViewController;
import salesmanagement.salesmanagement.ViewController.ProductsTab.ProductsTabController;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.Utils.exportToExcel;

public class OrdersExportViewController extends ExportViewController implements ProductsTabController {

    @FXML
    private JFXCheckBox comments;

    @FXML
    private JFXCheckBox customerNumber;

    @FXML
    private JFXCheckBox destination;

    @FXML
    private JFXCheckBox employeeNumber;

    @FXML
    private JFXCheckBox orderNumber;

    @FXML
    private JFXCheckBox orderedDate;

    @FXML
    private JFXCheckBox paymentMethod;

    @FXML
    private JFXCheckBox requiredDate;

    @FXML
    private JFXCheckBox shippedDate;

    @FXML
    private JFXCheckBox status;

    @FXML
    private JFXCheckBox type;

    @FXML
    private JFXCheckBox value;

    @FXML
    public File export(MouseEvent event) {
        setExportedFileName("orders_list");
        File file = super.export();
        if (file != null)
            runTask(() -> {
                close();
                List<String> selectedColumns = new ArrayList<>();
                if (orderNumber.isSelected()) selectedColumns.add("orderNumber");
                if (orderedDate.isSelected()) selectedColumns.add("orderedDate");
                if (requiredDate.isSelected()) selectedColumns.add("requiredDate");
                if (shippedDate.isSelected()) selectedColumns.add("shippedDate");
                if (status.isSelected()) selectedColumns.add("status");
                if (comments.isSelected()) selectedColumns.add("comments");
                if (customerNumber.isSelected()) selectedColumns.add("customerNumber");
                if (type.isSelected()) selectedColumns.add("type");
                if (value.isSelected()) selectedColumns.add("value");
                if (paymentMethod.isSelected()) selectedColumns.add("payment_method");
                if (employeeNumber.isSelected()) selectedColumns.add("created_by");
                if (destination.isSelected()) selectedColumns.add("deliver_to");
                String query = "SELECT * FROM orders;";
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
