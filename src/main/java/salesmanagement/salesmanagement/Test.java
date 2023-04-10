package salesmanagement.salesmanagement;

import java.util.List;
import java.util.regex.Pattern;
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage stage) {
        Button button = new Button("Click me!");

        // Thêm CSS style cho button
        button.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: #fff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(162, 155, 254, 0.5), 0, 5, 0, 0);");

        // Tạo layout và thêm button vào đó
        VBox root = new VBox(button);

        // Tạo scene và hiển thị stage
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}