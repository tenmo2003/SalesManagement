package salesmanagement.salesmanagement;

import javafx.application.Application;
import javafx.stage.Stage;
import org.burningwave.core.assembler.StaticComponentContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test extends Application {
    public static void main(String[] args) throws SQLException {
       launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StaticComponentContainer.JVMInfo.getVersion();
        //Modules.exportAllToAll();
        var password = "lbfj2nEwsHTelFcZAqLU";
        var user = "udomuzbs3hfulslz";
        var url = "jdbc:mysql://b7kidpocyxjnjhwdw73i-mysql.services.clever-cloud.com:3306/b7kidpocyxjnjhwdw73i";
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
        stage.show();
    }
}