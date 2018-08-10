package app.ui.components;

import app.OSRSUtilities;
import app.ui.FXMLElement;
import app.ui.components.buttons.SquareButton;
import app.ui.pages.DownloadsPage;
import app.ui.pages.TestPage;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Objects;

/**
 * The side bar menu that should appear on all pages. Is used to navigate.
 * @author Brett Taylor
 */
public class SideMenu extends FXMLElement {
    /**
     * The width the of the side bar menu.
     */
    public final static double MENU_WIDTH = 200;

    /**
     * The size that the side bar menu should be when it is collapsed.
     */
    public final static double AMOUNT_SHOWN_WHEN_COLLAPSED = 35;

    /**
     * The x position of the side bar when it is collapsed
     */
    public final static double COLLAPSED_POS_X = -(SideMenu.MENU_WIDTH - SideMenu.AMOUNT_SHOWN_WHEN_COLLAPSED);

    /**
     * The duration the slide in and slide out animations will last.
     */
    private static Duration SLIDE_IN_OUT_ANIMATION_TIME = Duration.millis(200);

    /**
     * Upper part Menu list.
     */
    private VBox upperMenu;

    /**
     * Lower part Menu list.
     */
    private VBox lowerMenu;

    /**
     * Update timer.
     */
    private AnimationTimer updateTimer;

    /**
     * Stops buttons accidentally being triggered when collapsed.
     */
    private Pane buttonConsumer;

    /**
     * The menu icon..
     */
    private FontAwesomeIconView menuIcon;

    /**
     * Constructor.
     */
    public SideMenu() {
        super("/fxml/components/sideMenu.fxml");
        getParentElement().setPrefWidth(MENU_WIDTH);
        getParentElement().setMinWidth(MENU_WIDTH);
        getParentElement().setMaxWidth(MENU_WIDTH);

        ScrollPane scrollPane = (ScrollPane) lookup("#scrollPane");
        Objects.requireNonNull(scrollPane);
        upperMenu = (VBox) scrollPane.getContent().lookup("#upperMenu");
        Objects.requireNonNull(scrollPane);
        lowerMenu = (VBox) scrollPane.getContent().lookup("#lowerMenu");
        Objects.requireNonNull(lowerMenu);

        buttonConsumer = (Pane) lookup("#buttonConsumer");
        Objects.requireNonNull(buttonConsumer);

        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);

        AnchorPane header = (AnchorPane) lookup("#header");
        Objects.requireNonNull(header);

        menuIcon = new FontAwesomeIconView(FontAwesomeIcon.BARS);
        menuIcon.setGlyphSize(20);
        menuIcon.setFill(Color.WHITE);
        header.getChildren().add(menuIcon);
        AnchorPane.setTopAnchor(menuIcon, 7d);
        AnchorPane.setRightAnchor(menuIcon, 7d);

        // Upper Menu
        addButton(upperMenu, "Loadouts", FontAwesomeIcon.SUITCASE, () -> System.out.println("Loadout clicked"));
        addButton(upperMenu, "Test Page", FontAwesomeIcon.COG, () -> OSRSUtilities.getWindow().showPage(new TestPage()));

