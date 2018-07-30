package app.ui;

import app.ui.pages.PopulateDatabaseController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;

import java.io.IOException;

/**
 * Main program window.
 * @author Brett Taylor
 */
public class OSRSUtilitiesWindow {
    /**
     * The location of the css file.
     */
    private final static String CSS_LOCATION = "/css/style.css";

    /**
     * The location of the runescape font face file.
     */
    private final static String RUNESCAPE_FONT_LOCATION = "/fonts/runescape.ttf";

    /**
     * The titlebar fxml location.
     */
    private final static String TITLEBAR_FXML_LOCATION = "/fxml/components/globals/Titlebar.fxml";

    /**
     * The main layout container.
     */
    private BorderPane mainLayout = null;

    /**
     * The constructor to the main window.
     * @param primaryStage the primary stage that is passed from javafx.
     */
    public OSRSUtilitiesWindow(Stage primaryStage) {
        // Load RS font and create the frame.
        Font.loadFont(getClass().getResource(RUNESCAPE_FONT_LOCATION).toExternalForm(), 12);
        mainLayout = new BorderPane();
        BorderlessScene scene = new BorderlessScene(primaryStage, StageStyle.UNDECORATED, mainLayout, 500, 500);
        primaryStage.setScene(scene);
        scene.removeDefaultCSS();
        scene.getStylesheets().add(getClass().getResource(CSS_LOCATION).toExternalForm());
        mainLayout.getStyleClass().add("main-background-panel");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);
        primaryStage.setTitle("OSRS");

        // Add titlebar
        try {
            Node titlebar = FXMLLoader.load(getClass().getResource(TITLEBAR_FXML_LOCATION));
            mainLayout.setTop(titlebar);
            scene.setMoveControl(titlebar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        showPage(PopulateDatabaseController.class);
        primaryStage.show();
    }

    /**
     * Shows the given page.
     * @param startViewControllerClass The page to show.
     */
    public void showPage(Class<?> startViewControllerClass) {
        try {
            Flow flow  = new Flow(startViewControllerClass);
            FlowHandler flowHandler = flow.createHandler();
            AnimatedFlowContainer animation = new AnimatedFlowContainer(Duration.ONE, ContainerAnimations.SWIPE_RIGHT);
            mainLayout.setCenter(flowHandler.start(animation));
        } catch (FlowException e) {
            e.printStackTrace();
        }
    }
}