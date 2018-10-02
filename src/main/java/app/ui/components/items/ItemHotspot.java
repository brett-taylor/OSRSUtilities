package app.ui.components.items;

import app.data.loadouts.events.OnItemHotspotItemChanged;
import app.data.runescape.Item;
import app.ui.components.popups.edititem.EditItemPopup;
import app.ui.components.popups.searchitem.SelectItemPopup;
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
     * Listener to the OnItemHotspotItemChanged event.
     */
    private OnItemHotspotItemChanged onItemHotspotItemChanged;

    /**
     * Sets whether or not this hotspot will allow items to be dragged from and to it.
     */
    private boolean itemDraggingEnabled = true;

    /**
     * Shows the stack size of the item.
     */
    private StackSizeLabel stackSize;

    /**
     * The item hover to show detailed information about item. There should only be one on screen ever thus its static.
     */
    private static ItemHover itemHover;

    /**
     * Should show detailed information about the item when hovered?
     */
    private boolean shouldShowItemDetailsWhenHovered = true;

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

        stackSize = new StackSizeLabel();
        getChildren().add(stackSize);
        AnchorPane.setLeftAnchor(stackSize, 3d);
        AnchorPane.setTopAnchor(stackSize, 2d);

        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);
        setOnDragDetected(this::onDragStarted);
        setOnMouseDragEntered(this::onDragEntered);
        setOnMouseDragExited(this::onDragExited);
        setOnMouseClicked(this::onMouseClicked);
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
        stackSize.setStackSize(attachedItem.getItem().getStackSize());

        if (onItemHotspotItemChanged != null)
            onItemHotspotItemChanged.onItemHotspotItemChanged(this, attachedItem.getItem());
    }

    /**
     * Unattaches any item that is already attached.
     */
    public void unattachItem() {
        if (attachedItem != null) {
            if (onItemHotspotItemChanged != null)
                onItemHotspotItemChanged.onItemHotspotItemChanged(this, null);

            getChildren().remove(attachedItem);
            attachedItem = null;
            stackSize.setStackSize(0);
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

        if (attachedItem != null) {
            if (itemHover != null) {
                itemHover.destroy();
                itemHover = null;
            }

            if (shouldShowItemDetailsWhenHovered) {
                itemHover = new ItemHover(attachedItem.getItem());
            }
        }
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

        if (itemHover != null) {
            itemHover.destroy();
            itemHover = null;
        }
    }

    /**
     * Called when the drag started.
     * @param e The mouse event
     */
    private void onDragStarted(MouseEvent e) {
        if (attachedItem == null)
            return;

        if (!itemDraggingEnabled)
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
        if (!itemDraggingEnabled)
            return;

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

    /**
     * Called when the hotspot is clicked.
     * @param e The mouse event
     */
    private void onMouseClicked(MouseEvent e) {
        if (getAttachedItem() == null) {
            SelectItemPopup popup = new SelectItemPopup();
            popup.startHelloAnimation();
            popup.setOnSelectItemConfirmed((item) ->  {
                createEditItemPopup(item, 1, false);
            });
        } else {
            createEditItemPopup(attachedItem.getItem(), attachedItem.getItem().getStackSize(), true);
        }
    }

    /**
     * Creates the edit item popup.
     * @param item The item to be default selected.
     * @param stackSize The stack size.
     * @param showDeleteButton Should show delete button or not.
     */
    private void createEditItemPopup(Item item, int stackSize, boolean showDeleteButton) {
        EditItemPopup popup = new EditItemPopup(item, stackSize, true, showDeleteButton);
        popup.startHelloAnimation();
        popup.setOnRequestToDestroy(this::unattachItem);
        popup.setOnEditItemSuccess((newItem) -> {
            unattachItem();
            attachItem(new ItemSprite(newItem));
        });
    }

    /**
     * Sets a new listener to the OnItemHotspotItemChanged event.
     * @param onItemHotspotItemChanged The listener.
     */
    public void setOnItemHotspotItemChanged(OnItemHotspotItemChanged onItemHotspotItemChanged) {
        this.onItemHotspotItemChanged = onItemHotspotItemChanged;
    }

    /**
     * Sets whether or not this hotspot will allow items to be dragged from and to it.
     * @param itemDragging True if you wish for this behaviour to happen.
     */
    public void setItemDragging(boolean itemDragging) {
        itemDraggingEnabled = itemDragging;
    }

    /**
     * Sets the stack size's visibility.
     * @param visible True if the stack size should be visible.
     */
    public void setStackSizeVisible(boolean visible) {
        stackSize.setVisible(visible);
    }

    /**
     * Hides the background
     */
    public void hideBackground() {
        background.setVisible(false);
    }

    /**
     * Sets whether or not when the item hotspot is hovered a popup showing the items details will occur.
     * @param show True if you wish this to happen.
     */
    public void setShouldShowItemDetailsWhenHovered(boolean show) {
        shouldShowItemDetailsWhenHovered = show;
    }
}