        // Lower Menu
        addButton(lowerMenu, "Downloads", FontAwesomeIcon.DOWNLOAD, () -> OSRSUtilities.getWindow().showPage(new DownloadsPage()));
        addButton(lowerMenu, "Blacklist", FontAwesomeIcon.LIST, () -> System.out.println("Blacklist clicked"));
        addButton(lowerMenu, "Item Editor", FontAwesomeIcon.EDIT, () -> System.out.println("Item Editor clicked"));
        addSpacer(lowerMenu, 5);

        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        updateTimer.start();
    }

    /**
     * Called often to update the side bar.
     */
    private void update() {
        toFront();
    }

    /**
     * Positions the sidebar correctly.
     */
    public void position() {
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        setLayoutX(COLLAPSED_POS_X);
    }

    /**
     * Adds a spacing element.
     * @param container The part of the menu it will be added to.
     * @param spacingHeight The height of the spacing element.
     */
    private void addSpacer(VBox container, double spacingHeight) {
        Pane panel = new Pane();
        panel.setPrefHeight(spacingHeight);
        panel.setMaxHeight(spacingHeight);
        panel.setMinHeight(spacingHeight);
        container.getChildren().add(panel);
    }

    /**
     * Adds a button to the side bar.
     * @param container The part of the menu it will be added to.
     * @param buttonText The text on the button.
     * @param icon The glyph icon
     * @param onClicked Method called when clicked.
     */
    private void addButton(VBox container, String buttonText, FontAwesomeIcon icon, Runnable onClicked) {
        SquareButton sb = new SquareButton();
        sb.setBackgroundColor(CSSColorParser.parseColor("-background-color"));
        sb.setBackgroundHoverColor(CSSColorParser.parseColor("-background-light-color"));
        sb.setBorder(Color.BLACK, 0);
        sb.setText(buttonText);
        sb.setTextPadding(0, 0, 0, 10);
        sb.setTextAllignment(Pos.CENTER_LEFT);
        sb.setTextColor(CSSColorParser.parseColor("-text-muted-color"));
        sb.setTextHoverColor(Color.WHITE);
        sb.setGlyph(icon, CSSColorParser.parseColor("-text-muted-dark-color"));
        sb.setGlyphSize(20);
        sb.setGlyphPadding(0, 0, 0, 10);
        sb.setOnClicked(onClicked);

        container.getChildren().add(sb);
        Platform.runLater(() -> sb.setSize(lowerMenu.getWidth(), 40));
    }

    /**
     * Called when the mouse enters the sidebar
     * @param e the mouse event.
     */
    private void onMouseEntered(MouseEvent e) {
        // Slide out
        Timeline slideOutAnimation = new Timeline();
        slideOutAnimation.setCycleCount(1);
        slideOutAnimation.getKeyFrames().add(new KeyFrame(SLIDE_IN_OUT_ANIMATION_TIME, new KeyValue(this.layoutXProperty(), 0d)));
        slideOutAnimation.play();
        slideOutAnimation.setOnFinished(t ->  {
            slideOutAnimation.stop();
            buttonConsumer.setVisible(false);
        });

        // Make menu icon invis.
        Timeline menuIconInvisAnimation = new Timeline();
        menuIconInvisAnimation.setCycleCount(1);
        menuIconInvisAnimation.getKeyFrames().add(new KeyFrame(SLIDE_IN_OUT_ANIMATION_TIME, new KeyValue(menuIcon.opacityProperty(), 0d)));
        menuIconInvisAnimation.play();
        menuIconInvisAnimation.setOnFinished(t -> menuIconInvisAnimation.stop());
    }

    /**
     * Called when the mouse exits the sidebar
     * @param e the mouse event.
     */
    private void onMouseExited(MouseEvent e) {
        // Slide in
        Timeline slideInAnimation = new Timeline();
        slideInAnimation.setCycleCount(1);
        slideInAnimation.getKeyFrames().add(
                new KeyFrame(SLIDE_IN_OUT_ANIMATION_TIME, new KeyValue(this.layoutXProperty(), COLLAPSED_POS_X)
                ));
        slideInAnimation.play();
        slideInAnimation.setOnFinished(t ->  {
            slideInAnimation.stop();
            buttonConsumer.setVisible(true);
        });

        // Make menu icon appeear.
        Timeline menuIconVisibleAnimation = new Timeline();
        menuIconVisibleAnimation.setCycleCount(1);
        menuIconVisibleAnimation.getKeyFrames().add(new KeyFrame(SLIDE_IN_OUT_ANIMATION_TIME, new KeyValue(menuIcon.opacityProperty(), 1d)));
        menuIconVisibleAnimation.play();
        menuIconVisibleAnimation.setOnFinished(t -> menuIconVisibleAnimation.stop());
    }
}
