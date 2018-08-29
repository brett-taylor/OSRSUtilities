package app.ui.components.popups.addmultipleitems;

import app.data.runescape.Item;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.ui.components.items.ItemHotspot;
import app.ui.components.items.ItemSprite;
import app.ui.components.popups.PopupMenu;
import app.ui.components.popups.searchitem.SelectItemPopup;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Popup used when adding multiple items.
 * @author Brett Taylor
 */
public class AddMultipleItemsPopup extends PopupMenu {
    /**
     * The layout component.
     */
    private VBox layout;

    /**
     * The selected item's hotspot.
     */
    private ItemHotspot itemHotspot;

    /**
     * The item name label.
     */
    private Label itemName;

    /**
     * The current square button that was selected.
     */
    private SquareButton currentSelectedButton = null;

    /**
     * Whether it can be submitted or not.
     */
    private boolean canBeSubmitted = false;

    /**
     * The submit button.
     */
    private CircularButton submitButton;

    /**
     * The OnAddMultipleItemsSuccess listener.
     */
    private OnAddMultipleItemsSuccess onAddMultipleItemsSuccess;

    /**
     * Constructor.
     * @param item Can be null.
     */
    public AddMultipleItemsPopup(Item item) {
        heading.setText("Add Inventory Items");
        CircularButton cancelledButton = CircularButton.failedButton();
        buttonRow.getChildren().add(cancelledButton);
        cancelledButton.setOnClicked(this::startByeAnimation);

        submitButton = CircularButton.successButton();
        buttonRow.getChildren().add(submitButton);
        submitButton.setOnClicked(() -> {
            if (canBeSubmitted) {
                if (onAddMultipleItemsSuccess != null) {
                    onAddMultipleItemsSuccess.onSucecss(
                            itemHotspot.getAttachedItem().getItem(),
                            Integer.parseInt(currentSelectedButton.getText())
                    );
                }

                startByeAnimation();
            }
        });

        layout = new VBox();
        mainBody.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);
        AnchorPane.setLeftAnchor(layout, 0d);
        layout.setPadding(new Insets(5, 5, 5,5 ));
        layout.setFillWidth(true);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setSpacing(5d);

        HBox selectItemRow = new HBox();
        layout.getChildren().add(selectItemRow);
        selectItemRow.setAlignment(Pos.CENTER);
        selectItemRow.setSpacing(5d);

        itemHotspot = new ItemHotspot();
        selectItemRow.getChildren().add(itemHotspot);
        itemHotspot.setItemDragging(false);
        itemHotspot.setOnMouseClicked((e) ->  {
            Item currentSelectedItem = null;
            if (itemHotspot.getAttachedItem() != null) {
                currentSelectedItem = itemHotspot.getAttachedItem().getItem();
            }
            this.onItemHotspotClicked(currentSelectedItem);
        });

        itemName = new Label();
        itemName.setFont(new Font("Open Sans", 15));
        itemName.setText("Select an Item");
        itemName.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        selectItemRow.getChildren().add(itemName);

        FlowPane buttonContainer = new FlowPane();
        layout.getChildren().add(buttonContainer);
        VBox.setVgrow(buttonContainer, Priority.ALWAYS);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setHgap(2d);
        buttonContainer.setVgap(2d);

        for (int i = 1; i <= 28; i++) {
            SquareButton button = SquareButton.defaultButton();
            button.setText("" + i);
            button.setGlyphSize(0);
            button.setGlyphPadding(0, 0, 0, 0);
            button.setSize(30, 35);
            button.setTextPadding(0, 0, 0, 0);
            button.setFont(new Font("Open Sans", 14));
            buttonContainer.getChildren().add(button);
            button.setOnClicked(() -> onAmountButtonClicked(button));
        }

        SquareButton firstButton = (SquareButton) buttonContainer.getChildren().get(0);
        currentSelectedButton = firstButton;
        onAmountButtonClicked(firstButton);

        if (item != null) {
            itemHotspot.attachItem(new ItemSprite(item));
            itemName.setText(item.getName());
        }
        checkIfCanBeSubmittedAndCorrectSubmitButton();
    }

    /**
     * Called when the item hotspot has been clicked
     * @param currentSelectedItem Current selected item. Can be null.
     */
    private void onItemHotspotClicked(Item currentSelectedItem) {
        startByeAnimation();
        SelectItemPopup popup = SelectItemPopup.show();
        OnAddMultipleItemsSuccess listener = onAddMultipleItemsSuccess;

        popup.setOnSelectItemCancelled(() ->  {
            AddMultipleItemsPopup addMultipleItemsPopup = new AddMultipleItemsPopup(currentSelectedItem);
            addMultipleItemsPopup.startHelloAnimation();
        });

        popup.setOnSelectItemConfirmed((item) -> {
            AddMultipleItemsPopup addMultipleItemsPopup = new AddMultipleItemsPopup(item);
            addMultipleItemsPopup.startHelloAnimation();
            addMultipleItemsPopup.setOnAddMultipleItemsSuccess(listener);
        });
    }

    /**
     * Called when one of the amount buttons have been clicked.
     * @param button The button.
     */
    private void onAmountButtonClicked(SquareButton button) {
        if (currentSelectedButton != null) {
            currentSelectedButton .setBorder(Color.BLACK, 1);
        }

        button.setBorder(CSSColorParser.parseColor("-good-color"), 3);
        currentSelectedButton = button;
        checkIfCanBeSubmittedAndCorrectSubmitButton();
    }

    /**
     * Checks to see if it can be submitted will activate the submit button.
     */
    private void checkIfCanBeSubmittedAndCorrectSubmitButton() {
        if (itemHotspot.getAttachedItem() == null || itemHotspot.getAttachedItem().getItem() == null || currentSelectedButton == null) {
            canBeSubmitted = false;
        } else {
            canBeSubmitted = true;
        }

        if (canBeSubmitted) {
            submitButton.setBackgroundColor(CSSColorParser.parseColor("-good-color"));
            submitButton.setBackgroundHoverColor(CSSColorParser.parseColor("-good-secondary-color"));
            submitButton.setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-good-overlay-color"));
        } else {
            submitButton.setBackgroundColor(CSSColorParser.parseColor("-background-light-color"));
            submitButton.setBackgroundHoverColor(CSSColorParser.parseColor("-background-light-color"));
            submitButton.setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-background-color"));
        }
    }

    /**
     * Sets the OnAddMultipleItemsSuccess listener.
     * @param onAddMultipleItemsSuccess The new listener.
     */
    public void setOnAddMultipleItemsSuccess(OnAddMultipleItemsSuccess onAddMultipleItemsSuccess) {
        this.onAddMultipleItemsSuccess = onAddMultipleItemsSuccess;
    }
}
