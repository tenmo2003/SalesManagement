package salesmanagement.salesmanagement.ViewController;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;

public abstract class SearchViewController<T extends SalesComponent> extends ViewController {
    @FXML
    protected FilteredTableView<T> searchTable;
    protected FilteredList<T> searchList;

    abstract protected void search();

    abstract protected void clearAll();
}
