import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Scheduler extends Application {

    // to display schedule in a table format
    private TableView<String> table = new TableView<>();

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
        TableColumn<String, String> projName = new TableColumn<>("Name");
        TableColumn<String, String> projTimeline = new TableColumn<>("Timeline");
        projTimeline.setPrefWidth(500); // timeline will contain colour coded blocks for each project phase
        TableColumn<String, String> projDetails = new TableColumn<>("Details");
        TableColumn<String, String> projMembers = new TableColumn<>("Members");

        // dropdown view for project members column;
        projMembers.setCellFactory(param -> new TableCell<>() {
            final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Member A", "Member B", "Member C"));

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                // display dropdown menu if cell is not empty
                if (!empty) {
                    setGraphic(comboBox);
                } else {
                    setGraphic(null);
                }
            }
        });

        // add all defined columns to TableView
        List<TableColumn<String, String>> columns = Arrays.asList(projName, projTimeline, projDetails, projMembers);
        table.getColumns().addAll(columns);


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

    public static void main(String[] args) {
        launch();
    }
}