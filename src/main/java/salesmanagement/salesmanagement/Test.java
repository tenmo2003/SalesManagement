package salesmanagement.salesmanagement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.text.html.HTML;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test extends Application {
    public static void main(String[] args) {
        Test.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
        DatePicker datePicker = new DatePicker();
        List<LocalDate> holidays = new ArrayList<>(Arrays.asList(LocalDate.of(2023, Month.MAY, 10)));
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (holidays.contains(item)) {
                              //  this.setStyle("-fx-background-color: pink");
                                this.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WARNING));
                            }
                        }
                    }
                };
            }
        });
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        StackPane stackPane = new StackPane(datePickerSkin.getPopupContent());
        stackPane.setOnMouseClicked(event -> {
            System.out.println(datePicker.getValue());
        });
        stage.setScene(new Scene(stackPane));
    }
}