package ManagerCheck;

import COOKIES.COOKIES;
import dashboardMana.Dashboard;
import employeeMana.Employee;
import employeeMana.EmployeeList;

public class ManagerCheck {

    public ManagerCheck(){
    }
    public static  boolean isManager(COOKIES COOKIE) {
        return COOKIE.getTitle().equals("Manager");
    }
}
