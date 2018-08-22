package app.ui.components.buttons;

import app.ui.FXMLElement;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FillTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Objects;

/**
 * The circular button
 * @author Brett Taylor
 */
public class CircularButton extends FXMLElement {
    /**
     * The time it takes to complete the hover animation time.
     */
    private final Duration HOVER_ANIMATION_TIME = Duration.millis(300);

    /**
     * The circle.
     */
    private Circle circle;

    /**
     * The stack pane which the circle will be on to overlay over the circle.
     */
    private StackPane circleOverlay;

    /**
     * The backgorund color
     */
    private Color backgroundColor;

    /**
     * The background hover color
     */
    private Color backgroundHoverColor;

    /**
     * What should be called when it is clicked.
     */
    private Runnable onClicked;

    /**
     * The icon view.
     */
    private FontAwesomeIconView fontAwesomeIconView;

    /**
     * Constructor
     */
    public CircularButton() {
        super("/fxml/components/buttons/CircularButton.fxml");

        circle = (Circle) lookup("#circle");
        Objects.requireNonNull(circle);

        circleOverlay = (StackPane) lookup("#circleOverlay");
        Objects.requireNonNull(circleOverlay);

        circle.setOnMouseEntered(this::onMouseEnter);
        circle.setOnMouseExited(this::onMouseExit);
        circle.setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * Creates a circular button to look like the success circular button
     * @return the sucess button.
     */
    public static CircularButton successButton() {
        CircularButton cb = new CircularButton();
        cb.setBackgroundColor(CSSColorParser.parseColor("-good-color"));
        cb.setBackgroundHoverColor(CSSColorParser.parseColor("-good-secondary-color"));
        cb.setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-good-overlay-color"));
        return cb;
    }

    /**
     * Creates a circular button to look like the failed circular button
     * @return the failed button.
     */
    public static CircularButton failedButton() {
        CircularButton cb = new CircularButton();
        cb.setBackgroundColor(CSSColorParser.parseColor("-bad-color"));
        cb.setBackgroundHoverColor(CSSColorParser.parseColor("-bad-secondary-color"));
        cb.setGlyph(FontAwesomeIcon.TIMES, CSSColorParser.parseColor("-bad-overlay-color"));
        return cb;
    }

    /**
     * Creates a circular button that has no color related function.
     * @return the button.
     */
    public static CircularButton regularButton() {
        CircularButton cb = new CircularButton();
        cb.setBackgroundColor(CSSColorParser.parseColor("-alert-color"));
        cb.setBackgroundHoverColor(CSSColorParser.parseColor("-alert-secondary-color"));
        cb.setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-alert-overlay-color"));
        return cb;
    }

    /**
     * Adds a glyph on top of the circle.
     * @param icon The glyph
     * @param color The color of the glyph. If this is null it will attempt to set it to the current glyph's color.
     *              If there was no previous glyph it will set it to white.
     */
    public void setGlyph(FontAwesomeIcon icon, Color color) {
        Paint currentColor = Color.WHITE;

        if (fontAwesomeIconView != null) {
            currentColor = fontAwesomeIconView.getFill();
            circleOverlay.getChildren().remove(fontAwesomeIconView);
            fontAwesomeIconView = null;
        }

        fontAwesomeIconView = new FontAwesomeIconView(icon);
        fontAwesomeIconView.setSize("40");
        fontAwesomeIconView.setMouseTransparent(true);
        if (color != null) {
            fontAwesomeIconView.setFill(color);
        } else {
            fontAwesomeIconView.setFill(currentColor);
        }
        circleOverlay.getChildren().add(fontAwesomeIconView);
    }

    /**
     * Called when the mouse enters
     * @param e The mouse event.
     */
    private void onMouseEnter(MouseEvent e) {
        if (backgroundHoverColor != null) {
            FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, circle, backgroundColor, backgroundHoverColor);
            ft.setCycleCount(1);
            ft.play();
        }
    }

    /**
     * Called when the mouse exits.
     * @param e The mouse event.
     */
    private void onMouseExit(MouseEvent e) {
        if (backgroundHoverColor != null) {
            FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, circle, backgroundHoverColor, backgroundColor);
            ft.setCycleCount(1);
            ft.play();
        }
    }

    /**
     * Called when the circle is clicked.
     * @param e The mouse event.
     */
    private void onMouseClicked(MouseEvent e) {
        if (onClicked != null)
            onClicked.run();
    }

    /**
     * Sets the background colour of the circle
     * @param backgroundColor The color.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        circle.setFill(backgroundColor);
    }

    /**
     * Sets the background hover colour of the circle
     * @param backgroundHoverColor The color.
     */
    public void setBackgroundHoverColor(Color backgroundHoverColor) {
        this.backgroundHoverColor = backgroundHoverColor;
    }

    /**
     * Sets what should be called when the button is clicked.
     * @param onClicked The runnable that should be called.
     */
    public void setOnClicked(Runnable onClicked) {
        this.onClicked = onClicked;
    }

    /**
     * Sets the radius of the circle.
     * @param radiusOfCircle the radius.
     */
    public void setSize(double radiusOfCircle) {
        circle.setRadius(radiusOfCircle);
    }

    /**
     * Sets the size of the glyph..
     * @param glyphSize the size of the glyph.
     */
    public void setGlyphSize(Integer glyphSize) {
        if (fontAwesomeIconView != null)
            fontAwesomeIconView.setSize("" + glyphSize);
    }
}
