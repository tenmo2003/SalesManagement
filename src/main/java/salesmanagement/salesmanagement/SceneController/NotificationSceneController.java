package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import salesmanagement.salesmanagement.Utils.NotificationCode;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationSceneController implements Initializable {
    @FXML
    private JFXTextArea contentLabel;

    @FXML
    private FontAwesomeIconView symbolView;

    @FXML
    StackPane root;

    @FXML
    private Label titleLabel;

    public void init(String title, String content, NotificationCode NotificationCode) {
        Rectangle rect = new Rectangle(root.getPrefWidth(), root.getPrefHeight());
        rect.setArcHeight(15.0);
        rect.setArcWidth(15.0);
        root.setClip(rect);

        contentLabel.setText(content);
        titleLabel.setText(title);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setVisible(false);
    }

    public void show() {
        root.setVisible(true);
    }

    @FXML
    public void close() {
        root.setVisible(false);
    }

    public StackPane getRoot() {
        return root;
    }
}
