package ManagerCheck;

import dashboardMana.Dashboard;
import employeeMana.Employee;
import employeeMana.EmployeeList;

public class ManagerCheck {

    public ManagerCheck(){
    }
    public static  boolean isManager() {
        for (Employee employee : EmployeeList.data) {
            if (employee.getEmployeeFirstName().equals(Dashboard.getUserFname()) && employee.getEmployeeLastName().equals(Dashboard.getUserLname())) {
                return employee.getTitle().equals("Manager");
            }
        }
        return false;
    }
}
