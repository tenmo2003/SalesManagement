package salesmanagement.salesmanagement.SalesComponent;

import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductLine implements SalesComponent {
    private String productLine;
    private String description;
    private double totalRevenue;
    private String mainProductVendor;
    private int numberOfProducts;

    public ProductLine(ResultSet resultSet) throws SQLException {
        productLine = resultSet.getString("productLine");
        description = resultSet.getString("textDescription");
        totalRevenue = resultSet.getDouble("totalRevenue");
        mainProductVendor = resultSet.getString("mainProductVendor");
        numberOfProducts = resultSet.getInt("numberOfProducts");
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getMainProductVendor() {
        return mainProductVendor;
    }

    public void setMainProductVendor(String mainProductVendor) {
        this.mainProductVendor = mainProductVendor;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
}
