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

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Test pagination");

        Pagination pagination = new Pagination();
        pagination.setPageCount(4);
        pagination.setMaxPageIndicatorCount(4);
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory((pageIndex) -> {

            Pane pagePane = new Pane();
            pagePane.setPrefWidth(600);
            pagePane.setPrefHeight(400);
            pagePane.setStyle("-fx-background-color: #DDF1F8;"); //sky color
            Button button = new Button("Hide/show page buttons");
            button.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    HBox paginationHBox = (HBox)pagination.lookup(".control-box");
                    Node paginationControl = pagination.lookup(".pagination-control");
                    paginationControl.setStyle("-fx-background-color: gray;");
                    paginationControl.setVisible(!paginationControl.isVisible()); //switch visibility
                    paginationControl.setManaged(paginationControl.isVisible());
                    paginationControl.minHeight(0.0);
                    paginationControl.prefHeight(0.0);
                    paginationControl.maxHeight(0.0);
                }
            });
            pagePane.getChildren().add(button);
            return pagePane;
        });

        VBox vBox = new VBox(pagination);
        Scene scene = new Scene(vBox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}