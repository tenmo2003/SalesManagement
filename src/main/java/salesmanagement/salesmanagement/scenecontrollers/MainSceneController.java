package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import salesmanagement.salesmanagement.EmployeeForm;
import salesmanagement.salesmanagement.Form;
import salesmanagement.salesmanagement.ImageController;

import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesComponent.Order;


import java.io.InputStream;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainSceneController extends SceneController {
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
    private Tab ordersTab;

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);

        initCreateOrder();

        statusIcon.setImage(ImageController.getImage("create_order_icon.png"));
    }

    @FXML
    void goToOrdersTab() {
        initOrders();
        tabPane.getSelectionModel().select(ordersTab);
    }

    @FXML
    void goToEmployeesTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
        haveChangeInEmployeesTab = true;
    }


    @FXML
    void goToProductsOperationTab() {
        initProducts();
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

    public void initCreateOrder() {
        statusInput.getItems().clear();
        statusInput.getItems().add("Cancelled");
        statusInput.getItems().add("Disputed");
        statusInput.getItems().add("In Process");
        statusInput.getItems().add("On Hold");
        statusInput.getItems().add("Resolved");
        statusInput.getItems().add("Shipped");

//        tableView.setItems(getItems());
        productCodeOD.setCellValueFactory(new PropertyValueFactory<Order, String>("productCode"));
        quantityOD.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantityOrdered"));
        priceEachOD.setCellValueFactory(new PropertyValueFactory<Order, Double>("priceEach"));
        totalOD.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Order, Double> param) {
                Order order = param.getValue();
                int quantity = order.getQuantityOrdered();
                double price = order.getPriceEach();
                double total = quantity * price;
                return new SimpleDoubleProperty(total).asObject();
            }
        });

        orderDetailsTable.setItems(getList());

        orderDetailsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        orderDetailsTable.setEditable(true);
        productCodeOD.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityOD.setCellFactory(TextFieldTableCell.forTableColumn((new IntegerStringConverter())));
        priceEachOD.setCellFactory(TextFieldTableCell.forTableColumn((new DoubleStringConverter())));
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
        for (Order order : orderDetailsTable.getItems()) {
            if (order.getProductCode().equals(productCode)) {
                // Update the existing order
                order.setQuantityOrdered(quantity);
                order.setPriceEach(priceEach);
                orderDetailsTable.refresh();
                return;
            }
        }

        // If no existing order was found, create a new one and add it to the tableView
        Order order = new Order(productCode, quantity, priceEach);
        orderDetailsTable.getItems().add(order);

        productCodeInput.clear();
        quantityInput.clear();
        priceEachInput.clear();
    }

    public void removeItems() {
        ObservableList<Order> selectedRows, allItems;
        allItems = orderDetailsTable.getItems();

        selectedRows = orderDetailsTable.getSelectionModel().getSelectedItems();

        allItems.removeAll(selectedRows);
    }

    public void changeProductCode(TableColumn.CellEditEvent edittedCell) {
        Order selected = orderDetailsTable.getSelectionModel().getSelectedItem();
        selected.setProductCode(edittedCell.getNewValue().toString());
        orderDetailsTable.refresh();
    }

    public void changeQuantity(TableColumn.CellEditEvent edittedCell) {
        Order selected = orderDetailsTable.getSelectionModel().getSelectedItem();
        selected.setQuantityOrdered((int) edittedCell.getNewValue());
        orderDetailsTable.refresh();
    }

    public void changePriceEach(TableColumn.CellEditEvent edittedCell) {
        Order selected = orderDetailsTable.getSelectionModel().getSelectedItem();
        selected.setPriceEach((double) edittedCell.getNewValue());
        orderDetailsTable.refresh();
    }

    @FXML
    TableView<Order> orderDetailsTable;
    @FXML
    TableColumn<Order, String> productCodeOD;
    @FXML
    TableColumn<Order, Integer> quantityOD;
    @FXML
    TableColumn<Order, Double> priceEachOD;
    @FXML
    TableColumn<Order, Double> totalOD;
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
    JFXButton submitOrderButton;

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
        ObservableList<Order> items = orderDetailsTable.getItems();

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

    @FXML
    TableView<ObservableList<Object>> ordersTable;
    @FXML
    TableColumn<ObservableList<Object>, Integer> orderNumberOrd;
    @FXML
    TableColumn<ObservableList<Object>, LocalDate> orderDateOrd;
    @FXML
    TableColumn<ObservableList<Object>, LocalDate> requiredDateOrd;
    @FXML
    TableColumn<ObservableList<Object>, LocalDate> shippedDateOrd;
    @FXML
    TableColumn<ObservableList<Object>, String> statusOrd;
    @FXML
    TableColumn<ObservableList<Object>, String> commentsOrd;
    @FXML
    TableColumn<ObservableList<Object>, Integer> customerNumberOrd;
    @FXML
    JFXButton createOrderButton;
    @FXML
    JFXButton removeOrderButton;

    void initOrders() {
        ordersTable.getItems().clear();
        orderNumberOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<Object>, Integer> param) {
                return new SimpleObjectProperty<Integer>((Integer) param.getValue().get(0));
            }
        });

        orderDateOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, LocalDate>, ObservableValue<LocalDate>>() {
            @Override
            public ObservableValue<LocalDate> call(TableColumn.CellDataFeatures<ObservableList<Object>, LocalDate> param) {
                return new SimpleObjectProperty<LocalDate>((LocalDate) param.getValue().get(1));
            }
        });

        requiredDateOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, LocalDate>, ObservableValue<LocalDate>>() {
            @Override
            public ObservableValue<LocalDate> call(TableColumn.CellDataFeatures<ObservableList<Object>, LocalDate> param) {
                return new SimpleObjectProperty<LocalDate>((LocalDate) param.getValue().get(2));
            }
        });

        shippedDateOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, LocalDate>, ObservableValue<LocalDate>>() {
            @Override
            public ObservableValue<LocalDate> call(TableColumn.CellDataFeatures<ObservableList<Object>, LocalDate> param) {
                return new SimpleObjectProperty<LocalDate>((LocalDate) param.getValue().get(3));
            }
        });

        statusOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
                return new SimpleStringProperty((String) param.getValue().get(4));
            }
        });

        commentsOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
                return new SimpleStringProperty((String) param.getValue().get(5));
            }
        });

        customerNumberOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<Object>, Integer> param) {
                return new SimpleObjectProperty<Integer>((Integer) param.getValue().get(6));
            }
        });
        try {
            String query = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            while (resultSet.next()) {
                ObservableList<Object> row = FXCollections.observableArrayList();
                row.add(resultSet.getInt("orderNumber"));
                row.add(resultSet.getDate("orderDate").toLocalDate());
                row.add(resultSet.getDate("requiredDate").toLocalDate());
                row.add(resultSet.getDate("shippedDate") != null ? resultSet.getDate("shippedDate").toLocalDate() : null);
                row.add(resultSet.getString("status"));
                row.add(resultSet.getString("comments"));
                row.add(resultSet.getInt("customerNumber"));
                ordersTable.getItems().add(row);
            }
            ordersTable.refresh();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void handleRemoveOrder() {
        // Get the selected row in the TableView
        ObservableList<Object> selectedRow = ordersTable.getSelectionModel().getSelectedItem();

        // If a row is selected, delete the corresponding order from the database and TableView
        if (selectedRow != null) {
            int orderNumber = (Integer) selectedRow.get(0);

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
    TableView<ObservableList<Object>> productsTable;
    @FXML
    TableColumn<ObservableList<Object>, String> productCodeProd;
    @FXML
    TableColumn<ObservableList<Object>, String> productNameProd;
    @FXML
    TableColumn<ObservableList<Object>, String> productLineProd;
//    @FXML
//    TableColumn<ObservableList<Object>, String> productScaleProd;
//    @FXML
//    TableColumn<ObservableList<Object>, String> productVendorProd;
//    @FXML
//    TableColumn<ObservableList<Object>, String> productDescriptionProd;
//    @FXML
//    TableColumn<ObservableList<Object>, Integer> inStockProd;
//    @FXML
//    TableColumn<ObservableList<Object>, Float> buyPriceProd;
//    @FXML
//    TableColumn<ObservableList<Object>, Float> MSRPProd;
    @FXML
    Pane bgPane;
    @FXML
    AnchorPane productDetailsPane;
    @FXML
    JFXButton closeProductDetailsButton;
    void initProducts() {
        productsTable.getItems().clear();
        productsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                ObservableList<Object> selected = productsTable.getSelectionModel().getSelectedItem();
                initProductDetails(selected);
                bgPane.setVisible(true);
                productDetailsPane.setVisible(true);
            }
        });
        closeProductDetailsButton.setOnAction(event -> {
            bgPane.setVisible(false);
            productDetailsPane.setVisible(false);
        });
        productCodeProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
                return new SimpleObjectProperty<String>((String) param.getValue().get(0));
            }
        });

        productNameProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
                return new SimpleObjectProperty<String>((String) param.getValue().get(1));
            }
        });

        productLineProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
                return new SimpleObjectProperty<String>((String) param.getValue().get(2));
            }
        });

