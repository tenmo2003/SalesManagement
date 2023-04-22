package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.MailSender;
import salesmanagement.salesmanagement.NotificationCode;
import salesmanagement.salesmanagement.NotificationSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;

public class LoginSceneController extends SceneController implements Initializable {
    @FXML
    CustomTextField username;
    @FXML
    CustomPasswordField password;
    @FXML
    private VBox loginPane;
    @FXML
    private StackPane loginRoot;
    @FXML
    private JFXCheckBox rememberMeCheckBox;

    public VBox getLoginPane() {
        return loginPane;
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
                String query = "select employeeNumber, username, password from employees where username = '" + username + "' and password = '" + password + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                if (resultSet.next()) {
                    if (!rememberMeCheckBox.isSelected()) {
                        saveLoginInfo();
                    }
                    MainSceneController.loggerID = resultSet.getInt("employeeNumber");
                    MainSceneController.haveJustOpened = true;
                    return true;
                }
                return false;
            }
        };

        checkAccountTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                NotificationSystem.throwNotification(NotificationCode.INVALID_LOGIN, stage);
            }
        });

        new Thread(checkAccountTask).start();
        setProgressIndicatorStatus(checkAccountTask, loginPane);
    }

    private void saveLoginInfo() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/salesmanagement/salesmanagement/loginInfo.txt", true));
            writer.write(username.getText() + " " + password.getCharacters().toString() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public void forgotPassword() {
        loginPane.setVisible(false);
        forgotPasswordPane.setVisible(true);
    }

    int securityCode;
    int employeeNumber;

    @FXML
    public void checkLoginInfo() {
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

//    @FXML
//    void changeNextSecurityCodeTextField(KeyEvent event) {
//        int index = securityCodeBox.getChildren().indexOf((Node)event.getSource());
//        securityCodeBox.getChildren().get(index + 1).requestFocus();
//    }

    @FXML
    HBox securityCodeBox;

    @FXML
    public void verifyEmail() {
        runTask(() -> {
            StringBuilder securityCodeInput = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                securityCodeInput.append(((CustomTextField) securityCodeBox.getChildren().get(i)).getText());
            }
            if (Integer.toString(securityCode).equals(securityCodeInput.toString())) {
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
        runTask(() -> {
            String query = "UPDATE employees SET password = " + "'" + passwordReset.getCharacters().toString() + "' WHERE employeeNumber = " + employeeNumber;
            sqlConnection.updateQuery(query);

        }, () -> {
            Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.SUCCEED_RESET_PASSWORD, stage));
            loginPane.setVisible(true);
            resetPasswordPane.setVisible(false);
        }, progressIndicator, resetPasswordPane);
    }


    public void setProgressIndicatorStatus(Task<?> databaseConnectionTask) {
        super.setProgressIndicatorStatus(databaseConnectionTask, loginPane);
    }


    public void setProgressIndicatorStatus(boolean loading) {
        if (loading) {
            showProgressIndicator();
        } else {

        }
    }

    public void showProgressIndicator() {
        super.showProgressIndicator(loginPane);
    }

    public void hideProgressIndicator() {
        super.hideProgressIndicator(loginPane);
    }

    ImageView[] imageViews = new ImageView[5];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle rect = new Rectangle(loginRoot.getPrefWidth(), loginRoot.getPrefHeight());
        rect.setArcHeight(15.0);
        rect.setArcWidth(15.0);
        loginRoot.setClip(rect);

        for (int i = 0; i < securityCodeBox.getChildren().size(); i++) {
            CustomTextField textField = (CustomTextField)securityCodeBox.getChildren().get(i);
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
            });
        }

    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
