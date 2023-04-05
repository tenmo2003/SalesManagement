package salesmanagement.salesmanagement;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Optional;

public abstract class Form {
    DialogPane content;
    Dialog dialog;
    ButtonType saveButton;

    public Form(StackPane container) {
        dialog = new Dialog();
        dialog.setTitle("MODIFYING INFORMATION");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

        dialog.initStyle(StageStyle.TRANSPARENT);

        content = new DialogPane();
        dialog.getDialogPane().setContent(content);
        dialog.initOwner(container.getScene().getWindow());
    }

    public void show() {
        double frameDuration = 1000.0 / 60.0;
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(frameDuration), dialog.getDialogPane());
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
        dialog.show();

    }

    public void closeForm(Runnable closeFunction) {
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
            }
            closeFunction.run();
            return null;
        });
    }

    abstract public void fillInForm(Object object);
}
