package salesmanagement.salesmanagement.ViewController.SettingsTab;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.textfield.CustomTextField;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.*;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.ImageController.isImageLoaded;
import static salesmanagement.salesmanagement.Utils.InputErrorCode.getInputErrorLabel;
import static salesmanagement.salesmanagement.Utils.Utils.shake;
import static salesmanagement.salesmanagement.Utils.Utils.skeletonEffect;

public class SettingsTabView extends TabView implements SettingsTab {
    @FXML
    private ComboBox<String> accessibilityComboBox;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private DatePicker joinngDatePicker;
    @FXML
    private DatePicker lastWorkingDatePicker;
    @FXML
    private TextField officeCodeTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> phoneCodeComboBox;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField supervisorCode;
    @FXML
    private TextField usernameTextField;
    @FXML
    private JFXButton saveInfo;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private VBox codeVerifiedBox;
    @FXML
    private TextField codeVerifiedTextField;
    @FXML
    private Label emailVerifiedLabel;

    private String avatarURI = "";
    private BooleanProperty avatarLoading;
    Employee user;
    AccountActivityLogView accountActivityLogView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/account-activity-log-view.fxml"));
            loader.load();
            accountActivityLogView = loader.getController();
            root.getChildren().add(accountActivityLogView.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addRegexChecker();
        avatarLoading = skeletonEffect(avatarImageView);
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
            avatarImageView.setImage(new Image(selectedFile.toURI().toString()));
            avatarURI = selectedFile.getAbsolutePath();
        }
    }

    @FXML
    void showAccountActivityLog() {
        accountActivityLogView.show();
    }

    @FXML
    void saveInfo() {
        if (!validInput()) {
            NotificationSystem.throwNotification(NotificationCode.INVALID_INPUTS, stage);
            return;
        }
        runTask(() -> {
                    String query = String.format("UPDATE employees SET username = '%s', password = '%s', email = '%s', mailVerified = %d WHERE employeeNumber = %d",
                            usernameTextField.getText(),
                            passwordField.getText(),
                            emailTextField.getText(),
                            (emailVerifiedLabel.getText().toLowerCase().contains("has been verified")) ? 1 : 0,
                            user.getEmployeeNumber());
                    try {
                        sqlConnection.updateQuery(query);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if (avatarImageView.getImage() != null) {
                        ImageController.uploadImage(avatarURI, "avatar_employee_" + user.getEmployeeNumber() + ".png");
                    }
                }, () -> NotificationSystem.throwNotification(NotificationCode.SUCCEED_SAVE_INFO, stage),
                loadingIndicator, null);
    }

    public void setUser(Employee user) {
        this.user = user;
        accountActivityLogView.setUser(user);
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        codeVerifiedBox.setVisible(false);
        runTask(() -> {
            String query = "SELECT * FROM employees WHERE employeeNumber = " + user.getEmployeeNumber();
            ResultSet employeeInfo = sqlConnection.getDataQuery(query);
            Platform.runLater(() -> {
                try {
                    if (employeeInfo.next()) {
                        fullNameTextField.setText(employeeInfo.getString("lastName") + " " + employeeInfo.getString("firstName"));
                        birthDatePicker.setValue(employeeInfo.getDate("birthDate").toLocalDate());
                        officeCodeTextField.setText(employeeInfo.getString("officeCode"));
                        supervisorCode.setText(employeeInfo.getString("reportsTo"));

                        accessibilityComboBox.setValue(employeeInfo.getString("jobTitle"));
                        phoneCodeComboBox.setValue(employeeInfo.getString("phoneCode"));

                        phoneNumberTextField.setText(employeeInfo.getString("phone"));
                        joinngDatePicker.setValue(employeeInfo.getDate("joiningDate").toLocalDate());
                        lastWorkingDatePicker.setValue(employeeInfo.getDate("lastWorkingDate") != null ? employeeInfo.getDate("lastWorkingDate").toLocalDate() : null);

                        usernameTextField.setText(employeeInfo.getString("username"));
                        passwordField.setText(employeeInfo.getString("password"));
                        emailTextField.setText(employeeInfo.getString("email"));
                        if (employeeInfo.getBoolean("mailVerified")) {
                            emailVerifiedLabel.setText("Your e-mail has been verified!");
                            emailVerifiedLabel.setDisable(true);
                        } else {
                            emailVerifiedLabel.setText("Verify your e-mail now!");
                            emailVerifiedLabel.setDisable(false);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }, () -> isShowing = false, loadingIndicator, null);

        runTask(() -> {
            Image image = ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true);
            if (isImageLoaded(image.getUrl())) {
                avatarImageView.setImage(image);
            } else {
                avatarImageView.setImage(ImageController.getImage("avatar_employee_default.png", true));
            }
        }, () -> avatarLoading.set(false), null, null);
    }

    protected boolean validInput() {
        if (!emailTextField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) return false;

        if (!phoneNumberTextField.getText().matches("\\d+")) return false;

        if (usernameTextField.getText().length() > 30 || usernameTextField.getText().length() < 6) {
            return false;
        } else if (!usernameTextField.getText().matches("^[a-zA-Z0-9.]*$")) {
            return false;
        } else {
            String query = "SELECT username FROM employees WHERE username = '" + usernameTextField.getText() + "'";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                if (resultSet.next() && user.isNewUser()) {
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return passwordField.getCharacters().toString().length() >= 8;
    }

    protected void addRegexChecker() {
        usernameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) usernameTextField.getParent().getParent();
                FontAwesomeIconView validIconView = (FontAwesomeIconView) ((HBox) usernameTextField.getParent()).getChildren().get(1);
                validIconView.getStyleClass().clear();
                if (usernameTextField.getText().length() > 30 || usernameTextField.getText().length() < 6) {
                    usernameTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_LENGTH_USERNAME));
                    validIconView.getStyleClass().add("invalid-icon-view");
                    shake(usernameTextField);
                } else if (!usernameTextField.getText().matches("^[a-zA-Z0-9.]*$")) {
                    usernameTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_USERNAME));
                    validIconView.getStyleClass().add("invalid-icon-view");
                    shake(usernameTextField);
                } else {
                    String query = "SELECT username FROM employees WHERE username = '" + usernameTextField.getText() + "'";
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    try {
                        if (resultSet.next()) {
                            if (!user.getUsername().equals(resultSet.getString("username"))) {
                                usernameTextField.setStyle("-fx-border-color: #f35050");
                                if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                                    container.getChildren().remove(container.getChildren().size() - 1);
                                }
                                container.getChildren().add(getInputErrorLabel(InputErrorCode.EXISTED_USERNAME));
                                validIconView.getStyleClass().add("invalid-icon-view");
                                shake(usernameTextField);
                            }
                        } else {
                            usernameTextField.setStyle("-fx-border-color: transparent");
                            if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                                container.getChildren().remove(container.getChildren().size() - 1);
                            }
                            validIconView.getStyleClass().add("valid-icon-view");

                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) passwordField.getParent().getParent();
                FontAwesomeIconView validIconView = (FontAwesomeIconView) ((HBox) passwordField.getParent()).getChildren().get(1);
                validIconView.getStyleClass().clear();

                if (passwordField.getCharacters().toString().length() < 8) {
                    passwordField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_LENGTH_PASSWORD));
                    validIconView.getStyleClass().add("invalid-icon-view");
                    shake(passwordField);
                } else {
                    passwordField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                        validIconView.getStyleClass().add("valid-icon-view");
                    }
                }
            }
        });

        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) emailTextField.getParent().getParent();
                FontAwesomeIconView validIconView = (FontAwesomeIconView) ((HBox) emailTextField.getParent()).getChildren().get(1);
                validIconView.getStyleClass().clear();

                if (!emailTextField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    emailTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_EMAIL));
                    }
                    emailVerifiedLabel.setDisable(true);
                    validIconView.getStyleClass().add("invalid-icon-view");
                    shake(emailTextField);
                } else {
                    emailTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                        validIconView.getStyleClass().add("valid-icon-view");
                    }
                    if (!emailTextField.getText().equals(user.getEmail())) {
                        emailVerifiedLabel.setText("Verify your e-mail now!");
                        emailVerifiedLabel.setDisable(false);
                    }
                }
            }
        });
    }

    @FXML
    private void verifyEmail() {
        if (String.valueOf(securityCode).equals(codeVerifiedTextField.getText())) {
            codeVerifiedBox.setVisible(false);
            emailVerifiedLabel.setText("Your e-mail has been verified!");
            emailVerifiedLabel.setDisable(true);
        }
    }

    private int securityCode = -1;

    @FXML
    private void sendEmailCodeVerified() {
        runTask(() -> {
            securityCode = MailSender.sendVerifyMail(emailTextField.getText());
        }, () -> {
            codeVerifiedBox.setVisible(true);
            emailVerifiedLabel.setDisable(true);
        }, loadingIndicator, null);
    }
}
