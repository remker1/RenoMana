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

    private TableView table = new TableView();

    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Scheduler");
        stage.setWidth(780);
        stage.setHeight(500);

        final Label label = new Label("Projects Schedule");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn projName = new TableColumn("Name");
        TableColumn projTimeline = new TableColumn("Timeline");
        projTimeline.setPrefWidth(500);
        TableColumn projDetails = new TableColumn("Details");
        TableColumn projMembers = new TableColumn("Members");

        projMembers.setCellFactory(param -> new TableCell() {
            final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("Member A", "Member B", "Member C"));

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(comboBox);
                } else {
                    setGraphic(null);
                }
            }
        });

        table.getColumns().addAll(projName, projTimeline, projDetails, projMembers);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}