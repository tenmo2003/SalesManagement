package salesmanagement.salesmanagement;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Test3 extends Application {

    public static void main(String[] args) {
       Application.launch();

    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 400, 400);

        Button employeeButton = new Button("Employee");

        Menu employeeMenu = new Menu("Employee");
        MenuItem addEmployeeMenuItem = new MenuItem("Add New Employee");
        MenuItem viewEmployeeMenuItem = new MenuItem("View Employee");
        employeeMenu.getItems().addAll(addEmployeeMenuItem, viewEmployeeMenuItem);

        employeeButton.setContextMenu(new ContextMenu(employeeMenu));

        pane.getChildren().add(employeeButton);

        stage.setScene(scene);
        stage.show();

    }
}
