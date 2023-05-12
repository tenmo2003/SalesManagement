package salesmanagement.salesmanagement.ViewController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import salesmanagement.salesmanagement.Utils.Utils;

import java.io.File;

public abstract class ExportView extends PopUpView {
    private String exportedFileName;

    @Override
    @FXML
    final protected void resetData() {
        for (Node node : Utils.getAllNodes(root)) {
            if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(true);
            }
        }
    }

    protected void setExportedFileName(String exportedFileName) {
        this.exportedFileName = exportedFileName;
    }

    @FXML
    protected File export() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx"));
        fileChooser.setInitialFileName(exportedFileName);
        return fileChooser.showSaveDialog(stage);
    }
}
