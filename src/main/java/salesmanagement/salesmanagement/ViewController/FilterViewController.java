package salesmanagement.salesmanagement.ViewController;

import javafx.collections.transformation.FilteredList;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;

public abstract class FilterViewController<T extends SalesComponent> extends ViewController {
    protected FilteredList<T> filteredList;

    protected abstract void applyFilter();

    protected abstract void clearFilter();

    public void setFilteredList(FilteredList<T> filteredList) {
        this.filteredList = filteredList;
    }

    public FilteredList<T> getFilteredList() {
        return filteredList;
    }
}
