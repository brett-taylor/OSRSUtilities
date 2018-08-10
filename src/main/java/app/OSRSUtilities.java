package app;

import app.data.DataLoadResult;
import app.data.DataManager;
import app.ui.OSRSUtilitiesWindow;
import app.ui.components.DialogBox;
import app.ui.pages.DownloadsPage;
import app.ui.pages.TestPage;
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

        DataLoadResult result = DataManager.start();
        if (result != DataLoadResult.SUCCESS) {
            StringBuilder message = new StringBuilder();
            message.append("An issue with the database occured. The application will now close.");
            message.append(System.lineSeparator());
            message.append("Does this application have access to: " + DataManager.DATA_LOCATION);
            message.append(System.lineSeparator());
            message.append(System.lineSeparator());
            message.append(result.toString());

            DialogBox.showError(message.toString(), true);
        }

        window.showPage(new TestPage());
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
