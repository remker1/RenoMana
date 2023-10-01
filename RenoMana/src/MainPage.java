/**
 * MainPage Class
 *
 * <p>
 * The application has a user interface with several tab where each tab shows a different feature
 * or part of the application. By putting the content of each tab into its own class, the
 * application makes it easier to change and maintain. The content of each tab is made and
 * put into place in its own class. This method lets developers work on single features at a time,
 * which makes the codebase more organised and easier to use.
 * <p>
 *
 * @author Jewel Magcawas
 * @version 1.0
 * @since 2023-08-01
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.TabPane;
import javafx.scene.Scene;
import javafx.scene.control.Tab;

public class MainPage extends Application {

    @Override
    public void start(Stage stage) {
        // The set-ups of pane and scene.
        TabPane tabPane = new TabPane();
        Scene scene = new Scene(tabPane, 1000, 500);

        // Adding the tabs and making them uncloseable, plus some additional
        // customization like paddings.
        Tab tab1 = new Tab("Tab 1", new MainPageTab1());
        Tab tab2 = new Tab("Tab 2", new MainPageTab2());
        Tab tab3 = new Tab("Tab 3", new MainPageTab3());
        Tab tab4 = new Tab("Tab 4", new MainPageTab4());
        Tab tab5 = new Tab("Tab 5", new MainPageTab5());
        Tab tab6 = new Tab("Tab 6", new MainPageTab6());

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5, tab6);

        for (Tab tab : tabPane.getTabs()) {
            tab.setClosable(false);
        }
        // The TabPane's tabMinWidthProperty is bound to the TabPane's width divided by the number of tabs. This makes
        // sure that the width of the tabs will change as the window is resized, keeping the space between all tabs the
        // same.
        tabPane.tabMinWidthProperty().bind(tabPane.widthProperty().divide(tabPane.getTabs().size()).subtract(10)); // subtracting 10 for padding

        stage.setScene(scene);
        stage.setTitle("[COMPANY HOMEPAGE]");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}