package salesmanagement.salesmanagement.SalesComponent;

import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Action implements SalesComponent {
    private int actionID;
    private String time;
    private String description;
    private Label result;

    public Action(ResultSet actionsSet) throws SQLException {
        actionID = actionsSet.getInt("actionID");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        time = dateFormat.format(actionsSet.getTimestamp("time"));
        description = actionsSet.getString("description");
        result = new Label(actionsSet.getString("result"));
        if(result.getText().equals("SUCCESSFUL")) result.setStyle("-fx-text-fill: WHITE; -fx-alignment: center; -fx-font-weight: bold; -fx-background-color: rgba(117,217,117,0.91); -fx-pref-width: 120;");
        else if(result.getText().equals("FAILED")) result.setStyle("-fx-text-fill: WHITE; -fx-alignment: center; -fx-font-weight: bold; -fx-background-color: #d92c2c;-fx-pref-width: 120;");
    }

    public int getActionID() {
        return actionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Label getResult() {
        return result;
    }

    public void setResult(Label result) {
        this.result = result;
    }
}
