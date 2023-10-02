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

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Label helloLabel = new Label("Welcome!");
        helloLabel.setFont(new Font(36));
        HBox helloCentre = new HBox();
        helloCentre.setAlignment(Pos.CENTER);
        helloCentre.getChildren().add(helloLabel);

        Label userLabel = new Label("Username: ");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");

        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");

        CheckBox keepLogIn = new CheckBox("Keep me logged in!");

        Button logInButton = new Button("Log In");
        logInButton.setFont(new Font(18));
        HBox logInCentre = new HBox();
        logInCentre.setAlignment(Pos.CENTER);
        logInCentre.getChildren().add(logInButton);

        Button newButton = new Button("New?");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(helloCentre, userLabel, userField, passLabel, passField, keepLogIn, logInCentre, newButton);

        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}