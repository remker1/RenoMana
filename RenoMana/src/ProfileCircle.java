import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ProfileCircle {
    private Circle profileCircle;
    private MainPage mainPage;
    private Stage profileStage; // Keep a reference to the profile window
    private VBox profileLayout;
    private VBox settingsLayout;
    private Label modeLabel;

    public ProfileCircle(MainPage mainPage) {
        this.mainPage = mainPage;
        this.profileCircle = createProfileCircle();
    }

    private Circle createProfileCircle() {
        Circle circle = new Circle(30);
        circle.setStyle("-fx-background-color: #9E1C29; -fx-stroke: #7C1715; -fx-border-radius: 2;");
        circle.setOnMouseClicked(event -> openProfileWindow());
        return circle;
    }

    private void openProfileWindow() {
        profileStage = new Stage();
        TabPane tabPane = new TabPane();

        // Profile Tab
        Tab profileTab = new Tab("Profile");
        profileTab.setClosable(false);
        profileLayout = new VBox(20);
        Text employeeName = new Text("Employee Full Name: John Doe");
        Text companyAddress = new Text("Company Address: 123 Main St, City, Country");
        Text companyEmail = new Text("Company Email: contact@company.com");
        profileLayout.getChildren().addAll(employeeName, companyAddress, companyEmail);
        profileTab.setContent(profileLayout);

        // Settings Tab
        Tab settingsTab = new Tab("Settings");
        settingsTab.setClosable(false);
        settingsLayout = new VBox(20);
        Slider modeSlider = mainPage.createModeSlider();

        if (mainPage.isDarkMode()){
            modeLabel = new Label("Dark Mode");
        } else {
            modeLabel = new Label("Light Mode");
        }


        settingsLayout.getChildren().addAll(modeLabel, modeSlider);
        settingsTab.setContent(settingsLayout);

        tabPane.getTabs().addAll(profileTab, settingsTab);

        Scene profileScene = new Scene(tabPane, 400, 300);
        profileStage.setScene(profileScene);
        profileStage.setTitle("Profile & Settings");
        profileStage.show();

        updateProfileWindowColors();
    }

    public void updateProfileWindowColors() {
        if (mainPage.isDarkMode()) {
            profileLayout.setStyle("-fx-background-color: #1C1C1C;");
            settingsLayout.setStyle("-fx-background-color: #1C1C1C;");
            modeLabel.setTextFill(Color.WHITE);
            profileStage.getScene().setFill(Color.web("#1C1C1C"));
        } else {
            profileLayout.setStyle("-fx-background-color: lightGray;");
            settingsLayout.setStyle("-fx-background-color: lightGray;");
            modeLabel.setTextFill(Color.BLACK);
            profileStage.getScene().setFill(Color.web("lightGray"));
        }
    }

    public Circle getProfileCircle() {
        return profileCircle;
    }
}
