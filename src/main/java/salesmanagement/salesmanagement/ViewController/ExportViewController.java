package salesmanagement.salesmanagement.ViewController;

import javafx.stage.FileChooser;

import java.io.File;

public abstract class ExportViewController extends ViewController {
    private String exportedFileName;

    protected void setExportedFileName(String exportedFileName) {
        this.exportedFileName = exportedFileName;
    }

    protected File export() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx"));
        fileChooser.setInitialFileName(exportedFileName);
        return fileChooser.showSaveDialog(stage);
    }
}
