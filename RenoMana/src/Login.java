import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Login extends BasicPage {

    private Label errorLabel = new Label();
    private LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4B1517")),
            new Stop(1, Color.web("#C49102")));
    @Override
    public void start(Stage stage) {

        // Hello label message
        Label helloLabel = new Label("Hello!");
        helloLabel.setFont(new Font(36));
        HBox helloCentre = new HBox(); // HBox to centre the greeting label
        helloCentre.setAlignment(Pos.CENTER);
        helloCentre.getChildren().add(helloLabel);
        helloLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold");


        // Username label that prompts text field
        Label userLabel = new Label("Username: ");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");
        userLabel.setStyle("-fx-text-fill: white;");



        // Password label that prompts password field
        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passLabel.setStyle("-fx-text-fill: white;");

        // Check box to keep user logged into device
        CheckBox keepLogIn = new CheckBox("Keep me logged in!");
        keepLogIn.setStyle("-fx-text-fill: white;");

        // Log in button
        Button logInButton = new Button("Log In");
        logInButton.setFont(new Font(18));
        logInButton.setStyle("-fx-text-fill: white; -fx-background-color: #a29f9f");
        HBox logInCentre = new HBox(); // HBox to centre button
        logInCentre.setAlignment(Pos.CENTER);
        logInCentre.getChildren().add(logInButton);

        // Set action for the login button
        logInButton.setOnAction(event -> {

            int status;
            try {
                status = login(userField.getText(), passField.getText());
                System.out.println("Cookies: " + COOKIES);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (status == 200) {
                // Close the login stage
                stage.close();
                // Launch the main page
                try {
                    new MainPage().start(new Stage());
                } catch (Exception e) {
                    System.out.println("Something went wrong when going into main page.");
                }
            }
            else {
                displayErrorMessage("Invalid username or password");
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
        root.getChildren().addAll(helloCentre, userLabel, userField, passLabel, passField, keepLogIn, logInCentre, newButton, errorLabel);
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));


        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();


    }

    public static int login(String username, String password) throws IOException, InterruptedException {
        String msg = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";

        System.out.println(msg);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5000/login"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println(request.toString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        // Find the index of the cookie key
        int startIndex = responseBody.indexOf("\"cookie\"");

        // Check if the key is found
        if (startIndex != -1) {
            // Move the index to the start of the value
            startIndex = responseBody.indexOf(":", startIndex) + 1;

            // Find the end of the value (up to the next comma or the end of the JSON object)
            int endIndex = responseBody.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = responseBody.indexOf("}", startIndex);
            }

            // Extract the value
            String value = responseBody.substring(startIndex, endIndex).trim();

            System.out.println("Value for 'cookie': " + value);
            if (value != "") {
                COOKIES = value;
            }
            else {
                COOKIES = null;
            }
            return response.statusCode();

        } else {
            System.out.println("'cookie' not found in the response");
            COOKIES = null;
            return response.statusCode();
        }

    }

    // Add this method to display an error message
    private void displayErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.RED);
    }

    public static void main(String[] args) {
        launch();
    }
}