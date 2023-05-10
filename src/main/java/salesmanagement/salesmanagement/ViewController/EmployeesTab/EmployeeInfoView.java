package salesmanagement.salesmanagement.ViewController.EmployeesTab;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import salesmanagement.salesmanagement.SalesComponent.Action;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.ViewController.UserRight;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.InputErrorCode;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.SettingsTab.AccountActivityLogView;
import salesmanagement.salesmanagement.ViewController.ViewController;

import javax.swing.plaf.SeparatorUI;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.ImageController.isImageLoaded;
import static salesmanagement.salesmanagement.Utils.InputErrorCode.getInputErrorLabel;
import static salesmanagement.salesmanagement.Utils.Utils.shake;

public class EmployeeInfoView extends ViewController implements EmployeesTab {
    @FXML
    private ComboBox<String> accessibilityBox;
    @FXML
    private ImageView avatar;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private JFXButton editButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField employeeCodeTextField;
    @FXML
    private JFXRadioButton femaleRadioButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private DatePicker joiningDatePicker;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private DatePicker lastWorkingDatePicker;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private JFXRadioButton maleRadioButton;
    @FXML
    private TextField officeCodeTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private JFXButton uploadAvatarButton;
    @FXML
    private ComboBox<String> phoneCodeBox;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton addButton;
    @FXML
    private ComboBox<String> statusBox;
    @FXML
    private TextField supervisorTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private StackPane loadingAvatar;
    @FXML
    private JFXButton showAccountLogActivityButton;

    private Employee user;
    private Employee loggedInUser;

    private AccountActivityLogView accountActivityLogView;

    public EmployeeInfoView setUser(Employee user) {
        this.user = user;
        return this;
    }

