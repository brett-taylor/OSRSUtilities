package app.ui.components.loadouts;

import app.OSRSUtilities;
import app.ui.components.DialogBox;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.awt.*;

/**
 * The item sprite drag drop controller.
 * Handles creating the floating item sprite, its positioning etc.
 * @author Brett Taylor
 */
public class ItemDragDropController {
    /**
     * The instance of ItemDragDropController if it has been activated by the page.
     */
    private static ItemDragDropController instance;

    /**
     * The update loop
     */
    private AnimationTimer updateLoop;

    /**
     * The item sprite currently being dragged.
     */
    private ItemSprite draggingItem;

    /**
     * The item hotspot the item came from before it got dragged.
     */
    private ItemHotspot originalItemHotspot;

    /**
     * Stores the current hovered item hotspot.
     */
    private ItemHotspot hoveredItemHotspot;

    /**
     * Holds the item sprite.
     */
    private Pane itemSpriteContainer;

    /**
     * Returns the current instance activated by a page of the ItemDragDropController.
     * @return the active ItemDragDropController. If no active ItemDragDropController then returns null.
     */
    public static ItemDragDropController get() {
        return instance;
    }

    /**
     * Activates the ItemDragDropController for the current page.
     */
    public static void activate() {
        if (instance != null) {
            DialogBox.showError("Item Drag Drop Error.");
            DialogBox.showBodyLabel("There is currently a active ItemDragDropController already.");
            return;
        } else {
            instance = new ItemDragDropController();
        }
    }

    /**
     * Deactives the current ItemDragDropController.
     */
    public static void deactive() {
        instance = null;
    }

    /**
     * Constructor.
     */
    private ItemDragDropController() {
        updateLoop = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                update();
            }
        };
    }

    /**
     * Update method.
     */
    private void update() {
        Point cursorPos = MouseInfo.getPointerInfo().getLocation();
        double newPosX = cursorPos.x - OSRSUtilities.getWindow().getPrimaryStage().getX() - (draggingItem.getWidth() / 2);
        double newPosY = cursorPos.y - OSRSUtilities.getWindow().getPrimaryStage().getY() - (draggingItem.getHeight());
        itemSpriteContainer.setLayoutX(newPosX);
        itemSpriteContainer.setLayoutY(newPosY);
        itemSpriteContainer.toFront();
    }

    /**
     * Starts dragging of a item.
     * @param itemSprite The item sprite that is representing the item.
     * @param originalItemSquare The original item hotspot the item is coming from.
     */
    public void startDragging(ItemSprite itemSprite, ItemHotspot originalItemSquare) {
        if (itemSprite != null) {
            draggingItem = itemSprite;
            this.originalItemHotspot = originalItemSquare;
            itemSpriteContainer = new Pane();
            itemSpriteContainer.getChildren().add(draggingItem);
            itemSpriteContainer.setMouseTransparent(true);
            OSRSUtilities.getWindow().getMainLayout().getChildren().add(itemSpriteContainer);
            updateLoop.start();
        }
    }

    /**
     * Called when it stops dragging.
     */
    public void stopDragging() {
        if (originalItemHotspot != null) {
            if (hoveredItemHotspot == null) {
                // Drop it in its original hotspot as it wasnt over any valid hotspot.
                originalItemHotspot.attachItem(draggingItem);
            } else {
                if (hoveredItemHotspot.getAttachedItem() != null) {
                    // Item in the new hotspot - swap those items.
                    originalItemHotspot.attachItem(hoveredItemHotspot.getAttachedItem());
                    hoveredItemHotspot.unattachItem();
                    hoveredItemHotspot.attachItem(draggingItem);
                } else {
                    // The new hotspot is empty just insert it into the new slot.
                    hoveredItemHotspot.attachItem(draggingItem);
                }
            }
        }

        updateLoop.stop();
        OSRSUtilities.getWindow().getMainLayout().getChildren().remove(itemSpriteContainer);
        draggingItem = null;
        originalItemHotspot = null;
        itemSpriteContainer = null;
    }

    /**
     * Sets the current hovered item hotspot.
     */
    public void setHoveredItemHotspot(ItemHotspot itemHotspot) {
        this.hoveredItemHotspot = itemHotspot;
    }
}
