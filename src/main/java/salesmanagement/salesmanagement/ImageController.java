package salesmanagement.salesmanagement;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageController {
    public static Image orderIcon = getImage("order_icon.png");
    public static Image blueOrderIcon = getImage("blue_order_icon.png");
    public static Image employeeIcon = getImage("employee_icon.png");
    public static Image blueEmployeeIcon = getImage("blue_employee_icon.png");
    public static Image newsIcon = getImage("news_icon.png");
    public static Image blueNewsIcon = getImage("blue_news_icon.png");
    public static Image settingsIcon = getImage("settings_icon.png");
    public static Image blueSettingsIcon =getImage("blue_settings_icon.png");
    public static Image productIcon = getImage("product_icon.png");
    public static Image blueProductIcon =getImage("blue_product_icon.png");
    public static Image customerIcon = getImage("customer_icon.png");
    public static Image blueCustomerIcon = getImage("blue_customer_icon.png");

    public static Image getImage(String imageName) {
        return new Image(Objects.requireNonNull(SalesManagement.class.getResourceAsStream("/salesmanagement/salesmanagement/img/" + imageName)));
    }
}
