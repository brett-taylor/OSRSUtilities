package app.ui.components;

import app.data.wiki.WikiBlacklist;
import app.ui.FXMLElement;
import app.ui.components.buttons.CircularButton;
import app.ui.components.popups.DialogBox;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

/**
 * View that will represent a blacklisted url.
 * @author Brett
 */
public class BlackListView extends FXMLElement {
    /**
     * The time it takes to complete the hover animation time.
     */
    private final static Duration HOVER_ANIMATION_TIME = Duration.millis(300);

    /**
     * The color of the background
     */
    private final static Color BACKGROUND_COLOR = CSSColorParser.parseColor("-background-color");

    /**
     * The color of the background when hovered.
     */
    private final static Color BACKGROUND_HOVER_COLOR = CSSColorParser.parseColor("-background-light-color");

    /**
     * The color of the label.
     */
    private final static Color TEXT_COLOR = CSSColorParser.parseColor("-text-muted-color");

    /**
     * The color of the label when hovered.
     */
    private final static Color TEXT_HOVER_COLOR = CSSColorParser.parseColor("-text-color");

    /**
     * The blacklisted url it is representing.
     */
    private String blacklistedURL;

    /**
     * The background square
     */
    private Rectangle background;

    /**
     * The label
     */
    private Label label;

    /**
     * Constructor
     */
    public BlackListView(String blacklistedURL) {
        super("/fxml/components/BlackListView.fxml");
        this.blacklistedURL = blacklistedURL;

        background = (Rectangle) lookup("#background");
        Objects.requireNonNull(background);
        background.setFill(BACKGROUND_COLOR);
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());

        label = (Label) lookup("#label");
        Objects.requireNonNull(label);
        label.setText(blacklistedURL);
        label.setTextFill(TEXT_COLOR);

        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);
        setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * Called when the mouse enters the panel.
     * @param e The mouse event
     */
    private void onMouseEntered(MouseEvent e) {
        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, background, BACKGROUND_COLOR, BACKGROUND_HOVER_COLOR);
        ft.setCycleCount(1);
        ft.play();

        ColorAdjust colorAdjust = new ColorAdjust();
        label.setEffect(colorAdjust);
        Timeline hoverTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(label.textFillProperty(), TEXT_COLOR, Interpolator.LINEAR)),
                new KeyFrame(HOVER_ANIMATION_TIME, new KeyValue(label.textFillProperty(), TEXT_HOVER_COLOR, Interpolator.LINEAR))
        );
        hoverTimeLine.setCycleCount(1);
        hoverTimeLine.play();
    }

    /**
     * Called when the mouse exits the panel.
     * @param e The mouse event
     */
    private void onMouseExited(MouseEvent e) {
        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, background, BACKGROUND_HOVER_COLOR, BACKGROUND_COLOR);
        ft.setCycleCount(1);
        ft.play();

        ColorAdjust colorAdjust = new ColorAdjust();
        label.setEffect(colorAdjust);
        Timeline hoverTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(label.textFillProperty(), TEXT_HOVER_COLOR, Interpolator.LINEAR)),
                new KeyFrame(HOVER_ANIMATION_TIME, new KeyValue(label.textFillProperty(), TEXT_COLOR, Interpolator.LINEAR))
        );
        hoverTimeLine.setCycleCount(1);
        hoverTimeLine.play();
    }

    /**
     * Called when the mouse clicks the panel.
     * @param e The mouse event
     */
    private void onMouseClicked(MouseEvent e) {
        DialogBox.showDialogBox("Delete this url?");
        DialogBox.showBodyLabel("Are you sure you would to delete the following blacklisted url: " + blacklistedURL + "?" + "\nYou should refresh your downloaded categories after doing this.");

        CircularButton closeButton = CircularButton.regularButton();
        closeButton.setGlyph(FontAwesomeIcon.TIMES, null);
        closeButton.setOnClicked(DialogBox::close);
        DialogBox.placeInTopRight(closeButton);

        Runnable delete = () -> {
            WikiBlacklist.delete(blacklistedURL);
            VBox vbox = (VBox) getParent();
            if (vbox != null) {
                vbox.getChildren().remove(this);
            }
            DialogBox.close();
        };

        CircularButton deleteButton = CircularButton.failedButton();
        deleteButton.setGlyph(FontAwesomeIcon.TRASH_ALT, null);
        DialogBox.addToButtonRow(deleteButton);
        deleteButton.setOnClicked(delete);

        DialogBox.setShortcutFailed(DialogBox::close);
        DialogBox.setShortcutSuccess(delete);
    }
}
