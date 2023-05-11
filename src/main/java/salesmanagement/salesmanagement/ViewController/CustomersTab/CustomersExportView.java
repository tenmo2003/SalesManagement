package salesmanagement.salesmanagement.ViewController.CustomersTab;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.ExportView;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.Utils.exportToExcel;

public class CustomersExportView extends ExportView implements CustomersTab {
    @FXML
    private JFXCheckBox SSN;

    @FXML
    private JFXCheckBox address;

    @FXML
    private JFXCheckBox customerNumber;

    @FXML
    private JFXCheckBox phoneNumber;

    @FXML
    private JFXCheckBox rank;

    @FXML
    private JFXCheckBox registerDate;

    @FXML
    private JFXCheckBox name;

    @FXML
    public File export() {
        setExportedFileName("Customers_List");
        File file = super.export();

        if (file != null)
            runTask(() -> {
                close();
                List<String> selectedColumns = new ArrayList<>();
                if (customerNumber.isSelected()) selectedColumns.add("customerNumber");
                if (name.isSelected()) selectedColumns.add("customerName");
                if (rank.isSelected()) selectedColumns.add("rank");
                if (address.isSelected()) selectedColumns.add("addressLine");
                if (phoneNumber.isSelected()) selectedColumns.add("phone");

                String query = "SELECT * FROM customers";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    exportToExcel(resultSet, file, selectedColumns);
                } catch (Exception e) {
                    Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.ERROR_EXPORTING, stage));
                }
            }, () -> {
                NotificationSystem.throwNotification(NotificationCode.SUCCEED_EXPORTING, stage);
            }, loadingIndicator, null);
        return file;
    }

}
