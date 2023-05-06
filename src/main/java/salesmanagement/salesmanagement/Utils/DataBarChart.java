package salesmanagement.salesmanagement.Utils;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class DataBarChart {
    CategoryAxis xAxis;
    NumberAxis yAxis;
    BarChart<String, Number> chart;
    Node parent;
    public DataBarChart(String barTitle, Node parent) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        chart = new BarChart<>(xAxis, yAxis);
        this.parent = parent;
    }

    public void initData() {

    }
}
