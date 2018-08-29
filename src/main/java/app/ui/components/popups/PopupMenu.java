package app.ui.components.popups;

import app.OSRSUtilities;
import app.ui.FXMLElement;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Base class for a popup menu that comes with a header and body and animations.
 * @author Brett Taylor
 */
public abstract class PopupMenu extends FXMLElement {
    /**
     * The duration the hello and bye animations will last.
     */
    protected static Duration HELLO_BYE_ANIMATION_TIME = Duration.millis(200);

    /**
     * The y position of where the dialog box will sit.
     */
    protected static double FINAL_POSITION_Y = 25;

    /**
     * The main background of the dialog box.
     */
    protected HBox mainBackground;

    /**
     * The heading of the dialog box.
     */
    protected Label heading;

    /**
     * The button row container.
     */
    protected HBox buttonRow;

    /**
     * The header body.
     */
    protected AnchorPane headingBody;

    /**
     * The main body.
     */
    protected AnchorPane mainBody;

    /**
     * Update timer.
     */
    protected AnimationTimer updateTimer;

    /**
     * The vbox that sits inside the main layout. Controls the size of the popup.
     */
    protected VBox innerMainLayout;

    /**
     * Creates a popup.
     */
    protected PopupMenu() {
        super("/fxml/components/Popup.fxml");

        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 0d);

        mainBackground = (HBox) lookup("#mainBackground");
        Objects.requireNonNull(mainBackground);

        headingBody = (AnchorPane) lookup("#headingBody");
        Objects.requireNonNull(headingBody);

        heading = (Label) lookup("#headingLabel");
        Objects.requireNonNull(heading);

        buttonRow = (HBox) lookup("#buttonRow");
        Objects.requireNonNull(buttonRow);

        mainBody = (AnchorPane) lookup("#mainBody");
        Objects.requireNonNull(mainBody);

        innerMainLayout = (VBox) lookup("#innerMainLayout");
        Objects.requireNonNull(innerMainLayout);

        setVisible(false);
        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        updateTimer.start();
    }

    /**
     * Called often to update the dialog box.
     */
    protected void update() {
        toFront();
    }

    /**
     * Starts the hello animation.
     */
    public void startHelloAnimation() {
        setVisible(true);
        OSRSUtilities.getWindow().getMainLayout().getChildren().add(this);
        mainBackground.setLayoutY(OSRSUtilities.getWindow().getPrimaryStage().getHeight() + 5);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(
                new KeyFrame(HELLO_BYE_ANIMATION_TIME, new KeyValue(mainBackground.layoutYProperty(), FINAL_POSITION_Y)
                ));
        timeline.play();
        timeline.setOnFinished(e ->  {
            timeline.stop();
            this.endHelloAnimation();
        });
    }

    /**
     * Called when the hello animation ends.
     */
    protected void endHelloAnimation() {
    }

    /**
     * Starts the bye animation.
     */
    public void startByeAnimation() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(
                new KeyFrame(HELLO_BYE_ANIMATION_TIME, new KeyValue(mainBackground.layoutYProperty(), OSRSUtilities.getWindow().getPrimaryStage().getHeight() + 5)
                ));
        timeline.play();
        timeline.setOnFinished(e ->  {
            timeline.stop();
            this.endByeAnimation();
        });
    }

    /**
     * Called when the bye animation ends.
     */
    protected void endByeAnimation() {
        OSRSUtilities.getWindow().getMainLayout().getChildren().remove(this);
    }

    /**
     * Sets the size of the popup
     * @param width The new width.
     * @param height The new height
     */
    protected void setSize(int width, int height) {
        innerMainLayout.setPrefWidth(width);
        innerMainLayout.setPrefHeight(height);
    }
}
