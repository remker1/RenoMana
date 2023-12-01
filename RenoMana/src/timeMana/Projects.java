package timeMana;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Projects {
    private ObservableList<Project> projects;

    public Projects() {
        this.projects = FXCollections.observableArrayList();
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ObservableList<Project> projects) {
        this.projects = projects;
    }
}