//        productScaleProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
//                return new SimpleObjectProperty<String>((String) param.getValue().get(3));
//            }
//        });
//
//        productVendorProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
//                return new SimpleObjectProperty<String>((String) param.getValue().get(4));
//            }
//        });
//
//        productDescriptionProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
//                return new SimpleObjectProperty<String>((String) param.getValue().get(5));
//            }
//        });
//
//        inStockProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, Integer>, ObservableValue<Integer>>() {
//            @Override
//            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList<Object>, Integer> param) {
//                return new SimpleObjectProperty<Integer>((Integer) param.getValue().get(6));
//            }
//        });
//
//        buyPriceProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, Float>, ObservableValue<Float>>() {
//            @Override
//            public ObservableValue<Float> call(TableColumn.CellDataFeatures<ObservableList<Object>, Float> param) {
//                return new SimpleObjectProperty<Float>((Float) param.getValue().get(7));
//            }
//        });
//
//        MSRPProd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, Float>, ObservableValue<Float>>() {
//            @Override
//            public ObservableValue<Float> call(TableColumn.CellDataFeatures<ObservableList<Object>, Float> param) {
//                return new SimpleObjectProperty<Float>((Float) param.getValue().get(8));
//            }
//        });

        try {
            String query = "SELECT productCode, productName, productLine FROM products";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            while (resultSet.next()) {
                ObservableList<Object> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("productCode"));
                row.add(resultSet.getString("productName"));
                row.add(resultSet.getString("productLine"));
//                row.add(resultSet.getString("productScale"));
//                row.add(resultSet.getString("productVendor"));
//                row.add(resultSet.getString("productDescription"));
//                row.add(resultSet.getInt("quantityInStock"));
//                row.add(resultSet.getFloat("buyPrice"));
//                row.add(resultSet.getFloat("MSRP"));
                productsTable.getItems().add(row);
            }
            productsTable.refresh();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    JFXTextField productCodePDetails;
    @FXML
    JFXTextField productNamePDetails;
    @FXML
    JFXComboBox productLinePDetails;
    @FXML
    JFXTextField productScalePDetails;
    @FXML
    JFXTextField productVendorPDetails;
    @FXML
    JFXTextField inStockPDetails;
    @FXML
    JFXTextField buyPricePDetails;
    @FXML
    JFXTextField MSRPPDetails;
    @FXML
    JFXTextField productDescriptionPDetails;

    void initProductDetails(ObservableList<Object> selected) {
        try {
            productLinePDetails.getItems().clear();
            String query = "SELECT productLine FROM productlines";
            ResultSet resultSet = sqlConnection.getDataQuery(query);

            while (resultSet.next()) {
                String productLine = resultSet.getString("productLine");
                productLinePDetails.getItems().add(productLine);
            }
            query = "SELECT productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP FROM products WHERE productCode = '" + selected.get(0).toString() + "'";
            resultSet = sqlConnection.getDataQuery(query);
            if (resultSet.next()) {
                productCodePDetails.setText(resultSet.getString("productCode"));
                productNamePDetails.setText(resultSet.getString("productName"));
                productLinePDetails.setValue(resultSet.getString("productLine"));
                productScalePDetails.setText(resultSet.getString("productScale"));
                productVendorPDetails.setText(resultSet.getString("productVendor"));
                productDescriptionPDetails.setText(resultSet.getString("productDescription"));
                inStockPDetails.setText(resultSet.getString("quantityInStock"));
                buyPricePDetails.setText(resultSet.getString("buyPrice"));
                MSRPPDetails.setText(resultSet.getString("MSRP"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

