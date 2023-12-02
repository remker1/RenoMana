package employeeMana;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import timeMana.Project;
import timeMana.Scheduler;

import java.util.Arrays;


public class Employee {

    private SimpleStringProperty employeeID;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty cellNumber;
    private SimpleStringProperty eMail;
    private SimpleStringProperty projectsNameString;
    private ObservableList<Project> projects;
    private SimpleStringProperty title;
    private SimpleStringProperty username;
    private SimpleStringProperty _id;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public Employee(@JsonProperty("_id") String _id,
                    @JsonProperty("id") String employeeID,
                    @JsonProperty("fname") String firstName,
                    @JsonProperty("lname") String lastName,
                    @JsonProperty("projects") String projectsNameString,
                    @JsonProperty("cellNumber") String cellNumber,
                    @JsonProperty("email") String eMail,
                    @JsonProperty("title") String title,
                    @JsonProperty("username") String username){

        this.employeeID = new SimpleStringProperty(employeeID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.cellNumber = new SimpleStringProperty(cellNumber);
        this.eMail = new SimpleStringProperty(eMail);
        this.projectsNameString = new SimpleStringProperty(projectsNameString);
        this.projects = FXCollections.observableArrayList();
        this.title = new SimpleStringProperty(title);
        this.username = new SimpleStringProperty(username);
        this._id = new SimpleStringProperty(_id);

    }

    public String get_id() {
        return _id.get();
    }

    public SimpleStringProperty _idProperty() {
        return _id;
    }
    public String getEmployeeFirstName() {

        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {

        return firstName;
    }

    public String getEmployeeLastName() {

        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {

        return lastName;
    }

    public String getEmployeeID() {
        return employeeID.get();
    }

    public SimpleStringProperty employeeIDProperty() {
        return employeeID;
    }

    public ObservableList<Project> getProjects() {

        String[] lines = projectsNameString.get().split("\\.");
        for (String line: lines){
            Project assignedProject = Scheduler.searchProjectByName(line);
            if (assignedProject == null && searchEmployeeProject(line) != null){
                projects.remove(assignedProject);
            }else if(assignedProject != null && !projects.contains(assignedProject)){
                projects.add(assignedProject);
            }
        }
        return projects;
    }

    public Project searchEmployeeProject(String pName){
        for (Project project:projects){
            if (project != null && project.getName().equals(pName)){
                return project;
            }
        }
        return null;
    }

    public String getProjectsNameString(){
        StringBuilder projectResult = new StringBuilder();
        for(Project project:projects){
            if (project!= null){
                projectResult.append(project.getName()).append(".");
            }

        }
        projectsNameString.set(projectResult.toString());
        return projectsNameString.get();
    }

    public SimpleStringProperty projectsNameStringProperty(){
        return projectsNameString;
    }

    public void addProject2Employee(Project project){
        projects.add(project);
    }
    public void removeProject2Employee(Project project){
        projects.remove(project);
    }

    public String getCell() {return cellNumber.get();}

    public SimpleStringProperty cellProperty() {
        return cellNumber;
    }

    public String getEMail() {return eMail.get();}

    public SimpleStringProperty eMailProperty() {
        return eMail;
    }

    public String getTitle() {return title.get();}

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getUsername() {return username.get();}

    public SimpleStringProperty usernameProperty() {
        return username;
    }

}


