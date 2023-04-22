package salesmanagement.salesmanagement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test extends Application {

    private Parent createContent() {
        Pane root = new Pane();
        CustomTextField customTextField = new CustomTextField();
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        customTextField.setLeft(icon);
        root.getChildren().add(customTextField);
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("D:\\JAVA\\SalesManagement\\src\\main\\resources\\salesmanagement\\salesmanagement\\loginInfo.txt", false))) {
            writer.println();
            System.out.println("Dữ liệu đã được ghi vào tệp loginInfo.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}