package timeMana;

import employeeMana.Employee;
import employeeMana.EmployeeList;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Scheduler extends VBox {

    // table to display projects
    private final TableView<Project> table;

    // list that holds the data for the table
    public static ObservableList<Project> data;

    public static ObservableList<String> projectsTimelineList;

    /***
     * Constructor for Scheduler UI
     */
    public Scheduler(String COOKIES) {
        // Project Schedule label
        final Label label = new Label("Projects Schedule");
        label.setFont(new Font("Arial", 20));

        // initialize data
        data = FXCollections.observableArrayList();
        projectsTimelineList = FXCollections.observableArrayList();

        // make table editable
        // to display schedule in a table format
        table = new TableView<>();
        table.setEditable(true);

        // columns with appropriate headers
        TableColumn<Project, String> projName = new TableColumn<>("Name");
        projName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        projName.prefWidthProperty().bind(table.widthProperty().multiply(0.20));

        TableColumn<Project, String> projTimeline = new TableColumn<>("Timeline");
        // set cell value factory to use timeline property of the Project class
        projTimeline.setCellValueFactory(cellData -> cellData.getValue().timelineProperty());
        projTimeline.setCellFactory(column -> new TableCell<Project, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // check if cell is empty or not
                if (empty || item == null) {
                    // clear the text and style if the cell is empty
                    setText(null);
                    setStyle("");
                } else {
                    // retrieve the project object associated with current row
                    Project project = getTableView().getItems().get(getIndex());
                    if (project != null) {
                        // get timeline status from project object
                        String status = project.getTimelineStatus();
                        setText(item);

                        // change the cell's background colour based on timeline status
                        switch (status) {
                            case "green":
                                setStyle("-fx-background-color: lightgreen;");
                                break;
                            case "yellow":
                                setStyle("-fx-background-color: yellow;");
                                break;
                            case "red":
                                setStyle("-fx-background-color: salmon;");
                                break;
                            case "overdue":
                                setStyle("-fx-background-color: darkred;");
                                break;
                            default:
                                setStyle(""); // reset the style for any unknown status
                                break;
                        }
                    } else {
                        // clear the text and style if project is null
                        setText(null);
                        setStyle("");
                    }
                }
            }
        });
        projTimeline.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        TableColumn<Project, String> projDetails = new TableColumn<>("Details");
        projDetails.setCellValueFactory(cellData -> cellData.getValue().detailsProperty());
        projDetails.prefWidthProperty().bind(table.widthProperty().multiply(0.45));

        // currently only displays first member from the list of members
        TableColumn<Project, String> projMembers = new TableColumn<>("Members");
        projMembers.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().membersProperty().get(0)));
        projMembers.prefWidthProperty().bind(table.widthProperty().multiply(0.20));

        // add all defined columns to TableView
        table.getColumns().addAll(projName, projTimeline, projDetails, projMembers);

        // add, modify, and delete buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> addProject());

        Button modifyButton = new Button("Modify");
        modifyButton.setOnAction(actionEvent -> modifyProject());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> deleteProject());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(actionEvent -> {
            try {
                fetchProjects(COOKIES);
            } catch(Exception e){
                showAlert("Error!", "Something went wrong when fetching Projects!");
            }
        });

        // container for the buttons
        HBox buttonBox = new HBox(10, addButton, modifyButton, deleteButton, refreshButton);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));

        // add the label, table, and buttons to the main container
        this.getChildren().addAll(label, table, buttonBox);
        table.setItems(data); // bind the data list to the table
    }

    /***
     * Method to add a project to the table list
     */
    private void addProject() {
        // open a dialog box to prompt the user to enter the project name
        TextInputDialog nameInput = new TextInputDialog();
        nameInput.setTitle("Add New Project");
        nameInput.setHeaderText("Enter Project Name");
        String projectName = nameInput.showAndWait().orElse("");

        // check if a project with the entered name already exists
        for (Project project : data) {
            if (project.getName().equals(projectName)) {
                // show error alert and exit if project of the same name exists
                Alert duplicateAlert = new Alert(Alert.AlertType.ERROR);
                duplicateAlert.setTitle("Error!");
                duplicateAlert.setHeaderText("Project already exists!");
                duplicateAlert.setContentText("Please choose a different project name.");
                duplicateAlert.showAndWait();
                return;
            }
        }

        // prompt the user to enter the project timeline
        TextInputDialog timelineInput = new TextInputDialog();
        timelineInput.setHeaderText("Enter Project Timeline");
        String projectTimeline = timelineInput.showAndWait().orElse("");

        // prompt the user to enter project details
        TextInputDialog detailsInput = new TextInputDialog();
        detailsInput.setHeaderText("Enter Project Details");
        String projectDetails = detailsInput.showAndWait().orElse("");

        // provide choices for members and prompt user to select one (for now)
        ObservableList<String> choices = EmployeeList.employeeFirstNameList;
        ChoiceDialog<String> memberDialog = new ChoiceDialog<>("Members", choices);
        memberDialog.setTitle("Choose a Member");
        memberDialog.setHeaderText("Choose a Project Member");
        String selectedMember = memberDialog.showAndWait().orElse("");

        // create a new project instance using the user-provided info
        Project newProject = new Project(
                new SimpleStringProperty(projectName),
                new SimpleStringProperty(projectTimeline),
                new SimpleStringProperty(projectDetails),
                new SimpleListProperty<>(FXCollections.observableArrayList(selectedMember))
        );

        // add the project to data list
        data.add(newProject);
        projectsTimelineList.add(projectTimeline);
        table.refresh();

        int chosenEmployeeIdx = EmployeeList.employeeSearch(selectedMember);
        if (chosenEmployeeIdx == -1){
            Alert notFoundError = new Alert(Alert.AlertType.ERROR);
            notFoundError.setTitle("Error!");
            notFoundError.setHeaderText("Employee Search");
            notFoundError.setContentText("Employee Not Found! Please try again");
            notFoundError.showAndWait();
        } else{
            EmployeeList.data.get(chosenEmployeeIdx).addProject2Employee(data.getLast());
        }

    }

    /***
     * Method to modify a selected project
     */
    private void modifyProject() {
        // get currently selected project from the table
        Project selectedProject = table.getSelectionModel().getSelectedItem();

        // show an alert and return if no project is selected
        if (selectedProject == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No project is selected!");
            noSelectedAlert.setContentText("Please select a project from the table to modify.");
            noSelectedAlert.showAndWait();
            return;
        }

        // prompt the user to enter a new name for the project
        // defaulting to the current name of the selected project
        TextInputDialog nameInput = new TextInputDialog(selectedProject.getName());
        nameInput.setTitle("Modify Project");
        nameInput.setHeaderText("Enter New Project Name");
        String newProjectName = nameInput.showAndWait().orElse("");

        // get new timeline from user
        TextInputDialog timelineInput = new TextInputDialog(selectedProject.getTimeline());
        timelineInput.setHeaderText("Enter New Project Timeline");
        String newProjectTimeline = timelineInput.showAndWait().orElse("");

        // get new details from user
        TextInputDialog detailsInput = new TextInputDialog(selectedProject.getDetails());
        detailsInput.setHeaderText("Enter New Project Details");
        String newProjectDetails = detailsInput.showAndWait().orElse("");

        // provide choices for members and prompt the user to select one
        // defaults to current member of selected project
        ObservableList<String> choices = EmployeeList.employeeFirstNameList;
        ChoiceDialog<String> memberDialog = new ChoiceDialog<>(selectedProject.getMembers().get(0), choices);
        memberDialog.setTitle("Choose a Member");
        memberDialog.setHeaderText("Choose a Project Member");
        String selectedMember = memberDialog.showAndWait().orElse("");

        // update the selected project's details with new values provided
        selectedProject.setName(newProjectName);
        selectedProject.setTimeline(newProjectTimeline);
        selectedProject.setDetails(newProjectDetails);
        selectedProject.setMembers(FXCollections.observableArrayList(selectedMember));

        // TODO: Modification functionality for syncing modified project in case of changing members

        table.refresh();
    }

    private String fetchProjects(String COOKIES) throws IOException, InterruptedException {
        System.out.println(COOKIES);
        String msg = "{" +
                "\"username\":\"" + COOKIES +
                "\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getProjectsData"))
                .timeout(java.time.Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[DASHBOARD] " + request.toString());
        System.out.println(msg);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println("[DASHBOARD] " + responseBody);
        System.out.println();
        return responseBody;
    }


    /***
     * Method to delete a selected project
     */
    private void deleteProject() {

        // get currently selected project from table
        Project selectedProject = table.getSelectionModel().getSelectedItem();

        // remove selected project from data list
        if (selectedProject != null) {
            data.remove(selectedProject);
            for(Employee employee:EmployeeList.data){
                if (employee.getProjects().contains(selectedProject)){
                    employee.removeProject2Employee(selectedProject);
                }
            }
        } else { // show alert if no project is selected
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No project is selected!");
            noSelectedAlert.setContentText("Please select a project from the table to delete.");
            noSelectedAlert.showAndWait();
        }
    }

    private void showAlert(String title, String content) {
        Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
        invalidNumAlert.setTitle(title);
        invalidNumAlert.setHeaderText(null);
        invalidNumAlert.setContentText(content);
        invalidNumAlert.showAndWait();
    }

}