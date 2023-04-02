package salesmanagement.salesmanagement;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.Arrays;

public abstract class Form {
    JFXDialogLayout content;
    JFXDialog dialog;

    public Form(StackPane container) {
        dialog = new JFXDialog();
        content = new JFXDialogLayout();
        dialog.setDialogContainer(container);
        dialog.setContent(content);
        dialog.setOverlayClose(false);


        saveButton.setOnAction(event -> dialog.close());
        cancelButton.setOnAction(event -> dialog.close());
        closeButton.setOnAction(event -> dialog.close());

        content.setActions(Arrays.asList(saveButton, cancelButton, closeButton));

        dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);

    }

    public void show() {
        dialog.show();
    }

    public void closeForm(Runnable closeFunction) {
        closeButton.setOnMouseClicked(e -> closeFunction.run());
    }

    abstract public void fillInForm(Object object);

    JFXButton saveButton = new JFXButton("Save");
    JFXButton cancelButton = new JFXButton("Cancel");
    JFXButton closeButton = new JFXButton("Close");
}
