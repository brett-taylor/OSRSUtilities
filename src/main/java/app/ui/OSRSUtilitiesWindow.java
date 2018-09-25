package app.ui;

import app.ui.components.SideMenu;
import app.ui.pages.BasePage;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Main program window.
 * @author Brett Taylor
 */
public class OSRSUtilitiesWindow {
    /**
     * The location of the css file.
     */
    public final static String CSS_LOCATION = "/css/style.css";

    /**
     * The minimum width of the window.
     */
    private final static int MINIMUM_WIDTH = 850;

    /**
     * The minimum height of the window.
     */
    private final static int MINIMUM_HEIGHT = 650;

    /**
     * The main layout container.
     */
    private AnchorPane mainLayout = null;

    /**
     * The current page being displayed
     */
    private BasePage currentPage;

    /**
     * The primary stage
     */
    private Stage primaryStage;

    /**
     * The primary scene
     */
    private Scene scene;

    /**
     * The sidemenu
     */
    private SideMenu sideMenu;

    /**
     * The constructor to the main window.
     * @param primaryStage the primary stage that is passed from javafx.
     */
    public OSRSUtilitiesWindow(Stage primaryStage) {
        // Load fonts
        Font.loadFont(getClass().getResource("/fonts/OpenSans-Bold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-BoldItalic.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-ExtraBold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-ExtraBoldItalic.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-Italic.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-Light.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-LightItalic.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-SemiBold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/OpenSans-SemiBoldItalic.ttf").toExternalForm(), 12);

        this.primaryStage = primaryStage;
        mainLayout = new AnchorPane();
        mainLayout.getStyleClass().add("main-background-panel");
        scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource(CSS_LOCATION).toExternalForm());
        primaryStage.setMinWidth(MINIMUM_WIDTH);
        primaryStage.setMinHeight(MINIMUM_HEIGHT);
        primaryStage.setTitle("Oldschool Runescape Utilities");

        sideMenu = new SideMenu();
        primaryStage.show();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> ShortcutManager.activateShortcut(ke.getCode()));

    }

    /**
     * Shows the given page.
     * @param page The page to show.
     */
    public void showPage(BasePage page) {
        if (currentPage != null) {
            if (page.getClass() == currentPage.getClass()) {
                return;
            }

            currentPage.onRemoved();
            mainLayout.getChildren().remove(currentPage);
        }

        this.currentPage = page;
        mainLayout.getChildren().add(currentPage);
        AnchorPane.setBottomAnchor(currentPage, 0d);
        AnchorPane.setRightAnchor(currentPage, 0d);
        AnchorPane.setTopAnchor(currentPage, 0d);
        AnchorPane.setLeftAnchor(currentPage, 0d);

        if (page.shouldShowSideBarMenu()) {
            if (!mainLayout.getChildren().contains(sideMenu)) {
                mainLayout.getChildren().add(sideMenu);
                sideMenu.position();
                AnchorPane.setLeftAnchor(currentPage, SideMenu.AMOUNT_SHOWN_WHEN_COLLAPSED);
            }
        } else {
            if (mainLayout.getChildren().contains(sideMenu)) {
                mainLayout.getChildren().remove(sideMenu);
            }
        }

        page.onLoaded();
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
    public Scene getScene() {
        return scene;
    }

    /**
     * @return The current page being shown.
     */
    public BasePage getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the sidebar enabled status.
     * @param isEnabled True if the sidebar should be enabled.
     */
    public void setSideBarEnabledStatus(boolean isEnabled) {
        sideMenu.setMouseTransparent(!isEnabled);
    }
}