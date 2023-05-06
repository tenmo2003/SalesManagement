package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.*;
import javafx.animation.AnimationTimer;
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
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import salesmanagement.salesmanagement.SalesComponent.*;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.*;
import salesmanagement.salesmanagement.ViewController.CustomersTab.CustomersTabViewController;
import salesmanagement.salesmanagement.ViewController.DashBoardTab.DashboardTabViewController;
import salesmanagement.salesmanagement.ViewController.EmployeesTab.EmployeesTabViewController;
import salesmanagement.salesmanagement.ViewController.OrdersTab.OrdersTabViewController;
import salesmanagement.salesmanagement.ViewController.ProductsTab.ProductsTabViewController;
import salesmanagement.salesmanagement.ViewController.SettingsTab.SettingTabViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private Tab customersTab;
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
    JFXButton customersTabButton;
    @FXML
    JFXButton employeesTabButton;
    JFXButton currentTabButton;


    SettingTabViewController settingTabViewController;
    EmployeesTabViewController employeesTabViewController;
    CustomersTabViewController customersTabViewController;
    ProductsTabViewController productsTabViewController;
    OrdersTabViewController ordersTabViewController;
    DashboardTabViewController dashboardTabViewController;

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
    }

    @FXML
    void goToOrdersTab() {
        tabPane.getSelectionModel().select(ordersTab);
        ordersTabViewController.show();
    }

    void goToEditOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
    }

    @FXML
    void goToProductsOperationTab() {
        tabPane.getSelectionModel().select(productsOperationTab);
        productsTabViewController.show();
    }

    @FXML
    void goToCustomersTab() {
        tabPane.getSelectionModel().select(customersTab);
        customersTabViewController.show();
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
        settingTabViewController.show();
    }

    //region DashBoard Tab
    @FXML
    Tab dashBoardTab;

    @FXML
    private StackPane chartPane1;
    @FXML
    private StackPane chartPane2;

    final int maximumChecked = 2;
    int numChecked = 2;

    @FXML
    JFXCheckBox totalRevenueCheck;
    @FXML
    JFXCheckBox topProductCheck;
    @FXML
    JFXCheckBox topProductLineCheck;
    @FXML
    JFXCheckBox topCustomerCheck;
    @FXML
    JFXCheckBox topStoreCheck;

    public void displayDashBoardTab() {
        tabPane.getSelectionModel().select(dashBoardTab);
        dashboardTabViewController.show();
    }
    //endregion

    //region Employees Tab: list employees, employee's info: details, order, operations.
    @FXML
    void displayEmployeesTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
        employeesTabViewController.show();
    }



    //endregion
    @FXML
    SplitPane firstSplitPane;
    @FXML
    SplitPane secondSplitPane;

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
        user = new Employee(sqlConnection, loggerID);
        settingTabViewController.setUser(user);
        ViewController.setSqlConnection(sqlConnection);

        // Load current UI.

        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((StackPane) firstSplitPane.getItems().get(0)).setMinHeight(0.06 * Screen.getPrimary().getVisualBounds().getHeight());
        ((SplitPane) firstSplitPane.getItems().get(1)).setMinHeight(0.94 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((TabPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());

        Insets hboxMargin = new Insets(0, 0.8333 * Screen.getPrimary().getVisualBounds().getWidth(), 0, 0);
        StackPane.setMargin(appName, hboxMargin);


        Circle clip = new Circle();
        clip.setRadius(35);
        clip.setCenterX(35);
        clip.setCenterY(35);
        smallAvatar.setClip(clip);

        currentTabButton = dashBoardTabButton;


        //TODO: test area
//        ScrollPane scroll = (ScrollPane) detailsInfoBox.getParent().getParent().getParent();
//        Transition down = new Transition() {
//            {
//                setCycleDuration(Duration.INDEFINITE);
//            }
//
//            @Override
//            protected void interpolate(double v) {
//                scroll.setVvalue(scroll.getVvalue() + 0.001);
//            }
//        };
//
//        Transition up = new Transition() {
//            {
//                setCycleDuration(Duration.INDEFINITE);
//            }
//
//            @Override
//            protected void interpolate(double v) {
//                scroll.setVvalue(scroll.getVvalue() - 0.001);
//            }
//        };


        // Load UI for others.
        runTask(() -> {
            // Load small avatar.
            smallAvatar.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true));


            // Employees Tab Preparation.


        }, null, null, null);
    }


    private Employee user;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-tab/employees-tab-view.fxml"));
            loader.load();
            employeesTabViewController = loader.getController();
            employeesOperationTab.setContent(employeesTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/setting-tab-view.fxml"));
            loader.load();
            settingTabViewController = loader.getController();
            settingTab.setContent(settingTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-tab-view.fxml"));
            loader.load();
            customersTabViewController = loader.getController();
            customersTab.setContent(customersTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-tab-view.fxml"));
            loader.load();
            productsTabViewController = loader.getController();
            productsOperationTab.setContent(productsTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/orders-tab-view.fxml"));
            loader.load();
            ordersTabViewController = loader.getController();
            ordersTab.setContent(ordersTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/dashboard-tab/dashboard-tab-view.fxml"));
            loader.load();
            dashboardTabViewController = loader.getController();
            dashBoardTab.setContent(dashboardTabViewController.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}