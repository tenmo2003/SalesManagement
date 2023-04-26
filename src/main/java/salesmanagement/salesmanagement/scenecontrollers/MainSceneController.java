package salesmanagement.salesmanagement.scenecontrollers;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jfoenix.controls.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import salesmanagement.salesmanagement.*;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesComponent.Order;
import salesmanagement.salesmanagement.SalesComponent.OrderItem;
import salesmanagement.salesmanagement.SalesComponent.Product;
import salesmanagement.salesmanagement.ViewController.EmployeesExportViewController;
import salesmanagement.salesmanagement.ViewController.EmployeesFilterViewController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static salesmanagement.salesmanagement.InputErrorCode.*;
import static salesmanagement.salesmanagement.Utils.*;

public class MainSceneController extends SceneController implements Initializable {
    @FXML
    Text usernameText;
    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesOperationTab;
    @FXML
    private Tab createOrderTab;
    boolean createOrderInit = false;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;
    boolean productsInit = false;
    @FXML
    private Tab ordersTab;
    boolean ordersInit = false;
    @FXML
    JFXButton dashBoardTabButton;
    @FXML
    JFXButton ordersTabButton;
    @FXML
    JFXButton newsTabButton;
    @FXML
    JFXButton settingsTabButton;
    @FXML
    JFXButton productsTabButton;
    @FXML
    JFXButton employeesTabButton;
    JFXButton currentTabButton;


