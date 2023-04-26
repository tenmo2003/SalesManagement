package salesmanagement.salesmanagement.ViewController;

import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import salesmanagement.salesmanagement.NotificationCode;
import salesmanagement.salesmanagement.NotificationSystem;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static salesmanagement.salesmanagement.Utils.exportToExcel;
import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class EmployeesExportViewController extends ViewController {


    @FXML
    private JFXCheckBox accessibility;

    @FXML
    private JFXCheckBox birthDate;

    @FXML
    private JFXCheckBox email;

    @FXML
    private JFXCheckBox employeeNumber;

    @FXML
    private VBox exportEmployeesBox;

    @FXML
    private JFXCheckBox firstName;

    @FXML
    private JFXCheckBox gender;

    @FXML
    private JFXCheckBox joiningDate;

    @FXML
    private JFXCheckBox lastName;

    @FXML
    private JFXCheckBox lastWorkingDate;

    @FXML
    private JFXCheckBox officeCode;

    @FXML
    private JFXCheckBox password;

    @FXML
    private JFXCheckBox phoneCode;

    @FXML
    private JFXCheckBox phoneNumber;


    @FXML
    private JFXCheckBox status;

    @FXML
    private JFXCheckBox supervisorCode;

    @FXML
    private JFXCheckBox username;


    @FXML
    void exportEmployeesList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx"));
        fileChooser.setInitialFileName("Employees_List.xlsx");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null)
            runTask(() -> {
                List<String> selectedColumns = new ArrayList<>();
                if (employeeNumber.isSelected()) selectedColumns.add("employeeNumber");
                if (officeCode.isSelected()) selectedColumns.add("officeCode");
                if (email.isSelected()) selectedColumns.add("email");
                if (firstName.isSelected()) selectedColumns.add("firstName");
                if (lastName.isSelected()) selectedColumns.add("lastName");
                if (supervisorCode.isSelected()) selectedColumns.add("reportsTo");
                if (accessibility.isSelected()) selectedColumns.add("jobTitle");
                if (username.isSelected()) selectedColumns.add("username");
                if (password.isSelected()) selectedColumns.add("password");
                if (status.isSelected()) selectedColumns.add("status");
                if (phoneNumber.isSelected()) selectedColumns.add("phone");
                if (phoneCode.isSelected()) selectedColumns.add("phoneCode");
                if (birthDate.isSelected()) selectedColumns.add("birthDate");
                if (joiningDate.isSelected()) selectedColumns.add("joiningDate");
                if (lastWorkingDate.isSelected()) selectedColumns.add("lastWorkingDate");
                if (gender.isSelected()) selectedColumns.add("gender");

                String query = "SELECT * FROM employees";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    exportToExcel(resultSet, file, selectedColumns);
                } catch (Exception e) {
                    Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.ERROR_EXPORTING, stage));
                }
            }, () -> {
                exportEmployeesBox.getParent().setVisible(false);
                NotificationSystem.throwNotification(NotificationCode.SUCCEED_EXPORTING, stage);
            }, null, null);


    }


}
