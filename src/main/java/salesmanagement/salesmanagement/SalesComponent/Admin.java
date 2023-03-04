package salesmanagement.salesmanagement.SalesComponent;

import salesmanagement.salesmanagement.SQLConnection;

public class Admin extends Employee {

    public Admin(SQLConnection sqlConnection, int employeeNumber) {
        super(sqlConnection, employeeNumber);
    }
}
