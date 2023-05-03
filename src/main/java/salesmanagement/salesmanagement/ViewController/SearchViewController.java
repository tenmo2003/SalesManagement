package salesmanagement.salesmanagement.ViewController;

import javafx.fxml.FXML;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;

public abstract class SearchViewController<T extends SalesComponent> extends ViewController {
    @FXML
    private FilteredTableView<T> searchTable;

    abstract protected void search();

    abstract protected void clearAll();
}
