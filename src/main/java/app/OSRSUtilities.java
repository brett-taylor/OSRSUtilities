package app;

import app.data.DatabaseManager;
import app.data.ImageManager;
import app.ui.OSRSUtilitiesWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Main program class.
 * @author Brett Taylor
 */
public class OSRSUtilities extends Application {
    /**
     * The address of the wiki
     */
    public static final String WIKI_ADDRESS = "http://oldschoolrunescape.wikia.com/";

    /**
     * The main window.
     */
    private static OSRSUtilitiesWindow window;

    /**
     * Entry into program.
     * @param primaryStage Passed by java.
     */
    @Override
    public void start(Stage primaryStage) {
        window = new OSRSUtilitiesWindow(primaryStage);
        DatabaseManager.start();

        System.out.println(ImageManager.wikiArticleImageUrl("/wiki/Necklace_of_anguish"));
        System.out.println(ImageManager.wikiArticleImageUrl("/wiki/Dharok's_greataxe"));
        System.out.println(ImageManager.wikiArticleImageUrl("/wiki/Dragon_scimitar"));
        System.out.println(ImageManager.wikiArticleImageUrl("/wiki/Rune_bolts"));
        System.out.println(ImageManager.wikiArticleImageUrl("/wiki/Coins"));
    }

    /**
     * Starts the shutdown of the program.
     */
    public static void close() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * @return the main window.
     */
    public static OSRSUtilitiesWindow getWindow() {
        return window;
    }
}
