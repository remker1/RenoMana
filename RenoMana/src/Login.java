import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends BasicPage {
    @Override
    public void start(Stage stage) {

        // Hello label message
        Label helloLabel = new Label("Hello!");
        helloLabel.setFont(new Font(36));
        HBox helloCentre = new HBox(); // HBox to centre the greeting label
        helloCentre.setAlignment(Pos.CENTER);
        helloCentre.getChildren().add(helloLabel);

        // Username label that prompts text field
        Label userLabel = new Label("Username: ");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");

        // Password label that prompts password field
        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");

        // Check box to keep user logged into device
        CheckBox keepLogIn = new CheckBox("Keep me logged in!");

        // Log in button
        Button logInButton = new Button("Log In");
        logInButton.setFont(new Font(18));
        HBox logInCentre = new HBox(); // HBox to centre button
        logInCentre.setAlignment(Pos.CENTER);
        logInCentre.getChildren().add(logInButton);

        logInButton.setOnAction(event -> {
            String username = userField.getText();
            String password = passField.getText();

            System.out.println(username + " + " + password);

            try {
                // Perform the login and get cookies
                COOKIES = login();

            } catch (IOException e) {
                e.printStackTrace();
            }

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

        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    private static String login() throws IOException {


        return "Hello";
    }

    public static void main(String[] args) {
        launch();
    }
}