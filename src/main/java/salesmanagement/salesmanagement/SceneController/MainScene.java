package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.ViewController.UserRight;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.*;
import salesmanagement.salesmanagement.ViewController.CustomersTab.CustomersTabView;
import salesmanagement.salesmanagement.ViewController.DashBoardTab.DashboardTabView;
import salesmanagement.salesmanagement.ViewController.EmployeesTab.EmployeesTabView;
import salesmanagement.salesmanagement.ViewController.OrdersTab.OrdersTabView;
import salesmanagement.salesmanagement.ViewController.ProductsTab.ProductsTabView;
import salesmanagement.salesmanagement.ViewController.SettingsTab.SettingTabView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static salesmanagement.salesmanagement.Utils.NotificationCode.NO_RIGHT;

public class MainScene extends SceneController implements Initializable {
    @FXML
    Text usernameText;
    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesOperationTab;
    @FXML
    private Tab createOrderTab;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;
    @FXML
    private Tab ordersTab;
    @FXML
    private Tab customersTab;
    @FXML
    JFXButton dashBoardTabButton;
    @FXML
    JFXButton ordersTabButton;
    @FXML
    JFXButton settingsTabButton;
    @FXML
    JFXButton productsTabButton;
    @FXML
    JFXButton customersTabButton;
    @FXML
    JFXButton employeesTabButton;
    JFXButton currentTabButton;


    SettingTabView settingTabView;
    EmployeesTabView employeesTabView;
    CustomersTabView customersTabView;
    ProductsTabView productsTabView;
    OrdersTabView ordersTabView;
    DashboardTabView dashboardTabView;

    @FXML
    void goToOrdersTab() {
        tabPane.getSelectionModel().select(ordersTab);
        ordersTabView.show();
    }

    @FXML
    void goToProductsOperationTab() {
        tabPane.getSelectionModel().select(productsOperationTab);
        productsTabView.show();
    }

    @FXML
    void goToCustomersTab() {
        tabPane.getSelectionModel().select(customersTab);
        customersTabView.show();
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
        settingTabView.show();
    }

    @FXML
    Tab dashBoardTab;

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
        dashboardTabView.show();
    }

    @FXML
    void displayEmployeesTab() {
        if (ViewController.getUserRight() != UserRight.EMPLOYEE) {
            tabPane.getSelectionModel().select(employeesOperationTab);
            employeesTabView.show();
        } else {
            NotificationSystem.throwNotification(NO_RIGHT, stage);
        }
    }

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
        settingTabView.setUser(user);
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


        runTask(() -> {
            smallAvatar.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true));
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
            if (MainScene.loggerID > 0) {
                runTask(() -> {
                    Platform.runLater(() -> {
                        stage.setScene(MainScene.this.scene);
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
            employeesTabView = loader.getController();
            employeesOperationTab.setContent(employeesTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/setting-tab-view.fxml"));
            loader.load();
            settingTabView = loader.getController();
            settingTab.setContent(settingTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-tab-view.fxml"));
            loader.load();
            customersTabView = loader.getController();
            customersTab.setContent(customersTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-tab-view.fxml"));
            loader.load();
            productsTabView = loader.getController();
            productsOperationTab.setContent(productsTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/orders-tab-view.fxml"));
            loader.load();
            ordersTabView = loader.getController();
            ordersTab.setContent(ordersTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/dashboard-tab/dashboard-tab-view.fxml"));
            loader.load();
            dashboardTabView = loader.getController();
            dashBoardTab.setContent(dashboardTabView.getRoot());
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