    public void setLoggedInUser(Employee loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @FXML
    void downloadResume() {

    }

    @FXML
    void downloadSalaryReport() {

    }

    @FXML
    void downloadSalesReport() {

    }


    @FXML
    void uploadResume() {

    }

    BooleanProperty avatarLoading;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/account-activity-log-view.fxml"));
            loader.load();
            accountActivityLogView = loader.getController();
            root.getChildren().add(accountActivityLogView.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> phoneCodes = new ArrayList<>();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        for (String regionCode : phoneUtil.getSupportedRegions()) {
            Phonenumber.PhoneNumber exampleNumber = phoneUtil.getExampleNumber(regionCode);
            if (exampleNumber != null) {
                int countryCode = exampleNumber.getCountryCode();
                phoneCodes.add(String.format("+%d (%s)", countryCode, phoneUtil.getRegionCodeForCountryCode(countryCode)));
            }
        }
        phoneCodeBox.setItems(FXCollections.observableArrayList(phoneCodes));

        List<String> accessibilitiesList = new ArrayList<>(Arrays.asList("HR", "Manager", "Employee"));
        accessibilityBox.setItems(FXCollections.observableArrayList(accessibilitiesList));

        List<String> statusList = new ArrayList<>(Arrays.asList("ACTIVE", "INACTIVE"));
        statusBox.setItems(FXCollections.observableArrayList(statusList));

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(femaleRadioButton, maleRadioButton);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == femaleRadioButton) {
                maleRadioButton.setSelected(false);
            } else if (newValue == maleRadioButton) {
                femaleRadioButton.setSelected(false);
            }
        });

        avatarLoading = new javafx.beans.property.SimpleBooleanProperty(true);
        loadingAvatar.setStyle("-fx-background-color: grey;-fx-background-radius: 10;");
        FadeTransition imageFadeTransition = new FadeTransition(Duration.seconds(2), loadingAvatar);
        imageFadeTransition.setFromValue(0.2);
        imageFadeTransition.setToValue(0.4);
        imageFadeTransition.setCycleCount(Timeline.INDEFINITE);
        imageFadeTransition.setAutoReverse(true);
        imageFadeTransition.play();

        ChangeListener<Boolean> imageLoadingListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                loadingAvatar.setStyle("-fx-background-color: grey;-fx-background-radius: 10;");
                imageFadeTransition.play();
            } else {
                imageFadeTransition.pause();
                loadingAvatar.setStyle("-fx-background-color: transparent");
            }
        };
        avatarLoading.addListener(imageLoadingListener);

        //region Check regex for user's input.
        firstNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) firstNameTextField.getParent();
                if (firstNameTextField.getText().equals("")) {
                    firstNameTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.EMPTY_FIRST_NAME));
                    }
                    shake(firstNameTextField);
                } else {
                    firstNameTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        lastNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) lastNameTextField.getParent();
                if (lastNameTextField.getText().equals("")) {
                    lastNameTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.EMPTY_LAST_NAME));
                    }
                    shake(lastNameTextField);
                } else {
                    lastNameTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) emailTextField.getParent().getParent();
                if (!emailTextField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    emailTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_EMAIL));
                    }
                    shake(emailTextField);
                } else {
                    emailTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        birthDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) birthDatePicker.getParent();
                if (birthDatePicker.getValue() == null) {
                    birthDatePicker.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.EMPTY_DATE));
                    }
                    shake(birthDatePicker);
                } else {
                    birthDatePicker.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        joiningDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) joiningDatePicker.getParent();
                if (joiningDatePicker.getValue() == null) {
                    joiningDatePicker.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.EMPTY_DATE));
                    }
                    shake(joiningDatePicker);
                } else {
                    joiningDatePicker.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        lastWorkingDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) lastWorkingDatePicker.getParent();
                if (lastWorkingDatePicker.getValue() == null) {
                    lastWorkingDatePicker.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.EMPTY_DATE));
                    }
                    shake(lastWorkingDatePicker);
                } else {
                    lastWorkingDatePicker.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        officeCodeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) officeCodeTextField.getParent().getParent();
                String query = "SELECT officeCode FROM offices WHERE officeCode = '" + officeCodeTextField.getText() + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    if (!resultSet.next()) {
                        officeCodeTextField.setStyle("-fx-border-color: #f35050");
                        if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_OFFICE_CODE));
                        }
                        shake(officeCodeTextField);
                    } else {
                        officeCodeTextField.setStyle("-fx-border-color: transparent");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        supervisorTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) supervisorTextField.getParent().getParent();
                int employeeCode = 0;
                try {
                    employeeCode = Integer.parseInt(supervisorTextField.getText());
                } catch (Exception e) {
                    employeeCode = -1;
                }
                String query = "SELECT employeeNumber FROM employees WHERE employeeNumber = " + employeeCode;
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    if (!resultSet.next()) {
                        supervisorTextField.setStyle("-fx-border-color: #f35050");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_SUPERVISOR_CODE));
                        shake(supervisorTextField);
                    } else {
                        supervisorTextField.setStyle("-fx-border-color: transparent");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        phoneNumberTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) phoneNumberTextField.getParent();
                if (!phoneNumberTextField.getText().matches("\\d+")) {
                    phoneNumberTextField.setStyle("-fx-border-color: #f35050");
                    if (!(container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_PHONE_NUMBER));
                    }
                    shake(phoneNumberTextField);
                } else {
                    phoneNumberTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
        usernameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) usernameTextField.getParent();
                if (usernameTextField.getText().length() > 30 || usernameTextField.getText().length() < 6) {
                    usernameTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_LENGTH_USERNAME));
                    shake(usernameTextField);
                } else if (!usernameTextField.getText().matches("^[a-zA-Z0-9.]*$")) {
                    usernameTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_USERNAME));
                    shake(usernameTextField);
                } else {
                    String query = "SELECT username FROM employees WHERE username = '" + usernameTextField.getText() + "'";
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    try {
                        if (resultSet.next() && user.isNewUser()) {
                            usernameTextField.setStyle("-fx-border-color: #f35050");
                            if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                                container.getChildren().remove(container.getChildren().size() - 1);
                            }
                            container.getChildren().add(getInputErrorLabel(InputErrorCode.EXISTED_USERNAME));
                            shake(usernameTextField);
                        } else {
                            usernameTextField.setStyle("-fx-border-color: transparent");
                            if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                                container.getChildren().remove(container.getChildren().size() - 1);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) passwordField.getParent();
                if (passwordField.getCharacters().toString().length() < 8) {
                    passwordField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_LENGTH_PASSWORD));
                    shake(passwordField);
                } else {
                    passwordField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                }
            }
        });
    }

    @FXML
    public void editEmployeeInfo() {
        if (!loggedInUser.getJobTitle().equals("Employee")) {
            // UNLOCK ALL
            disableNodes(false);
            editButton.setVisible(false);
            saveButton.setVisible(true);
        } else {
            NotificationSystem.throwNotification(NotificationCode.NOT_AUTHORIZED, stage);
        }
    }

    @FXML
    public void saveEmployeeInfo() {
        if (!checkValidEmployeeInput()) {
            NotificationSystem.throwNotification(NotificationCode.INVALID_INPUTS, stage);
            return;
        }

        disableNodes(true);
        editButton.setVisible(true);
        saveButton.setVisible(false);

        // Save DATA To DB
        runTask(() -> {
            String query = "UPDATE employees SET lastName='" + lastNameTextField.getText() + "', firstName='" + firstNameTextField.getText()
                    + "', birthDate='" + birthDatePicker.getValue() + "', gender='" + (maleRadioButton.isSelected() ? "male" : "female")
                    + "', email='" + emailTextField.getText() + "', mailVerified='0', officeCode='" + officeCodeTextField.getText()
                    + "', reportsTo=" + ((supervisorTextField.getText().equals("")) ? "null" : supervisorTextField.getText()) + ", jobTitle='" + accessibilityBox.getValue()
                    + "', username='" + usernameTextField.getText() + "', password='" + passwordField.getText() + "', phoneCode='"
                    + phoneCodeBox.getValue() + "', phone='" + phoneNumberTextField.getText() + "', status='" + statusBox.getValue() + "', joiningDate='"
                    + joiningDatePicker.getValue() + "' WHERE employeeNumber=" + user.getEmployeeNumber();

            sqlConnection.updateQuery(query, employeeCodeTextField.getText(),
                    Action.ComponentModified.EMPLOYEE, Action.ActionCode.EDIT);

            if (avatar.getImage() != null)
                if (!avatar.getImage().getUrl().toLowerCase().equals(ImageController.getURL("avatar_employee_default.png")))
                    ImageController.uploadImage(avatarURI, "avatar_employee_" + user.getEmployeeNumber() + ".png");
        }, () -> {
            show(user);
        }, loadingIndicator, employeeInfoBox);
    }

    @FXML
    ScrollPane employeeInfoBox;

    @FXML
    public void addNewEmployee() {
        AtomicBoolean allValid = new AtomicBoolean(false);
        runTask(() -> {
            allValid.set(checkValidEmployeeInput());
            if (!allValid.get()) {
                Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.INVALID_INPUTS, stage));
            } else {
                String lastWorking = lastWorkingDatePicker.getValue() != null ? "'" + lastWorkingDatePicker.getValue() + "'" : "null";
                String query = "INSERT INTO `employees` (`employeeNumber`, `lastName`, `firstName`, `birthDate`, `gender`, `email`, `mailVerified`, " +
                        "`officeCode`, `reportsTo`, `jobTitle`, `username`, `password`, `phoneCode`, `phone`, `status`, `joiningDate`, `lastWorkingDate`) " +
                        "VALUES (NULL,'" + lastNameTextField.getText() + "', '" + firstNameTextField.getText() + "', '" + birthDatePicker.getValue()
                        + "','" + (maleRadioButton.isSelected() ? "male" : "female") + "', '" + emailTextField.getText() + "', '0', " +
                        "'" + officeCodeTextField.getText() + "'," + supervisorTextField.getText() + ", '" + accessibilityBox.getValue() + "', '"
                        + usernameTextField.getText() + "', '" + passwordField.getText() + "', '" + phoneCodeBox.getValue()
                        + "', '" + phoneNumberTextField.getText() + "', 'ACTIVE', '" + joiningDatePicker.getValue() + "', "
                        + lastWorking + ")";

                Action action;
                try {
                    sqlConnection.updateQuery(query);
                    action = new Action(null,
                            Action.ComponentModified.EMPLOYEE,
                            Action.ActionCode.CREATE_NEW,
                            Action.ResultCode.SUCCESSFUL);

                    query = "SELECT * FROM employees WHERE username = '" + usernameTextField.getText() + "'";
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    try {
                        if (resultSet.next()) {
                            user = new Employee(resultSet);
                            action.setComponentModifiedID(String.valueOf(user.getEmployeeNumber()));
                            if (!avatar.getImage().getUrl().toLowerCase().equals(ImageController.getURL("avatar_employee_default.png")))
                                ImageController.uploadImage(avatarURI, "avatar_employee_" + resultSet.getInt("employeeNumber") + ".png");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    action = new Action(null,
                            Action.ComponentModified.EMPLOYEE,
                            Action.ActionCode.CREATE_NEW,
                            Action.ResultCode.FAILED);
                }
                action.pushAction(sqlConnection);
            }
        }, () -> {
            if (allValid.get()) {
                show(user);
                NotificationSystem.throwNotification(NotificationCode.SUCCEED_ADD_NEW_EMPLOYEE, stage);
            }
        }, loadingIndicator, employeeInfoBox);
    }

    private boolean checkValidEmployeeInput() {
        if (firstNameTextField.getText().equals("")) return false;

        if (lastNameTextField.getText().equals("")) return false;

        if (!emailTextField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) return false;

        if (birthDatePicker.getValue() == null) return false;

        if (joiningDatePicker.getValue() == null) return false;

        String query = "SELECT officeCode FROM offices WHERE officeCode = '" + officeCodeTextField.getText() + "'";
        ResultSet resultSet = sqlConnection.getDataQuery(query);
        try {
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            int employeeCode = Integer.parseInt(supervisorTextField.getText());
            query = "SELECT employeeNumber FROM employees WHERE employeeNumber = " + employeeCode;
            resultSet = sqlConnection.getDataQuery(query);
            try {
                if (!resultSet.next())
                    return false;
            } catch (SQLException e) {
                return false;
            }
        } catch (Exception ignored) {

        }

        if (!phoneNumberTextField.getText().matches("\\d+")) return false;

        if (usernameTextField.getText().length() > 30 || usernameTextField.getText().length() < 6) {
            return false;
        } else if (!usernameTextField.getText().matches("^[a-zA-Z0-9.]*$")) {
            return false;
        } else {
            query = "SELECT username FROM employees WHERE username = '" + usernameTextField.getText() + "'";
            resultSet = sqlConnection.getDataQuery(query);
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

    String avatarURI = "";

    @FXML
    void uploadAvatar(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            avatar.setImage(new Image(selectedFile.toURI().toString()));
            avatarURI = selectedFile.getAbsolutePath();
        }
    }

    Node[] disabledNodes;

    private void disableNodes(boolean flag) {
        for (Node node : disabledNodes) {
            node.setDisable(flag);
        }
    }

    @Override
    public void show() {
        if (user != null)
            show(user);
        else show(new Employee());
    }

    protected void show(Employee employee) {
        employeeInfoBox.setVvalue(0.0);

        if (disabledNodes == null) {
            disabledNodes = new Node[]{lastNameTextField, firstNameTextField, birthDatePicker, maleRadioButton, femaleRadioButton, emailTextField,
                    officeCodeTextField, supervisorTextField, accessibilityBox, usernameTextField, passwordField, phoneCodeBox,
                    phoneNumberTextField, joiningDatePicker, statusBox, uploadAvatarButton};
        }

        resetData();

        super.show();

        int userCode = employee.getEmployeeNumber();
        if (employee.isNewUser()) {
            user = employee;

            addButton.setVisible(true);
            showAccountLogActivityButton.setVisible(false);
            disableNodes(false);

            employeeCodeTextField.setText("");
            supervisorTextField.setText("");
            fillUpInfo();
            joiningDatePicker.setValue(LocalDate.now());
        } else {
            addButton.setVisible(false);
            saveButton.setVisible(false);
            editButton.setVisible(true);
            showAccountLogActivityButton.setVisible(true);

            disableNodes(true);

            runTask(() -> {
                String query = "select * from employees where employeeNumber = " + userCode;
                try {
                    ResultSet employeeInfo = sqlConnection.getDataQuery(query);
                    if (employeeInfo.next())
                        user = new Employee(employeeInfo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                employeeCodeTextField.setText(Integer.toString(employee.getEmployeeNumber()));
            }, this::fillUpInfo, loadingIndicator, null);
        }
    }

    @FXML
    public void showAccountActivityLog() {
        accountActivityLogView.setUser(user).show();
    }

    @Override
    public void close() {
        super.close();
        editButton.setVisible(false);
        saveButton.setVisible(false);
        addButton.setVisible(false);
    }

    private void fillUpInfo() {
        if (user.getReportsTo() > 0) supervisorTextField.setText(Integer.toString(user.getReportsTo()));
        else supervisorTextField.setText("");

        lastNameTextField.setText(user.getLastName());
        firstNameTextField.setText(user.getFirstName());
        usernameTextField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        birthDatePicker.setValue(user.getBirthDate());
        emailTextField.setText(user.getEmail());
        phoneCodeBox.setValue(user.getPhoneCode());
        joiningDatePicker.setValue(user.getJoiningDate());
        officeCodeTextField.setText(user.getOfficeCode());
        lastWorkingDatePicker.setValue(user.getLastWorkingDate());
        phoneNumberTextField.setText(user.getPhone());
        statusBox.setValue(user.getStatus());
        accessibilityBox.setValue(user.getJobTitle());
        if (user.getGender() == null) {
            maleRadioButton.setSelected(false);
            femaleRadioButton.setSelected(false);
        }
        if (Objects.equals(user.getGender(), "male")) {
            maleRadioButton.setSelected(true);
            femaleRadioButton.setSelected(false);
        } else if (Objects.equals(user.getGender(), "female")) {
            femaleRadioButton.setSelected(true);
            maleRadioButton.setSelected(false);
        } else {
            femaleRadioButton.setSelected(false);
            maleRadioButton.setSelected(false);
        }
        avatar.setImage(null);
        avatarLoading.set(true);
        runTask(() -> {
            Image image = ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true);
            if (isImageLoaded(image.getUrl())) {
                avatar.setImage(image);
            } else {
                avatar.setImage(ImageController.getImage("avatar_employee_default.png", true));
            }
        }, () -> avatarLoading.set(false), loadingIndicator, null);
    }

    protected void resetData() {
        super.resetData();

        VBox container = (VBox) firstNameTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        firstNameTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) lastNameTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        lastNameTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) emailTextField.getParent().getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        emailTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) supervisorTextField.getParent().getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        supervisorTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) officeCodeTextField.getParent().getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        officeCodeTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) phoneNumberTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        phoneNumberTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) lastWorkingDatePicker.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        lastWorkingDatePicker.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) birthDatePicker.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        birthDatePicker.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) joiningDatePicker.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        joiningDatePicker.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) usernameTextField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        usernameTextField.setStyle("-fx-border-color: #d1d1d1");

        container = (VBox) passwordField.getParent();
        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        passwordField.setStyle("-fx-border-color: #d1d1d1");
    }

    private void disableModification() {

    }
}
