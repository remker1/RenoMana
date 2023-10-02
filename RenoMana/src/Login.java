import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Drawing the circle for face
        Circle face = new Circle(125, 125, 100);
        face.setFill(Color.GOLD);
        face.setStroke(Color.BLACK);

        // Drawing the rightEye rectangle
        Rectangle rightEye = new Rectangle(86,75,10,50);
        rightEye.setFill(Color.BLACK);
        rightEye.setStroke(Color.BLACK);

        // Drawing the leftEye rectangle
        Rectangle leftEye = new Rectangle(162, 75, 10,50);
        leftEye.setFill(Color.BLACK);
        leftEye.setStroke(Color.BLACK);

        // Drawing the mouth rectangle
        Rectangle mouth = new Rectangle(86, 175, 86,10);
        mouth.setFill(Color.BLACK);
        mouth.setStroke(Color.BLACK);

        // Words showing the face type
        Text caption = new Text(80, 250, "Robo Face");
        caption.setFill(Color.BLUE);
        caption.setFont(Font.font( 15));

        // group everything in a group
        Group root = new Group(face, rightEye, leftEye, mouth, caption);
        // draw everything on scene, create the window, color background wheat color.
        Scene scene = new Scene(root, 250, 275, Color.WHEAT);

        // creat window
        stage.setScene(scene);
        stage.setTitle("Face Design");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
