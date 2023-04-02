package salesmanagement.salesmanagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test extends Application {
    String url = "jdbc:mysql://bjeoeejo9jrw0qoibqmj-mysql.services.clever-cloud.com:3306/bjeoeejo9jrw0qoibqmj";
    private final String user = "ugxxkw9sh32lhroy";
    private final String password = "QtXTyK7jzCyWztQv80TM";
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        SQLConnection sqlConnection = new SQLConnection();
        sqlConnection.logInSQLServer(url, user, password);

        String sql = "UPDATE employees SET avatar = ? WHERE employeeNumber = ?";
        try (PreparedStatement pstmt = sqlConnection.getConnection().prepareStatement(sql)) {
            // set binary stream for blob data
            pstmt.setBinaryStream(1, new FileInputStream("D:/DOWNLOAD/2257402d821023dd69a315a170614872.jpg"));
            // set accountID
            pstmt.setInt(2, 21000000);
            // execute the update
            pstmt.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }

        PreparedStatement ps = sqlConnection.getConnection().prepareStatement("SELECT avatar FROM employees WHERE employeeNumber = ?");
        ps.setInt(1, 21000000);
        ResultSet rs = ps.executeQuery();
        ImageView imageView = null;
        if (rs.next()) {
            InputStream is = rs.getBinaryStream("avatar");
            Image image = new Image(is);
            imageView = new ImageView();
            imageView.setImage(image);
        }
        primaryStage.setScene(new Scene(new StackPane(imageView)));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
