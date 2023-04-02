package salesmanagement.salesmanagement;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import salesmanagement.salesmanagement.SalesComponent.Employee;

import java.util.Arrays;

public class EmployeeForm extends Form {
    final private Label employeeNumber = new Label("Employee Number:");
    final private Label lastName = new Label("Last Name:");
    final private Label firstName = new Label("First Name:");
    final private Label email = new Label("Email:");
    final private Label officeCode = new Label("Office Code:");
    final private Label reportsTo = new Label("Reports To:");
    final private Label jobTitle = new Label("Job Title:");
    final private JFXTextField employeeNumberTextField = new JFXTextField();
    final private JFXTextField lastNameTextField = new JFXTextField();
    final private JFXTextField firstNameTextField = new JFXTextField();
    final private JFXTextField emailTextField = new JFXTextField();
    final private JFXTextField officeCodeTextField = new JFXTextField();
    final private JFXTextField reportsToTextField = new JFXTextField();
    final private JFXComboBox<String> jobTitleComboBox = new JFXComboBox<>();
    private ImageView avatar;

    public void fillInForm(Object e) {
        Employee employee = (Employee) e;
        employeeNumberTextField.setText(Integer.toString(employee.getEmployeeNumber()));
        employeeNumberTextField.setEditable(false);
        lastNameTextField.setText(employee.getLastName());
        firstNameTextField.setText(employee.getFirstName());
        emailTextField.setText(employee.getEmail());
        officeCodeTextField.setText(employee.getOfficeCode());
        reportsToTextField.setText(Integer.toString(employee.getReportsTo()));
        jobTitleComboBox.setValue(employee.getJobTitle());
        if (employee.getAvatar().getImage() != null) avatar.setImage(employee.getAvatar().getImage());
    }

    public EmployeeForm(StackPane container) {
        super(container);
        Label heading = new Label("Employee Information");
        // heading.setFont(new Font(16));
        heading.setStyle("-fx-font-size: 16; -fx-font-weight: bold");
        heading.setPrefSize(200, 30);
        heading.setAlignment(Pos.CENTER);
        HBox headerContainer = new HBox(heading);
        headerContainer.setAlignment(Pos.CENTER);
        content.setHeading(headerContainer);

        HBox form = new HBox();
        form.setAlignment(Pos.CENTER);
        form.setSpacing(30);
        VBox mainForm = new VBox();
        mainForm.setMinWidth(400);
        mainForm.setSpacing(14);
        HBox[] formLineList = new HBox[7];
        for (int i = 0; i < 7; i++) {
            formLineList[i] = new HBox();
            mainForm.getChildren().add(formLineList[i]);
            formLineList[i].setAlignment(Pos.CENTER_RIGHT);
            formLineList[i].setSpacing(10);
        }

        int fieldLen = 250;
        int fontSize = 16;

        employeeNumberTextField.setPrefWidth(fieldLen);
        employeeNumberTextField.setAlignment(Pos.CENTER);
        employeeNumberTextField.setFont(new Font(fontSize));
        employeeNumberTextField.setStyle("-fx-font-weight: bold;");

        firstNameTextField.setPrefWidth(fieldLen);
        firstNameTextField.setAlignment(Pos.CENTER);
        firstNameTextField.setFont(new Font(fontSize));

        lastNameTextField.setPrefWidth(fieldLen);
        lastNameTextField.setAlignment(Pos.CENTER);
        lastNameTextField.setFont(new Font(fontSize));

        emailTextField.setPrefWidth(fieldLen);
        emailTextField.setAlignment(Pos.CENTER);
        emailTextField.setFont(new Font(fontSize));

        officeCodeTextField.setPrefWidth(fieldLen);
        officeCodeTextField.setAlignment(Pos.CENTER);
        officeCodeTextField.setFont(new Font(fontSize));

        jobTitleComboBox.getItems().addAll(Arrays.asList("EMPLOYEE", "ADMIN", "MANAGER"));
        jobTitleComboBox.setPrefWidth(fieldLen);
        jobTitleComboBox.setStyle("-fx-font-size: 16px;");
        jobTitleComboBox.setButtonCell(new ListCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER_RIGHT);
                    Insets old = getPadding();
                    setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                }
            }
        });
        jobTitleComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            setAlignment(Pos.CENTER_RIGHT);
                            setPadding(new Insets(3, 3, 3, 0));
                        }
                    }
                };
            }
        });
        reportsToTextField.setPrefWidth(fieldLen);
        reportsToTextField.setAlignment(Pos.CENTER);
        reportsToTextField.setFont(new Font(fontSize));

        formLineList[0].getChildren().addAll(Arrays.asList(employeeNumber, employeeNumberTextField));
        formLineList[1].getChildren().addAll(Arrays.asList(lastName, lastNameTextField));
        formLineList[2].getChildren().addAll(Arrays.asList(firstName, firstNameTextField));
        formLineList[3].getChildren().addAll(Arrays.asList(email, emailTextField));
        formLineList[4].getChildren().addAll(Arrays.asList(reportsTo, reportsToTextField));
        formLineList[5].getChildren().addAll(Arrays.asList(officeCode, officeCodeTextField));
        formLineList[6].getChildren().addAll(Arrays.asList(jobTitle, jobTitleComboBox));

        if (avatar == null || avatar.getImage() == null) {
            avatar = new ImageView();
            avatar.setImage(ImageController.getImage("sample_avatar.jpg"));
        }
        avatar.setFitWidth(120);
        avatar.setFitHeight(avatar.getImage().getHeight() / avatar.getImage().getWidth() * avatar.getFitWidth());
        form.getChildren().addAll(Arrays.asList(mainForm, avatar));
        content.setBody(form);
    }
}
