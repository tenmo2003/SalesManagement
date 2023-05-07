package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Duration;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.CustomersTab.CustomersTabView;
import salesmanagement.salesmanagement.ViewController.DashBoardTab.DashboardTabView;
import salesmanagement.salesmanagement.ViewController.EmployeesTab.EmployeesTabView;
import salesmanagement.salesmanagement.ViewController.OrdersTab.OrdersTabView;
import salesmanagement.salesmanagement.ViewController.ProductsTab.ProductsTabView;
import salesmanagement.salesmanagement.ViewController.SettingsTab.SettingsTabView;
import salesmanagement.salesmanagement.ViewController.UserRight;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.Utils.NotificationCode.NOT_AUTHORIZED;

public class MainScene extends SceneController implements Initializable {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label jobTitleLabel;
    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesTab;
    @FXML
    private Tab ordersTab;
    @FXML
    private Tab settingsTab;
    @FXML
    private Tab productsTab;
    @FXML
    private Tab customersTab;
    @FXML
    private Tab dashBoardTab;
    @FXML
    private JFXButton dashBoardTabButton;
    @FXML
    private JFXButton ordersTabButton;
    @FXML
    private JFXButton settingsTabButton;
    @FXML
    private JFXButton productsTabButton;
    @FXML
    private JFXButton customersTabButton;
    @FXML
    private JFXButton employeesTabButton;
    @FXML
    private JFXButton shrinkSideBarButton;
    @FXML
    SplitPane firstSplitPane;
    @FXML
    HBox appName;
    @FXML
    ImageView smallAvatar;
    @FXML
    StackPane menuPane;

    JFXButton previousTabButton = dashBoardTabButton;

    SettingsTabView settingsTabView;
    EmployeesTabView employeesTabView;
    CustomersTabView customersTabView;
    ProductsTabView productsTabView;
    OrdersTabView ordersTabView;
    DashboardTabView dashboardTabView;

    @Override
    protected void maximumStage(MouseEvent mouseEvent) {

    }

    public void initialSetup() {
        user = new Employee(sqlConnection, loggerID);
        settingsTabView.setUser(user);
        employeesTabView.setLoggedInUser(user);
        ViewController.setSqlConnection(sqlConnection);

        usernameLabel.setText(user.getFullName());
        jobTitleLabel.setText(user.getJobTitle());

        Insets hboxMargin = new Insets(0, 0.8333 * Screen.getPrimary().getVisualBounds().getWidth(), 0, 0);
        StackPane.setMargin(appName, hboxMargin);

        previousTabButton = dashBoardTabButton;

        runTask(() -> smallAvatar.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true)), null, null, null);
    }

    @FXML
    VBox root;

    private Employee user;
    public static boolean haveJustOpened = false;
    public static int loggerID = -1;
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


                        root.setPrefSize(1000, 500);
                        stage.show();

                        sideBarBox.setMinWidth(300);
                        System.out.println(sideBarBox.getPrefWidth());
                        // dashBoardTabButton.fire();
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
            employeesTab.setContent(employeesTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/setting-tab-view.fxml"));
            loader.load();
            settingsTabView = loader.getController();
            settingsTab.setContent(settingsTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-tab-view.fxml"));
            loader.load();
            customersTabView = loader.getController();
            customersTab.setContent(customersTabView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-tab-view.fxml"));
            loader.load();
            productsTabView = loader.getController();
            productsTab.setContent(productsTabView.getRoot());

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


        dashBoardTabButton.setOnMouseClicked(event -> {
            tabSelectingEffect(dashBoardTabButton);
            tabPane.getSelectionModel().select(dashBoardTab);
            dashboardTabView.show();
        });

        employeesTabButton.setOnMouseClicked(event -> {
            if (ViewController.getUserRight() != UserRight.EMPLOYEE) {
                tabSelectingEffect(employeesTabButton);
                tabPane.getSelectionModel().select(employeesTab);
                employeesTabView.show();
            } else {
                NotificationSystem.throwNotification(NOT_AUTHORIZED, stage);
            }
        });

        customersTabButton.setOnMouseClicked(event -> {
            tabSelectingEffect(customersTabButton);
            tabPane.getSelectionModel().select(customersTab);
            customersTabView.show();
        });

        ordersTabButton.setOnMouseClicked(event -> {
            tabSelectingEffect(ordersTabButton);
            tabPane.getSelectionModel().select(ordersTab);
            ordersTabView.show();
        });

        settingsTabButton.setOnMouseClicked(event -> {
            tabSelectingEffect(settingsTabButton);
            tabPane.getSelectionModel().select(settingsTab);
            settingsTabView.show();
        });

        productsTabButton.setOnMouseClicked(event -> {
            tabSelectingEffect(productsTabButton);
            tabPane.getSelectionModel().select(productsTab);
            productsTabView.show();
        });

        shrinkSideBarButton.setOnMouseClicked(event -> {
            shrink();
        });
    }

    @FXML
    VBox sideBarBox;
    List<JFXButton> tabButtons;

    public void shrink() {
        if (tabButtons == null) {
            tabButtons = new ArrayList<>(Arrays.asList(dashBoardTabButton, employeesTabButton, customersTabButton, ordersTabButton, productsTabButton, settingsTabButton));
        }
        for (JFXButton button : tabButtons)
            button.getStyleClass().add("shrink-tab-button");

        sideBarBox.setMinWidth(30);
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(1));
            }

            @Override
            protected void interpolate(double frac) {
                double width = sideBarBox.getWidth() * (1 - frac);
                sideBarBox.setPrefWidth(width);
            }
        };
        transition.play();
    }

    private void tabSelectingEffect(JFXButton selectedTabButton) {
        previousTabButton.getStyleClass().remove("active-tab-button");
        previousTabButton = selectedTabButton;
        previousTabButton.getStyleClass().add("active-tab-button");
    }
}