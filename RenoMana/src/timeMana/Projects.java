package timeMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import employeeMana.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Projects {
    private List<Project> projects;
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Projects(@JsonProperty("times")List<Project> projects){
            this.projects = projects;
    }
    public Projects(){
    }

    public List<Project> getProjects() {
        return projects;
    }

    public String getProjectsToString(){
        return projects.toString();
    }
}