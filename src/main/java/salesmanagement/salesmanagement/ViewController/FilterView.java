package salesmanagement.salesmanagement.ViewController;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;

public abstract class FilterView<T extends SalesComponent> extends PopUpView {
    protected FilteredList<T> filteredList;

    @FXML
    protected abstract void applyFilter();

    @FXML
    final protected void clearFilter() {
        resetData();
    }

    public void setFilteredList(FilteredList<T> filteredList) {
        this.filteredList = filteredList;
    }

    public FilteredList<T> getFilteredList() {
        return filteredList;
    }
}
