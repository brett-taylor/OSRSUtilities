package app.ui;

import app.OSRSUtilities;
import app.runescape.Item;
import app.ui.components.equipment.ItemSprite;
import app.ui.components.equipment.ItemSquare;
import javafx.animation.AnimationTimer;

import java.awt.*;

/**
 * The item sprite drag drop controller.
 * Handles creating the floating item sprite, its positioning etc.
 * @author Brett Taylor
 */
public class ItemSpriteDragDropController {
    /**
     * The item sprite.
     */
    private static ItemSprite itemSprite = null;

    /**
     * The update loop
     */
    private static AnimationTimer updateLoop = null;

    /**
     * True if the mouse is down.
     */
    private static Boolean isMouseDown = false;

    /**
     * The original item square that the item comes from.
     */
    private static ItemSquare originalItemSquare = null;

    /**
     * The possible nwe item square the item will go into.
     */
    private static ItemSquare newItemSquare = null;

    /**
     * The initialisation method call.
     * Creates the floating item sprite and the timer to update it.
     */
    public static void init() {
        itemSprite = new ItemSprite(null);
        OSRSUtilities.getWindow().getMainLayout().getChildren().add(itemSprite);
        itemSprite.setMouseTransparent(true);

        updateLoop = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                update();
            }
        };
    }

    /**
     * Alert the item drag drop controller that the mouse has been released.
     */
    public static void mouseReleased() {
        isMouseDown = false;
    }

    /**
     * Update method.
     */
    private static void update() {
        if (!isMouseDown)
            stopDragging();

        Point cursorPos = MouseInfo.getPointerInfo().getLocation();
        double newPosX = cursorPos.x - OSRSUtilities.getWindow().getPrimaryStage().getX() - (itemSprite.getWidth() / 2);
        double newPosY = cursorPos.y - OSRSUtilities.getWindow().getPrimaryStage().getY() - (itemSprite.getHeight() / 2);
        itemSprite.relocate(newPosX, newPosY);
        itemSprite.toFront();
    }

    /**
     * Starts dragging of a item.
     * @param item The item that will be attached to the item sprite - the image of the item will display.
     * @param originalItemSquare The original item square the item is coming from.
     */
    public static void startDragging(Item item, ItemSquare originalItemSquare) {
        if (itemSprite.getItem() == null) {
            itemSprite.setItem(item);
            updateLoop.start();
            isMouseDown = true; // Set so the mouse is checked at least one.
            ItemSpriteDragDropController.originalItemSquare = originalItemSquare;
        }
    }

    /**
     * Stops the dragging.
     */
    private static void stopDragging() {
        if (newItemSquare != null) {
            if (newItemSquare.hasItemAttached()) {
                Item replaceItem = newItemSquare.getAttachedItem().getItem();
                originalItemSquare.attachItem(new ItemSprite(replaceItem));
            }
            newItemSquare.attachItem(new ItemSprite(itemSprite.getItem()));
        } else {
            originalItemSquare.attachItem(new ItemSprite(itemSprite.getItem()));
        }

        itemSprite.setItem(null);
        originalItemSquare = null;
        updateLoop.stop();
    }

    /**
     * Updates the item drag drop controller about a possible square the item can be dropped into.
     * @param itemSquare
     */
    public static void registerSuccessfullItemSquare(ItemSquare itemSquare) {
        newItemSquare = itemSquare;
    }

    /**
     * Updates the item drag drop controller that the possible square is no longer valid to be dropped into.
     */
    public static void deRegisterSuccessfullItmeSquare() {
        newItemSquare = null;
    }
}
