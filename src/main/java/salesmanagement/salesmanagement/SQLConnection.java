package salesmanagement.salesmanagement;

import java.sql.*;

/**
 * SQLConnection helps you connect to SQL server and database table.
 *
 * @author thanh_an
 * @since 1.0
 */
public class SQLConnection {
    private Connection connection;

    /**
     * You need url to database server, username and password to log in database server.
     *
     * @see Connection
     * @since 1.0
     */
    public void logInSQLServer(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get result set of a SQL query.
     *
     * @since 1.0
     */
    public ResultSet getDataQuery(String query) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Close connection to SQL Server.
     *
     * @since 1.0
     */
    public void addClosingWork() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
