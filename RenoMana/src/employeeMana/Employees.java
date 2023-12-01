package employeeMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Employees {
    private List<Employee> employees;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Employees(@JsonProperty("employees") List<Employee> employees){
        this.employees = employees;
    }
    public Employees(){
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
