package salesmanagement.salesmanagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Test2 extends Application {

    private ImageView imageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Create UI elements
        Label label = new Label("Upload Image:");
        TextField textField = new TextField();
        Button browseButton = new Button("Browse");
        Button uploadButton = new Button("Upload");
        imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        // Set action for browse button
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            File file = fileChooser.showOpenDialog(stage);
            System.out.println(file);
            if (file != null) {
                textField.setText(file.getAbsolutePath());
                try {
                    Image image = new Image(new FileInputStream(file));
                    imageView.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set action for upload button
        uploadButton.setOnAction(event -> {
            // Insert code to upload the image file to the database or save it to a file
            System.out.println(imageView.getImage().getUrl());
            System.out.println("Image uploaded successfully.");
        });

        // Create UI layout
       VBox pane = new VBox();

        pane.getChildren().addAll(browseButton, uploadButton);

        pane.getChildren().add(imageView);
        // Create scene and show window
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Image Uploader");
        stage.show();
    }
}
