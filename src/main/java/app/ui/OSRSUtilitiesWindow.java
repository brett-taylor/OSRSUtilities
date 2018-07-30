package app.ui;

import app.ui.pages.PopulateDatabaseController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
    private AnchorPane mainLayout = null;

    /**
     * The titlebar
     */
    private AnchorPane titlebar;

    /**
     * The current page being displayed
     */
    private Node currentPage;

    /**
     * The primary stage
     */
    private Stage primaryStage;

    /**
     * The primary scene
     */
    private BorderlessScene scene;

    /**
     * The constructor to the main window.
     * @param primaryStage the primary stage that is passed from javafx.
     */
    public OSRSUtilitiesWindow(Stage primaryStage) {
        // Load RS font and create the frame.
        this.primaryStage = primaryStage;
        Font.loadFont(getClass().getResource(RUNESCAPE_FONT_LOCATION).toExternalForm(), 12);

        mainLayout = new AnchorPane();
        scene = new BorderlessScene(primaryStage, StageStyle.UNDECORATED, mainLayout, 500, 500);
        primaryStage.setScene(scene);
        scene.removeDefaultCSS();
        scene.getStylesheets().add(getClass().getResource(CSS_LOCATION).toExternalForm());
        mainLayout.getStyleClass().add("main-background-panel");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);
        primaryStage.setTitle("OSRS");

        // Add titlebar
        try {
            titlebar = FXMLLoader.load(getClass().getResource(TITLEBAR_FXML_LOCATION));
            mainLayout.getChildren().add(titlebar);
            AnchorPane.setTopAnchor(titlebar, 0d);
            AnchorPane.setLeftAnchor(titlebar, 0d);
            AnchorPane.setRightAnchor(titlebar, 0d);
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

            if (currentPage != null)
                mainLayout.getChildren().remove(currentPage);

            currentPage = flowHandler.start(animation);
            mainLayout.getChildren().add(currentPage);
            AnchorPane.setBottomAnchor(currentPage, 0d);
            AnchorPane.setLeftAnchor(currentPage, 0d);
            AnchorPane.setRightAnchor(currentPage, 0d);
            AnchorPane.setTopAnchor(currentPage, titlebar.getPrefHeight() + 5);
        } catch (FlowException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the main layout container.
     */
    public AnchorPane getMainLayout() {
        return mainLayout;
    }

    /**
     * @return The main stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * @return The main scene
     */
    public BorderlessScene getScene() {
        return scene;
    }
}