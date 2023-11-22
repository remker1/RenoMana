import javafx.application.Application;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

public class BasicPage extends Application {
    public static String COOKIES;
    public static final Preferences prefs = Preferences.userNodeForPackage(BasicPage.class);

    @Override
    public void start(Stage stage) throws Exception {

    }

    public static String parseJson(String string, String target) {
        // Find the index of the cookie key
        int startIndex = string.indexOf("\"" + target + "\"");

        // Check if the key is found
        if (startIndex != -1) {
            // Move the index to the start of the value
            startIndex = string.indexOf(":", startIndex) + 1;

            // Find the end of the value (up to the next comma or the end of the JSON object)
            int endIndex = string.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = string.indexOf("}", startIndex);
            }

            // Extract the value
            String value = string.substring(startIndex, endIndex).trim().replace("\"", "");

            return value;

        } else {
            System.out.println(target + " not found in the response");
            return null;
        }
    }
}
