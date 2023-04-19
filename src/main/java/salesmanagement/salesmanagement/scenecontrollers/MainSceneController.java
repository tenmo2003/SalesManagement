package salesmanagement.salesmanagement.scenecontrollers;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jfoenix.controls.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import salesmanagement.salesmanagement.Form;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesComponent.OrderItem;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static salesmanagement.salesmanagement.Utils.getAllNodes;

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
    private Tab homeTab;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;
    boolean productsInit = false;
    @FXML
    private Tab ordersTab;
    boolean ordersInit = false;

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
        initCreateOrder();
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
    private TableColumn<?, ?> actionColumn;
    @FXML
    private TableColumn<?, ?> phoneColumn;
    @FXML
    private TableColumn<?, ?> employeeStatusColumn;
    @FXML
    AnchorPane employeeOperationPane;
    ArrayList<Employee> employees;
    Employee employeeSelected;



    @FXML
    void displayEmployeesTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);

        employeeTable.setSelectionModel(null);
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
                    employees.add(new Employee(resultSet, MainSceneController.this));
                }
            } catch (SQLException ignored) {
            }
        }, () -> {
            ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
            employeeTable.setItems(employeeList);
            this.employees = employees;
        }, progressIndicator, employeeOperationPane);
    }

    @FXML
    VBox employeeInfoBox;
    @FXML
    VBox detailsInfoBox;
    @FXML
    JFXButton editInfoButton;
    @FXML
    JFXButton saveInfoButton;

    /**
     * When click on Name Text in Employee table, this function will be called to
     * display detail information of employee. It's called by an Employee object.
     * It hides employees table box and shows employee information box.
     */
    public void displayEmployeeInfoBox(Employee employee) {
        employeeInfoBox.toFront();
        employeeInfoBox.setVisible(true);

        employeeTableBox.toBack();
        employeeTableBox.setVisible(false);

        for (Node node : getAllNodes(detailsInfoBox)) {
            node.setDisable(false);
        }

        fullNameLabel.setText(employee.getFullName());
        avatar.setImage(employee.getAvatar().getImage());
        lastNameTextField.setText(employee.getLastName());
        firstNameTextField.setText(employee.getFirstName());
        usernameTextField.setText(employee.getUsername());
        passwordField.setText(employee.getPassword());
        birthDatePicker.setValue(employee.getBirthDate());
        emailTextField.setText(employee.getEmail());
        phoneCodeBox.setValue(employee.getPhoneCode());
        joiningDatePicker.setValue(employee.getJoiningDate());
        lastWorkingDatePicker.setValue(employee.getLastWorkingDate());
        phoneNumberTextField.setText(employee.getPhone());
        statusBox.setValue(employee.getStatus().getText());
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



    @FXML
    public void editEmployeeInfo() {
        // UNLOCK ALL
        for (Node node : getAllNodes(detailsInfoBox)) {
            node.setDisable(false);

        }
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
    void goBackToEmployeeTableBox() {
        System.out.println(123);
        employeeInfoBox.toBack();
        employeeInfoBox.setVisible(false);

        employeeTableBox.toFront();
        employeeInfoBox.setVisible(true);

        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
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
    SplitPane thirdSplitPane;
    @FXML
    VBox employeeTableBox;
    @FXML
    HBox appName;
    @FXML
    ImageView smallAvatar;

    @FXML
    private void goToNewsTab() {
        tabPane.getSelectionModel().select(homeTab);
        newsTabButton.fire();
    }

    @Override
    protected void maximumStage(MouseEvent mouseEvent) {

    }

    @FXML
    StackPane menuPane;

    //region News Tab: display news, post news.
    @FXML
    WebView webPreview;
    @FXML
    HTMLEditor htmlEditor;
    String htmlText;
    MenuButton insertMenuButton;
    @FXML
    VBox newsPreviewBox;
    @FXML
    VBox newsCreatingBox;
    @FXML
    JFXTextField newsTitle;
    @FXML
    ImageView newsImageInserted;
    @FXML
    WebView newsDisplayedView;
    @FXML
    VBox newsPiecesBox;
    String mainContent = "";

    @FXML
    void goToCreateNews() {
        thirdSplitPane.setVisible(false);
        thirdSplitPane.setDisable(true);
        newsCreatingBox.setVisible(true);
        newsCreatingBox.setDisable(false);
    }

    @FXML
    public void previewNews() {
        newsPreviewBox.setVisible(true);
        newsPreviewBox.setDisable(false);
        newsCreatingBox.setVisible(false);
        htmlText = htmlEditor.getHtmlText();
        htmlText = htmlText.replace("<body contenteditable=\"true\">", "<body style='background : rgba(0,0,0,0);' contenteditable=\"false\";> " + "<h1 style=\"text-align:center; font-size:30px; font-weight:bold;\">" + newsTitle.getText() + "</h1>\n");
        webPreview.setPageFill(Color.TRANSPARENT);
        webPreview.getEngine().loadContent(htmlText);
    }

    @FXML
    public void backToNewsCreatingBox() {
        newsPreviewBox.setVisible(false);
        newsPreviewBox.setDisable(true);
        newsCreatingBox.setVisible(true);
    }

    @FXML
    public void postNews() throws SQLException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(newsImageInserted.getImage(), null), "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] imageBytes = outputStream.toByteArray();
        String postNewsQuery = "insert into notifications(employeeNumber, title, content, newsImage) values (?,?,?,?)";
        PreparedStatement statement = sqlConnection.getConnection().prepareStatement(postNewsQuery);
        statement.setInt(1, user.getEmployeeNumber());
        statement.setString(2, newsTitle.getText());
        statement.setString(3, htmlText);
        statement.setBytes(4, imageBytes);
        statement.executeUpdate();

        thirdSplitPane.setVisible(true);
        thirdSplitPane.setDisable(false);
        newsCreatingBox.setVisible(false);
        newsCreatingBox.setDisable(true);
    }

    private void uploadNotificationText() {
        System.out.println(123);
        runTask(() -> {
            String query = "SELECT * FROM notifications ORDER BY notifications.notificationID DESC LIMIT 6;";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                if (resultSet.next()) {
                    mainContent = resultSet.getString("content");
                }
                Platform.runLater(() -> {
                    newsDisplayedView.getEngine().loadContent(mainContent);
                });
                int newsPieceCount = 0;
                while (resultSet.next()) {
                    HBox newsPiece = ((HBox) newsPiecesBox.getChildren().get(newsPieceCount));
                    ((Text) newsPiece.getChildren().get(1)).setText(resultSet.getString("title"));
                    InputStream is = resultSet.getBinaryStream("newsImage");
                    Image image = new Image(is);
                    ((ImageView) newsPiece.getChildren().get(0)).setImage(image);
                    newsPieceCount++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, null, (Pane) thirdSplitPane.getItems().get(0));

    }

    @FXML
    void uploadImageNews(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose News Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            ((ImageView) event.getSource()).setImage(new Image(selectedFile.toURI().toString()));
        }
    }
    //endregion

    public void initialSetup() {
        // Load current UI.
        user = new Employee(sqlConnection, loggerID);
        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((StackPane) firstSplitPane.getItems().get(0)).setMinHeight(0.06 * Screen.getPrimary().getVisualBounds().getHeight());
        ((SplitPane) firstSplitPane.getItems().get(1)).setMinHeight(0.94 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((TabPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());

        thirdSplitPane.setMaxWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        ((Pane) thirdSplitPane.getItems().get(0)).setMinWidth(0.75 * thirdSplitPane.getMaxWidth());
        ((Pane) thirdSplitPane.getItems().get(1)).setMinWidth(0.25 * thirdSplitPane.getMaxWidth());
        Insets hboxMargin = new Insets(0, 0.8333 * Screen.getPrimary().getVisualBounds().getWidth(), 0, 0);
        StackPane.setMargin(appName, hboxMargin);

        double tableWidth = employeeTableBox.getWidth() * 0.95;
        employeeTable.setMaxWidth(tableWidth);
        employeeNumberColumn.setMinWidth(0.1 * tableWidth);
        nameColumn.setMinWidth(0.25 * tableWidth);
        phoneColumn.setMinWidth(0.15 * tableWidth);
        emailColumn.setMinWidth(0.2 * tableWidth);
        employeeStatusColumn.setMinWidth(0.1 * tableWidth);
        actionColumn.setMinWidth(0.2 * tableWidth);

        Circle clip = new Circle();
        clip.setRadius(35);
        clip.setCenterX(35);
        clip.setCenterY(35);
        smallAvatar.setClip(clip);

        currentTabButton = newsTabButton;
        goToNewsTab();

        insertMenuButton = new MenuButton("Insert...");
        ToolBar bar = null;
        Node topToolbar = htmlEditor.lookup(".top-toolbar");
        if (topToolbar instanceof ToolBar) {
            bar = (ToolBar) topToolbar;
        }
        if (bar != null) {
            bar.getItems().add(insertMenuButton);
        }

        webPreview.setPageFill(Color.TRANSPARENT);
        newsDisplayedView.setPageFill(Color.TRANSPARENT);

        //TODO: test area

        // Load UI for others.
        runTask(() -> {
            // Load small avatar.
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

            // Prepare for employee table structure.
            employeeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

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

            List<String> statusList = new ArrayList<>(Arrays.asList("ACTIVE", "INACTIVE"));
            statusBox.getItems().addAll(statusList);
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
                        uploadNotificationText();
                    });

                }, null, null, null);
                stop();
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

        productCodeOD.setCellValueFactory(new PropertyValueFactory<OrderItem, String>("productCode"));
        quantityOD.setCellValueFactory(new PropertyValueFactory<OrderItem, Integer>("quantityOrdered"));
        priceEachOD.setCellValueFactory(new PropertyValueFactory<OrderItem, Double>("priceEach"));
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
        productCodeInput.setOnKeyReleased(event -> {
            if (!(event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP)) {
                String newValue = productCodeInput.getText();
                // Update the suggestion list based on the current input
                if (newValue.equals("")) {
                    suggestionList.setVisible(false);
                    suggestionList.setMouseTransparent(true);
                } else {
                    if (newValue.length() > 2) {
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
            }
        });

        productCodeInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String selectedValue = suggestionList.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                    productCodeInput.setText(selectedValue);
                    setPrice();
                    suggestionList.setVisible(false);
                    productCodeInput.requestFocus();
                }
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.DOWN) {
                suggestionList.getSelectionModel().selectNext();
                event.consume();
                productCodeInput.requestFocus();
            } else if (event.getCode() == KeyCode.UP) {
                suggestionList.getSelectionModel().selectPrevious();
                event.consume();
                productCodeInput.requestFocus();
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

        customerNameOrd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<Object>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<Object>, String> param) {
                return new SimpleStringProperty((String) param.getValue().get(7));
            }
        });

        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // check for double-click event
                ObservableList<Object> selectedOrderRow = ordersTable.getSelectionModel().getSelectedItem();
                initEditOrder(selectedOrderRow);
                goToEditOrderTab();
            }
        });

        editOrderButton.setOnAction(e -> {
            ObservableList<Object> selectedRow = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                initEditOrder(selectedRow);
                goToEditOrderTab();
            }
        });

        productsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                ObservableList<Object> selected = productsTable.getSelectionModel().getSelectedItem();
                initEditProductDetails(selected);
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

        removeItemButton.setOnAction(event -> {
            removeItemButtonClicked = true;
            removeItems();
        });

        removeProductButton.setOnAction(event -> {
            removeProductButtonClicked = true;
            ObservableList<Object> selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Are you sure you want to delete this product?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, so delete the item
                    String query = String.format("DELETE FROM products WHERE productCode = '%s'", selected.get(0));
                    sqlConnection.updateQuery(query);
                    productsTable.getItems().remove(selected);
                } else {
                    // User clicked Cancel or closed the dialog box, so do nothing
                    // ...
                }
            }
        });

        removeOrderButton.setOnAction(event -> {
            removeOrderButtonClicked = true;
            ObservableList<Object> selected = ordersTable.getSelectionModel().getSelectedItem();
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

    private void initCreateOrder() {
        clearCreateOrderTab();
        customerNameInput.setEditable(true);
        phoneNumberInput.setEditable(true);
        submitOrderButton.setText("Create Order");
        submitOrderButton.setOnAction(event -> {
            createOrder();
        });
        orderDetailsTable.setItems(getList());
    }

    private void initEditOrder(ObservableList<Object> selectedOrderRow) {
        clearCreateOrderTab();
        submitOrderButton.setText("Save");
        customerNameInput.setEditable(false);
        phoneNumberInput.setEditable(false);
        if (selectedOrderRow != null) {
            // Extract the order number from the selected row
            int orderNumber = (int) selectedOrderRow.get(0);

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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Set the date pickers and comments text field with values from the selected order
            LocalDate orderDate = (LocalDate) selectedOrderRow.get(1);
            LocalDate requiredDate = (LocalDate) selectedOrderRow.get(2);
            LocalDate shippedDate = (LocalDate) selectedOrderRow.get(3);
            String comments = (String) selectedOrderRow.get(5);
            int customerNumber = (int) selectedOrderRow.get(6);
            orderDateInput.setValue(orderDate);
            requiredDateInput.setValue(requiredDate);
            shippedDateInput.setValue(shippedDate);
            commentsInput.setText(comments);

            // Set the status combo box to the value from the selected order
            String status = (String) selectedOrderRow.get(4);
            statusInput.setValue(status);

            query = String.format("SELECT customerName, phone FROM customers WHERE customerNumber = %d", customerNumber);
            rs = sqlConnection.getDataQuery(query);
            try {
                if (rs.next()) {
                    // Extract the customer name and phone number from the result set
                    String customerName = rs.getString("customerName");
                    String phoneNumber = rs.getString("phone");

                    // Set the customer name and phone number fields
                    customerNameInput.setText(customerName);
                    phoneNumberInput.setText(phoneNumber);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
            LocalDate requiredDate = requiredDateInput.getValue();
            String shippedDate = shippedDateInput.getValue() != null ? "'" + shippedDateInput.getValue().toString() + "'" : "null";
            String status = (String) statusInput.getValue();
            String comments = commentsInput.getText();

            // Get the customer number from the customer name input field
            String customerName = customerNameInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            int customerNumber = 0;

            query = String.format("UPDATE orders SET orderDate = '%s', requiredDate = '%s', shippedDate = %s, status = '%s', comments = '%s' WHERE orderNumber = %d", orderDate.toString(), requiredDate.toString(), shippedDate, status, comments, orderNumber);
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
                orderItem.setQuantityOrdered(quantity);
                orderItem.setPriceEach(priceEach);
                orderDetailsTable.refresh();
                return;
            }
        }

        // If no existing order was found, create a new one and add it to the tableView
        OrderItem orderItem = new OrderItem(productCode, quantity, priceEach, quantity * priceEach);
        orderDetailsTable.getItems().add(orderItem);

        productCodeInput.clear();
        quantityInput.clear();
        priceEachInput.clear();
    }

    public void removeItems() {
        ObservableList<OrderItem> selectedRows, allItems;
        allItems = orderDetailsTable.getItems();

        selectedRows = orderDetailsTable.getSelectionModel().getSelectedItems();

        allItems.removeAll(selectedRows);
    }

    public void clearItems() {
        orderDetailsTable.getItems().clear();
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
    private boolean removeItemButtonClicked = false;
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
    @FXML
    JFXButton printInvoiceButton;
    @FXML
    JFXTextField customerNameInput;
    @FXML
    JFXTextField phoneNumberInput;


    public void createOrder() {
        runTask(() -> {
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
            String order = "insert into orders(orderDate, requiredDate, shippedDate, status, comments, customerNumber) values ('"
                    + orderDate + "','"
                    + requiredDateInput.getValue().format(DateTimeFormatter.ISO_DATE) + "',"
                    + shippedDate + ",'"
                    + statusInput.getValue() + "','"
                    + commentsInput.getText() + "',"
                    + customerNumber + ");";
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

    /**
     * Handle ORDERS tab.
     */
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
    TableColumn<ObservableList<Object>, String> customerNameOrd;
    @FXML
    JFXButton createOrderButton;
    @FXML
    JFXButton editOrderButton;
    @FXML
    JFXButton removeOrderButton;
    private boolean removeOrderButtonClicked = false;

    void initOrders() {
        runTask(() -> {
            ordersTable.getItems().clear();

            try {
                String query = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, orders.customerNumber, customerName FROM orders INNER JOIN customers ON orders.customerNumber = customers.customerNumber";
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
                    row.add(resultSet.getString("customerName"));
                    ordersTable.getItems().add(row);
                }
                ordersTable.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, progressIndicator, ordersTab.getTabPane());

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
    @FXML
    JFXButton productDetailsButton;
    @FXML
    JFXButton addProductButton;
    @FXML
    JFXButton removeProductButton;
    private boolean removeProductButtonClicked = false;
    @FXML
    Pane bgPane;
    @FXML
    AnchorPane productDetailsPane;
    @FXML
    JFXButton closeProductDetailsButton;

    void initProducts() {
        runTask(() -> {
            productsInit = true;
            productsTable.getItems().clear();

            try {
                String query = "SELECT productCode, productName, productLine FROM products";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    ObservableList<Object> row = FXCollections.observableArrayList();
                    row.add(resultSet.getString("productCode"));
                    row.add(resultSet.getString("productName"));
                    row.add(resultSet.getString("productLine"));
                    productsTable.getItems().add(row);
                }
                productsTable.refresh();
                productLinePDetails.getItems().clear();
                query = "SELECT productLine FROM productlines";
                resultSet = sqlConnection.getDataQuery(query);

                while (resultSet.next()) {
                    String productLine = resultSet.getString("productLine");
                    productLinePDetails.getItems().add(productLine);
                }
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
    JFXComboBox productLinePDetails;
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
        productLinePDetails.setValue(null);
        inStockPDetails.setText("");
        buyPricePDetails.setText("");
        sellPricePDetails.setText("");
        productDescriptionPDetails.setText("");

        bgPane.setVisible(true);
        productDetailsPane.setVisible(true);

        productDetailsButton.setOnAction(event -> {
            String query = String.format("insert into products(productCode, productName, productLine, productVendor, productDescription, quantityInStock, buyPrice, sellPrice) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', %d, %f, %f);", productCodePDetails.getText(), productNamePDetails.getText(),
                            productLinePDetails.getValue(), productVendorPDetails.getText(), productDescriptionPDetails.getText(),
                            Integer.parseInt(inStockPDetails.getText()), Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()));
            sqlConnection.updateQuery(query);

            ObservableList<Object> row = FXCollections.observableArrayList();
            row.add(productCodePDetails.getText());
            row.add(productNamePDetails.getText());
            row.add(productLinePDetails.getValue());
            productsTable.getItems().add(0, row);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added successfully!", ButtonType.OK);
            alert.setTitle(null);
            alert.setHeaderText(null);

            alert.showAndWait();

            bgPane.setVisible(false);
            productDetailsPane.setVisible(false);

            productsTable.refresh();


        });
    }

    void initEditProductDetails(ObservableList<Object> selected) {
        productDetailsButton.setText("Save");
        try {
            String query = "SELECT productCode, productName, productLine, productVendor, productDescription, quantityInStock, buyPrice, sellPrice FROM products WHERE productCode = '" + selected.get(0).toString() + "'";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            if (resultSet.next()) {
                productCodePDetails.setText(resultSet.getString("productCode"));
                productNamePDetails.setText(resultSet.getString("productName"));
                productLinePDetails.setValue(resultSet.getString("productLine"));
                productVendorPDetails.setText(resultSet.getString("productVendor"));
                productDescriptionPDetails.setText(resultSet.getString("productDescription"));
                inStockPDetails.setText(resultSet.getString("quantityInStock"));
                buyPricePDetails.setText(resultSet.getString("buyPrice"));
                sellPricePDetails.setText(resultSet.getString("sellPrice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        productDetailsButton.setOnAction(e -> {
            String query = String.format("UPDATE products SET productCode = '%s', productName = '%s', productLine = '%s', productVendor = '%s', productDescription = '%s', quantityInStock = %d, buyPrice = %f, sellPrice = %f WHERE productCode = '%s'",
                    productCodePDetails.getText(), productNamePDetails.getText(), productLinePDetails.getValue(), productVendorPDetails.getText(),
                    productDescriptionPDetails.getText(), Integer.parseInt(inStockPDetails.getText()),
                    Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()), selected.get(0));
            sqlConnection.updateQuery(query);

            selected.set(0, productCodePDetails.getText());
            selected.set(1, productNamePDetails.getText());
            selected.set(2, productLinePDetails.getValue());

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

