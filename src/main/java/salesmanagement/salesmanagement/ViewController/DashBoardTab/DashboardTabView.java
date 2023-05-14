package salesmanagement.salesmanagement.ViewController.DashBoardTab;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class DashboardTabView extends TabView {
    @FXML
    private LineChart<String, Number> revenueByChannelChart;

    @FXML
    private PieChart revenueByRegionChart;

    @FXML
    private BarChart<String, Number> totalRevenueChart;

    @FXML
    private BarChart<String, Number> topProductsChart;

    @FXML
    private BarChart<String, Number> topCustomersChart;

    @FXML
    private BarChart<Number, String> topProductLinesChart;

    private ResultSet[] chartResultSet;
    private boolean[] showingChartFlag;
    NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        chartResultSet = new ResultSet[6];
        showingChartFlag = new boolean[6];
        Arrays.fill(showingChartFlag, false);
        nf.setMaximumFractionDigits(2);
    }

    @Override
    public void figureShow() {
        super.figureShow();
        Arrays.fill(showingChartFlag, true);

        runTask(() -> {
            try {
                initTotalRevenueChart();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, null, loadingIndicator, null);
        runTask(() -> {
            try {
                initTopProductsChart();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, null, loadingIndicator, null);
//        runTask(() -> {
//            try {
//                initTopProductLines();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, null, loadingIndicator, null);
        runTask(() -> {
            try {
                initCustomersChart();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, null, loadingIndicator, null);
        runTask(() -> {
            try {
                initRevenueByRegionChart();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, null, loadingIndicator, null);
        runTask(() -> {
            try {
                initRevenueByChannelChart();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, null, loadingIndicator, null);
    }

    private void initTotalRevenueChart() throws InterruptedException {
        Platform.runLater(() -> totalRevenueChart.getData().clear());
        Thread.sleep(300);
        String query = "SELECT YEAR(months.month) AS year, " +
                "MONTH(months.month) AS month, " +
                "COALESCE(SUM(value), 0) AS revenue " +
                "FROM ( SELECT DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL n MONTH) AS month " +
                "FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS numbers ) AS months " +
                "LEFT JOIN orders " +
                "ON YEAR(orderDate) = YEAR(months.month) AND MONTH(orderDate) = MONTH(months.month) " +
                "WHERE months.month >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 12 MONTH) " +
                "GROUP BY YEAR(months.month), MONTH(months.month) " +
                "ORDER BY year ASC, month ASC";
        chartResultSet[ChartCode.TOTAL_REVENUE.ordinal()] = sqlConnection.getDataQuery(query);
        try {
            ((CategoryAxis) totalRevenueChart.getXAxis()).setCategories(FXCollections.observableArrayList());
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            List<String> xAxisLabels = new ArrayList<>();
            ResultSet rs = chartResultSet[ChartCode.TOTAL_REVENUE.ordinal()];
            while (rs.next()) {
                double value = rs.getDouble(3);
                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2) + "-" + rs.getString(1), value);
                xAxisLabels.add(rs.getString(2) + "-" + rs.getString(1));
                Label label = new Label(nf.format(value));
                label.setGraphic(new Group());
                label.setAlignment(Pos.CENTER);
                label.setStyle("-fx-background-color: #459ee8; -fx-text-fill: #000000");
                data.setNode(label);
                series.getData().add(data);
            }
            ((CategoryAxis) totalRevenueChart.getXAxis()).setCategories(FXCollections.observableList(xAxisLabels));

            Platform.runLater(() -> totalRevenueChart.getData().add(series));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        showingChartFlag[ChartCode.TOTAL_REVENUE.ordinal()] = false;
        boolean check = false;
        for (boolean b : showingChartFlag) check = check || b;
        if (!check) isShowing = false;
    }

    private void initTopProductsChart() throws InterruptedException {
        Platform.runLater(() -> topProductsChart.getData().clear());
        Thread.sleep(300);
        String query = "SELECT SUM(quantityOrdered * priceEach) AS revenue, products.productCode FROM orderdetails " +
                "RIGHT JOIN products ON orderdetails.productCode = products.productCode " +
                "GROUP BY products.productCode " +
                "ORDER BY revenue DESC " +
                "LIMIT 6";
        chartResultSet[ChartCode.TOP_PRODUCTS.ordinal()] = sqlConnection.getDataQuery(query);
        topProductsChart.setTitle("Top Products");
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            ResultSet rs = chartResultSet[ChartCode.TOP_PRODUCTS.ordinal()];
            List<String> xAxisLabels = new ArrayList<>();
            while (rs.next()) {
                double value = rs.getDouble("revenue");
                xAxisLabels.add(rs.getString("productCode"));
                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
                Label label = new Label(nf.format(value));
                label.setGraphic(new Group());
                label.setAlignment(Pos.CENTER);
                data.setNode(label);
                series.getData().add(data);
            }
            ((CategoryAxis) topProductsChart.getXAxis()).setCategories(FXCollections.observableList(xAxisLabels));
            Platform.runLater(() -> topProductsChart.getData().add(series));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        showingChartFlag[ChartCode.TOP_PRODUCTS.ordinal()] = false;
        boolean check = false;
        for (boolean b : showingChartFlag) check = check || b;
        if (!check) isShowing = false;
    }

    private void initCustomersChart() throws InterruptedException {
        Platform.runLater(() -> topCustomersChart.getData().clear());
        Thread.sleep(300);
        ((CategoryAxis) topCustomersChart.getXAxis()).setCategories(FXCollections.observableArrayList());
        String query = "SELECT SUM(orderdetails.quantityOrdered * orderdetails.priceEach) AS revenue, customers.customerNumber " +
                "FROM customers " +
                "INNER JOIN orders ON customers.customerNumber = orders.customerNumber " +
                "INNER JOIN orderdetails ON orders.orderNumber = orderdetails.orderNumber " +
                "GROUP BY customers.customerNumber " +
                "ORDER BY revenue DESC " +
                "LIMIT 10; ";
        chartResultSet[ChartCode.TOP_CUSTOMERS.ordinal()] = sqlConnection.getDataQuery(query);
        topCustomersChart.setTitle("Top Customers");
        ResultSet rs = chartResultSet[ChartCode.TOP_CUSTOMERS.ordinal()];
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            List<String> xAxisLabels = new ArrayList<>();
            while (rs.next()) {
                double value = rs.getDouble(1);
                XYChart.Data<String, Number> data = new XYChart.Data<>(String.valueOf(rs.getInt(2)), rs.getDouble(1));
                xAxisLabels.add(rs.getString(2));

                Label label = new Label(nf.format(value));
                label.setGraphic(new Group());
                label.setAlignment(Pos.CENTER);
                data.setNode(label);

                series.getData().add(data);
            }
            ((CategoryAxis) topCustomersChart.getXAxis()).setCategories(FXCollections.observableList(xAxisLabels));
            Platform.runLater(() -> topCustomersChart.getData().add(series));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        showingChartFlag[ChartCode.TOP_CUSTOMERS.ordinal()] = false;
        boolean check = false;
        for (boolean b : showingChartFlag) check = check || b;
        if (!check) isShowing = false;
    }


    private void initTopProductLines() throws InterruptedException {
        Platform.runLater(() -> topProductLinesChart.getData().clear());
        Thread.sleep(300);
        String query = "SELECT SUM(quantityOrdered * priceEach) AS revenue, products.productLine FROM orderdetails " +
                "RIGHT JOIN products ON orderdetails.productCode = products.productCode " +
                "GROUP BY products.productLine " +
                "ORDER BY revenue DESC " +
                "LIMIT 6";
        chartResultSet[ChartCode.TOP_PRODUCTLINES.ordinal()] = sqlConnection.getDataQuery(query);
        ResultSet rs = chartResultSet[ChartCode.TOP_PRODUCTLINES.ordinal()];
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        List<String> xAxisLabels = new ArrayList<>();
        try {
            while (rs.next()) {
                double value = rs.getDouble(1);
                XYChart.Data<Number, String> data = new XYChart.Data<>(rs.getDouble(1), rs.getString(2));
                xAxisLabels.add(rs.getString(2));
                Label label = new Label(nf.format(value));
                label.setGraphic(new Group());
                label.setAlignment(Pos.CENTER);
                data.setNode(label);
                series.getData().add(data);
            }
            ((CategoryAxis) topProductLinesChart.getYAxis()).setCategories(FXCollections.observableList(xAxisLabels));
            Platform.runLater(() -> {
               // topProductLinesChart.getYAxis().setStyle("-fx-text-fill: green");
                topProductLinesChart.getData().add(series);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        showingChartFlag[ChartCode.TOP_PRODUCTLINES.ordinal()] = false;
        boolean check = false;
        for (boolean b : showingChartFlag) check = check || b;
        if (!check) isShowing = false;
    }

    private void initRevenueByRegionChart() throws InterruptedException {
        Platform.runLater(() -> revenueByRegionChart.getData().clear());
        Thread.sleep(300);
        String query = "SELECT o.region AS `region`,\n" +
                "       SUM(value) AS `totalRevenue`\n" +
                "FROM orders AS od\n" +
                "         INNER JOIN employees AS e ON od.created_by = e.employeeNumber\n" +
                "         INNER JOIN offices AS o ON e.officeCode = o.officeCode\n" +
                "WHERE od.orderDate >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH)\n" +
                "GROUP BY o.region\n" +
                "             ORDER BY o.region;";
        chartResultSet[ChartCode.REVENUE_BY_REGION.ordinal()] = sqlConnection.getDataQuery(query);

        try {
            ResultSet rs = chartResultSet[ChartCode.REVENUE_BY_REGION.ordinal()];
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            while (rs.next()) {
                String region = rs.getString("region");
                double revenue = rs.getDouble("totalRevenue");
                PieChart.Data data = new PieChart.Data(region, revenue);
                pieChartData.add(data);
            }
            Platform.runLater(() -> revenueByRegionChart.getData().addAll(pieChartData));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showingChartFlag[ChartCode.REVENUE_BY_REGION.ordinal()] = false;
        boolean check = false;
        for (boolean b : showingChartFlag) check = check || b;
        if (!check) isShowing = false;
    }

    private void initRevenueByChannelChart() throws InterruptedException {
        Platform.runLater(() -> revenueByChannelChart.getData().clear());
        Thread.sleep(300);
        String query = "SELECT DATE_FORMAT(orderDate, '%m-%Y') AS `month - year`,\n" +
                "       SUM(CASE WHEN type = 'online' THEN value ELSE 0 END) AS `onlineRevenue`,\n" +
                "       SUM(CASE WHEN type = 'onsite' THEN value ELSE 0 END) AS `onsiteRevenue`\n" +
                "FROM orders\n" +
                "WHERE orders.orderDate >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)\n" +
                "GROUP BY `month - year`,YEAR(orderDate), MONTH(orderDate)\n" +
                "ORDER BY orderDate;";
        chartResultSet[ChartCode.REVENUE_BY_CHANNEL.ordinal()] = sqlConnection.getDataQuery(query);

        try {
            XYChart.Series<String, Number> onlineSeries = new XYChart.Series<>();
            onlineSeries.setName("Online Revenue");
            XYChart.Series<String, Number> onsiteSeries = new XYChart.Series<>();
            onsiteSeries.setName("Onsite Revenue");

            ((CategoryAxis) revenueByChannelChart.getXAxis()).setCategories(FXCollections.observableArrayList());

            List<String> xAxisLabels = new ArrayList<>();
            ResultSet rs = chartResultSet[ChartCode.REVENUE_BY_CHANNEL.ordinal()];
            while (rs.next()) {
                double value = rs.getDouble("onlineRevenue");
                XYChart.Data<String, Number> onlineData = new XYChart.Data<>(rs.getString("month - year"), value);
                xAxisLabels.add(rs.getString("month - year"));
                onlineSeries.getData().add(onlineData);

                value = rs.getDouble("onsiteRevenue");
                XYChart.Data<String, Number> onsiteData = new XYChart.Data<>(rs.getString("month - year"), value);
                onsiteSeries.getData().add(onsiteData);
            }

            ((CategoryAxis) revenueByChannelChart.getXAxis()).setCategories(FXCollections.observableList(xAxisLabels));
            Platform.runLater(() -> revenueByChannelChart.getData().add(onsiteSeries));
            Platform.runLater(() -> revenueByChannelChart.getData().add(onlineSeries));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showingChartFlag[ChartCode.REVENUE_BY_CHANNEL.ordinal()] = false;
        boolean check = false;
        for (boolean b : showingChartFlag) check = check || b;
        if (!check) isShowing = false;
    }
}