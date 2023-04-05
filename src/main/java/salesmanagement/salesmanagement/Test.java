package salesmanagement.salesmanagement;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("login Dialog");
        dialog.setHeaderText("Sign Up");

        ButtonType loginButtonType = new ButtonType("login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        JFXTextField userName = new JFXTextField();
        userName.setPromptText("Username");
        JFXPasswordField password = new JFXPasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(userName, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        userName.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType){
                return new Pair<>(userName.getText(), password.getText());
            }
            return null;
        } );
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(userNamepassword -> {
            System.out.println("Username="+userNamepassword.getKey()+", Password="+userNamepassword.getValue());
        });

        primaryStage.setScene(new Scene(grid));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
