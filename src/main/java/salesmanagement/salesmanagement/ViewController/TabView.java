package salesmanagement.salesmanagement.ViewController;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

public abstract class TabView extends ViewController {
    @FXML
    protected JFXButton addButton;

    protected boolean isShowing = false;

    protected void figureShow() {
        if (!rightSet && addButton != null) {
            rightSet = true;
            addButton.setDisable(userRight == UserRight.HR);
        }
        super.show();
    }

    @FXML
    @Override
    public void show() {
        if (isShowing) return;
        isShowing = true;
        figureShow();
    }

    @Override
    final protected void resetData() {
    }
}