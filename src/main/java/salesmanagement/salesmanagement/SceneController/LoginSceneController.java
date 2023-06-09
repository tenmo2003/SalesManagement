package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import salesmanagement.salesmanagement.Utils.*;
import salesmanagement.salesmanagement.ViewController.InputValidator;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static salesmanagement.salesmanagement.Utils.InputErrorCode.getInputErrorLabel;
import static salesmanagement.salesmanagement.Utils.Utils.shake;

public class LoginSceneController extends SceneController implements Initializable, InputValidator {
    @FXML
    CustomTextField username;
    @FXML
    CustomPasswordField password;
    @FXML
    private VBox loginPane;
    @FXML
    private StackPane loginRoot;
    @FXML
    VBox forgotPasswordPane;
    @FXML
    VBox resetPasswordPane;
    @FXML
    VBox emailVerifyPane;
    @FXML
    CustomTextField usernameForgot;
    @FXML
    CustomTextField emailForgot;
    @FXML
    CustomPasswordField passwordReset;
    @FXML
    private HBox securityCodeBox;

    @FXML
    JFXButton resendButton;

    private int securityCode;
    private int employeeNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle rect = new Rectangle(loginRoot.getPrefWidth(), loginRoot.getPrefHeight());
        rect.setArcHeight(15.0);
        rect.setArcWidth(15.0);
        loginRoot.setClip(rect);

