package salesmanagement.salesmanagement.ViewController;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.SalesComponent.Employee;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static salesmanagement.salesmanagement.ImageController.isImageLoaded;
import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class EmployeeInfoViewController extends ViewController {

    @FXML
    private ComboBox<String> accessibilityBox;

    @FXML
    private ImageView avatar;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private VBox detailsInfoBox;

    @FXML
    private JFXButton editInfoButton;

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
    private ComboBox<String> phoneCodeBox;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private JFXButton saveInfoButton;

    @FXML
    private JFXButton saveNewEmployeeButton;

    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private TextField supervisorTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private JFXButton verifyButton;
    private Employee user;
    @FXML
    private StackPane loadingAvatar;

    public void setUser(Employee user) {
        this.user = user;
    }

    @FXML
    void downloadResume(MouseEvent event) {

    }

    @FXML
    void downloadSalaryReport(MouseEvent event) {

    }

    @FXML
    void downloadSalesReport(MouseEvent event) {

    }


    @FXML
    void uploadResume(MouseEvent event) {

    }
    BooleanProperty avatarLoading;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

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

        List<String> accessibilitiesList = new ArrayList<>(Arrays.asList("HR", "Manager", "Employee", "Admin"));
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
    }

    @FXML
    void editEmployeeInfo(MouseEvent event) {

    }

    @FXML
    void saveEmployeeInfo(MouseEvent event) {

    }

    @FXML
    void saveNewEmployee(MouseEvent event) {

    }

    @FXML
    void uploadAvatar(MouseEvent event) {

    }

    @Override
    public void show() {
        if (user != null)
            show(user);
        else show(new Employee());
    }

    protected void show(Employee employee) {
        super.show();
        //TODO: upload default avatar to ftp server and download.

        int userCode = employee.getEmployeeNumber();
        if (employee.isNewUser()) {
            user = employee;
            employeeCodeTextField.setText("");
            supervisorTextField.setText("");
            fillUpInfo();
        } else {
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
        }, ()->avatarLoading.set(false), loadingIndicator, null);
    }
}
