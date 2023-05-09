package salesmanagement.salesmanagement.ViewController.ProductLinesTab;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import salesmanagement.salesmanagement.ViewController.ExportView;

import java.io.File;

public class ProductLinesExportView  extends ExportView implements ProductLinesTab{
    @FXML
    private JFXCheckBox description;

    @FXML
    private JFXCheckBox mainProductVendor;

    @FXML
    private JFXCheckBox productLine;

    @FXML
    private JFXCheckBox quantityInStock;

    @FXML
    private StackPane root;

    @FXML
    private JFXCheckBox totalRevenue;

    @Override
    protected File export() {
        File file = super.export();

        return file;
    }

    @FXML
    void resetData(MouseEvent event) {

    }
}
