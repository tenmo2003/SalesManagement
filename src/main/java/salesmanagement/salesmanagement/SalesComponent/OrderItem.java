package salesmanagement.salesmanagement.SalesComponent;

public class OrderItem implements SalesComponent {
    String productCode;
    Integer quantityOrdered;
    Double priceEach;
    Double amount;

    public OrderItem(String productCode, int quantityOrdered, double priceEach) {
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
        //System.out.println(String.format("%.2f", quantityOrdered * priceEach));
        this.amount = Double.parseDouble(String.format("%.2f", quantityOrdered * priceEach).replaceAll(",","."));
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
        amount = Double.parseDouble(String.format("%.2f", priceEach * quantityOrdered));
    }

    public Double getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(double priceEach) {
        this.priceEach = priceEach;
        amount = priceEach * quantityOrdered;
    }

    public Double getAmount() {
        return amount;
    }
}
