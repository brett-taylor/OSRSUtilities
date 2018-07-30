package app.ui.components.equipment;

import app.ui.ItemSpriteDragDropController;
import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * The item ui element.
 * @author Brett Taylor
 */
public class ItemSquare extends BorderPane {
    /**
     * The width of the item square.
     */
    private final static float WIDTH = 50;

    /**
     * The height of the item square.
     */
    private final static float HEIGHT = 50;

    /**
     * The width of the border
     */
    private final static int BORDER_WIDTH = 1;

    /**
     * The background color
     */
    private final static Color BACKGROUND_COLOR = Color.color(0.2f, 0.2f, 0.2f);

    /**
     * The background color while mouse is hovered.
     */
    private final static Color BACKGROUND_HOVERED_COLOR = Color.color(0.25f, 0.25f, 0.25f);

    /**
     * The border color
     */
    private final static Color BORDER_COLOR = Color.color(0.1f, 0.1f, 0.1f);

    /**
     * Item Sprite.
     */
    private ItemSprite item;

    /**
     * Constructor.
     */
    public ItemSquare() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setMinWidth(WIDTH);
        setMinHeight(HEIGHT);
        setMaxWidth(WIDTH);
        setMaxHeight(HEIGHT);
        setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, null, null)));
        setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BORDER_WIDTH))));

        setOnMouseEntered(e -> onMouseEntered());
        setOnMouseExited(e -> onMouseExit());
    }

    /**
     * Attaches a item to the square.
     * @param item The item to be attached
     */
    public void attachItem(ItemSprite item) {
        this.item = item;
        setCenter(item);
        item.attachToSquare(this);
    }

    /**
     * Unattaches any item that is already attached.
     */
    public void unAttachItem() {
        item.unAttachFromSquare();
        setCenter(null);
        item = null;
    }

    /**
     * @return The item attached to the square.
     */
    public ItemSprite getAttachedItem() {
        return item;
    }

    /**
     * @return True if it has a item attached to it.
     */
    public boolean hasItemAttached() {
        return item != null;
    }

    /**
     * Called when the mouse enters
     */
    private void onMouseEntered() {
        setBackground(new Background(new BackgroundFill(BACKGROUND_HOVERED_COLOR, null, null)));
        if (hasItemAttached()) {
            setCursor(Cursor.CLOSED_HAND);
        }

        // Dont allow lodaing items to be swapped.
        if (getAttachedItem() != null && getAttachedItem().isLoading())
            return;

        ItemSpriteDragDropController.registerSuccessfullItemSquare(this);
    }

    /**
     * Called when the mouse exits
     */
    private void onMouseExit() {
        setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, null, null)));
        if (hasItemAttached()) {
            setCursor(Cursor.DEFAULT);
        }
        ItemSpriteDragDropController.deRegisterSuccessfullItmeSquare();
    }
}
