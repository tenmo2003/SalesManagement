package salesmanagement.salesmanagement;

import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Test3 {

    public static void main(String[] args) {
        SQLConnection sqlConnection = new SQLConnection();
        String url = "jdbc:mysql://bjeoeejo9jrw0qoibqmj-mysql.services.clever-cloud.com:3306/bjeoeejo9jrw0qoibqmj";
        String user = "ugxxkw9sh32lhroy";
        String password = "QtXTyK7jzCyWztQv80TM";
        sqlConnection.logInSQLServer(url, user, password);

        String query = "SELECT * FROM employees";
        try {
            System.out.println(123);
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            System.out.println(12334);
            System.out.println(123);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
