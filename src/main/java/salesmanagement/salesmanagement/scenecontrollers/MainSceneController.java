package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import salesmanagement.salesmanagement.SQLConnection;

import salesmanagement.salesmanagement.EmployeeForm;
import salesmanagement.salesmanagement.Form;
import salesmanagement.salesmanagement.ImageController;

import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesComponent.Order;


import java.net.URL;
import java.io.InputStream;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController extends SceneController implements Initializable {
    @FXML
    Text usernameText;
    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesOperationTab;
    @FXML
    private Tab createOrderTab;
    @FXML
    private Tab homeTab;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;

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

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
        statusIcon.setImage(ImageController.getImage("create_order_icon.png"));
    }

    @FXML
    void goToEmployeesTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
        haveChangeInEmployeesTab = true;
    }


    @FXML
    void goToProductsOperationTab() {
        tabPane.getSelectionModel().select(productsOperationTab);
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
    }

    @FXML
    Text author;
    @FXML
    Text notificationTitle;
    @FXML
    Text publishedDate;
    @FXML
    Text content;
    @FXML
    HBox contentBox;

    private void uploadNotificationText() {
        runTask(() -> {
            String query = "SELECT * FROM notifications  " +
                    "WHERE notificationID = (SELECT MAX(notificationID) " +
                    "FROM notifications)";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                if (resultSet.next()) {
                    //content.wrappingWidthProperty().bind(contentBox.widthProperty().multiply(0.9));
                    content.setWrappingWidth(contentBox.getWidth() * 0.9);
                    content.setText(resultSet.getString("content"));
                    notificationTitle.setText(resultSet.getString("title"));
                    int authorID = Integer.parseInt(resultSet.getString("employeeNumber"));
                    Employee employee = new Employee(authorID);
                    author.setText("\t\tPosted by " + employee.getFullName() + ".");
                    publishedDate.setText("\t\tPublished " + resultSet.getString("publishedDate") + ".");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, null, progressIndicator, homeTab.getTabPane());

    }

    @FXML
    ImageView statusIcon;

    /**
     * Handle employee management tab.
     */
    @FXML
    TableView<Employee> employeeTable;
    @FXML
    private TableColumn<?, ?> email;
    @FXML
    private TableColumn<Employee, Integer> employeeNumber;
    @FXML
    private TableColumn<?, ?> firstName;
    @FXML
    private TableColumn<?, ?> jobTitle;
    @FXML
    private TableColumn<?, ?> lastName;
    @FXML
    private TableColumn<?, ?> officeCode;
    @FXML
    private TableColumn<?, ?> operation;
    @FXML
    private TableColumn<?, ?> reportsTo;
    @FXML
    AnchorPane employeeOperationPane;
    ArrayList<Employee> employees;

    @FXML
    void selectEmployeesTab() {
        employeeTable.setSelectionModel(null);
        ArrayList<Employee> employees = new ArrayList<>();
        runTask(() -> {
            String query = "SELECT * FROM employees";
            try {
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    employees.add(new Employee(resultSet, MainSceneController.this));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, () -> {
            ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
            employeeTable.setItems(employeeList);
            this.employees = employees;
        }, progressIndicator, employeeOperationPane);
    }

    @FXML
    SplitPane firstSplitPane;
    @FXML
    SplitPane secondSplitPane;
    @FXML
    SplitPane thirdSplitPane;
    @FXML
    VBox employeeTabBox;
    @FXML
    HBox statusIconBox;

    @FXML
    ImageView smallAvatar;

    @FXML
    private void goToNewsTab() {
        tabPane.getSelectionModel().select(homeTab);
        newsTabButton.fire();
    }

    enum tab {
        newsTab,
        employeesTab,
        settingsTab,
        ordersTab,
        productsTab
    }


    public void initialSetup() {
        // Load current UI.
        user = new Employee(sqlConnection, loggerID);
        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((AnchorPane) firstSplitPane.getItems().get(0)).setMinHeight(0.05 * Screen.getPrimary().getVisualBounds().getHeight());
        ((AnchorPane) firstSplitPane.getItems().get(1)).setMinHeight(0.95 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        thirdSplitPane.setMaxWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        ((VBox) thirdSplitPane.getItems().get(0)).setMinWidth(0.75 * thirdSplitPane.getMaxWidth());
        ((VBox) thirdSplitPane.getItems().get(1)).setMinWidth(0.25 * thirdSplitPane.getMaxWidth());

        statusIconBox.setPrefWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());

        double tableWidth = employeeTabBox.getWidth() * 0.95;
        employeeTable.setMaxWidth(tableWidth);
        employeeNumber.setMinWidth(0.1 * tableWidth);
        firstName.setMinWidth(0.125 * tableWidth);
        lastName.setMinWidth(0.125 * tableWidth);
        email.setMinWidth(0.15 * tableWidth);
        officeCode.setMinWidth(0.125 * tableWidth);
        reportsTo.setMinWidth(0.125 * tableWidth);
        jobTitle.setMinWidth(0.125 * tableWidth);
        operation.setMinWidth(0.125 * tableWidth);

        Circle clip = new Circle();
        clip.setRadius(35);
        clip.setCenterX(35);
        clip.setCenterY(35);
        smallAvatar.setClip(clip);

        employeeForm = new EmployeeForm(employeeInfoBoxContainer);
        employeeForm.closeForm(() -> employeeInfoBoxContainer.setMouseTransparent(true));

        currentTabButton = newsTabButton;
        goToNewsTab();

        // Load UI for others.
        runTask(() -> {
            //Load small avatar.
            try {
                PreparedStatement ps = sqlConnection.getConnection().prepareStatement("SELECT avatar FROM employees WHERE employeeNumber = ?");
                ps.setInt(1, loggerID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    InputStream is = rs.getBinaryStream("avatar");
                    Image image = new Image(is);
                    smallAvatar.setImage(image);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            employeeNumber.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
            lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            officeCode.setCellValueFactory(new PropertyValueFactory<>("officeCode"));
            reportsTo.setCellValueFactory(new PropertyValueFactory<>("reportsTo"));
            jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
            operation.setCellValueFactory(new PropertyValueFactory<>("operation"));
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
                reloadFlagListener.start();
                stop();
            }
        }
    };
    private final AnimationTimer reloadFlagListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (haveJustOpened) {
                haveJustOpened = false;
                stage.setScene(MainSceneController.this.scene);
                stage.hide();
                initialSetup();
                stage.setX(0);
                stage.setY(0);
                stage.show();
                uploadNotificationText();
            }

            if (haveChangeInEmployeesTab) {
                haveChangeInEmployeesTab = false;
                selectEmployeesTab();
            }

            if (haveChangeInHomeTab) {

            }
        }
    };
    @FXML
    StackPane employeeInfoBoxContainer;
    Form employeeForm;


    @FXML
    JFXComboBox statusInput;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusInput.getItems().add("Cancelled");
        statusInput.getItems().add("Disputed");
        statusInput.getItems().add("In Process");
        statusInput.getItems().add("On Hold");
        statusInput.getItems().add("Resolved");
        statusInput.getItems().add("Shipped");

//        tableView.setItems(getItems());
        productCode.setCellValueFactory(new PropertyValueFactory<Order, String>("productCode"));
        quantity.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantityOrdered"));
        priceEach.setCellValueFactory(new PropertyValueFactory<Order, Double>("priceEach"));
        total.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Order, Double> param) {
                Order order = param.getValue();
                int quantity = order.getQuantityOrdered();
                double price = order.getPriceEach();
                double total = quantity * price;
                return new SimpleDoubleProperty(total).asObject();
            }
        });

        tableView.setItems(getList());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableView.setEditable(true);
        productCode.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity.setCellFactory(TextFieldTableCell.forTableColumn((new IntegerStringConverter())));
        priceEach.setCellFactory(TextFieldTableCell.forTableColumn((new DoubleStringConverter())));
    }

    public ObservableList<Order> getList() {
        ObservableList<Order> items = FXCollections.observableArrayList();
        return items;
    }

    public void addItem() {
        String productCode = productCodeInput.getText();
        int quantity = Integer.parseInt(quantityInput.getText());
        double priceEach = Double.parseDouble(priceEachInput.getText());

        // Check if an order with the same productCode already exists
        for (Order order : tableView.getItems()) {
            if (order.getProductCode().equals(productCode)) {
                // Update the existing order
                order.setQuantityOrdered(quantity);
                order.setPriceEach(priceEach);
                tableView.refresh();
                return;
            }
        }

        // If no existing order was found, create a new one and add it to the tableView
        Order order = new Order(productCode, quantity, priceEach);
        tableView.getItems().add(order);
    }

    public void removeItems() {
        ObservableList<Order> selectedRows, allItems;
        allItems = tableView.getItems();

        selectedRows = tableView.getSelectionModel().getSelectedItems();

        allItems.removeAll(selectedRows);
    }

    public void changeProductCode(TableColumn.CellEditEvent edittedCell) {
        Order selected = tableView.getSelectionModel().getSelectedItem();
        selected.setProductCode(edittedCell.getNewValue().toString());
        tableView.refresh();
    }
    public void changeQuantity(TableColumn.CellEditEvent edittedCell) {
        Order selected = tableView.getSelectionModel().getSelectedItem();
        selected.setQuantityOrdered((int) edittedCell.getNewValue());
        tableView.refresh();
    }
    public void changePriceEach(TableColumn.CellEditEvent edittedCell) {
        Order selected = tableView.getSelectionModel().getSelectedItem();
        selected.setPriceEach((double) edittedCell.getNewValue());
        tableView.refresh();
    }

    @FXML
    TableView<Order> tableView;
    @FXML
    TableColumn<Order, String> productCode;
    @FXML
    TableColumn<Order, Integer> quantity;
    @FXML
    TableColumn<Order, Double> priceEach;
    @FXML
    TableColumn<Order, Double> total;
    @FXML
    JFXTextField customerNumberInput;
    @FXML
    JFXTextField productCodeInput;
    @FXML
    JFXTextField quantityInput;
    @FXML
    JFXTextField priceEachInput;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton removeButton;
    @FXML
    DatePicker orderDateInput;
    @FXML
    DatePicker requiredDateInput;
    @FXML
    DatePicker shippedDateInput;
    @FXML
    JFXTextField commentsInput;
    @FXML
    JFXButton createOrderButton;
    public void createOrder() throws SQLException {
        String orderDate;
        if (orderDateInput.getValue() == null) {
            orderDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        } else {
            orderDate = orderDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
        }
        String shippedDate;
        if (shippedDateInput.getValue() != null) {
            shippedDate = shippedDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
            shippedDate = "'" + shippedDate;
            shippedDate += "'";
        } else {
            shippedDate = "null";
        }
        String comment;
        if (commentsInput.getText().equals("")) {
            comment = "null";
        }
        String order = "insert into orders(orderDate, requiredDate, shippedDate, status, comments, customerNumber) values ('"
                + orderDate + "','"
                + requiredDateInput.getValue().format(DateTimeFormatter.ISO_DATE) + "',"
                + shippedDate + ",'"
                + statusInput.getValue() + "','"
                + commentsInput.getText() + "',"
                + Integer.parseInt(customerNumberInput.getText()) + ");";
        sqlConnection.updateQuery(order);
        ResultSet result = sqlConnection.getDataQuery("SELECT LAST_INSERT_ID() FROM orders;");

        int orderNumber = 0;
        if (result.next()) {
            orderNumber = result.getInt(1);
        }

        StringBuilder orderdetails = new StringBuilder("insert into orderdetails values");
        ObservableList<Order> items = tableView.getItems();

        for (Order item : items) {
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
    }
    public void editEmployees(Employee employee) {
        employeeInfoBoxContainer.setMouseTransparent(false);
        employeeForm.fillInForm(employee);
        employeeForm.show();
    }

    @FXML
    void tabSelectingEffect(Event event) {
        HBox hbox = (HBox) currentTabButton.getGraphic();
        Label label = (Label) hbox.getChildren().get(1);
        label.setTextFill(Color.valueOf("#7c8db5"));
        ImageView buttonIcon = (ImageView) hbox.getChildren().get(0);
        if (currentTabButton.equals(newsTabButton)) buttonIcon.setImage(ImageController.newsIcon);
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
        if (currentTabButton.equals(newsTabButton)) buttonIcon.setImage(ImageController.blueNewsIcon);
        else if (currentTabButton.equals(ordersTabButton)) buttonIcon.setImage(ImageController.blueOrderIcon);
        else if (currentTabButton.equals(productsTabButton)) buttonIcon.setImage(ImageController.blueProductIcon);
        else if (currentTabButton.equals(employeesTabButton)) buttonIcon.setImage(ImageController.blueEmployeeIcon);
        else if (currentTabButton.equals(settingsTabButton)) buttonIcon.setImage(ImageController.blueSettingsIcon);

        currentTabButton.setStyle("-fx-border-color: #60b1fd; -fx-background-color : #fafafa;-fx-border-width: 0 0 0 4; -fx-border-radius: 0;");

    }
}

