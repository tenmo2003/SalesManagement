package salesmanagement.salesmanagement.ViewController;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;

public abstract class SearchView<T extends SalesComponent> extends ViewController {
    @FXML
    protected TableView<T> searchTable;

    protected FilteredList<T> searchList;

    abstract protected void search();

    abstract protected void clearAll();
}
