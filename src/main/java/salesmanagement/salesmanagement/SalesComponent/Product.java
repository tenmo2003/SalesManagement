package salesmanagement.salesmanagement.SalesComponent;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    SimpleStringProperty name;

    public Product(SimpleStringProperty name) {
        this.name = name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
