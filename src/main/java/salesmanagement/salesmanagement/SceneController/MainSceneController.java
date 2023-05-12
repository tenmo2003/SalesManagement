package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.Utils.Utils;
import salesmanagement.salesmanagement.ViewController.CustomersTab.CustomersTabView;
import salesmanagement.salesmanagement.ViewController.DashBoardTab.DashboardTabView;
import salesmanagement.salesmanagement.ViewController.EmployeesTab.EmployeesTabView;
import salesmanagement.salesmanagement.ViewController.OrdersTab.OrdersTabView;
import salesmanagement.salesmanagement.ViewController.ProductLinesTab.ProductLinesTabView;
import salesmanagement.salesmanagement.ViewController.ProductsTab.ProductsTabView;
import salesmanagement.salesmanagement.ViewController.SettingsTab.SettingsTabView;
import salesmanagement.salesmanagement.ViewController.UserRight;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static salesmanagement.salesmanagement.Utils.ImageController.isImageLoaded;
import static salesmanagement.salesmanagement.Utils.NotificationCode.NOT_AUTHORIZED;

public class MainSceneController extends SceneController implements Initializable {
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
    private Tab productLinesTab;
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
    private JFXButton logOutButton;
    @FXML
    private JFXButton productLinesTabButton;
    @FXML
    private JFXToggleButton switchModeButton;
    @FXML
    ImageView smallAvatar;
    @FXML
    HBox switchSubBox;
    @FXML
    Label modeLabel;
    @FXML
    FontAwesomeIconView modeIconView;

    JFXButton previousTabButton = dashBoardTabButton;

    SettingsTabView settingsTabView;
    EmployeesTabView employeesTabView;
    CustomersTabView customersTabView;
    ProductsTabView productsTabView;
    OrdersTabView ordersTabView;
    DashboardTabView dashboardTabView;
    ProductLinesTabView productLinesTabView;


    double xOffset;
    double yOffset;

    public void initialSetup() {
        stage.getScene().setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        stage.getScene().setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        user = new Employee(sqlConnection, loggerID);
        settingsTabView.setUser(user);
        employeesTabView.setLoggedInUser(user);
        ViewController.setSqlConnection(sqlConnection);

        usernameLabel.setText(user.getFullName());
        jobTitleLabel.setText(user.getJobTitle());

        previousTabButton = dashBoardTabButton;

        runTask(() -> {
            Image image = ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true);
            if (isImageLoaded(image.getUrl())) {
                smallAvatar.setImage(image);
            } else {
                smallAvatar.setImage(ImageController.getImage("avatar_employee_default.png", true));
            }
        }, () -> avatarLoading.set(false), null, null);
    }

    @FXML
    VBox root;

    private Employee user;
    public static boolean haveJustOpened = false;
    public static int loggerID = -1;


    public AnimationTimer loginDataListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (MainSceneController.loggerID > 0) {
                runTask(() -> {
                    Platform.runLater(() -> {
                        stage.setScene(MainSceneController.this.scene);
                        stage.hide();
                        initialSetup();

                        root.setMinSize(800, 400);
                        stage.show();

                        sideBarBox.setPrefWidth(250);
                        sideBarBox.setMinWidth(95);
                        sideBarBox.setMaxWidth(250);

                        dashBoardTabButton.fire();
                    });
                }, null, null, null);
                stop();
            }
        }
    };

    private boolean close = false;

    public boolean beClosed() {
        return close;
    }

    public void close() {
        close = false;
    }

    BooleanProperty avatarLoading;

    @FXML
    StackPane avatarLayer;

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

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/productlines-tab/productlines-tab-view.fxml"));
            loader.load();
            productLinesTabView = loader.getController();
            productLinesTab.setContent(productLinesTabView.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Prevent tab switching by Ctrl + Tab.
        tabPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.TAB) {
                event.consume();
            }
        });

        avatarLoading = Utils.skeletonEffect(smallAvatar);

        // Time label.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);
                timeLabel.setText(formattedDateTime);
            }
        };
        timer.start();

        // Figure button event.
        dashBoardTabButton.setOnAction(event -> {
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

        productLinesTabButton.setOnMouseClicked(event -> {
            tabSelectingEffect(productLinesTabButton);
            tabPane.getSelectionModel().select(productLinesTab);
            productLinesTabView.show();
        });

        shrinkSideBarButton.setOnMouseClicked(event -> {
            shrink();
        });

        logOutButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Logout");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                close = true;
                // Perform other logout actions here
            }
        });

        switchModeButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                FadeTransition ft = new FadeTransition(Duration.millis(200), root);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.setOnFinished(event -> {
                    root.getStylesheets().remove(1);

                    FadeTransition ft2 = new FadeTransition(Duration.millis(200), root);
                    ft2.setFromValue(0.0);
                    ft2.setToValue(1.0);
                    ft2.play();
                });
                ft.play();
                modeLabel.setText("Dark mode");
                modeIconView.setGlyphName("MOON_ALT");
            } else {
                FadeTransition ft = new FadeTransition(Duration.millis(200), root);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.setOnFinished(event -> {
                    root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/salesmanagement/salesmanagement/css/dark-mode/dark-mode-main-scene.css")).toExternalForm());

                    FadeTransition ft2 = new FadeTransition(Duration.millis(200), root);
                    ft2.setFromValue(0.0);
                    ft2.setToValue(1.0);
                    ft2.play();
                });
                ft.play();
                modeLabel.setText("Light mode");
                modeIconView.setGlyphName("SUN_ALT");
            }
        }));
    }

    @FXML
    VBox sideBarBox;
    List<JFXButton> tabButtons;
    boolean shrinkSideBar = false;

    public void shrink() {
        if (tabButtons == null) {
            tabButtons = new ArrayList<>(Arrays.asList(dashBoardTabButton, employeesTabButton,
                    customersTabButton, ordersTabButton, productsTabButton,
                    settingsTabButton, productLinesTabButton, logOutButton));
        }

        if (!shrinkSideBar) {
            shrinkSideBar = true;

            for (JFXButton button : tabButtons)
                button.getStyleClass().add("shrink-tab-button");
            usernameLabel.getStyleClass().add("shrink-tab-button");
            shrinkSideBarButton.getStyleClass().add("active-shrink-button");

            ((HBox) switchModeButton.getParent()).getChildren().remove(switchSubBox);

            Duration cycleDuration = Duration.millis(300);
            Timeline timeline = new Timeline(
                    new KeyFrame(cycleDuration,
                            new KeyValue(sideBarBox.prefWidthProperty(), 95, Interpolator.EASE_BOTH))
            );
            timeline.play();
        } else {
            shrinkSideBar = false;

            for (JFXButton button : tabButtons)
                button.getStyleClass().remove("shrink-tab-button");
            usernameLabel.getStyleClass().remove("shrink-tab-button");
            shrinkSideBarButton.getStyleClass().remove("active-shrink-button");

            ((HBox) switchModeButton.getParent()).getChildren().add(0, switchSubBox);

            Duration cycleDuration = Duration.millis(300);
            Timeline timeline = new Timeline(
                    new KeyFrame(cycleDuration,
                            new KeyValue(sideBarBox.prefWidthProperty(), 250, Interpolator.EASE_BOTH))
            );

            timeline.play();
        }
    }

    private void tabSelectingEffect(JFXButton selectedTabButton) {
        previousTabButton.getStyleClass().remove("active-tab-button");
        previousTabButton = selectedTabButton;
        previousTabButton.getStyleClass().add("active-tab-button");
    }

    @FXML
    Label timeLabel;
}