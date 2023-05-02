package salesmanagement.salesmanagement.SalesComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer implements SalesComponent{
    private int customerNumber;
    private String name = "";
    private String contact = "";
    private String address;
    private String rank;

    boolean isNewUser = true;

    public Customer() {
        name = "";
        contact = "";
        address = "";
        rank = null;
    }

    public Customer(ResultSet customerInfo) throws SQLException {
        isNewUser = false;
        this.customerNumber = customerInfo.getInt("customerNumber");
        this.name = customerInfo.getString("customerName");
        if (name == null) name = "";
        this.contact = customerInfo.getString("phone");
        if (contact == null) contact = "";
        this.address = customerInfo.getString("addressLine");
        if (address == null) address = "";
        this.rank = customerInfo.getString("rank");
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

    public boolean isNewUser() {
        return isNewUser;
    }
}