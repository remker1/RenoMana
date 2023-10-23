package timeMana;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Scheduler extends VBox {

    private TableView<Project> table;
    private ObservableList<Project> data;

    public Scheduler() {
        // Project Schedule label
        final Label label = new Label("Projects Schedule");
        label.setFont(new Font("Arial", 20));

        // initialize data
        data = FXCollections.observableArrayList();

        // make table editable
        // to display schedule in a table format
        table = new TableView<>();
        table.setEditable(true);

        // columns with appropriate headers
        TableColumn<Project, String> projName = new TableColumn<>("Name");
        projName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Project, String> projTimeline = new TableColumn<>("Timeline");
        projTimeline.setCellValueFactory(cellData -> cellData.getValue().timelineProperty());
        projTimeline.setPrefWidth(500);

        TableColumn<Project, String> projDetails = new TableColumn<>("Details");
        projDetails.setCellValueFactory(cellData -> cellData.getValue().detailsProperty());

        TableColumn<Project, String> projMembers = new TableColumn<>("Members");
        projMembers.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().membersProperty().get(0)));


        // add all defined columns to TableView
        table.getColumns().addAll(projName, projTimeline, projDetails, projMembers);

        // add, modify, and delete buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> addProject());

        Button modifyButton = new Button("Modify");
        modifyButton.setOnAction(actionEvent -> modifyProject());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> deleteProject());

        HBox buttonBox = new HBox(10, addButton, modifyButton, deleteButton);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));

        this.getChildren().addAll(label, table, buttonBox);
        table.setItems(data);
    }

    private void addProject() {
        TextInputDialog nameInput = new TextInputDialog();
        nameInput.setTitle("Add New Project");
        nameInput.setHeaderText("Enter Project Name");
        String projectName = nameInput.showAndWait().orElse("");

        for (Project project : data) {
            if (project.getName().equals(projectName)) {
                Alert duplicateAlert = new Alert(Alert.AlertType.ERROR);
                duplicateAlert.setTitle("Error!");
                duplicateAlert.setHeaderText("Project already exists!");
                duplicateAlert.setContentText("Please choose a different project name.");
                duplicateAlert.showAndWait();
                return;
            }
        }

        TextInputDialog timelineInput = new TextInputDialog();
        timelineInput.setHeaderText("Enter Project Timeline");
        String projectTimeline = timelineInput.showAndWait().orElse("");

        TextInputDialog detailsInput = new TextInputDialog();
        detailsInput.setHeaderText("Enter Project Details");
        String projectDetails = detailsInput.showAndWait().orElse("");

        List<String> choices = Arrays.asList("Member A", "Member B", "Member C");
        ChoiceDialog<String> memberDialog = new ChoiceDialog<>("Member A", choices);
        memberDialog.setTitle("Choose a Member");
        memberDialog.setHeaderText("Choose a Project Member");
        String selectedMember = memberDialog.showAndWait().orElse("");

        Project newProject = new Project(
                new SimpleStringProperty(projectName),
                new SimpleStringProperty(projectTimeline),
                new SimpleStringProperty(projectDetails),
                new SimpleListProperty<>(FXCollections.observableArrayList(selectedMember))
        );

        data.add(newProject);
    }

    private void modifyProject() {
        Project selectedProject = table.getSelectionModel().getSelectedItem();

        if (selectedProject == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No project is selected!");
            noSelectedAlert.setContentText("Please select a project from the table to modify.");
            noSelectedAlert.showAndWait();
            return;
        }

        TextInputDialog nameInput = new TextInputDialog(selectedProject.getName());
        nameInput.setTitle("Modify Project");
        nameInput.setHeaderText("Enter New Project Name");
        String newProjectName = nameInput.showAndWait().orElse("");

        TextInputDialog timelineInput = new TextInputDialog(selectedProject.getTimeline());
        timelineInput.setHeaderText("Enter New Project Timeline");
        String newProjectTimeline = timelineInput.showAndWait().orElse("");

        TextInputDialog detailsInput = new TextInputDialog(selectedProject.getDetails());
        detailsInput.setHeaderText("Enter New Project Details");
        String newProjectDetails = detailsInput.showAndWait().orElse("");

        List<String> choices = Arrays.asList("Member A", "Member B", "Member C");
        ChoiceDialog<String> memberDialog = new ChoiceDialog<>(selectedProject.getMembers().get(0), choices);
        memberDialog.setTitle("Choose a Member");
        memberDialog.setHeaderText("Choose a Project Member");
        String selectedMember = memberDialog.showAndWait().orElse("");

        selectedProject.setName(newProjectName);
        selectedProject.setTimeline(newProjectTimeline);
        selectedProject.setDetails(newProjectDetails);
        selectedProject.setMembers(FXCollections.observableArrayList(selectedMember));
    }


    private void deleteProject() {
        Project selectedProject = table.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            data.remove(selectedProject);
        } else {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No project is selected!");
            noSelectedAlert.setContentText("Please select a project from the table to delete.");
            noSelectedAlert.showAndWait();
        }
    }

}