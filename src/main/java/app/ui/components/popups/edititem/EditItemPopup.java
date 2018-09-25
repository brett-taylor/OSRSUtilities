package app.ui.components.popups.edititem;

import app.data.runescape.Item;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.ui.components.items.ItemHotspot;
import app.ui.components.items.ItemSprite;
import app.ui.components.popups.PopupMenu;
import app.ui.components.popups.searchitem.SelectItemPopup;
import app.utils.CSSColorParser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Edit item menu.
 * @author Brett Taylor
 */
public class EditItemPopup extends PopupMenu {
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
     * The current stack size.
     */
    private int stackSize;

    /**
     * The text field that will show the stack size.
     */
    private TextField stackSizeTextfield;

    /**
     * Counts down after the user has stopped typing in the stack field til it should format and check what was entered..
     */
    private Timer stackSizeEditCooldownTimer;

    /**
     * The time till the timer activates.
     */
    private final static long stackSizeEditCooldown = 1000;

    /**
     * The OnEditItemSuccess listener.
     */
    private OnEditItemSuccess onEditItemSuccess;

    /**
     * The runnable that will be called when the popup closes.
     */
    private Runnable onEditItemFailed;

    /**
     * Called when the delete button is pressed.
     */
    private Runnable onRequestToDestroy;

    /**
     * Component that allows items inside of the item to be edited.
     */
    private EditItemsInEditComponent editItemsInEditComponent;

    /**
     * Constructor.
     * @param item The item to open the edit menu on.
     * @param currentStackSize The stack size of the item currently.
     * @param showItemsInsideItem If it should show a way to edit the items inside of the item.
     * @param showDeleteButton  If it should show the delete button.
     */
    public EditItemPopup(Item item, int currentStackSize, boolean showItemsInsideItem, boolean showDeleteButton) {
        innerMainLayout.setPrefHeight(450);
        heading.setText("Edit item");
        stackSize = currentStackSize;

        CircularButton cancelledButton = CircularButton.failedButton();
        buttonRow.getChildren().add(cancelledButton);
        Runnable onCancel = () -> {
            startByeAnimation();
            if (onEditItemFailed != null) {
                onEditItemFailed.run();
            }
        };

        cancelledButton.setOnClicked(onCancel);
        setOnShortcutFailed(onCancel);

        CircularButton successButton = CircularButton.successButton();
        buttonRow.getChildren().add(successButton);
        Runnable onSuccess = () -> {
            if (onEditItemSuccess != null) {
                Item newItem = itemHotspot.getAttachedItem().getItem();
                newItem.setStackSize(stackSize);
                if (editItemsInEditComponent != null) {
                    newItem.getItemsInside().clear();
                    newItem.getItemsInside().addAll(editItemsInEditComponent.getItems());
                }
                onEditItemSuccess.onSucecss(newItem);
            }

            startByeAnimation();
        };

        successButton.setOnClicked(onSuccess);
        setOnShortcutSuccess(onSuccess);

        layout = new VBox();
        mainBody.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);
        AnchorPane.setLeftAnchor(layout, 0d);
        layout.setFillWidth(true);
        layout.setAlignment(Pos.TOP_CENTER);

