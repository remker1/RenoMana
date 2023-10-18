import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.paint.CycleMethod;

public class Login extends Application {
    @Override
    public void start(Stage stage) {

        // Hello label message
        Label helloLabel = new Label("Hello!");
        helloLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        helloLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        HBox helloCentre = new HBox(); // HBox to centre the greeting label
        helloCentre.setAlignment(Pos.CENTER);
        helloCentre.getChildren().add(helloLabel);

        // Username label that prompts text field
        Label userLabel = new Label("Username: ");
        TextField userField = new TextField();
        userLabel.setStyle("-fx-text-fill: white;");
        userField.setPromptText("Enter your username");


        // Password label that prompts password field
        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passLabel.setStyle("-fx-text-fill: white;");
        passField.setPromptText("Enter your password");

        // Check box to keep user logged into device
        CheckBox keepLogIn = new CheckBox("Keep me logged in!");
        keepLogIn.setStyle("-fx-text-fill: white;");

        // Log in button
        Button logInButton = new Button("Log In");
        logInButton.setStyle("-fx-text-fill: white; -fx-background-color: #a29f9f");
        logInButton.setFont(new Font(18));
        HBox logInCentre = new HBox(); // HBox to centre button
        logInCentre.setAlignment(Pos.CENTER);
        logInCentre.getChildren().add(logInButton);

        // Set action for the login button
        logInButton.setOnAction(event -> {
            // Close the login stage
            stage.close();
            // Launch the main page
            try {
                new MainPage().start(new Stage());
            } catch (Exception e) {
                System.out.println("Something went wrong when going into main page.");
            }
        });

        // Registration button for new users
        Button newButton = new Button("New?");
        newButton.setStyle("-fx-text-fill: white; -fx-background-color: #a29f9f");


        // Set action for the registration button
        newButton.setOnAction(event -> {
            // Close the login stage
            stage.close();
            // Launch the registration page
            try {
                new Registration().start(new Stage());
            } catch (Exception e) {
                System.out.println("Something went wrong when going into registration page.");
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(helloCentre, userLabel, userField, passLabel, passField, keepLogIn, logInCentre, newButton);

        // Set background colour.
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#4B1517")),
                new Stop(1, Color.web("#C49102")));
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));



        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}