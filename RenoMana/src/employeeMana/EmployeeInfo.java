package employeeMana;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import timeMana.Project;



/**
 * EmployeeInfo class displays detailed information about a selected employee.
 */
public class EmployeeInfo extends Application {

    private Employee employee;

    /**
     * Constructor for EmployeeInfo class.
     *
     * @param selectedEmployee The employee for which detailed information is displayed.
     */
    public EmployeeInfo(Employee selectedEmployee) {
        this.employee = selectedEmployee;
    }

    /**
     * The entry point of the JavaFX application. Displays employee information in a window.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Info");

        // Create text elements to display employee information
        Text employeeTitle = new Text("Job Title: " + employee.getTitle());
        Text employeeName = new Text("Full Name: " + employee.getEmployeeFirstName()+" "+employee.getEmployeeLastName());
        Text employeeID = new Text("Employee ID: " + employee.getEmployeeID());
        Text employeeEmail = new Text("Email: " + employee.getEMail());
        Text employeeCell = new Text("Cell Number: " + employee.getCell());

        Text projectMsg = new Text("Current Projects:");
        ObservableList<Project> projectList = employee.getProjects();
        Text projectsString;
        if (projectList.isEmpty()){
            projectsString = new Text("No project assigned to this employee yet...");
        }else{
            int index = 1;
            StringBuffer result = new StringBuffer("");
            for (Project project: projectList){
                result.append("Project: "+index+
                        "\nProject Name: "+ project.getName()+
                        "\nProject Deadline: "+ project.getTimeline()+
                        "\nProject Description: "+ project.getDetails()+"\n\n");
                index++;
            }
            projectsString = new Text(result.toString());
        }

        // Create VBox containers for organizing the text elements
        VBox nameBox = new VBox(employeeTitle, employeeName, employeeID);
        VBox contactBox = new VBox(employeeEmail, employeeCell);
        VBox projectBox = new VBox(projectMsg, projectsString);

        // Create a main VBox container for the entire employee information layout
        VBox employeeInfo = new VBox(nameBox, contactBox, projectBox);

        // Wrap the employeeInfo VBox in a ScrollPane for handling overflow
        ScrollPane infoPane = new ScrollPane();
        infoPane.setContent(employeeInfo);

        // Create the scene and set it on the primary stage
        Scene scene = new Scene(infoPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
