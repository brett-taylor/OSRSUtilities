package app.ui.components.items;

import app.OSRSUtilities;
import app.data.runescape.Item;
import app.utils.CSSColorParser;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Shows when a user hovers over a item hotspot.
 * Will show details about the item such as name, stack size and items held within it.
 * @author Brett Taylor
 */
public class ItemHover extends AnchorPane {
    /**
     * Width of the item hover.
     */
    private final static double WIDTH = 170d;

    /**
     * Height of the item hover if the item has no items within and stack size set to 1.
     */
    private final static double BASE_HEIGHT = 24d;

    /**
     * The extra height to be added if the stack size of the item is above one and so will be shown
     */
    private final static double HEIGHT_IF_STACK_SIZE_ABOVE_ONE = 20d;

    /**
     * The extra height to be added if the item has items stored inside of it.
     */
    private final static double HEIGHT_IF_HAS_ITEMS_INSIDE = 40d;

    /**
     * The limit on how many items that are stored inside it will show.
     */
    private final static int AMOUNT_OF_ITEMS_STORED_INSIDE_TO_SHOW = 3;

    /**
     * The background
     */
    private Rectangle background;

    /**
     * The item it is showing the details of.
     */
    private Item item;

    /**
     * Layout container.
     */
    private VBox layout;

    /**
     * Update timer.
     */
    private AnimationTimer updateTimer;

    /**
     * Constructor.
     * @param item The item it is representing.
     */
    public ItemHover(Item item) {
        this.item = item;

        setPrefWidth(WIDTH);
        setPrefHeight(calculateRequiredHeight());

        background = new Rectangle();
        background.setWidth(WIDTH);
        background.setHeight(calculateRequiredHeight());
        getChildren().add(background);
        background.setFill(new Color(0, 0, 0, 0.4));
        background.setStroke(CSSColorParser.parseColor("-accent-color"));

        layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(2, 2, 2, 2));
        layout.setFillWidth(true);

        getChildren().add(layout);
        AnchorPane.setLeftAnchor(layout, 0d);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);

        Label itemName = new Label();
        layout.getChildren().add(itemName);
        itemName.setText(item.getName());
        itemName.setTextFill(new Color(1, 1, 1, 0.7));
        itemName.setFont(new Font("Open Sans Bold", 14));

        ifRequiredShowStackSize();
        ifRequiredShowItemsStoredInside();

        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        updateTimer.start();

        OSRSUtilities.getWindow().getMainLayout().getChildren().add(this);
    }

    /**
     * Called often to update the dialog box.
     */
    private void update() {
        toFront();

        double windowTitlebarHeight = 30;
        Point cursorLocation = MouseInfo.getPointerInfo().getLocation();
        double newX = cursorLocation.x - OSRSUtilities.getWindow().getPrimaryStage().getX();
        double newY = cursorLocation.y - OSRSUtilities.getWindow().getPrimaryStage().getY() - windowTitlebarHeight ;

        if (newX + WIDTH + 20 > OSRSUtilities.getWindow().getPrimaryStage().getWidth()) {
            newX = cursorLocation.x - OSRSUtilities.getWindow().getPrimaryStage().getX() - WIDTH;
        }

        if (newY + calculateRequiredHeight() + windowTitlebarHeight  > OSRSUtilities.getWindow().getPrimaryStage().getHeight()) {
            newY = cursorLocation.y - OSRSUtilities.getWindow().getPrimaryStage().getY() - calculateRequiredHeight() - windowTitlebarHeight ;
        }

        setLayoutX(newX);
        setLayoutY(newY);
    }

    /**
     * If required to show the stack size, add and show the stack size label.
     */
    private void ifRequiredShowStackSize() {
        if (item.getStackSize() <= 1)
            return;

        DecimalFormat formatter = new DecimalFormat("#,###");
        String stackSizeFormatted = formatter.format(item.getStackSize());

        Label itemName = new Label();
        layout.getChildren().add(itemName);
        itemName.setText("x " + stackSizeFormatted);
        itemName.setTextFill(new Color(1, 1, 1, 0.7));
        itemName.setFont(new Font("Open Sans Light", 10));
    }

    /**
     * If required to show some items stored inside of it
     */
    private void ifRequiredShowItemsStoredInside() {
        HBox itemContainer = new HBox();
        itemContainer.setSpacing(2d);
        itemContainer.setAlignment(Pos.CENTER);
        layout.getChildren().add(itemContainer);

        int currentAmount = 0;
        for (Item i : item.getItemsInside()) {
            if (currentAmount < AMOUNT_OF_ITEMS_STORED_INSIDE_TO_SHOW) {
                ItemHotspot ih = new ItemHotspot();
                ih.attachItem(new ItemSprite(i));
                ih.hideBackground();
                ih.setItemDragging(false);
                ih.setOnMouseClicked((e) -> {});
                itemContainer.getChildren().add(ih);
                currentAmount += 1;
            }
        }
    }

    /**
     * Calculates the required height the item hover needs to be.
     * @return The calculated height.
     */
    private double calculateRequiredHeight() {
        double height = BASE_HEIGHT;

        if (item.getStackSize() > 1) {
            height += HEIGHT_IF_STACK_SIZE_ABOVE_ONE;
        }

        if (!item.getItemsInside().isEmpty()) {
            height += HEIGHT_IF_HAS_ITEMS_INSIDE;
        }

        return height;
    }

    /**
     * Destroys the item overlay correctly.
     */
    public void destroy() {
        updateTimer.stop();
        updateTimer = null;
        OSRSUtilities.getWindow().getMainLayout().getChildren().remove(this);
    }
}
