package salesmanagement.salesmanagement.ViewController;

import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;
import salesmanagement.salesmanagement.Utils.Utils;

public abstract class FilterViewController<T extends SalesComponent> extends ViewController {
    protected FilteredList<T> filteredList;

    protected abstract void applyFilter();

    public void setFilteredList(FilteredList<T> filteredList) {
        this.filteredList = filteredList;
    }

    public FilteredList<T> getFilteredList() {
        return filteredList;
    }
}
