package salesmanagement.salesmanagement.ViewController.DashBoardTab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class DashboardTabViewController extends ViewController {

    @FXML
    private StackPane chart1Container;

    @FXML
    private StackPane chart2Container;

    @FXML
    private StackPane chart3Container;

    @FXML
    private StackPane chart4Container;
    BarChart<String, Number> totalRevenueChart;
    BarChart<String, Number> productRevenueChart;
    BarChart<String, Number> productLineRevenueChart;
    BarChart<String, Number> customerRevenueChart;

    @FXML
    public void show() {
        super.show();
        runTask(() -> {
            final CategoryAxis totalRevenueAxis = new CategoryAxis();
            final NumberAxis timeAxis = new NumberAxis();
            totalRevenueChart = new BarChart<>(totalRevenueAxis, timeAxis);
            //Platform.runLater();
            totalRevenueChart.setTitle("Total Revenue");

            String query = "SELECT YEAR(months.month) AS year, " +
                    "MONTH(months.month) AS month, " +
                    "COALESCE(SUM(value), 0) AS revenue " +
                    "FROM ( SELECT DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL n MONTH) AS month " +
                    "FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS numbers ) AS months " +
                    "LEFT JOIN orders " +
                    "ON YEAR(orderDate) = YEAR(months.month) AND MONTH(orderDate) = MONTH(months.month) " +
                    "WHERE months.month >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 5 MONTH) " +
                    "GROUP BY YEAR(months.month), MONTH(months.month) " +
                    "ORDER BY year ASC, month ASC";
            ResultSet rs = sqlConnection.getDataQuery(query);

            try {
                List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                nf.setMaximumFractionDigits(2);
                while (rs.next()) {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    double value = rs.getDouble(3);
                    XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2) + "-" + rs.getString(1), value);
                    Label label = new Label(nf.format(value));
                    label.setGraphic(new Group());
                    label.setAlignment(Pos.CENTER);
                    data.setNode(label);
                    series.getData().add(data);
                    seriesList.add(series);
                }
                for (XYChart.Series<String, Number> stringNumberSeries : seriesList)
                    totalRevenueChart.getData().add(stringNumberSeries);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, () -> chart1Container.getChildren().add(totalRevenueChart), loadingIndicator, null);


        runTask(() -> {
            final NumberAxis productRevenueAxis = new NumberAxis();
            final CategoryAxis productAxis = new CategoryAxis();
            productRevenueChart = new BarChart<>(productAxis, productRevenueAxis);

            productRevenueChart.setTitle("Top Products");

            String query = "SELECT SUM(quantityOrdered * priceEach) AS revenue, products.productCode FROM orderdetails " +
                    "RIGHT JOIN products ON orderdetails.productCode = products.productCode " +
                    "GROUP BY products.productCode " +
                    "ORDER BY revenue DESC " +
                    "LIMIT 6";
            ResultSet rs = sqlConnection.getDataQuery(query);

            try {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
                nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
                while (rs.next()) {
                    double value = rs.getDouble(1);
                    XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
                    Label label = new Label(nf.format(value)); // create a Label with the formatted value
                    label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
                    label.setAlignment(Pos.CENTER);
                    data.setNode(label); // set the Label as the node for the Data object
                    series.getData().add(data);
                }
                productRevenueChart.getData().add(series);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, () -> chart2Container.getChildren().add(productRevenueChart), loadingIndicator, null);

        runTask(() -> {
            final NumberAxis productLineRevenueAxis = new NumberAxis();
            final CategoryAxis productLineAxis = new CategoryAxis();
            productLineRevenueChart = new BarChart<>(productLineAxis, productLineRevenueAxis);
            productLineRevenueChart.setTitle("Top Product Lines");

            String query = "SELECT SUM(quantityOrdered * priceEach) AS revenue, products.productLine FROM orderdetails " +
                    "RIGHT JOIN products ON orderdetails.productCode = products.productCode " +
                    "GROUP BY products.productLine " +
                    "ORDER BY revenue DESC " +
                    "LIMIT 6";
            ResultSet rs = sqlConnection.getDataQuery(query);

            try {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
                nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
                while (rs.next()) {
                    double value = rs.getDouble(1);
                    XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
                    Label label = new Label(nf.format(value)); // create a Label with the formatted value
                    label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
                    label.setAlignment(Pos.CENTER);
                    data.setNode(label); // set the Label as the node for the Data object
                    series.getData().add(data);
                }
                productLineRevenueChart.getData().add(series);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, () -> chart3Container.getChildren().add(productLineRevenueChart), loadingIndicator, null);

        runTask(() -> {
            final NumberAxis customerRevenueAxis = new NumberAxis();
            final CategoryAxis customerAxis = new CategoryAxis();
            customerRevenueChart = new BarChart<>(customerAxis, customerRevenueAxis);

            customerRevenueChart.setTitle("Top Customers");

            String query = "SELECT SUM(orderdetails.quantityOrdered * orderdetails.priceEach) AS revenue, customers.customerName " +
                    "FROM customers " +
                    "INNER JOIN orders ON customers.customerNumber = orders.customerNumber " +
                    "INNER JOIN orderdetails ON orders.orderNumber = orderdetails.orderNumber " +
                    "GROUP BY customers.customerNumber " +
                    "ORDER BY revenue DESC " +
                    "LIMIT 10; ";
            ResultSet rs = sqlConnection.getDataQuery(query);

            try {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
                nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
                while (rs.next()) {
                    double value = rs.getDouble(1);
                    XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
                    Label label = new Label(nf.format(value)); // create a Label with the formatted value
                    label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
                    label.setAlignment(Pos.CENTER);
                    data.setNode(label); // set the Label as the node for the Data object
                    series.getData().add(data);
                }
                customerRevenueChart.getData().add(series);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, () -> chart4Container.getChildren().add(customerRevenueChart), loadingIndicator, null);
    }
}