        for (int i = 0; i < securityCodeBox.getChildren().size(); i++) {
            CustomTextField textField = (CustomTextField) securityCodeBox.getChildren().get(i);
            int finalI = i;
            textField.setOnMouseClicked(event -> {
                textField.selectAll();
            });
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*") || newValue.length() > 1) {
                    textField.setText(oldValue);
                } else if (newValue.length() == 1 && finalI < securityCodeBox.getChildren().size() - 1) {
                    securityCodeBox.getChildren().get(finalI + 1).requestFocus();
                }
                if (newValue.length() == 0 && finalI > 0) {
                    securityCodeBox.getChildren().get(finalI - 1).requestFocus();
                }
            });
        }

        resendButton.setOnAction(event -> {
            runTask(() -> {
                securityCode = MailSender.sendResetPasswordMail(emailForgot.getText());
            }, () -> {
                // Disable the button
                resendButton.setDisable(true);

                // Set the initial text
                resendButton.setText("Resend");

                // Create a timer that will run for 20 seconds
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    int secondsLeft = 20;

                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            // Update the text of the button with the remaining time
                            resendButton.setText("Resend(" + secondsLeft + "s)");
                            secondsLeft--;

                            if (secondsLeft < 0) {
                                // Re-enable the button and set the text back to "Resend"
                                resendButton.setDisable(false);
                                resendButton.setText("Resend");

                                // Cancel the timer
                                timer.cancel();
                            }
                        });
                    }
                }, 0, 1000); // Start the timer immediately and run it every 1000 milliseconds (1 second)
            }, progressIndicator, emailVerifyPane);


        });

        FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
        userIcon.setFill(Color.WHITE);
        FontAwesomeIconView keyIcon = new FontAwesomeIconView(FontAwesomeIcon.KEY);
        keyIcon.setFill(Color.WHITE);

        StackPane userIconContainer = new StackPane(userIcon);
        userIconContainer.setAlignment(Pos.CENTER);
        userIconContainer.setPrefSize(30, username.getPrefHeight());

        StackPane keyIconContainer = new StackPane(keyIcon);
        keyIconContainer.setAlignment(Pos.CENTER);
        keyIconContainer.setPrefSize(30, password.getPrefHeight());

        username.setLeft(userIconContainer);
        password.setLeft(keyIconContainer);

        addRegexChecker();
    }

    @FXML
    public void checkAccount(Event event) {
        if (event instanceof KeyEvent)
            if (((KeyEvent) event).getCode() != KeyCode.ENTER) return;
        String password = this.password.getCharacters().toString();
        String username = this.username.getText();

        Task<Boolean> checkAccountTask = new Task<>() {
            @Override
            protected Boolean call() throws SQLException {
                String query = "SELECT employeeNumber, username, password, jobTitle FROM employees WHERE username = '" + username + "' AND password = '" + password + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                if (resultSet.next()) {
                    MainSceneController.loggerID = resultSet.getInt("employeeNumber");
                    sqlConnection.setUserID(resultSet.getInt("employeeNumber"));
                    ViewController.setUserRight(resultSet.getString("jobTitle"));
                    MainSceneController.haveJustOpened = true;
                    return true;
                }
                return false;
            }
        };

        checkAccountTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                NotificationSystem.throwNotification(NotificationCode.INVALID_LOGIN, getStage());
            }
        });

        new Thread(checkAccountTask).start();
        setProgressIndicatorStatus(checkAccountTask, loginPane);
    }


    @FXML
    public void forgotPassword() {
        loginPane.setVisible(false);
        forgotPasswordPane.setVisible(true);
    }

    @FXML
    public void checkLoginInfo(Event event) {
        if (event instanceof KeyEvent) {
            if (((KeyEvent) event).getCode() != KeyCode.ENTER) {
                return;
            }
        }
        runTask(() -> {
            String query = "select employeeNumber, email from employees where username = '" + usernameForgot.getText() + "' and email = '" + emailForgot.getText() + "'";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                if (!resultSet.next()) {
                    Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.INVALID_LOGIN_INFO, stage));
                } else {
                    employeeNumber = resultSet.getInt("employeeNumber");
                    securityCode = MailSender.sendResetPasswordMail(resultSet.getString("email"));
                    emailVerifyPane.setVisible(true);
                    forgotPasswordPane.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, null, progressIndicator, forgotPasswordPane);
    }

    @FXML
    public void backToLoginPane() {
        loginPane.setVisible(true);
        forgotPasswordPane.setVisible(false);
        resetPasswordPane.setVisible(false);
        emailVerifyPane.setVisible(false);

    }

    @FXML
    public void verifyEmail() {
        runTask(() -> {
            StringBuilder securityCodeInput = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                securityCodeInput.append(((CustomTextField) securityCodeBox.getChildren().get(i)).getText());
            }
            if (Integer.toString(securityCode).contentEquals(securityCodeInput)) {
                emailVerifyPane.setVisible(false);
                resetPasswordPane.setVisible(true);
                Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.SUCCEED_VERIFY_MAIL, stage));
            } else {
                Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.INVALID_SECURITY_CODE, stage));
            }
        }, null, progressIndicator, emailVerifyPane);
    }

    @FXML
    public void resetPassword() {
        if (!validInput()) {
            NotificationSystem.throwNotification(NotificationCode.INVALID_INPUTS, stage);
            return;
        }
        runTask(() -> {
            String query = "UPDATE employees SET password = " + "'" + passwordReset.getCharacters().toString() + "' WHERE employeeNumber = " + employeeNumber;
            try {
                sqlConnection.updateQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.SUCCEED_RESET_PASSWORD, stage));
            loginPane.setVisible(true);
            resetPasswordPane.setVisible(false);
        }, progressIndicator, resetPasswordPane);
    }

    @Override
    public void addRegexChecker() {
        passwordReset.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) passwordReset.getParent();
                if (passwordReset.getCharacters().toString().length() < 8) {
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_LENGTH_PASSWORD));
                    shake(passwordReset);
                } else {
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
    }

    @Override
    public boolean validInput() {
        return passwordReset.getCharacters().toString().length() >= 8;
    }

    @Override
    public void removeInvalidAlert() {
        VBox container = (VBox) passwordReset.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
    }

    public VBox getLoginPane() {
        return loginPane;
    }

    public void resetData() {
        MainSceneController.loggerID = -1;
        for (Node node : Utils.getAllNodes(loginRoot)) {
            if (node instanceof TextField) ((TextField) node).setText("");
        }
    }
}
