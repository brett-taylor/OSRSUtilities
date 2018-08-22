package app.ui.components.popups.searchItem;

import app.data.runescape.Item;
import app.ui.components.items.ItemSprite;
import app.utils.CSSColorParser;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;


/**
 * The item result that will appear in a select item popup.
 * @author Brett Taylor
 */
public class ItemSearchResult extends AnchorPane {
    /**
     * The duration the hello and bye animations will last.
     */
    private static Duration ANIMATION_TIME = Duration.millis(200);

    /**
     * The background color
     */
    private final static Color BACKGROUND_COLOR = CSSColorParser.parseColor("-background-dark-color");

    /**
     * The background hover color
     */
    private final static Color BACKGROUND_HOVER_COLOR = CSSColorParser.parseColor("-background-light-color");

    /**
     * The item it is representing.
     */
    private Item item;

    /**
     * The background.
     */
    private Rectangle background;

    /**
     * The item name label.
     */
    private Label itemName;

    /**
     * The OnItemSearchResultClicked event.
     */
    private OnItemSearchResultClicked onItemSearchResultClicked;

    /**
     * Constructor.
     * @param item the item it will be representing.
     */
    public ItemSearchResult(Item item) {
        this.item = item;
        setPrefWidth(200);
        setPrefHeight(50);

        background = new Rectangle();
        getChildren().add(background);
        background.setWidth(getPrefWidth());
        background.setHeight(getPrefHeight());
        background.setFill(BACKGROUND_COLOR);
        setNormalBorder();

        HBox hbox = new HBox();
        hbox.prefWidthProperty().bind(this.widthProperty());
        hbox.prefHeightProperty().bind(this.heightProperty());
        getChildren().add(hbox);
        hbox.setFillHeight(true);

        ItemSprite itemSprite = new ItemSprite(item);
        hbox.getChildren().add(itemSprite);

        BorderPane labelParent = new BorderPane();
        hbox.getChildren().add(labelParent);
        HBox.setHgrow(labelParent, Priority.ALWAYS);

        itemName = new Label();
        itemName.setText(item.getName());
        itemName.setAlignment(Pos.CENTER);
        itemName.setFont(new Font("Open Sans", 13));
        itemName.setWrapText(true);
        itemName.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        labelParent.setCenter(itemName);

        setOnMouseEntered(this::onMouseEnter);
        setOnMouseExited(this::onMouseExit);
        setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * Called when the mouse enters
     * @param e The mouse event.
     */
    private void onMouseEnter(MouseEvent e) {
        FillTransition ft = new FillTransition(ANIMATION_TIME , background, BACKGROUND_COLOR, BACKGROUND_HOVER_COLOR);
        ft.setCycleCount(1);
        ft.play();
        setCursor(Cursor.HAND);
        itemName.setTextFill(CSSColorParser.parseColor("-text-color"));
    }

    /**
     * Called when the mouse exits.
     * @param e The mouse event.
     */
    private void onMouseExit(MouseEvent e) {
        FillTransition ft = new FillTransition(ANIMATION_TIME , background, BACKGROUND_HOVER_COLOR, BACKGROUND_COLOR);
        ft.setCycleCount(1);
        ft.play();
        setCursor(Cursor.DEFAULT);
        itemName.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
    }

    /**
     * Called when the mouse clicks.
     * @param e The mouse event
     */
    private void onMouseClicked(MouseEvent e) {
        if (onItemSearchResultClicked != null)
            onItemSearchResultClicked.onClicked(this, item);
    }

    /**
     * Sets the OnItemSearchResultClicked listener.
     * @param onItemSearchResultClicked The listener.
     */
    public void setOnItemSearchResultClicked(OnItemSearchResultClicked onItemSearchResultClicked) {
        this.onItemSearchResultClicked = onItemSearchResultClicked;
    }

    /**
     * Selects this item searchItem result.
     */
    public void setSelectedBorder() {
        background.setStroke(CSSColorParser.parseColor("-good-color"));
        background.setStrokeWidth(3);
    }

    /**
     * Unselects this item searchItem result.
     */
    public void setNormalBorder() {
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1);
    }

    /**
     * @return The item it is representing.
     */
    public Item getItem() {
        return item;
    }
}
