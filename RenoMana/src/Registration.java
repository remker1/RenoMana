import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;


public class Registration extends BasicPage {

    final double scene_width = 800;
    final double scene_height = 500;

    private Label errorLabel = new Label();

    @Override
    public void start(Stage stage) {
//        stage.getIcons().add(new Image("./resources/icon.png"));

        // Hello label message
        Label helloLabel = new Label("Registration");
        helloLabel.setFont(new Font(36));
        HBox helloCentre = new HBox(); // HBox to centre the greeting label
        helloCentre.setAlignment(Pos.CENTER);
        helloCentre.getChildren().add(helloLabel);

        // First Name label that prompts text field
        Label fnameLabel = new Label("First Name: ");
        TextField fnameField = new TextField();
        fnameField.setPromptText("Enter your First Name: ");

        // First Name label that prompts text field
        Label lnameLabel = new Label("Last Name: ");
        TextField lnameField = new TextField();
        lnameField.setPromptText("Enter your Last Name");

        // Username label that prompts text field
        Label userLabel = new Label("Username: ");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");

        // Password label that prompts password field
        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");

        // Verify Password label that prompts password field
        Label verifyPassLabel = new Label("Confirm your Password: ");
        PasswordField verifyPassField = new PasswordField();
        verifyPassField.setPromptText("Confirm your Password");

        // Email label that prompts text field
        Label emailLabel = new Label("Email: ");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your Email");

        // Cell number label that prompts text field
        Label cellLabel = new Label("Cell: ");
        TextField cellField = new TextField();
        cellField.setPromptText("Enter your Cellphone #");

        // Log in button
        Button registerButton = new Button("Complete Registration");
        registerButton.setFont(new Font(18));
        HBox logInCentre = new HBox(); // HBox to centre button
        logInCentre.setAlignment(Pos.CENTER);
        logInCentre.getChildren().add(registerButton);

        // Set action for the register button
        registerButton.setOnAction(event -> {
            try {
                if (passwordValid(passField.getText(), verifyPassField.getText())) {
                    register(fnameField.getText(), lnameField.getText(), userField.getText(), passField.getText(), emailField.getText(), cellField.getText());
                    // Close the login stage
                    stage.close();
                    // Launch the main page
                    try {
                        new Login().start(new Stage());
                    } catch (Exception e) {
                        System.out.println("Something went wrong when going into main page.");
                    }
                }
                else {
                    if (!(passField.getText().length() >= 8))
                        displayErrorMessage("Password must be 8 characters or longer!");
                    else if (!passField.getText().equals(verifyPassField.getText()))
                        displayErrorMessage("Passwords do not Match!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Registration button for new users
        Button newButton = new Button("Return to Login");

        // Set action for the registration button
        newButton.setOnAction(event -> {
            // Close the login stage
            stage.close();
            // Launch the registration page
            try {
                new Login().start(new Stage());
            } catch (Exception e) {
                System.out.println("Something went wrong when going into registration page.");
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(helloCentre, fnameLabel, fnameField, lnameLabel, lnameField, userLabel, userField, passLabel, passField, verifyPassLabel, verifyPassField, emailLabel, emailField, cellLabel, cellField, logInCentre, newButton, errorLabel);

        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();


    }

    private boolean passwordValid(String password, String verifyPassword) {
        return (password.equals(verifyPassword) && password.length() >= 8);
    }


    private static void register(String fname, String lname, String username, String password, String email, String cellNumber) throws IOException, InterruptedException {
        String msg = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"cellNumber\":\"" + cellNumber + "\"," +
                "\"fname\":\"" + fname + "\"," +
                "\"lname\":\"" + lname + "\"" +
                "}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5000/register"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println(request.toString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.toString());
    }

    private void displayErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.RED);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