        addItemSelectRow(item);
        addSpacer();
        addStackEdit();
        if (showItemsInsideItem) {
            addSpacer();
            addItemsInsideEditRow();
        }
        if (showDeleteButton) {
            addSpacer();
            addDeleteItemRow();
        }
    }

    /**
     * Adds the select item row.
     * @param item The item to be default in the box.
     */
    private void addItemSelectRow(Item item) {
        HBox selectItemRow = new HBox();
        layout.getChildren().add(selectItemRow);
        selectItemRow.setAlignment(Pos.CENTER);
        selectItemRow.setSpacing(5d);
        selectItemRow.setPadding(new Insets(5, 5, 10,5 ));

        itemHotspot = new ItemHotspot();
        selectItemRow.getChildren().add(itemHotspot);
        itemHotspot.setItemDragging(false);
        itemHotspot.attachItem(new ItemSprite(item));
        itemHotspot.setStackSizeVisible(false);
        itemHotspot.setShouldShowItemDetailsWhenHovered(false);
        itemHotspot.setOnMouseClicked((e) ->  {
            startByeAnimation();
            SelectItemPopup popup = SelectItemPopup.show();
            popup.setOnSelectItemCancelled(this::startHelloAnimation);
            popup.setOnSelectItemConfirmed((newItem) -> {
                startHelloAnimation();
                itemHotspot.unattachItem();
                itemHotspot.attachItem(new ItemSprite(newItem));
                itemName.setText(newItem.getName());
            });
        });

        itemName = new Label();
        itemName.setFont(new Font("Open Sans", 15));
        itemName.setText("Select an Item");
        itemName.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        itemName.setText(item.getName());
        selectItemRow.getChildren().add(itemName);
    }

    /**
     * Adds a black spacer.
     */
    private void addSpacer() {
        Rectangle rectangle = new Rectangle();
        Platform.runLater(() -> rectangle.setWidth(innerMainLayout.getWidth() - 10));
        rectangle.setHeight(2d);
        layout.getChildren().add(rectangle);
    }

    /**
     * Adds the change stack size of the item row.
     */
    private void addStackEdit() {
        Label label = new Label();
        label.setFont(new Font("Open Sans", 13));
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        label.setText("Quantity:");
        layout.getChildren().add(label);
        label.setPadding(new Insets(5, 5, 0,5 ));

        HBox editStackSizeRow = new HBox();
        layout.getChildren().add(editStackSizeRow);
        editStackSizeRow.setAlignment(Pos.CENTER);
        editStackSizeRow.setSpacing(5d);
        editStackSizeRow.setPadding(new Insets(5, 5, 5,5 ));

        HBox buttonContainer = new HBox();
        editStackSizeRow.getChildren().add(buttonContainer);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(5d);
        addStackEditButton(buttonContainer, "-1M", () -> adjustStackSize(-1000000));
        addStackEditButton(buttonContainer, "-100k", () -> adjustStackSize(-100000));
        addStackEditButton(buttonContainer, "-1", () -> adjustStackSize(-1));
        addStackEditButton(buttonContainer, "+1", () -> adjustStackSize(1));
        addStackEditButton(buttonContainer, "+100K", () -> adjustStackSize(100000));
        addStackEditButton(buttonContainer, "+1M", () -> adjustStackSize(1000000));

        HBox stackSizeTextfieldContainer = new HBox();
        layout.getChildren().add(stackSizeTextfieldContainer);
        stackSizeTextfieldContainer.setAlignment(Pos.CENTER);
        stackSizeTextfieldContainer.setSpacing(5d);
        stackSizeTextfieldContainer.setPadding(new Insets(5, 5, 5,5 ));

        stackSizeTextfield = new TextField();
        stackSizeTextfield.setPromptText("Quantity");
        stackSizeTextfield.setAlignment(Pos.CENTER);
        stackSizeTextfield.getStyleClass().add("nice-textfield");
        stackSizeTextfield.setId("dark-searchbox");
        stackSizeTextfield.setPrefHeight(30d);
        stackSizeTextfield.prefWidthProperty().bind(stackSizeTextfieldContainer.widthProperty());
        stackSizeTextfield.setText(formatNumberWithCommas(stackSize));
        stackSizeTextfieldContainer.getChildren().add(stackSizeTextfield);
        stackSizeTextfield.setOnKeyTyped((event) -> {
            String textContents = stackSizeTextfield.getText().replaceAll("[^\\d.]", "");
            if (!textContents.isEmpty()) {
                stackSize = Integer.parseInt(textContents);
            }

            if (stackSizeEditCooldownTimer != null) {
                stackSizeEditCooldownTimer.cancel();
                stackSizeEditCooldownTimer = null;
            }

            stackSizeEditCooldownTimer = new Timer();
            stackSizeEditCooldownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stackSizeTextfield.setText(formatNumberWithCommas(stackSize));
                }
            }, stackSizeEditCooldown);
        });
    }

    /**
     * Adds stack edit button
     * @param buttonContainer Where the button will be added
     * @param buttonLabel The label the button will be
     * @param onClicked What happens when its clicked.
     */
    private void addStackEditButton(HBox buttonContainer, String buttonLabel, Runnable onClicked) {
        SquareButton editStackButton = SquareButton.defaultButton();
        editStackButton.setText(buttonLabel);
        editStackButton.setSize(60, 35);
        editStackButton.setGlyphSize(0);
        editStackButton.setGlyphPadding(0, 0, 0, 0);
        editStackButton.setTextPadding(0, 0, 0, 0);
        editStackButton.setFont(new Font("Open Sans", 14));
        editStackButton.setOnClicked(onClicked);
        buttonContainer.getChildren().add(editStackButton);
    }

    /**
     * Adjusts the stack size by a certain amount.
     * @param amountToAdjustBy adjust the stack size by this amount.
     */
    private void adjustStackSize(int amountToAdjustBy) {
        int newStackSize;
        try {
            newStackSize = Math.addExact(stackSize, amountToAdjustBy);
            if (newStackSize < 1) {
                newStackSize = 1;
            }
        } catch (ArithmeticException overFlowDetected) {
            newStackSize = Integer.MAX_VALUE;
        }

        stackSize = newStackSize;
        if (stackSizeTextfield != null) {
            stackSizeTextfield.setText(formatNumberWithCommas(stackSize));
        }
    }

    /**
     * Adds the row dedicated to editing the items inside of the item.
     */
    private void addItemsInsideEditRow() {
        Label label = new Label();
        label.setFont(new Font("Open Sans", 13));
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        label.setText("Items stored inside:");
        layout.getChildren().add(label);
        label.setPadding(new Insets(5, 5, 0,5 ));

        editItemsInEditComponent = new EditItemsInEditComponent(this, itemHotspot.getAttachedItem().getItem().getItemsInside());
        layout.getChildren().add(editItemsInEditComponent);
        VBox.setVgrow(editItemsInEditComponent, Priority.ALWAYS);
    }

    /**
     * Adds a row dedicated to deleting the item.
     */
    private void addDeleteItemRow() {
        SquareButton delete = new SquareButton();
        layout.getChildren().add(delete);
        delete.setSize(0, 30d);
        delete.setTextAllignment(Pos.CENTER);
        delete.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        delete.setText("Delete Item");
        delete.setBackgroundColor(CSSColorParser.parseColor("-accent-color"));
        delete.setBackgroundHoverColor(CSSColorParser.parseColor("-alert-overlay-color"));
        delete.setTextColor(Color.WHITE);
        delete.setFont(new Font("Open Sans", 12));
        delete.setPadding(new Insets(5, 5, 20,5 ));
        delete.setOnClicked(() -> {
            startByeAnimation();
            if (onRequestToDestroy != null) {
                onRequestToDestroy.run();
            }
        });
    }

    /**
     * Formats a number to add commas to make it readable.
     * @param number The number to format
     * @return The formatted number iwth commas as a string
     */
    private String formatNumberWithCommas(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    /**
     * Sets the OnEditItemSuccess listener.
     * @param onEditItemSuccess The new listener.
     */
    public void setOnEditItemSuccess(OnEditItemSuccess onEditItemSuccess) {
        this.onEditItemSuccess = onEditItemSuccess;
    }

    /**
     * Sets the runnable to be called when the popup closes.
     * @param runnable the runnable.
     */
    public void setOnEditItemFailed(Runnable runnable) {
        this.onEditItemFailed = runnable;
    }

    /**
     * Sets the runnable to be called when the delete button is pressed.
     * @param runnable the runnable.
     */
    public void setOnRequestToDestroy(Runnable runnable) {
        this.onRequestToDestroy = runnable;
    }

    /**
     * @return The selected item.
     */
    public Item getItem() {
        return itemHotspot.getAttachedItem().getItem();
    }

    /**
     * @return The currently selected stack size.
     */
    public int getStackSize() {
        return stackSize;
    }

    /**
     * @return What will be called when it successfully closes.
     */
    public OnEditItemSuccess getOnSuccess() {
        return onEditItemSuccess;
    }

    /**
     * @return What will be called when it should not do anything.
     */
    public Runnable getOnFailed() {
        return onEditItemFailed;
    }
}
