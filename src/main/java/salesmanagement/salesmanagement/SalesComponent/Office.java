package salesmanagement.salesmanagement.SalesComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Office implements SalesComponent {
    private int officeCode;
    private String phone;
    private String addressLine;
    private String region;

    public Office(ResultSet resultSet) throws SQLException {
        officeCode = resultSet.getInt("officeCode");
        phone = resultSet.getString("phone");
        addressLine = resultSet.getString("addressLine");
        region = resultSet.getString("region");
    }

    public int getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(int officeCode) {
        this.officeCode = officeCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
