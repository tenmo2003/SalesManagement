package salesmanagement.salesmanagement.SalesComponent;

public class Customer {
    private int customerNumber;
    private String name;
    private String contact;
    private String address;
    private String rank;

    public Customer(int customerNumber, String name, String contact, String address, String rank) {
        this.customerNumber = customerNumber;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.rank = rank;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}