    Node employeesExportView;
    Node employeesFilterView;
    EmployeesExportViewController employeesExportViewController;
    EmployeesFilterViewController employeesFilterViewController;

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);

    }

    @FXML
    void goToOrdersTab() {
        if (!ordersInit) {
            initOrders();
        }
        tabPane.getSelectionModel().select(ordersTab);
    }

    void goToEditOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
    }

    @FXML
    void goToProductsOperationTab() {
        if (!productsInit) {
            initProducts();
        }
        tabPane.getSelectionModel().select(productsOperationTab);
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
    }

    public Node getMainScenePane() {
        return secondSplitPane;
    }

    //region DashBoard Tab
    @FXML
    Tab dashBoardTab;

    @FXML
    private StackPane chartPane;

    public void displayDashBoardTab() {

        tabPane.getSelectionModel().select(dashBoardTab);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc =
                new BarChart<>(xAxis, yAxis);
        chartPane.getChildren().add(bc);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2003");
        series1.getData().add(new XYChart.Data("austria", 25601.34));
        series1.getData().add(new XYChart.Data("brazil", 20148.82));
        series1.getData().add(new XYChart.Data("france", 10000));
        series1.getData().add(new XYChart.Data("italy", 35407.15));
        series1.getData().add(new XYChart.Data("usa", 12000));


        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data("austria", 57401.85));
        series2.getData().add(new XYChart.Data("brazil", 41941.19));
        series2.getData().add(new XYChart.Data("france", 45263.37));
        series2.getData().add(new XYChart.Data("italy", 117320.16));
        series2.getData().add(new XYChart.Data("usa", 14845.27));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2005");
        series3.getData().add(new XYChart.Data("austria", 45000.65));
        series3.getData().add(new XYChart.Data("brazil", 44835.76));
        series3.getData().add(new XYChart.Data("france", 18722.18));
        series3.getData().add(new XYChart.Data("italy", 17557.31));
        series3.getData().add(new XYChart.Data("usa", 92633.68));
        bc.getData().addAll(series1, series2, series3);
    }
    //endregion

    //region Employees Tab: list employees, employee's info: details, order, operations.
    @FXML
    TableView<Employee> employeeTable;
    @FXML
    private TableColumn<?, ?> emailColumn;
    @FXML
    private TableColumn<Employee, Integer> employeeNumberColumn;
    @FXML
    private TableColumn<?, ?> nameColumn;
    @FXML
    private TableColumn<?, ?> phoneColumn;
    @FXML
    private TableColumn<?, ?> employeeStatusColumn;
    @FXML
    private TableColumn<?, ?> accessibilityColumn;
    ArrayList<Employee> employees;
    @FXML
    StackPane employeesTabPane;
    @FXML
    ProgressIndicator employeeLoadingIndicator;
    @FXML
    VBox employeeFilterBox;
    @FXML
    TextField employeeNameFilterTextField;
    @FXML
    TextField phoneFilterTextField;
    @FXML
    TextField emailFilterTextField;
    @FXML
    ComboBox<String> employeeStatusFilterComboBox;
    @FXML
    ComboBox<String> accessibilityFilterComboBox;
    FilteredList<Employee> filteredEmployees;
    SortedList<Employee> sortedAndFilteredEmployees;

    @FXML
    void addEmployeeFilter() {
        employeeFilterBox.getParent().setVisible(true);
    }

    @FXML
    public void clearEmployeeFilter() {
        employeeNameFilterTextField.setText("");
        phoneFilterTextField.setText("");
        emailFilterTextField.setText("");
        employeeStatusFilterComboBox.setValue(null);
        accessibilityFilterComboBox.setValue(null);
    }

    @FXML
    public void applyEmployeeFilter() {
        filteredEmployees.setPredicate(employee -> {
            // Check if the order matches the filter criteria
            boolean nameMatch = employee.getFullName().toLowerCase().contains(employeeNameFilterTextField.getText().toLowerCase());
            boolean emailMatch = employee.getEmail().toLowerCase().contains(emailFilterTextField.getText().toLowerCase());
            boolean phoneMatch = employee.getPhone().toLowerCase().contains(phoneFilterTextField.getText().toLowerCase());
            boolean statusMatch = employee.getStatus().equals(employeeStatusFilterComboBox.getValue());
            if (employeeStatusFilterComboBox.getValue() == null) statusMatch = true;
            boolean accessibilityMatch = employee.getJobTitle().equals(accessibilityFilterComboBox.getValue());
            if (accessibilityFilterComboBox.getValue() == null) accessibilityMatch = true;
            return nameMatch && emailMatch && phoneMatch && statusMatch && accessibilityMatch;
        });
        employeeFilterBox.getParent().setVisible(false);
    }

    @FXML
    public void closeLayerBox(MouseEvent event) {
        ((Node) event.getSource()).getParent().getParent().getParent().setVisible(false);
    }

    @FXML
    void displayEmployeesTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
        ArrayList<Employee> employees = new ArrayList<>();
        runTask(() -> {
            ResultSet resultSet = null;
            String query = "SELECT * FROM employees";
            resultSet = sqlConnection.getDataQuery(query);
//            while (resultSet == null) {
//                System.out.println("reconnect...");
//                sqlConnection.connectServer();
//                resultSet = sqlConnection.getDataQuery(query);
//            }
            try {
                while (resultSet.next()) {
                    employees.add(new Employee(resultSet));
                }
            } catch (SQLException ignored) {
            }
        }, () -> {
            ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
            filteredEmployees = new FilteredList<>(employeeList, p -> true);
            sortedAndFilteredEmployees = new SortedList<>(filteredEmployees);
            employeeTable.setItems(sortedAndFilteredEmployees);
            sortedAndFilteredEmployees.comparatorProperty().bind(employeeTable.comparatorProperty());
            this.employees = employees;
        }, employeeLoadingIndicator, employeeTable.getParent());
    }

    @FXML
    VBox employeeInfoBox;
    @FXML
    VBox detailsInfoBox;
    @FXML
    JFXButton editInfoButton;
    @FXML
    JFXButton saveInfoButton;
    @FXML
    JFXButton saveNewEmployeeButton;

    @FXML
    public void openExportEmployeesBox() {
        employeesExportViewController.show();
    }

    @FXML
    public void addNewEmployee() {
        employeeInfoBox.setVisible(true);
        saveNewEmployeeButton.setVisible(true);

        employeeTableBox.setVisible(false);

        for (Node node : getAllNodes(detailsInfoBox)) {
            node.setDisable(false);
        }
        employeeCodeTextField.setDisable(true);
        topDetailBoxLabel.setText("Add New Employee");
        avatar.setImage(ImageController.getImage("sample_avatar.jpg"));
        lastNameTextField.setText("");
        firstNameTextField.setText("");
        usernameTextField.setText("");
        passwordField.setText("");
        birthDatePicker.setValue(null);
        emailTextField.setText("");
        employeeCodeTextField.setText("");
        officeCodeTextField.setText("");
        phoneCodeBox.setValue("+84 (VN)");
        joiningDatePicker.setValue(null);
        lastWorkingDatePicker.setValue(null);
        phoneNumberTextField.setText("");
        supervisorTextField.setText("");
        statusBox.setValue(null);
        accessibilityBox.setValue("Employee");
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
    }

    @FXML
    public void saveNewEmployee() {
        AtomicBoolean allValid = new AtomicBoolean(false);
        runTask(() -> {
            allValid.set(checkValidEmployeeInput());
            if (!allValid.get()) {
                Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.INVALID_INPUTS, stage));
            } else {
                String query = "INSERT INTO `employees` (`employeeNumber`, `lastName`, `firstName`, `birthDate`, `gender`, `email`, `mailVerified`, " +
                        "`officeCode`, `reportsTo`, `jobTitle`, `username`, `password`, `phoneCode`, `phone`, `status`, `joiningDate`, `lastWorkingDate`) " +
                        "VALUES (NULL,'" + lastNameTextField.getText() + "', '" + firstNameTextField.getText() + "', '" + birthDatePicker.getValue()
                        + "','" + (maleRadioButton.isSelected() ? "male" : "female") + "', '" + emailTextField.getText() + "', '0', " +
                        "'" + officeCodeTextField.getText() + "'," + supervisorTextField.getText() + ", '" + accessibilityBox.getValue() + "', '"
                        + usernameTextField.getText() + "', '" + passwordField.getText() + "', '" + phoneCodeBox.getValue()
                        + "', '" + phoneNumberTextField.getText() + "', 'ACTIVE', '" + joiningDatePicker.getValue() + "', '"
                        + lastWorkingDatePicker.getValue() + "')";
                sqlConnection.updateQuery(query);
                query = "SELECT employeeNumber FROM employees WHERE username = '" + usernameTextField.getText() + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    if (resultSet.next()) {
                        ImageController.uploadImage(avatarAddress.getText(), "avatar_employee_" + resultSet.getInt("employeeNumber") + ".png");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, () -> {
            if (allValid.get()) {
                saveNewEmployeeButton.setVisible(false);
                backToEmployeeTableBox();
                displayEmployeesTab();
                NotificationSystem.throwNotification(NotificationCode.SUCCEED_ADD_NEW_EMPLOYEE, stage);
            }
        }, employeeDetailLoading, employeeDetailTabPane);
    }

    @FXML
    ProgressIndicator employeeDetailLoading;
    @FXML
    TabPane employeeDetailTabPane;
    @FXML
    Label topDetailBoxLabel;

    /**
     * When click on Name Text in Employee table, this function will be called to
     * display detail information of employee. It's called by an Employee object.
     * It hides employees table box and shows employee information box.
     */
    public void displayEmployeeInfoBox(Employee employee) {
        employeeInfoBox.setVisible(true);

        employeeTableBox.setVisible(false);

        editInfoButton.setVisible(true);
        saveInfoButton.setVisible(true);

        for (Node node : getAllNodes(detailsInfoBox)) {
            node.setDisable(false);
        }
        avatar.setImage(ImageController.getImage("avatar_employee_" + employee.getEmployeeNumber() + ".png", true));
        topDetailBoxLabel.setText(employee.getFullName());
        lastNameTextField.setText(employee.getLastName());
        firstNameTextField.setText(employee.getFirstName());
        usernameTextField.setText(employee.getUsername());
        passwordField.setText(employee.getPassword());
        birthDatePicker.setValue(employee.getBirthDate());
        emailTextField.setText(employee.getEmail());
        phoneCodeBox.setValue(employee.getPhoneCode());
        joiningDatePicker.setValue(employee.getJoiningDate());
        officeCodeTextField.setText(employee.getOfficeCode());
        supervisorTextField.setText(Integer.toString(employee.getReportsTo()));
        lastWorkingDatePicker.setValue(employee.getLastWorkingDate());
        phoneNumberTextField.setText(employee.getPhone());
        statusBox.setValue(employee.getStatus());
        accessibilityBox.setValue(employee.getJobTitle());
        if (Objects.equals(employee.getGender(), "male")) maleRadioButton.setSelected(true);
        else if (Objects.equals(employee.getGender(), "female")) femaleRadioButton.setSelected(true);

        // LOCK ALL
        for (Node node : getAllNodes(detailsInfoBox)) {
            if (node instanceof JFXTextField || node instanceof JFXPasswordField || node instanceof JFXButton || node instanceof JFXRadioButton || node instanceof JFXComboBox<?> || node instanceof DatePicker) {
                node.setDisable(true);
            }
        }
        editInfoButton.setDisable(false);
    }

    private boolean checkValidEmployeeInput() {
        if (firstNameTextField.getText().equals("")) return false;

        if (lastNameTextField.getText().equals("")) return false;

        if (!emailTextField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) return false;

        if (birthDatePicker.getValue() == null) return false;

        if (joiningDatePicker.getValue() == null) return false;

        if (lastWorkingDatePicker.getValue() == null) return false;

        String query = "SELECT officeCode FROM offices WHERE officeCode = '" + officeCodeTextField.getText() + "'";
        ResultSet resultSet = sqlConnection.getDataQuery(query);
        try {
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            int employeeCode = Integer.parseInt(supervisorTextField.getText());
            query = "SELECT employeeNumber FROM employees WHERE employeeNumber = " + employeeCode;
            resultSet = sqlConnection.getDataQuery(query);
            try {
                if (!resultSet.next())
                    return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            return false;
        }


        if (!phoneNumberTextField.getText().matches("\\d+")) return false;

        if (usernameTextField.getText().length() > 30 || usernameTextField.getText().length() < 6) {
            return false;
        } else if (!usernameTextField.getText().matches("^[a-zA-Z0-9.]*$")) {
            return false;
        } else {
            query = "SELECT username FROM employees WHERE username = '" + usernameTextField.getText() + "'";
            resultSet = sqlConnection.getDataQuery(query);
            try {
                if (resultSet.next()) {
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return passwordField.getCharacters().toString().length() >= 8;

    }

    @FXML
    public void editEmployeeInfo() {
        // UNLOCK ALL
        for (Node node : getAllNodes(detailsInfoBox)) {
            node.setDisable(false);
        }
        editInfoButton.setDisable(true);
        saveInfoButton.setDisable(false);
        employeeCodeTextField.setDisable(true);
    }

    @FXML
    public void saveEmployeeInfo() {
        // LOCK ALL
        for (Node node : getAllNodes(detailsInfoBox)) {
            if (node instanceof JFXTextField || node instanceof JFXPasswordField || node instanceof JFXButton || node instanceof JFXRadioButton || node instanceof JFXComboBox<?> || node instanceof DatePicker) {
                node.setDisable(true);
            }
        }
        editInfoButton.setDisable(false);
        saveInfoButton.setDisable(true);
        // Save DATA To DB

    }

    @FXML
    TableView orders_employeeTable;

    @FXML
    public void chooseOrders_employeeTab() {

    }

    @FXML
    private JFXButton employeeBackButton;

    /**
     * Called when clicking "BACK" button, return to employees list.
     * It shows employees table box and hides employee information box.
     * It relates to {@link  #displayEmployeeInfoBox(Employee)};
     */
    @FXML
    void backToEmployeeTableBox() {
        employeeInfoBox.setVisible(false);

        employeeTableBox.setVisible(true);

        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);

        editInfoButton.setVisible(false);
        saveInfoButton.setVisible(false);
        saveNewEmployeeButton.setVisible(false);

        refreshEmployeeInfoTab();
    }

    private void refreshEmployeeInfoTab() {
        VBox container = (VBox) firstNameTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        firstNameTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) lastNameTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        lastNameTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) emailTextField.getParent().getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        emailTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) supervisorTextField.getParent().getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        supervisorTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) officeCodeTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        officeCodeTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) phoneNumberTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        phoneNumberTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) lastWorkingDatePicker.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        lastWorkingDatePicker.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) birthDatePicker.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        birthDatePicker.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) joiningDatePicker.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        joiningDatePicker.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) usernameTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        usernameTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) passwordField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        passwordField.setStyle("-fx-border-color: #d1d1d1");
    }

    @FXML
    void uploadAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            avatar.setImage(new Image(selectedFile.toURI().toString()));
            avatarAddress.setDisable(false);
            avatarAddress.setText(selectedFile.getAbsolutePath());
            avatarAddress.setDisable(true);
        }
    }

    @FXML
    ImageView avatar;
    @FXML
    Label fullNameLabel;
    @FXML
    JFXTextField lastNameTextField;
    @FXML
    JFXTextField firstNameTextField;
    @FXML
    DatePicker birthDatePicker;
    @FXML
    JFXRadioButton maleRadioButton;
    @FXML
    JFXRadioButton femaleRadioButton;
    @FXML
    JFXTextField avatarAddress;
    @FXML
    JFXComboBox<String> statusBox;
    @FXML
    JFXTextField emailTextField;
    @FXML
    JFXButton verifyButton;
    @FXML
    JFXComboBox<String> phoneCodeBox;
    @FXML
    JFXTextField phoneNumberTextField;
    @FXML
    JFXTextField usernameTextField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    JFXTextField employeeCodeTextField;
    @FXML
    JFXTextField officeCodeTextField;
    @FXML
    JFXComboBox<String> accessibilityBox;
    @FXML
    JFXTextField supervisorTextField;
    @FXML
    DatePicker joiningDatePicker;
    @FXML
    DatePicker lastWorkingDatePicker;

    //endregion
    @FXML
    SplitPane firstSplitPane;
    @FXML
    SplitPane secondSplitPane;

    @FXML
    VBox employeeTableBox;
    @FXML
    HBox appName;
    @FXML
    ImageView smallAvatar;

    @Override
    protected void maximumStage(MouseEvent mouseEvent) {

    }

    @FXML
    StackPane menuPane;

    public void initialSetup() {
        employeesExportViewController.setSqlConnection(sqlConnection);
        // Load current UI.
        user = new Employee(sqlConnection, loggerID);
        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((StackPane) firstSplitPane.getItems().get(0)).setMinHeight(0.06 * Screen.getPrimary().getVisualBounds().getHeight());
        ((SplitPane) firstSplitPane.getItems().get(1)).setMinHeight(0.94 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((TabPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());

        Insets hboxMargin = new Insets(0, 0.8333 * Screen.getPrimary().getVisualBounds().getWidth(), 0, 0);
        StackPane.setMargin(appName, hboxMargin);

        double tableWidth = employeeTableBox.getWidth() * 0.85;
        employeeTable.setMaxWidth(tableWidth);
        employeeNumberColumn.setMinWidth(0.15 * tableWidth);
        nameColumn.setMinWidth(0.25 * tableWidth);
        phoneColumn.setMinWidth(0.2 * tableWidth);
        emailColumn.setMinWidth(0.2 * tableWidth);
        accessibilityColumn.setMinWidth(0.1 * tableWidth);
        employeeStatusColumn.setMinWidth(0.1 * tableWidth);

        Circle clip = new Circle();
        clip.setRadius(35);
        clip.setCenterX(35);
        clip.setCenterY(35);
        smallAvatar.setClip(clip);

        currentTabButton = dashBoardTabButton;


        //TODO: test area
        ScrollPane scroll = (ScrollPane) detailsInfoBox.getParent().getParent().getParent();
        Transition down = new Transition() {
            {
                setCycleDuration(Duration.INDEFINITE);
            }

            @Override
            protected void interpolate(double v) {
                scroll.setVvalue(scroll.getVvalue() + 0.001);
            }
        };

        Transition up = new Transition() {
            {
                setCycleDuration(Duration.INDEFINITE);
            }

            @Override
            protected void interpolate(double v) {
                scroll.setVvalue(scroll.getVvalue() - 0.001);
            }
        };

        //region Check regex for user's input.
        firstNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) firstNameTextField.getParent();
                if (firstNameTextField.getText().equals("")) {
                    firstNameTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(EMPTY_FIRST_NAME));
                    }
                    shake(firstNameTextField);
                } else {
                    firstNameTextField.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        lastNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) lastNameTextField.getParent();
                if (lastNameTextField.getText().equals("")) {
                    lastNameTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(EMPTY_LAST_NAME));
                    }
                    shake(lastNameTextField);
                } else {
                    lastNameTextField.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) emailTextField.getParent().getParent();
                if (!emailTextField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    emailTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(INVALID_EMAIL));
                    }
                    shake(emailTextField);
                } else {
                    emailTextField.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        birthDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) birthDatePicker.getParent();
                if (birthDatePicker.getValue() == null) {
                    birthDatePicker.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(EMPTY_DATE));
                    }
                    shake(birthDatePicker);
                } else {
                    birthDatePicker.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        joiningDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) joiningDatePicker.getParent();
                if (joiningDatePicker.getValue() == null) {
                    joiningDatePicker.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(EMPTY_DATE));
                    }
                    shake(joiningDatePicker);
                } else {
                    joiningDatePicker.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        lastWorkingDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) lastWorkingDatePicker.getParent();
                if (lastWorkingDatePicker.getValue() == null) {
                    lastWorkingDatePicker.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(EMPTY_DATE));
                    }
                    shake(lastWorkingDatePicker);
                } else {
                    lastWorkingDatePicker.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        officeCodeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) officeCodeTextField.getParent();
                String query = "SELECT officeCode FROM offices WHERE officeCode = '" + officeCodeTextField.getText() + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    if (!resultSet.next()) {
                        officeCodeTextField.setStyle("-fx-border-color: #f35050");
                        if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().add(getInputErrorLabel(INVALID_OFFICE_CODE));
                        }
                        shake(officeCodeTextField);
                    } else {
                        officeCodeTextField.setStyle("-fx-border-color: #d1d1d1");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        supervisorTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) supervisorTextField.getParent().getParent();
                int employeeCode = 0;
                try {
                    employeeCode = Integer.parseInt(supervisorTextField.getText());
                } catch (Exception e) {
                    employeeCode = -1;
                }
                String query = "SELECT employeeNumber FROM employees WHERE employeeNumber = " + employeeCode;
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    if (!resultSet.next()) {
                        supervisorTextField.setStyle("-fx-border-color: #f35050");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                        container.getChildren().add(getInputErrorLabel(INVALID_SUPERVISOR_CODE));
                        shake(supervisorTextField);
                    } else {
                        supervisorTextField.setStyle("-fx-border-color: #d1d1d1");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        phoneNumberTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) phoneNumberTextField.getParent();
                if (!phoneNumberTextField.getText().matches("\\d+")) {
                    phoneNumberTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(INVALID_PHONE_NUMBER));
                    }
                    shake(phoneNumberTextField);
                } else {
                    phoneNumberTextField.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        usernameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) usernameTextField.getParent();
                if (usernameTextField.getText().length() > 30 || usernameTextField.getText().length() < 6) {
                    usernameTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(INVALID_LENGTH_USERNAME));
                    shake(usernameTextField);
                } else if (!usernameTextField.getText().matches("^[a-zA-Z0-9.]*$")) {
                    usernameTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(INVALID_USERNAME));
                    shake(usernameTextField);
                } else {
                    String query = "SELECT username FROM employees WHERE username = '" + usernameTextField.getText() + "'";
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    try {
                        if (resultSet.next()) {
                            usernameTextField.setStyle("-fx-border-color: #f35050");
                            if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                                container.getChildren().remove(container.getChildren().size() - 1);
                            }
                            container.getChildren().add(getInputErrorLabel(EXISTED_USERNAME));
                            shake(usernameTextField);
                        } else {
                            usernameTextField.setStyle("-fx-border-color: #d1d1d1");
                            if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                                container.getChildren().remove(container.getChildren().size() - 1);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) passwordField.getParent();
                if (passwordField.getCharacters().toString().length() < 8) {
                    passwordField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(INVALID_LENGTH_PASSWORD));
                    shake(passwordField);
                } else {
                    passwordField.setStyle("-fx-border-color: #d1d1d1");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        //endregion

        // Load UI for others.
        runTask(() -> {
            // Load small avatar.
            smallAvatar.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true));

            // Prepare for employee table structure.
            employeeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
            accessibilityColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

            employeeTable.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    Employee selected = employeeTable.getSelectionModel().getSelectedItem();
                    if (selected != null) displayEmployeeInfoBox(selected);
                }
            });

            // Employees Tab Preparation.
            List<String> phoneCodes = new ArrayList<>();
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            for (String regionCode : phoneUtil.getSupportedRegions()) {
                Phonenumber.PhoneNumber exampleNumber = phoneUtil.getExampleNumber(regionCode);
                if (exampleNumber != null) {
                    int countryCode = exampleNumber.getCountryCode();
                    phoneCodes.add(String.format("+%d (%s)", countryCode, phoneUtil.getRegionCodeForCountryCode(countryCode)));
                }
            }
            phoneCodeBox.getItems().addAll(phoneCodes);

            List<String> accessibilitiesList = new ArrayList<>(Arrays.asList("HR", "Manager", "Employee", "Admin"));
            accessibilityBox.getItems().addAll(accessibilitiesList);
            accessibilityFilterComboBox.getItems().addAll(accessibilitiesList);

            List<String> statusList = new ArrayList<>(Arrays.asList("ACTIVE", "INACTIVE"));
            statusBox.getItems().addAll(statusList);
            employeeStatusFilterComboBox.getItems().addAll(statusList);

        }, null, null, null);
    }

    private Employee user;

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static boolean haveJustOpened = false;
    public static int loggerID = -1;
    public static boolean haveChangeInEmployeesTab = false;
    public static boolean haveChangeInOrdersTab = false;
    public static boolean haveChangeInHomeTab = false;
    public static boolean haveChangeInSettingTab = false;
    public static boolean haveChangeInProductTab = false;
    public AnimationTimer loginDataListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (MainSceneController.loggerID > 0) {
                runTask(() -> {
                    Platform.runLater(() -> {
                        stage.setScene(MainSceneController.this.scene);
                        stage.hide();
                        initialSetup();
                        stage.setX(0);
                        stage.setY(0);
                        stage.show();
                        displayDashBoardTab();
                    });

                }, null, null, null);
                stop();
            }
        }
    };
    @FXML
    ComboBox<String> statusInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("employees-export-view.fxml"));
            employeesExportView = (new Scene(loader.load())).getRoot();
            employeesExportViewController = loader.getController();
            employeesTabPane.getChildren().add(employeesExportView);

            loader = new FXMLLoader(SalesManagement.class.getResource("employees-filter-view.fxml"));
            employeesFilterView = (new Scene(loader.load())).getRoot();
            employeesFilterViewController = loader.getController();
            employeesTabPane.getChildren().add(employeesFilterView);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        statusInput.getItems().add("Cancelled");
        statusInput.getItems().add("Disputed");
        statusInput.getItems().add("In Process");
        statusInput.getItems().add("On Hold");
        statusInput.getItems().add("Resolved");
        statusInput.getItems().add("Shipped");

        productCodeOD.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        quantityOD.setCellValueFactory(new PropertyValueFactory<>("quantityOrdered"));
        priceEachOD.setCellValueFactory(new PropertyValueFactory<>("priceEach"));
        totalOD.setCellValueFactory(param -> {
            OrderItem orderItem = param.getValue();
            int quantity = orderItem.getQuantityOrdered();
            double price = orderItem.getPriceEach();
            double total = quantity * price;
            return new SimpleStringProperty(String.format("%.2f", total));
        });

        orderDetailsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        orderDetailsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                priceEachInput.setText(String.valueOf(newSelection.getPriceEach()));
                productCodeInput.setText(newSelection.getProductCode());
                quantityInput.setText(String.valueOf(newSelection.getQuantityOrdered()));
            }
        });

