package salesmanagement.salesmanagement.ViewController;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;


public abstract class InfoView<T extends SalesComponent> extends ViewController {
    protected T selectedSalesComponent;
    @FXML
    protected Label boxLabel;

    @FXML
    protected JFXButton addButton;

    @FXML
    protected JFXButton saveButton;

    protected abstract void save();

    protected abstract void add();

    protected abstract void show(T selectedSalesComponent);

    @Override
    public void close() {
        super.close();
        addButton.setVisible(false);
        saveButton.setVisible(false);
    }
}
