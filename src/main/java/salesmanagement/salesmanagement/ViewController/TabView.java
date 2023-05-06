package salesmanagement.salesmanagement.ViewController;

import javafx.fxml.FXML;

public abstract class TabView extends ViewController {
    protected boolean isShowing = false;

    protected void figureShow() {
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