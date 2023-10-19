import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Registration extends BasicPage {

    final double scene_width = 800;
    final double scene_height = 500;

    @Override
    public void start(Stage stage) {

        // navigation menu for sections.
        Menu navi = new Menu("Sections");

        // menu options for different sections
        MenuItem prsnlInfo = new MenuItem("Personal Information");
        MenuItem cmpInfo = new MenuItem("Company Information");
        MenuItem cmpvalid = new MenuItem("Company Validation");

        // add all items to Sections menu
        navi.getItems().addAll(prsnlInfo,cmpInfo,cmpvalid);

        // navigation menu for back to login Page
        Menu backToLogin = new Menu("Login");
        MenuItem bkToLog = new MenuItem("Login Page");
        backToLogin.getItems().addAll(bkToLog);

        // menu and opstions are created, they do not work yet
        Menu theme = new Menu("Theme");
        MenuItem dkmd = new MenuItem("Dark Mode");
        MenuItem ltmd = new MenuItem("Light Mode");
        theme.getItems().addAll(dkmd,ltmd);

        // create a manu bar and add menu options to the navigation bar
        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(navi,backToLogin,theme);

        // inside of personal information section, this section includes all
        // parts within Personal Info Section.
        Label prsn_title = new Label("Personal Information:");
        prsn_title.setFont(new Font(28));

        // creating and modifying personal information section box
        VBox prsnInfoSect = new VBox();
        prsnInfoSect.setMinHeight(scene_height-mb.getHeight());
        prsnInfoSect.setMaxHeight(scene_height);
        prsnInfoSect.setPadding(new Insets(10));
        prsnInfoSect.getChildren().addAll(prsn_title);

        // same progress as personal information section box.
        Label cmpif_title = new Label("Company Information:");
        cmpif_title.setFont(new Font(28));

        VBox cmpInfoSect = new VBox();
        cmpInfoSect.setMinHeight(scene_height-mb.getHeight());
        cmpInfoSect.setMaxHeight(scene_height);
        cmpInfoSect.setPadding(new Insets(10));
        cmpInfoSect.getChildren().addAll(cmpif_title);

        Label cmpva_title = new Label("Company Validation:");
        cmpva_title.setFont(new Font(28));

        VBox cmpValidSect = new VBox();
        cmpValidSect.setMinHeight(scene_height-mb.getHeight());
        cmpValidSect.setMaxHeight(scene_height);
        cmpValidSect.setPadding(new Insets(10));
        cmpValidSect.getChildren().addAll(cmpva_title);

        VBox infoBox = new VBox();
        infoBox.setPrefWidth(scene_width);
        infoBox.getChildren().addAll(prsnInfoSect,cmpInfoSect,cmpValidSect);

        // scroll panel for easier scrolling down and such.
        ScrollPane sp = new ScrollPane();
        sp.setContent(infoBox);
        // hides horizontal scrollbar.
        sp.setFitToWidth(true);

        HBox infoPage = new HBox();
        infoPage.getChildren().addAll(infoBox,sp);

        bkToLog.setOnAction(actionEvent -> {
            stage.close();
            try {
                new Login().start(new Stage());
            } catch (Exception e) {
                System.out.println("Couldn't go back to Login Screen.");
            }
        });

        // following section helps jumping and navigating in different parts of
        // the registration part.
        prsnlInfo.setOnAction(actionEvent -> {
            sp.setVvalue(0);
        });

        cmpInfo.setOnAction(actionEvent -> {
            // still looking for other ways instead of hardcode
            sp.setVvalue(0.46552161746232185);
        });

        cmpvalid.setOnAction(actionEvent -> {
            // still looking for other ways instead of hardcode
            sp.setVvalue(0.9310432349246438);
        });

        // root VBox, the whole page.
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(mb,infoPage);

        // create scene.
        Scene scene = new Scene(root, scene_width, scene_height);
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
