package app.ui.components.buttons;

import app.ui.FXMLElement;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;
import java.util.concurrent.RecursiveTask;

/**
 * The square button
 * @author Brett Taylor
 */
public class SquareButton extends FXMLElement {
    /**
     * The time it takes to complete the hover animation time.
     */
    private final Duration HOVER_ANIMATION_TIME = Duration.millis(300);

    /**
     * The rectangle.
     */
    private Rectangle rectangle;

    /**
     * The stack pane which the rectangle will be on to overlay over the rectangle.
     */
    private StackPane rectangleOverlay;

    /**
     * The backgorund color
     */
    private Color backgroundColor;

    /**
     * The background hover color
     */
    private Color backgroundHoverColor;

    /**
     * The backgorund color
     */
    private Color textColor;

    /**
     * The background hover color
     */
    private Color textHoverColor;

    /**
     * What should be called when it is clicked.
     */
    private Runnable onClicked;

    /**
     * The icon view.
     */
    private FontAwesomeIconView fontAwesomeIconView;

    /**
     * The button label.
     */
    private Label buttonLabel;

    /**
     * The container for hte glyph
     */
    private BorderPane glyphContainer;

    /**
     * Constructor
     */
    public SquareButton() {
        super("/fxml/components/buttons/squareButton.fxml");

        rectangle = (Rectangle) lookup("#rectangle");
        Objects.requireNonNull(rectangle);

        rectangleOverlay = (StackPane) lookup("#rectangleOverlay");
        Objects.requireNonNull(rectangleOverlay);

        buttonLabel = (Label) lookup("#buttonLabel");
        Objects.requireNonNull(buttonLabel);

        glyphContainer = (BorderPane) lookup("#glyphContainer");
        Objects.requireNonNull(glyphContainer);

        rectangleOverlay.setOnMouseEntered(this::onMouseEnter);
        rectangleOverlay.setOnMouseExited(this::onMouseExit);
        rectangleOverlay.setOnMouseClicked(this::onMouseClicked);

        rectangle.widthProperty().bind(rectangleOverlay.widthProperty());
    }

    /**
     * Creates a default themed button.
     * @return A default themed button.
     */
    public static SquareButton defaultButton() {
        SquareButton sb = new SquareButton();
        sb.setBackgroundColor(CSSColorParser.parseColor("-background-color"));
        sb.setBackgroundHoverColor(CSSColorParser.parseColor("-background-light-color"));
        sb.setBorder(Color.BLACK, 1);
        sb.setTextPadding(0, 0, 0, 10);
        sb.setTextAllignment(Pos.CENTER);
        sb.setTextColor(CSSColorParser.parseColor("-text-muted-color"));
        sb.setTextHoverColor(Color.WHITE);
        sb.setGlyphSize(20);
        sb.setGlyphPadding(0, 0, 0, 10);

        return sb;
    }

    /**
     * Adds a glyph to the left of the button text.
     * @param icon The glyph
     * @param color The color of the glyph.
     */
    public void setGlyph(FontAwesomeIcon icon, Color color) {
        if (fontAwesomeIconView != null) {
            rectangleOverlay.getChildren().remove(fontAwesomeIconView);
            fontAwesomeIconView = null;
        }

        fontAwesomeIconView = new FontAwesomeIconView(icon);
        setGlyphSize(40);
        fontAwesomeIconView.setMouseTransparent(true);
        fontAwesomeIconView.setFill(color);
        glyphContainer.setCenter(fontAwesomeIconView);
    }

    /**
     * Sets the size of the glyph..
     * @param glyphSize the size of the glyph.
     */
    public void setGlyphSize(Integer glyphSize) {
        if (fontAwesomeIconView != null) {
            fontAwesomeIconView.setSize("" + glyphSize);
            glyphContainer.setPrefWidth(glyphSize);
            glyphContainer.setMaxWidth(glyphSize);
        }
    }

