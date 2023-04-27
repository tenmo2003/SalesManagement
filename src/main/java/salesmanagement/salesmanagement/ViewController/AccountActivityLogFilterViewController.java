package salesmanagement.salesmanagement.ViewController;


import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Action;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AccountActivityLogFilterViewController extends ViewController {
    @FXML
    private TextField actionIDTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private ComboBox<String> resultComboBox;

    @FXML
    private TextField timeTextField;

    private FilteredList<Action> actionFilteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        resultComboBox.getItems().addAll(Arrays.asList("SUCCESSFUL", "FAILED"));
    }

    public void setActionFilteredList(FilteredList<Action> actionFilteredList) {
        this.actionFilteredList = actionFilteredList;
    }

    public FilteredList<Action> getActionFilteredList() {
        return actionFilteredList;
    }

    @FXML
    void applyFilter() {
        actionFilteredList.setPredicate(action -> {
            boolean actionIDMatch = Integer.toString(action.getActionID()).contains(actionIDTextField.getText());
            boolean timeMatch = action.getTime().contains(timeTextField.getText());
            boolean resultMatch = action.getResult().getText().equals(resultComboBox.getValue());
            boolean description = action.getDescription().contains(descriptionTextField.getText());
            if (resultComboBox.getValue() == null) resultMatch = true;
            return actionIDMatch && timeMatch && resultMatch && description;
        });
        close();
    }

    @FXML
    void clearFilter() {
        actionIDTextField.setText("");
        timeTextField.setText("");
        resultComboBox.setValue(null);
        descriptionTextField.setText("");
    }
}
