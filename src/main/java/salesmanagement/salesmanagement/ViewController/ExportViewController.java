package salesmanagement.salesmanagement.ViewController;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * The "ExportViewController" is an abstract class in the "salesmanagement.salesmanagement.ViewController" package,
 * which extends the "ViewController" class. The purpose of this class is to provide functionality to export data
 * to an Excel workbook.
 * The class contains a single method called "export", which is annotated with @FXML. The method creates a new
 * instance of the "FileChooser" class, which allows the user to select a file to save the exported data.
 * The file extension is set to ".xlsx", and the initial file name is set to the value of the "exportedFileName" variable.
 * Finally, the method returns the file that the user has selected.
 * The "exportedFileName" variable is a private String variable, which is set using the "setExportedFileName" method.
 * This method allows subclasses to set the name of the file that will be used as the initial file name in the "FileChooser" dialog.
 * Overall, this class provides a simple and useful abstraction for exporting data to an Excel workbook.
 * It can be extended by other classes in the "salesmanagement.salesmanagement.ViewController" package
 * to provide more specific functionality.
 */
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
