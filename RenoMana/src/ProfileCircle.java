import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.io.File;

/**
 * The ProfileCircle class represents a user's profile in the application.
 * It provides functionalities to view and edit the profile and settings.
 */
public class ProfileCircle {
    private Circle profileCircle;
    private MainPage mainPage;
    private Stage profileStage; // Window for the profile and settings
    private VBox profileLayout;
    private VBox settingsLayout;
    private Label modeLabel;
    private Circle bigProfileCircle; // Circle for the profile picture

    /**
     * Constructor for the ProfileCircle class.
     * @param mainPage Reference to the main page of the application.
     */
    public ProfileCircle(MainPage mainPage) {
        this.mainPage = mainPage;
        this.profileCircle = createProfileCircle();
    }

    /**
     * This method creates a clickable circle that represents the user's profile.
     * @return A Circle object.
     */
    private Circle createProfileCircle() {
        Circle circle = new Circle(30);
        circle.setStyle("-fx-background-color: #9E1C29; -fx-stroke: #7C1715; -fx-border-radius: 2;");
        circle.setOnMouseClicked(event -> openProfileWindow());
        return circle;
    }

    /**
     * This method opens a window where the user can view and edit their profile and settings.
     */
    private void openProfileWindow() {
        profileStage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER); // Align content to start from the top

        // Create buttons for switching between profile and settings
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button profileBtn = new Button("Profile");
        Button settingsBtn = new Button("Settings");

        // Style the buttons to be transparent
        profileBtn.setStyle("-fx-background-color: transparent; -fx-border-color: darkGray; -fx-border-width: 2;");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-border-color: darkGray; -fx-border-width: 2;");

        buttonBox.getChildren().addAll(profileBtn, settingsBtn);

        // Create a circle for the profile picture and allow users to upload an image
        bigProfileCircle = new Circle(50);
        bigProfileCircle.setFill(Color.GRAY); // Default color
        bigProfileCircle.setOnMouseClicked(event -> uploadProfilePicture());

        // Layout for the profile information
        profileLayout = new VBox(10);
        profileLayout.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Employee Full Name");
        TextField nameField = new TextField("John Doe");
        Label addressLabel = new Label("Company Address");
        TextField addressField = new TextField("123 Main St, City, Country");
        Label emailLabel = new Label("Company Email");
        TextField emailField = new TextField("contact@company.com");

        nameField.setEditable(false);
        addressField.setEditable(false);
        emailField.setEditable(false);
        profileLayout.getChildren().addAll(bigProfileCircle, nameLabel, nameField, addressLabel, addressField, emailLabel, emailField);

        // Layout for the settings
        settingsLayout = new VBox(10);
        settingsLayout.setAlignment(Pos.CENTER);
        Slider modeSlider = mainPage.createModeSlider();
        if (mainPage.isDarkMode()){
            modeLabel = new Label("Dark Mode");
        } else {
            modeLabel = new Label("Light Mode");
        }
        settingsLayout.getChildren().addAll(modeLabel, modeSlider);

        root.getChildren().addAll(buttonBox, profileLayout);

        // Switch between profile and settings layouts when buttons are clicked
        profileBtn.setOnAction(e -> {
            root.getChildren().remove(settingsLayout);
            root.getChildren().add(profileLayout);
        });

        settingsBtn.setOnAction(e -> {
            root.getChildren().remove(profileLayout);
            root.getChildren().add(settingsLayout);
        });

        Scene profileScene = new Scene(root, 400, 400);
        profileStage.setScene(profileScene);
        profileStage.setTitle("Profile & Settings");
        profileStage.show();
    }

    /**
     * This method allows the user to upload a profile picture from their computer.
     */
    private void uploadProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(profileStage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            bigProfileCircle.setFill(new ImagePattern(image));
        }
    }

    /**
     * This method updates the colors of the main page's windows based on the selected mode (dark or light).
     */
    public void updateProfileWindowColors() {
        if (mainPage.isDarkMode()) {
            mainPage.getContentArea().setStyle("-fx-background-color: #1C1C1C;");
            mainPage.getContentTitle().setTextFill(Color.WHITE);
            mainPage.getMainLayout().setStyle("-fx-background-color: #1C1C1C;");
        } else {
            mainPage.getContentArea().setStyle("-fx-background-color: lightGray;");
            mainPage.getContentTitle().setTextFill(Color.BLACK);
            mainPage.getMainLayout().setStyle("-fx-background-color: lightGray;");
        }
    }

    /**
     * This method returns the circle representing the user's profile.
     * @return A Circle object.
     */
    public Circle getProfileCircle() {
        return profileCircle;
    }
}
