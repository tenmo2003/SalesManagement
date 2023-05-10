package salesmanagement.salesmanagement.ViewController;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;


public abstract class InfoView<T extends SalesComponent> extends ViewController implements InputValidator {
    @FXML
    protected Label boxLabel;
    @FXML
    protected JFXButton addButton;
    @FXML
    protected JFXButton saveButton;

    protected T selectedSalesComponent;

    protected abstract void figureSave();

    @FXML
    protected void save() {
        if (!validInput()) {
            NotificationSystem.throwNotification(NotificationCode.INVALID_INPUTS, stage);
            return;
        }
        figureSave();
    }

    ;

    @FXML
    protected abstract void add();

    protected abstract void show(T selectedSalesComponent);

    @Override
    public void show() {
        if (!rightSet) {
            rightSet = true;
            if (userRight == UserRight.HR) {
                lockData(true);
                saveButton.setDisable(true);
            } else {
                lockData(false);
                saveButton.setDisable(false);
            }
        }
        super.show();
        resetData();
    }

    @Override
    public void close() {
        super.close();
        addButton.setVisible(false);
        saveButton.setVisible(false);
    }
}