//        orderDetailsTable.getItems().addListener((ListChangeListener<OrderItem>) c -> {
//            while (c.next()) {
//                if (c.wasAdded()) {
//                    double addedTotal = c.getAddedSubList().stream()
//                            .mapToDouble(OrderItem::getAmount)
//                            .sum();
//                    totalAmount.set(totalAmount.get() + addedTotal);
//                } else if (c.wasRemoved()) {
//                    double removedTotal = c.getRemoved().stream()
//                            .mapToDouble(OrderItem::getAmount)
//                            .sum();
//                    totalAmount.set(totalAmount.get() - removedTotal);
//                }
//            }
//        });

//        orderDetailsTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal && !removeItemButtonClicked) {
//                orderDetailsTable.getSelectionModel().clearSelection();
//            }
//            removeItemButtonClicked = false;
//        });
//
//        productsTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal && !removeProductButtonClicked) {
//                productsTable.getSelectionModel().clearSelection();
//            }
//            removeProductButtonClicked = false;
//        });
//
//        ordersTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal && !removeOrderButtonClicked) {
//                ordersTable.getSelectionModel().clearSelection();
//            }
//            removeOrderButtonClicked = false;
//        });

        // Add a listener to the text property of the text field
        productCodeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 2 && newValue.length() > oldValue.length() && suggestionList.getItems().isEmpty()) {

            } else {
                // Update the suggestion list based on the current input
                if (newValue.equals("")) {
                    suggestionList.setVisible(false);
                    suggestionList.setMouseTransparent(true);
                } else {
                    if (newValue.length() > 1) {
                        List<String> suggestions = new ArrayList<>(); // Replace with your own function to retrieve suggestions
                        String sql = "SELECT productCode, sellPrice FROM products WHERE upper(productCode) LIKE upper('" + newValue + "%');";
                        ResultSet rs = sqlConnection.getDataQuery(sql);
                        try {
                            // Add each suggestion to the list
                            while (rs.next()) {
                                suggestions.add(rs.getString("productCode"));
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        if (suggestions.isEmpty() || (!suggestions.isEmpty() && suggestions.get(0).equals(newValue))) {
                            suggestionList.setMouseTransparent(true);
                            suggestionList.setVisible(false);
                            suggestionList.getItems().clear();
                            if (!suggestions.isEmpty() && suggestions.get(0).equals(newValue)) {
                                setPrice();
                            }
                        } else {
                            suggestionList.getItems().setAll(suggestions);
                            suggestionList.getSelectionModel().selectFirst();
                            suggestionList.setMouseTransparent(false);
                            suggestionList.setVisible(true);
                        }
                    }
                }
            }

        });

        // Add an event handler to the suggestion list
        suggestionList.setOnMouseClicked(event -> {
            String selectedValue = suggestionList.getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                productCodeInput.setText(selectedValue);
                setPrice();
                suggestionList.setVisible(false);
                suggestionList.getItems().clear();
            }
        });

        productCodeInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String selectedValue = suggestionList.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                    productCodeInput.setText(selectedValue);
                    setPrice();
                    suggestionList.setVisible(false);
                    suggestionList.getItems().clear();
                    productCodeInput.requestFocus();
                }
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.DOWN) {
                int selectedIndex = suggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex < suggestionList.getItems().size() - 1) {
                    suggestionList.getSelectionModel().selectNext();
                    suggestionList.scrollTo(selectedIndex + 1);
                }
                event.consume();
                productCodeInput.requestFocus();
            } else if (event.getCode() == KeyCode.UP) {
                int selectedIndex = suggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex > 0) {
                    suggestionList.getSelectionModel().selectPrevious();
                    suggestionList.scrollTo(selectedIndex - 1);
                }
                event.consume();
                productCodeInput.requestFocus();
            }
        });


        productLinePDetails.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // TextField has focus, show suggestion list
                if (!plSuggestionList.getItems().isEmpty()) {
                    plSuggestionList.setVisible(true);
                    plSuggestionList.setMouseTransparent(false);
                }
            } else {
                if (plSuggestionList.getItems().isEmpty()) {
                    plSuggestionList.setVisible(false);
                    plSuggestionList.setMouseTransparent(true);
                }
            }
        });

        productLinePDetails.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > oldValue.length() && plSuggestionList.getItems().isEmpty()) {

            } else {
                // Update the suggestion list based on the current input
                List<String> suggestions = new ArrayList<>(); // Replace with your own function to retrieve suggestions
                String sql = "SELECT productLine FROM productlines WHERE upper(productLine) LIKE upper('" + newValue + "%');";
                ResultSet rs = sqlConnection.getDataQuery(sql);
                try {
                    // Add each suggestion to the list
                    while (rs.next()) {
                        suggestions.add(rs.getString("productLine"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (suggestions.isEmpty() || (!suggestions.isEmpty() && suggestions.get(0).equals(newValue))) {
                    plSuggestionList.setMouseTransparent(true);
                    plSuggestionList.setVisible(false);
                    plSuggestionList.getItems().clear();
                } else {
                    plSuggestionList.getItems().setAll(suggestions);
                    plSuggestionList.getSelectionModel().selectFirst();
                    plSuggestionList.setMouseTransparent(false);
                    plSuggestionList.setVisible(true);
                }
            }
        });

        // Add an event handler to the suggestion list
        plSuggestionList.setOnMouseClicked(event -> {
            String selectedValue = plSuggestionList.getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                productLinePDetails.setText(selectedValue);
                plSuggestionList.setVisible(false);
                plSuggestionList.getItems().clear();
                plSuggestionList.setMouseTransparent(true);
            }
        });

        productLinePDetails.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String selectedValue = plSuggestionList.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                    productLinePDetails.setText(selectedValue);
                    plSuggestionList.setVisible(false);
                    plSuggestionList.getItems().clear();
                    productLinePDetails.requestFocus();
                }
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.DOWN) {
                int selectedIndex = plSuggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex < plSuggestionList.getItems().size() - 1) {
                    plSuggestionList.getSelectionModel().selectNext();
                    plSuggestionList.scrollTo(selectedIndex + 1);
                }
                event.consume();
                productLinePDetails.requestFocus();
            } else if (event.getCode() == KeyCode.UP) {
                int selectedIndex = plSuggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex > 0) {
                    plSuggestionList.getSelectionModel().selectPrevious();
                    plSuggestionList.scrollTo(selectedIndex - 1);
                }
                event.consume();
                productLinePDetails.requestFocus();
            }
        });

        quantityInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                totalInput.setText("");
            } else if (!priceEachInput.getText().equals(""))
                totalInput.setText(String.format("%.2f", Integer.parseInt(newValue) * Double.parseDouble(priceEachInput.getText())));
        });

        priceEachInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                totalInput.setText("");
            } else if (!quantityInput.getText().equals(""))
                totalInput.setText(String.format("%.2f", Double.parseDouble(newValue) * Integer.parseInt(quantityInput.getText())));
        });

        totalAmountForOrder.textProperty().bind(totalAmount.asString("%.2f"));

        addOrderFilterButton.setOnAction(event -> {
            bgPaneOrders.setVisible(true);
            orderFilterPane.setVisible(true);
        });
        typeFilter.getItems().add("online");
        typeFilter.getItems().add("onsite");
        applyOrderFilterButton.setOnAction(event -> {
            updateOrderFilteredData();
            bgPaneOrders.setVisible(false);
            orderFilterPane.setVisible(false);
        });
        clearOrderFilterButton.setOnAction(event -> {
            customerNameFilter.clear();
            contactFilter.clear();
            createdFilter.clear();
            typeFilter.setValue(null);
            commentsFilter.clear();
            orderDateFilter.setValue(null);
            updateOrderFilteredData();
            bgPaneOrders.setVisible(false);
            orderFilterPane.setVisible(false);
        });
        closeOrderFilterButton.setOnAction(event -> {
            bgPaneOrders.setVisible(false);
            orderFilterPane.setVisible(false);
        });

        // Initialize columns
        orderNumberOrd.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        createdOrd.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        orderDateOrd.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        typeOrd.setCellValueFactory(new PropertyValueFactory<>("type"));
        commentsOrd.setCellValueFactory(new PropertyValueFactory<>("comments"));
        valueOrd.setCellValueFactory(new PropertyValueFactory<>("value"));
        customerNameOrd.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactOrd.setCellValueFactory(new PropertyValueFactory<>("contact"));

        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // check for double-click event
                Order selectedOrderRow = ordersTable.getSelectionModel().getSelectedItem();
                if (selectedOrderRow != null) {
                    initEditOrder(selectedOrderRow);
                    goToEditOrderTab();
                }
            }
        });

        editOrderButton.setOnAction(e -> {
            Order selectedRow = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                initEditOrder(selectedRow);
                goToEditOrderTab();
            }
        });

        editProductButton.setOnAction(e -> {
            Product selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                initEditProductDetails(selected);
                bgPaneProducts.setVisible(true);
                productDetailsPane.setVisible(true);
            }
        });


        productsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Product selected = productsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    initEditProductDetails(selected);
                    bgPaneProducts.setVisible(true);
                    productDetailsPane.setVisible(true);
                }
            }
        });

        closeProductDetailsButton.setOnAction(event -> {
            bgPaneProducts.setVisible(false);
            productDetailsPane.setVisible(false);
        });

        productCodeProd.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        productNameProd.setCellValueFactory(new PropertyValueFactory<>("productName"));

        productLineProd.setCellValueFactory(new PropertyValueFactory<>("productLine"));

        productVendorProd.setCellValueFactory(new PropertyValueFactory<>("productVendor"));

        addProductFilterButton.setOnAction(event -> {
            bgPaneProducts.setVisible(true);
            productFilterPane.setVisible(true);
        });
        typeFilter.getItems().add("online");
        typeFilter.getItems().add("onsite");
        applyProductFilterButton.setOnAction(event -> {
            updateProductFilteredData();
            bgPaneProducts.setVisible(false);
            productFilterPane.setVisible(false);
        });
        clearProductFilterButton.setOnAction(event -> {
            productCodeFilter.clear();
            productNameFilter.clear();
            productLineFilter.clear();
            productVendorFilter.clear();
            updateProductFilteredData();
            bgPaneProducts.setVisible(false);
            productFilterPane.setVisible(false);
        });
        closeProductFilterButton.setOnAction(event -> {
            bgPaneProducts.setVisible(false);
            productFilterPane.setVisible(false);
        });

        removeItemButton.setOnAction(event -> {
            removeItemButtonClicked = true;
            removeItems();
        });

        removeProductButton.setOnAction(event -> {
            removeProductButtonClicked = true;
            Product selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Are you sure you want to delete this product?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, so delete the item
                    String query = String.format("DELETE FROM products WHERE productCode = '%s'", selected.getProductCode());
                    sqlConnection.updateQuery(query);
                    productsTable.getItems().remove(selected);
                } else {
                    // User clicked Cancel or closed the dialog box, so do nothing
                    // ...
                }
            }
        });

        createOrderButton.setOnAction(e -> {
            String type = "";
            // Create a ChoiceDialog to ask the user for the order type
            ChoiceDialog<String> dialog = new ChoiceDialog<>("onsite", "onsite", "online");
            dialog.setTitle("Order Type");
            dialog.setHeaderText("Select the order type:");
            dialog.setContentText("Order Type:");

            // Show the dialog and wait for the user's response
            dialog.showAndWait().ifPresent(orderType -> {
                // Handle the user's selection
                goToCreateOrderTab();
                initCreateOrder(orderType);
            });
        });

        removeOrderButton.setOnAction(event -> {
            removeOrderButtonClicked = true;
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Are you sure you want to delete this order?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, so delete the item
                    handleRemoveOrder();
                } else {
                    // User clicked Cancel or closed the dialog box, so do nothing
                    // ...
                }
            }
        });
    }

    private void initCreateOrder(String type) {
        if (type.equals("online")) {
            requiredDateInput.setVisible(true);
            requiredDateText.setVisible(true);
            shippedDateInput.setVisible(true);
            shippedDateText.setVisible(true);
            statusInput.setVisible(true);
            paymentMethodInput.getItems().clear();
            paymentMethodInput.getItems().addAll("Cash On Delivery", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");
            paymentMethodInput.setValue(null);
        } else {
            requiredDateInput.setVisible(false);
            requiredDateText.setVisible(false);
            shippedDateInput.setVisible(false);
            shippedDateText.setVisible(false);
            statusInput.setVisible(false);
            paymentMethodInput.getItems().clear();
            paymentMethodInput.getItems().addAll("Cash", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");
            paymentMethodInput.setValue(null);
        }
        clearCreateOrderTab();
        totalAmount.set(0);
        customerNameInput.setEditable(true);
        phoneNumberInput.setEditable(true);
        submitOrderButton.setText("Create Order");
        submitOrderButton.setOnAction(event -> {
            createOrder(type);
        });
        orderDetailsTable.setItems(getList());
    }

    private void initEditOrder(Order selectedOrderRow) {
        clearCreateOrderTab();
        submitOrderButton.setText("Save");
        customerNameInput.setEditable(false);
        phoneNumberInput.setEditable(false);
        totalAmount.set(0);
        if (selectedOrderRow != null) {

            // Extract the order number from the selected row
            int orderNumber = selectedOrderRow.getOrderNumber();

            printInvoiceButton.setOnAction(event -> {
                printInvoice(orderNumber);
            });

            // Query the orderdetails table to get the order details information for the selected order
            String query = String.format("SELECT productCode, quantityOrdered, priceEach FROM orderdetails WHERE orderNumber = %d", orderNumber);
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                while (rs.next()) {
                    // Extract the relevant information from the result set
                    String productCode = rs.getString("productCode");
                    int quantity = rs.getInt("quantityOrdered");
                    double priceEach = rs.getDouble("priceEach");
                    OrderItem orderItem = new OrderItem(productCode, quantity, priceEach, quantity * priceEach);
                    orderDetailsTable.getItems().add(orderItem);
                    totalAmount.set(totalAmount.get() + orderItem.getAmount());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            if (selectedOrderRow.getType().equals("online")) {
                requiredDateInput.setVisible(true);
                requiredDateText.setVisible(true);
                shippedDateInput.setVisible(true);
                shippedDateText.setVisible(true);
                statusInput.setVisible(true);
                paymentMethodInput.getItems().clear();
                paymentMethodInput.getItems().addAll("Cash On Delivery", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");

                requiredDateInput.setValue(selectedOrderRow.getRequiredDate());
                shippedDateInput.setValue(selectedOrderRow.getShippedDate());

                // Set the status combo box to the value from the selected ord
                statusInput.setValue(selectedOrderRow.getStatus());
                paymentMethodInput.setValue(selectedOrderRow.getPayment_method());
            } else {
                requiredDateInput.setVisible(false);
                requiredDateText.setVisible(false);
                shippedDateInput.setVisible(false);
                shippedDateText.setVisible(false);
                statusInput.setVisible(false);
                paymentMethodInput.getItems().clear();
                paymentMethodInput.getItems().addAll("Cash", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");

                paymentMethodInput.setValue(selectedOrderRow.getPayment_method());
            }


//            int customerNumber = -1;
//            query = String.format("SELECT customerNumber FROM customers WHERE customerName = '%s' AND phone = '%s'", selectedOrderRow.getCustomerName(), selectedOrderRow.getContact());
//            rs = sqlConnection.getDataQuery(query);
//            try {
//                if (rs.next()) {
//                    customerNumber = rs.getInt("customerNumber");
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }

            // Set the date pickers and comments text field with values from the selected order
            LocalDate orderDate = selectedOrderRow.getOrderDate();
            String comments = selectedOrderRow.getComments();
            orderDateInput.setValue(orderDate);
            commentsInput.setText(comments);

            customerNameInput.setText(selectedOrderRow.getCustomerName());
            phoneNumberInput.setText(selectedOrderRow.getContact());

            submitOrderButton.setOnAction(event -> {
                editOrder(orderNumber);
            });
        }
    }

    private void editOrder(int orderNumber) {
        runTask(() -> {
            String query = String.format("DELETE FROM orderdetails WHERE orderNumber = %d", orderNumber);
            sqlConnection.updateQuery(query);
            for (OrderItem orderItem : orderDetailsTable.getItems()) {
                String productCode = orderItem.getProductCode();
                int quantity = orderItem.getQuantityOrdered();
                double priceEach = orderItem.getPriceEach();
                query = String.format("INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach) VALUES (%d, '%s', %d, %f)", orderNumber, productCode, quantity, priceEach);
                sqlConnection.updateQuery(query);
            }

            // Get the values from the input fields
            LocalDate orderDate = orderDateInput.getValue();
            String requiredDate = requiredDateInput.getValue() != null ? "'" + requiredDateInput.getValue().toString() + "'" : null;
            String shippedDate = shippedDateInput.getValue() != null ? "'" + shippedDateInput.getValue().toString() + "'" : "null";
            String status = (String) statusInput.getValue();
            String comments = commentsInput.getText();

            query = String.format("UPDATE orders SET orderDate = '%s', requiredDate = %s, shippedDate = %s, status = '%s', comments = '%s', payment_method = '%s' WHERE orderNumber = %d", orderDate.toString(), requiredDate, shippedDate, status, comments, paymentMethodInput.getValue(), orderNumber);
            sqlConnection.updateQuery(query);

        }, null, progressIndicator, createOrderTab.getTabPane());
        customerNameInput.setEditable(true);
        phoneNumberInput.setEditable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order edited successfully!", ButtonType.OK);
        alert.setTitle(null);
        alert.setHeaderText(null);

        alert.showAndWait();
        goToOrdersTab();
    }

    private void setPrice() {
        String productCode = productCodeInput.getText();

        // Query the database for the sellPrice of the product with the given code
        double sellPrice = -1;

        String sql = "SELECT sellPrice FROM products WHERE productCode = '" + productCode + "';";
        ResultSet result = sqlConnection.getDataQuery(sql);
        try {
            if (result.next()) {
                sellPrice = result.getDouble("sellPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the sellPrice as the text of the priceEachInput
        priceEachInput.setText(Double.toString(sellPrice));
    }

    public ObservableList<OrderItem> getList() {
        ObservableList<OrderItem> items = FXCollections.observableArrayList();
        return items;
    }

    public void addItem() {
        String productCode = productCodeInput.getText();
        int quantity = Integer.parseInt(quantityInput.getText());
        double priceEach = Double.parseDouble(priceEachInput.getText());

        // Check if an order with the same productCode already exists
        for (OrderItem orderItem : orderDetailsTable.getItems()) {
            if (orderItem.getProductCode().equals(productCode)) {
                // Update the existing order
                totalAmount.set(totalAmount.get() - orderItem.getAmount() + quantity * priceEach);
                orderItem.setQuantityOrdered(quantity);
                orderItem.setPriceEach(priceEach);
                orderDetailsTable.refresh();
                return;
            }
        }

        // If no existing order was found, create a new one and add it to the tableView
        OrderItem orderItem = new OrderItem(productCode, quantity, priceEach, quantity * priceEach);
        orderDetailsTable.getItems().add(orderItem);

        totalAmount.set(totalAmount.get() + quantity * priceEach);

        productCodeInput.clear();
        quantityInput.clear();
        priceEachInput.clear();
    }

    public void removeItems() {
        ObservableList<OrderItem> selectedRows, allItems;
        allItems = orderDetailsTable.getItems();

        selectedRows = orderDetailsTable.getSelectionModel().getSelectedItems();

        for (OrderItem orderItem : selectedRows) {
            totalAmount.set(totalAmount.get() - orderItem.getAmount());
        }

        allItems.removeAll(selectedRows);
    }

    public void clearItems() {
        orderDetailsTable.getItems().clear();
        totalAmount.set(0);
    }

    @FXML
    TableView<OrderItem> orderDetailsTable;
    @FXML
    TableColumn<OrderItem, String> productCodeOD;
    @FXML
    TableColumn<OrderItem, Integer> quantityOD;
    @FXML
    TableColumn<OrderItem, Double> priceEachOD;
    @FXML
    TableColumn<OrderItem, String> totalOD;
    @FXML
    JFXTextField productCodeInput;
    @FXML
    ListView<String> suggestionList;
    @FXML
    JFXTextField quantityInput;
    @FXML
    JFXTextField priceEachInput;
    @FXML
    JFXTextField totalInput;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton removeItemButton;
    @FXML
    TextField totalAmountForOrder;
    private DoubleProperty totalAmount = new SimpleDoubleProperty(0);

    private boolean removeItemButtonClicked = false;
    @FXML
    DatePicker orderDateInput;
    @FXML
    DatePicker requiredDateInput;
    @FXML
    Text requiredDateText;
    @FXML
    DatePicker shippedDateInput;
    @FXML
    Text shippedDateText;
    @FXML
    JFXTextField commentsInput;
    @FXML
    ComboBox<String> paymentMethodInput;
    @FXML
    JFXButton submitOrderButton;
    @FXML
    JFXButton printInvoiceButton;
    @FXML
    JFXTextField customerNameInput;
    @FXML
    JFXTextField phoneNumberInput;

    public void createOrder(String type) {
        runTask(() -> {
            String orderDate;
            if (orderDateInput.getValue() == null) {
                orderDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            } else {
                orderDate = orderDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
            }
            String shippedDate;
            String requiredDate;
            if (type.equals("online")) {
                if (shippedDateInput.getValue() != null) {
                    shippedDate = shippedDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
                    shippedDate = "'" + shippedDate;
                    shippedDate += "'";
                } else {
                    shippedDate = "null";
                }
                requiredDate = "'" + requiredDateInput.getValue().format(DateTimeFormatter.ISO_DATE) + "'";
            } else {
                shippedDate = "null";
                requiredDate = "null";
            }
            String check = "SELECT customerNumber FROM customers WHERE customerName = '" + customerNameInput.getText() + "' AND phone = '" + phoneNumberInput.getText() + "';";
            ResultSet result = sqlConnection.getDataQuery(check);
            int customerNumber = -1;
            try {
                if (result.next()) {
                    customerNumber = result.getInt("customerNumber");
                } else {
                    check = "INSERT INTO customers (customerName, phone) VALUES ('" + customerNameInput.getText() + "', '" + phoneNumberInput.getText() + "')";
                    sqlConnection.updateQuery(check);
                    check = "SELECT LAST_INSERT_ID() FROM customers;";
                    result = sqlConnection.getDataQuery(check);
                    if (result.next()) {
                        customerNumber = result.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            double value = 0;
            for (OrderItem item : orderDetailsTable.getItems()) {
                value += item.getAmount();
            }

            String order = "insert into orders(orderDate, requiredDate, shippedDate, status, comments, customerNumber, type, value, payment_method, created_by) values ('"
                    + orderDate + "',"
                    + requiredDate + ","
                    + shippedDate + ",'"
                    + statusInput.getValue() + "','"
                    + commentsInput.getText() + "',"
                    + customerNumber + ", '"
                    + type + "', "
                    + value + ", '"
                    + paymentMethodInput.getValue() + "'," +
                    loggerID + ");";
            sqlConnection.updateQuery(order);
            result = sqlConnection.getDataQuery("SELECT LAST_INSERT_ID() FROM orders;");

            int orderNumber = 0;
            try {
                if (result.next()) {
                    orderNumber = result.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            StringBuilder orderdetails = new StringBuilder("insert into orderdetails values");
            ObservableList<OrderItem> items = orderDetailsTable.getItems();

            for (OrderItem item : items) {
                orderdetails.append("(").append(orderNumber)
                        .append(", '")
                        .append(item.getProductCode())
                        .append("',")
                        .append(item.getQuantityOrdered())
                        .append(",")
                        .append(item.getPriceEach())
                        .append("),");
            }
            orderdetails.deleteCharAt(orderdetails.length() - 1);
            orderdetails.append(';');
            sqlConnection.updateQuery(orderdetails.toString());

            int finalOrderNumber = orderNumber;
            printInvoiceButton.setOnAction(event -> {
                printInvoice(finalOrderNumber);
            });
        }, null, progressIndicator, createOrderTab.getTabPane());

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order created successfully!", ButtonType.OK);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setGraphic(null);

        alert.showAndWait();
        goToOrdersTab();
    }


    @FXML
    void tabSelectingEffect(Event event) {
        HBox hbox = (HBox) currentTabButton.getGraphic();
        Label label = (Label) hbox.getChildren().get(1);
        label.setTextFill(Color.valueOf("#7c8db5"));
        ImageView buttonIcon = (ImageView) hbox.getChildren().get(0);
        if (currentTabButton.equals(dashBoardTabButton)) buttonIcon.setImage(ImageController.newsIcon);
        else if (currentTabButton.equals(ordersTabButton)) buttonIcon.setImage(ImageController.orderIcon);
        else if (currentTabButton.equals(productsTabButton)) buttonIcon.setImage(ImageController.productIcon);
        else if (currentTabButton.equals(employeesTabButton)) buttonIcon.setImage(ImageController.employeeIcon);
        else if (currentTabButton.equals(settingsTabButton)) buttonIcon.setImage(ImageController.settingsIcon);
        currentTabButton.setStyle("-fx-border-color: transparent;-fx-background-color :#ffffff; -fx-border-width: 0 0 0 4; -fx-border-radius: 0;");

        currentTabButton = (JFXButton) event.getSource();
        hbox = (HBox) currentTabButton.getGraphic();
        label = (Label) hbox.getChildren().get(1);
        label.setTextFill(Color.valueOf("#329cfe"));
        buttonIcon = (ImageView) hbox.getChildren().get(0);
        if (currentTabButton.equals(dashBoardTabButton)) buttonIcon.setImage(ImageController.blueNewsIcon);
        else if (currentTabButton.equals(ordersTabButton)) buttonIcon.setImage(ImageController.blueOrderIcon);
        else if (currentTabButton.equals(productsTabButton)) buttonIcon.setImage(ImageController.blueProductIcon);
        else if (currentTabButton.equals(employeesTabButton)) buttonIcon.setImage(ImageController.blueEmployeeIcon);
        else if (currentTabButton.equals(settingsTabButton)) buttonIcon.setImage(ImageController.blueSettingsIcon);

        currentTabButton.setStyle("-fx-border-color: #60b1fd; -fx-background-color : #fafafa;-fx-border-width: 0 0 0 4; -fx-border-radius: 0;");
    }

    /**
     * Handle ORDERS tab.
     */
    @FXML
    TableView<Order> ordersTable;
    @FXML
    TableColumn<Order, Integer> orderNumberOrd;
    @FXML
    TableColumn<Order, String> createdOrd;
    @FXML
    TableColumn<Order, LocalDate> orderDateOrd;
    @FXML
    TableColumn<Order, String> typeOrd;
    @FXML
    TableColumn<Order, String> commentsOrd;
    @FXML
    TableColumn<Order, String> valueOrd;
    @FXML
    TableColumn<Order, String> customerNameOrd;
    @FXML
    TableColumn<Order, String> contactOrd;
    @FXML
    JFXButton createOrderButton;
    @FXML
    JFXButton editOrderButton;
    @FXML
    JFXButton removeOrderButton;
    @FXML
    JFXButton addOrderFilterButton;
    @FXML
    Pane bgPaneOrders;
    @FXML
    AnchorPane orderFilterPane;
    @FXML
    JFXTextField customerNameFilter;
    @FXML
    JFXTextField contactFilter;
    @FXML
    ComboBox<String> typeFilter;
    @FXML
    JFXTextField createdFilter;
    @FXML
    DatePicker orderDateFilter;
    @FXML
    JFXTextField commentsFilter;
    @FXML
    JFXButton applyOrderFilterButton;
    @FXML
    JFXButton clearOrderFilterButton;
    @FXML
    JFXButton closeOrderFilterButton;
    FilteredList<Order> filteredOrders;
    SortedList<Order> sortedAndFilteredOrders;
    private boolean removeOrderButtonClicked = false;

    void initOrders() {
        runTask(() -> {
            ObservableList<Order> orders = FXCollections.observableArrayList();
            ordersTable.setItems(orders);

            try {
                String query = "SELECT orderNumber, CONCAT(lastName, ' ', firstName)  AS name, created_by, orderDate, requiredDate, shippedDate, orders.status, type, comments, value, payment_method, customerName, customers.phone FROM orders INNER JOIN customers ON orders.customerNumber = customers.customerNumber INNER JOIN employees ON orders.created_by = employees.employeeNumber";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("orderNumber"),
                            resultSet.getInt("created_by"),
                            resultSet.getString("name"),
                            resultSet.getDate("orderDate").toLocalDate(),
                            resultSet.getDate("requiredDate") != null ? resultSet.getDate("requiredDate").toLocalDate() : null,
                            resultSet.getDate("shippedDate") != null ? resultSet.getDate("shippedDate").toLocalDate() : null,
                            resultSet.getString("status"),
                            resultSet.getString("comments"),
                            resultSet.getString("customerName"),
                            resultSet.getString("phone"),
                            resultSet.getString("type"),
                            resultSet.getDouble("value"),
                            resultSet.getString("payment_method")
                    );
                    ordersTable.getItems().add(order);
                }
                ordersTable.refresh();

                filteredOrders = new FilteredList<>(FXCollections.observableArrayList(ordersTable.getItems()), p -> true);
                sortedAndFilteredOrders = new SortedList<>(filteredOrders);
                ordersTable.setItems(sortedAndFilteredOrders);
                sortedAndFilteredOrders.comparatorProperty().bind(ordersTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, progressIndicator, ordersTab.getTabPane());

    }

    public void handleRemoveOrder() {
        // Get the selected row in the TableView
        Order selectedRow = ordersTable.getSelectionModel().getSelectedItem();

        // If a row is selected, delete the corresponding order from the database and TableView
        if (selectedRow != null) {
            int orderNumber = selectedRow.getOrderNumber();

            String deleteQuery = "DELETE FROM orderdetails WHERE orderNumber = " + orderNumber;
            // Delete the order from the database
            sqlConnection.updateQuery(deleteQuery);

            deleteQuery = "DELETE FROM orders WHERE orderNumber = " + orderNumber;
            // Delete the order from the database
            sqlConnection.updateQuery(deleteQuery);

            // Delete the order from the TableView
            ordersTable.getItems().remove(selectedRow);

        }
    }

    @FXML
    TableView<Product> productsTable;
    @FXML
    TableColumn<Product, String> productCodeProd;
    @FXML
    TableColumn<Product, String> productNameProd;
    @FXML
    TableColumn<Product, String> productLineProd;
    @FXML
    TableColumn<Product, String> productVendorProd;
    @FXML
    JFXButton productDetailsButton;
    @FXML
    JFXButton editProductButton;
    @FXML
    JFXButton addProductButton;
    @FXML
    JFXButton removeProductButton;
    @FXML
    JFXTextField productCodeFilter;
    @FXML
    JFXTextField productNameFilter;
    @FXML
    JFXTextField productLineFilter;
    @FXML
    JFXTextField productVendorFilter;
    @FXML
    JFXButton applyProductFilterButton;
    @FXML
    JFXButton clearProductFilterButton;
    @FXML
    JFXButton closeProductFilterButton;
    @FXML
    JFXButton addProductFilterButton;
    FilteredList<Product> filteredProducts;
    SortedList<Product> sortedAndFilteredProducts;

    private boolean removeProductButtonClicked = false;
    @FXML
    Pane bgPaneProducts;
    @FXML
    AnchorPane productDetailsPane;
    @FXML
    AnchorPane productFilterPane;
    @FXML
    JFXButton closeProductDetailsButton;

    void initProducts() {
        runTask(() -> {
            productsInit = true;
            ObservableList<Product> products = FXCollections.observableArrayList();
            productsTable.setItems(products);

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
                    productsTable.getItems().add(product);
                }
                productsTable.refresh();

                filteredProducts = new FilteredList<>(FXCollections.observableArrayList(productsTable.getItems()), p -> true);
                sortedAndFilteredProducts = new SortedList<>(filteredProducts);
                productsTable.setItems(sortedAndFilteredProducts);
                sortedAndFilteredProducts.comparatorProperty().bind(productsTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, progressIndicator, productsOperationTab.getTabPane());

    }

    @FXML
    JFXTextField productCodePDetails;
    @FXML
    JFXTextField productNamePDetails;
    @FXML
    JFXTextField productLinePDetails;
    @FXML
    ListView<String> plSuggestionList;
    @FXML
    JFXTextField productVendorPDetails;
    @FXML
    JFXTextField inStockPDetails;
    @FXML
    JFXTextField buyPricePDetails;
    @FXML
    JFXTextField sellPricePDetails;
    @FXML
    JFXTextField productDescriptionPDetails;

    public void initAddProduct() {
        productDetailsButton.setText("Add");
        productCodePDetails.setText("");
        productNamePDetails.setText("");
        productVendorPDetails.setText("");
        productLinePDetails.setText("");
        inStockPDetails.setText("");
        buyPricePDetails.setText("");
        sellPricePDetails.setText("");
        productDescriptionPDetails.setText("");

        bgPaneProducts.setVisible(true);
        productDetailsPane.setVisible(true);

        productDetailsButton.setOnAction(event -> {
            String query = String.format("SELECT productLine FROM productlines WHERE productLine = '%s'", productLinePDetails.getText());
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                if (!rs.next()) {
                    query = String.format("insert into productlines(productLine) values ('%s')", productLinePDetails.getText());
                    sqlConnection.updateQuery(query);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            query = String.format("insert into products(productCode, productName, productLine, productVendor, productDescription, quantityInStock, buyPrice, sellPrice) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', %d, %f, %f);", productCodePDetails.getText(), productNamePDetails.getText(),

                    productLinePDetails.getText(), productVendorPDetails.getText(), productDescriptionPDetails.getText(),
                    Integer.parseInt(inStockPDetails.getText()), Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()));
            sqlConnection.updateQuery(query);

            Product product = new Product(productCodePDetails.getText(), productNamePDetails.getText(),
                    productLinePDetails.getText(), productVendorPDetails.getText(), productDescriptionPDetails.getText(),
                    Integer.parseInt(inStockPDetails.getText()), Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()));
            productsTable.getItems().add(0, product);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added successfully!", ButtonType.OK);
            alert.setTitle(null);
            alert.setHeaderText(null);

            alert.showAndWait();

            bgPaneProducts.setVisible(false);
            productDetailsPane.setVisible(false);

            productsTable.refresh();

        });
    }

    void initEditProductDetails(Product selected) {
        productDetailsButton.setText("Save");
        productCodePDetails.setText(selected.getProductCode());
        productNamePDetails.setText(selected.getProductName());
        productLinePDetails.setText(selected.getProductLine());
        productVendorPDetails.setText(selected.getProductVendor());
        productDescriptionPDetails.setText(selected.getProductDescription());
        inStockPDetails.setText(String.valueOf(selected.getQuantityInStock()));
        buyPricePDetails.setText(String.valueOf(selected.getBuyPrice()));
        sellPricePDetails.setText(String.valueOf(selected.getSellPrice()));


        productDetailsButton.setOnAction(e -> {
            String query = String.format("SELECT productLine FROM productlines WHERE productLine = '%s'", productLinePDetails.getText());
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                if (!rs.next()) {
                    query = String.format("insert into productlines(productLine) values ('%s')", productLinePDetails.getText());
                    sqlConnection.updateQuery(query);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            query = String.format("UPDATE products SET productCode = '%s', productName = '%s', productLine = '%s', productVendor = '%s', productDescription = '%s', quantityInStock = %d, buyPrice = %f, sellPrice = %f WHERE productCode = '%s'",
                    productCodePDetails.getText(), productNamePDetails.getText(), productLinePDetails.getText(), productVendorPDetails.getText(),
                    productDescriptionPDetails.getText(), Integer.parseInt(inStockPDetails.getText()),
                    Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()), selected.getProductCode());
            sqlConnection.updateQuery(query);

            selected.setProductCode(productCodePDetails.getText());
            selected.setProductName(productNamePDetails.getText());
            selected.setProductLine(productLinePDetails.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product details saved successfully!", ButtonType.OK);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.showAndWait();

            productsTable.refresh();
        });
    }

    public void printInvoice(int orderNumber) {
        String sourceFile = "src/main/resources/salesmanagement/salesmanagement/invoice.jrxml";
        try {
            JasperReport jr = JasperCompileManager.compileReport(sourceFile);
            HashMap<String, Object> para = new HashMap<>();
            para.put("invoiceNo", "INV" + String.format("%04d", orderNumber));
            para.put("customerName", customerNameInput.getText());
            para.put("contact", phoneNumberInput.getText());
            para.put("totalAmount", "totalAmount");
            para.put("otherAmount", "discount");
            para.put("paybleAmount", "toBePaid");
            para.put("paidAmount", "paid");
            para.put("dueAmount", "due");
            para.put("comments", commentsInput.getText());
            para.put("point1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
            para.put("point2", "If you have any questions concerning this invoice, use the following contact information:");
            para.put("point3", "+243 999999999, purchase@example.com");
            LocalDate orderDate = orderDateInput.getValue() != null ? orderDateInput.getValue() : LocalDate.now();
            para.put("orderYear", orderDate.getYear() - 1900);
            para.put("orderMonth", orderDate.getMonthValue() - 1);
            para.put("orderDay", orderDate.getDayOfMonth());
            para.put("paymentMethod", paymentMethodInput.getValue());
            String query = String.format("SELECT value, type FROM orders WHERE orderNumber = %d", orderNumber);
            ResultSet rs = sqlConnection.getDataQuery(query);
            if (rs.next()) {
                para.put("totalAmount", rs.getDouble(1));
                para.put("type", rs.getString(2).substring(0, 1).toUpperCase() + rs.getString(2).substring(1));
            }

            ArrayList<OrderItem> plist = new ArrayList<>();

            for (OrderItem item : orderDetailsTable.getItems()) {
                plist.add(new OrderItem(item.getProductCode(), item.getQuantityOrdered(), item.getPriceEach(), item.getQuantityOrdered() * item.getPriceEach()));
            }

            JRBeanCollectionDataSource jcs = new JRBeanCollectionDataSource(plist);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, jcs);
            JasperViewer.viewReport(jp, false);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void updateOrderFilteredData() {
        filteredOrders.setPredicate(order -> {
            // Get the filter text from the text fields
            String customerNameFilterText = customerNameFilter.getText().toLowerCase();
            String contactFilterText = contactFilter.getText().toLowerCase();
            String createdFilterText = createdFilter.getText().toLowerCase();
            LocalDate orderDateFilterValue = orderDateFilter.getValue();
            String typeFilterText = typeFilter.getValue() != null ? typeFilter.getValue().toLowerCase() : "";
            String commentsFilterText = commentsFilter.getText().toLowerCase();
            // Check if the order matches the filter criteria
            boolean customerNameMatch = customerNameFilterText.isEmpty() || order.getCustomerName().toLowerCase().contains(customerNameFilterText);
            boolean contactMatch = contactFilterText.isEmpty() || order.getContact().toLowerCase().contains(contactFilterText);
            boolean createdMatch = createdFilterText.isEmpty() || order.getEmployeeName().toLowerCase().contains(createdFilterText);
            boolean orderDateMatch = orderDateFilterValue == null || order.getOrderDate().equals(orderDateFilterValue);
            boolean typeMatch = typeFilterText.isEmpty() || order.getType().toLowerCase().contains(typeFilterText);
            boolean commentsMatch = commentsFilterText.isEmpty() || order.getComments().toLowerCase().contains(commentsFilterText);
            return customerNameMatch && contactMatch && createdMatch && orderDateMatch && typeMatch && commentsMatch;
        });
    }

    private void updateProductFilteredData() {
        filteredProducts.setPredicate(product -> {
            // Check if the order matches the filter criteria
            boolean productCodeMatch = product.getProductCode().toLowerCase().contains(productCodeFilter.getText().toLowerCase());
            boolean productLineMatch = product.getProductLine().toLowerCase().contains(productLineFilter.getText().toLowerCase());
            boolean productNameMatch = product.getProductName().toLowerCase().contains(productNameFilter.getText().toLowerCase());
            boolean productVendorMatch = product.getProductVendor().toLowerCase().contains(productVendorFilter.getText().toLowerCase());
            return productCodeMatch && productLineMatch && productNameMatch && productVendorMatch;
        });
    }

    private void clearCreateOrderTab() {
        orderDetailsTable.getItems().clear();
        productCodeInput.clear();
        quantityInput.clear();
        priceEachInput.clear();
        totalInput.clear();
        customerNameInput.clear();
        phoneNumberInput.clear();
        orderDateInput.setValue(null);
        requiredDateInput.setValue(null);
        shippedDateInput.setValue(null);
        statusInput.setValue(null);
        commentsInput.clear();
    }
}