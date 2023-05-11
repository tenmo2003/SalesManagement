package salesmanagement.salesmanagement.ViewController;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public abstract class PopUpView extends ViewController {
    @FXML
    StackPane centerBox;

    @Override
    public void show() {
        super.show();
        if (centerBox != null) {
            Rectangle rect = new Rectangle(centerBox.getWidth(), centerBox.getHeight());
            rect.setArcHeight(15.0);
            rect.setArcWidth(15.0);
            centerBox.setClip(rect);
        }
    }
}
