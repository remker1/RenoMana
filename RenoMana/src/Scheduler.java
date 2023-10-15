import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Scheduler extends Application {

    // to display schedule in a table format
    private TableView table = new TableView();

    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Scheduler");
        stage.setWidth(780);
        stage.setHeight(500);

        // Project Schedule label
        final Label label = new Label("Projects Schedule");
        label.setFont(new Font("Arial", 20));

        // make table editable
        table.setEditable(true);

        // columns with appropriate headers
        TableColumn projName = new TableColumn("Name");
        TableColumn projTimeline = new TableColumn("Timeline");
        projTimeline.setPrefWidth(500); // timeline will contain colour coded blocks for each project phase
        TableColumn projDetails = new TableColumn("Details");
        TableColumn projMembers = getTableColumn();

        // add all defined columns to TableView
        table.getColumns().addAll(projName, projTimeline, projDetails, projMembers);

        // VBox to organize each project vertically
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        // add VBox to the root of the scene
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    private static TableColumn getTableColumn() {
        TableColumn projMembers = new TableColumn("Members");

        // dropdown view for project members column
        projMembers.setCellFactory(param -> new TableCell() {
            final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Member A", "Member B", "Member C"));

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                // display dropdown menu if cell is not empty
                if (!empty) {
                    setGraphic(comboBox);
                } else {
                    setGraphic(null);
                }
            }
        });
        return projMembers;
    }

    public static void main(String[] args) {
        launch();
    }
}