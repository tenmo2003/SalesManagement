package salesmanagement.salesmanagement.ViewController.SettingsTab;


import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Action;
import salesmanagement.salesmanagement.ViewController.FilterView;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AccountActivityLogFilterView extends FilterView<Action> implements SettingsTab {
    @FXML
    private TextField actionIDTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private ComboBox<String> resultComboBox;
    @FXML
    private TextField timeTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        resultComboBox.getItems().addAll(Arrays.asList("SUCCESSFUL", "FAILED"));
    }

    @Override
    protected void applyFilter() {
        filteredList.setPredicate(action -> {
            boolean actionIDMatch = Integer.toString(action.getActionID()).contains(actionIDTextField.getText());
            boolean timeMatch = action.getTime().contains(timeTextField.getText());
            boolean resultMatch = action.getResult().getText().equals(resultComboBox.getValue());
            boolean description = action.getDescription().contains(descriptionTextField.getText());
            if (resultComboBox.getValue() == null) resultMatch = true;
            return actionIDMatch && timeMatch && resultMatch && description;
        });
        close();
    }
}
