package salesmanagement.salesmanagement.SalesComponent;

public class OrderItem {
    //    int orderNumber;
//    Date orderDate;
//    Date requiredDate;
//    Date shippedDate;
//    String status;
//    String comments;
    String productCode;
    Integer quantityOrdered;
    Double priceEach;
    Double amount;

    public OrderItem(String productCode, int quantityOrdered, double priceEach, double amount) {
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
        this.amount = amount;
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
        amount = priceEach * quantityOrdered;
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
