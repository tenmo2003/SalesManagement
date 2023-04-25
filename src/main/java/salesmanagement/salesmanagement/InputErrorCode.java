package salesmanagement.salesmanagement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import javax.swing.plaf.PanelUI;

public enum InputErrorCode {
    INVALID_USERNAME,
    EXISTED_USERNAME,
    EMPTY_LAST_NAME,
    INVALID_OFFICE_CODE,
    INVALID_SUPERVISOR_CODE,
    EMPTY_FIRST_NAME,
    INVALID_LENGTH_USERNAME,
    INVALID_EMAIL,
    INVALID_DATE,
    EMPTY_DATE,
    INVALID_PHONE_NUMBER,
    INVALID_LENGTH_PASSWORD;

    public static String getInputErrorNotification(InputErrorCode code) {
        switch (code) {
            case INVALID_USERNAME -> {
                return "Sorry, only letters (a-z), numbers (0-9), and periods (.) are allowed.";
            }
            case INVALID_LENGTH_USERNAME -> {
                return "Sorry, first name must be between 6 and 30 characters long.";
            }
            case EMPTY_LAST_NAME -> {
                return "Enter last name";
            }
            case EMPTY_FIRST_NAME -> {
                return "Enter first name";
            }
            case INVALID_EMAIL -> {
                return "Email is invalid!";
            }
            case INVALID_DATE -> {
                return "Date is invalid!";
            }
            case EMPTY_DATE -> {
                return "Sorry, date must not be empty.";
            }
            case INVALID_OFFICE_CODE -> {
                return "Office code does not exist!";
            }
            case INVALID_SUPERVISOR_CODE -> {
                return "Supervisor code does not exist!";
            }
            case INVALID_PHONE_NUMBER -> {
                return "Phone Number is invalid!";
            }
            case EXISTED_USERNAME -> {
                return "That username is taken. Try another.";
            }
            case INVALID_LENGTH_PASSWORD -> {
                return "Use 8 or more characters with a mix of letters, numbers & symbols";
            }
        }
        return null;
    }
    public static Label getInputErrorLabel(InputErrorCode code) {
        Label invalid_warning = new Label(getInputErrorNotification(code));
        invalid_warning.setStyle("-fx-text-fill: red");
        FontAwesomeIconView warningGraphic = new FontAwesomeIconView(FontAwesomeIcon.WARNING, "12.5");
        warningGraphic.setFill(Color.RED);
        invalid_warning.setGraphic(warningGraphic);
        return invalid_warning;
    }
}
