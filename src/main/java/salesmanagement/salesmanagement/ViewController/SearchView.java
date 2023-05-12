package salesmanagement.salesmanagement.ViewController;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;

public abstract class SearchView<T extends SalesComponent> extends PopUpView {
    @FXML
    protected TableView<T> searchTable;

    protected FilteredList<T> searchList;

    @FXML
    abstract protected void search();

    @FXML
    protected void clearAll() {
        resetData();
    };
}
