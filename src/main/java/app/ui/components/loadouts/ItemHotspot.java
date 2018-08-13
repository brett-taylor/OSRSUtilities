package app.ui.components.loadouts;

import app.utils.CSSColorParser;
import javafx.animation.FillTransition;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The hotspot that an item will sit on. It is also the hotspot for when items are dragged by default.
 * @author Brett Taylor
 */
public class ItemHotspot extends AnchorPane {
    /**
     * The time it takes to complete the hover animation time.
     */
    private final Duration HOVER_ANIMATION_TIME = Duration.millis(300);

    /**
     * The width of the item square.
     */
    public final static double WIDTH = 50d;

    /**
     * The height of the item square.
     */
    public final static double HEIGHT = 50d;

    /**
     * The background color
     */
    private final static Color BACKGROUND_COLOR = CSSColorParser.parseColor("-background-light-color");

    /**
     * The background color while mouse is hovered or when it is being dragged.
     */
    private final static Color BACKGROUND_HOVER_COLOR = CSSColorParser.parseColor("-background-dark-color");

    /**
     * The border color
     */
    private final static Color BORDER_COLOR = Color.BLACK;

    /**
     * Background.
     */
    private Rectangle background;

    /**
     * The item currently attached to the square.
     */
    private ItemSprite attachedItem;

    /**
     * Constructor.
     */
    public ItemHotspot() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setMinWidth(WIDTH);
        setMinHeight(HEIGHT);
        setMaxWidth(WIDTH);
        setMaxHeight(HEIGHT);

        background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.setFill(BACKGROUND_COLOR);
        background.setStroke(BORDER_COLOR);
        background.setMouseTransparent(true);
        getChildren().addAll(background);
        AnchorPane.setLeftAnchor(background, 0d);
        AnchorPane.setTopAnchor(background, 0d);
        AnchorPane.setRightAnchor(background, 0d);
        AnchorPane.setBottomAnchor(background, 0d);

        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);
        setOnDragDetected(this::onDragStarted);
        setOnMouseDragEntered(this::onDragEntered);
        setOnMouseDragExited(this::onDragExited);
    }

    /**
     * Attaches a item to the square.
     * @param item The item to be attached
     */
    public void attachItem(ItemSprite item) {
        attachedItem = item;

        getChildren().addAll(item);
        AnchorPane.setLeftAnchor(item, 0d);
        AnchorPane.setTopAnchor(item, 0d);
        AnchorPane.setRightAnchor(item, 0d);
        AnchorPane.setBottomAnchor(item, 0d);
        item.toFront();
    }

    /**
     * Unattaches any item that is already attached.
     */
    public void unattachItem() {
        if (attachedItem != null) {
            getChildren().remove(attachedItem);
            attachedItem = null;
        }
    }

    /**
     * @return The current attached item.
     */
    public ItemSprite getAttachedItem() {
        return attachedItem;
    }

    /**
     * Called when the mouse enters.
     * @param e The mouse event
     */
    private void onMouseEntered(MouseEvent e) {
        if (attachedItem != null) {
            setCursor(Cursor.HAND);
        }

        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, background, BACKGROUND_COLOR, BACKGROUND_HOVER_COLOR);
        ft.setCycleCount(1);
        ft.play();
    }

    /**
     * Called when the mouse exits.
     * @param e The mouse event
     */
    private void onMouseExited(MouseEvent e) {
        setCursor(Cursor.DEFAULT);
        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, background, BACKGROUND_HOVER_COLOR, BACKGROUND_COLOR);
        ft.setCycleCount(1);
        ft.play();
    }

    /**
     * Called when the drag started.
     * @param e The mouse event
     */
    private void onDragStarted(MouseEvent e) {
        if (attachedItem == null)
            return;

        this.startFullDrag();
        setCursor(Cursor.HAND);
        ItemDragDropController.get().startDragging(attachedItem, this);
        unattachItem();

    }

    /**
     * Called when a drag is occurring and the mouse has entered this node.
     * @param e The mouse event
     */
    private void onDragEntered(MouseEvent e) {
        if (ItemDragDropController.get() != null) {
            ItemDragDropController.get().setHoveredItemHotspot(this);
        }

        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, background, BACKGROUND_COLOR, BACKGROUND_HOVER_COLOR);
        ft.setCycleCount(1);
        ft.play();
    }

    /**
     * Called when a drag is occurring and the mouse has left this node.
     * @param e The mouse event
     */
    private void onDragExited(MouseEvent e) {
        if (ItemDragDropController.get() != null) {
            ItemDragDropController.get().setHoveredItemHotspot(null);
        }

        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, background, BACKGROUND_HOVER_COLOR, BACKGROUND_COLOR);
        ft.setCycleCount(1);
        ft.play();
    }
}