    /**
     * Sets the padding of the glyph.
     * @param top The padding from the top.
     * @param right The padding from the right.
     * @param bottom The padding from the bottom.
     * @param left The padding from the left.
     */
    public void setGlyphPadding(int top, int right, int bottom, int left) {
        glyphContainer.setPadding(new Insets(top, right, bottom, left));
    }

    /**
     * Called when the mouse enters
     * @param e The mouse event.
     */
    private void onMouseEnter(MouseEvent e) {
        if (backgroundHoverColor != null) {
            FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, rectangle, backgroundColor, backgroundHoverColor);
            ft.setCycleCount(1);
            ft.play();
        }

        if (textHoverColor != null) {
            ColorAdjust colorAdjust = new ColorAdjust();
            buttonLabel.setEffect(colorAdjust);
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(buttonLabel.textFillProperty(), textColor, Interpolator.LINEAR)),
                    new KeyFrame(HOVER_ANIMATION_TIME, new KeyValue(buttonLabel.textFillProperty(), textHoverColor, Interpolator.LINEAR))
            );
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.play();
        }
    }

    /**
     * Called when the mouse exits.
     * @param e The mouse event.
     */
    private void onMouseExit(MouseEvent e) {
        if (backgroundHoverColor != null) {
            FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, rectangle, backgroundHoverColor, backgroundColor);
            ft.setCycleCount(1);
            ft.play();
        }

        if (textHoverColor != null) {
            ColorAdjust colorAdjust = new ColorAdjust();
            buttonLabel.setEffect(colorAdjust);
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(buttonLabel.textFillProperty(), textHoverColor, Interpolator.LINEAR)),
                    new KeyFrame(HOVER_ANIMATION_TIME, new KeyValue(buttonLabel.textFillProperty(), textColor, Interpolator.LINEAR))
            );
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.play();
        }
    }

    /**
     * Called when the rectangle is clicked.
     * @param e The mouse event.
     */
    private void onMouseClicked(MouseEvent e) {
        if (onClicked != null)
            onClicked.run();
    }

    /**
     * Sets the background colour of the rectangle
     * @param backgroundColor The color.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        rectangle.setFill(backgroundColor);
    }

    /**
     * Sets the background hover colour of the rectangle
     * @param backgroundHoverColor The color.
     */
    public void setBackgroundHoverColor(Color backgroundHoverColor) {
        this.backgroundHoverColor = backgroundHoverColor;
    }

    /**
     * Sets the text colour of the label
     * @param textColor The color.
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        buttonLabel.setTextFill(textColor);
    }

    /**
     * Sets the text hover colour of the label
     * @param textHoverColor The color.
     */
    public void setTextHoverColor(Color textHoverColor) {
        this.textHoverColor = textHoverColor;
    }

    /**
     * Sets the border of the button.
     * @param color The new color of the border.
     * @param width The new width of the border.
     */
    public void setBorder(Color color, double width) {
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(width);
    }

    /**
     * Sets what should be called when the button is clicked.
     * @param onClicked The runnable that should be called.
     */
    public void setOnClicked(Runnable onClicked) {
        this.onClicked = onClicked;
    }

    /**
     * Sets the size of the rectangle.
     * @param width the width of the button.
     * @param height the width of the button.
     */
    public void setSize(double width, double height) {
        rectangleOverlay.setPrefWidth(width);
        rectangleOverlay.setPrefHeight(height);
        rectangle.setHeight(height);
    }

    /**
     * Sets the text of the button.
     * @param text the text to dispaly.
     */
    public void setText(String text) {
        buttonLabel.setText(text);
    }

    /**
     * Sets the alignment of the text.
     * @param alignment The new alignment.
     */
    public void setTextAllignment(Pos alignment) {
        buttonLabel.setAlignment(alignment);
    }

    /**
     * Sets the padding of the text label.
     * @param top The padding from the top.
     * @param right The padding from the right.
     * @param bottom The padding from the bottom.
     * @param left The padding from the left.
     */
    public void setTextPadding(int top, int right, int bottom, int left) {
        buttonLabel.setPadding(new Insets(top, right, bottom, left));
    }
}
