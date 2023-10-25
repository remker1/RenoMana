import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;


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

    // Fields to store the employee details
    private Text employeeName;
    private Text companyAddress;
    private Text companyEmail;

    private static final String PROFILE_PICTURE_PATH = "profile_picture.jpg";
    private static final String JSON_DATA_PATH = "profile_data.json";

    /**
     * Constructor for the ProfileCircle class.
     * @param mainPage Reference to the main page of the application.
     */
    public ProfileCircle(MainPage mainPage) {
        this.mainPage = mainPage;
        this.profileCircle = createProfileCircle();
        this.employeeName = new Text();
        this.companyAddress = new Text();
        this.companyEmail = new Text();
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
    public void openProfileWindow() {
        profileStage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER); // Align content to start from the top

        // Create buttons for switching between profile and settings
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button profileBtn = new Button("Profile");
        Button settingsBtn = new Button("Settings");
        Button importBtn = new Button("Import JSON");

        // Style the buttons to be transparent
        profileBtn.setStyle("-fx-background-color: transparent; -fx-border-color: darkGray; -fx-border-width: 2;");
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-border-color: darkGray; -fx-border-width: 2;");
        importBtn.setStyle("-fx-background-color: transparent; -fx-border-color: darkGray; -fx-border-width: 2;");

        // Switch between profile and settings layouts when buttons are clicked
        profileBtn.setOnAction(e -> {
            root.getChildren().remove(settingsLayout);
            root.getChildren().add(profileLayout);
        });

        settingsBtn.setOnAction(e -> {
            root.getChildren().remove(profileLayout);
            root.getChildren().add(settingsLayout);
        });

        importBtn.setOnAction(e -> {
            importJsonFile();
        });

        buttonBox.getChildren().addAll(profileBtn, settingsBtn, importBtn);

        // Create a circle for the profile picture and allow users to upload an image
        bigProfileCircle = new Circle(50);
        bigProfileCircle.setFill(Color.GRAY); // Default color
        bigProfileCircle.setOnMouseClicked(event -> uploadProfilePicture());

        // Layout for the profile information
        profileLayout = new VBox(10);
        profileLayout.setAlignment(Pos.CENTER);
        profileLayout.getChildren().addAll(bigProfileCircle, employeeName, companyAddress, companyEmail);

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

        // Load saved profile picture and JSON data
        loadSavedProfilePic();
        loadSavedJson();

        Scene profileScene = new Scene(root, 400, 400);
        profileStage.setScene(profileScene);
        profileStage.setTitle("Profile & Settings");
        profileStage.show();
    }

    // Add a method to load the saved JSON data
    private void loadSavedJson() {
        File savedJson = new File(JSON_DATA_PATH);
        if (savedJson.exists()) {
            try {
                String content = new String(Files.readAllBytes(savedJson.toPath()));
                String employeeNameValue = extractFromJson(content, "employeeName");
                String companyAddressValue = extractFromJson(content, "companyAddress");
                String companyEmailValue = extractFromJson(content, "companyEmail");

                employeeName.setText("Employee Full Name: " + employeeNameValue);
                companyAddress.setText("Company Address: " + companyAddressValue);
                companyEmail.setText("Company Email: " + companyEmailValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Add a method to load the saved profile picture
    private void loadSavedProfilePic() {
        File savedImage = new File(PROFILE_PICTURE_PATH);
        if (savedImage.exists()) {
            Image image = new Image(savedImage.toURI().toString());
            bigProfileCircle.setFill(new ImagePattern(image));
        }
    }

    /**
     * This method extracts a specific value from a JSON string based on a given key.
     * For example, if you have a JSON like {"name":"John"} and you want to get "John",
     * you'd use this method with the key "name".
     *
     * @param json The JSON string you want to search in.
     * @param key The key whose value you want to find.
     * @return The value associated with the given key, or null if the key isn't found.
     */
    private String extractFromJson(String json, String key) {
        String searchPattern = "\"" + key + "\":";
        int startIndex = json.indexOf(searchPattern);
        if (startIndex == -1) return null;

        startIndex += searchPattern.length();
        // Skip any whitespace after the colon
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        // Ensure the next character is a quote
        if (startIndex >= json.length() || json.charAt(startIndex) != '\"') return null;
        startIndex++;  // Skip the quote

        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) return null;

        return json.substring(startIndex, endIndex);
    }




    /**
     * This method lets you pick a JSON file from your computer and then
     * reads the file to get details like employee name, company address, and email.
     * After reading, it updates the screen with these details.
     */
    private void importJsonFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(profileStage);

        if (selectedFile != null) {
            try {
                // Parse JSON and extract the values
                String content = new Scanner(selectedFile).useDelimiter("\\Z").next();
                String employeeNameValue = extractFromJson(content, "employeeName");
                String companyAddressValue = extractFromJson(content, "companyAddress");
                String companyEmailValue = extractFromJson(content, "companyEmail");

                if (employeeNameValue != null) {
                    employeeName.setText("Employee Full Name: " + employeeNameValue);
                } else {
                    employeeName.setText("Employee Full Name: N/A");
                }

                if (companyAddressValue != null) {
                    companyAddress.setText("Company Address: " + companyAddressValue);
                } else {
                    companyAddress.setText("Company Address: N/A");
                }

                if (companyEmailValue != null) {
                    companyEmail.setText("Company Email: " + companyEmailValue);
                } else {
                    companyEmail.setText("Company Email: N/A");
                }

                // Save the JSON data to a file (will be deleted later when database is set up)
                try {
                    Files.write(Paths.get(JSON_DATA_PATH), content.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to import JSON");
                alert.setContentText("There was an error importing the JSON data. Please ensure the file format is correct.");
                alert.showAndWait();
            }

        }

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

            // Save the image to a file (will be deleted later when database is set up)
            try {
                Files.copy(selectedFile.toPath(), Paths.get(PROFILE_PICTURE_PATH), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
