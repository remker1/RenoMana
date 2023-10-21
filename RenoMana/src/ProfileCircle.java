import javafx.scene.shape.Circle;

public class ProfileCircle {
    private Circle profileCircle;
    private MainPage mainPage;

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
        mainPage.openProfileWindow();
    }

    public Circle getProfileCircle() {
        return profileCircle;
    }
}
