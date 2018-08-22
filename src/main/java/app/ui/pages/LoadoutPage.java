package app.ui.pages;

import app.OSRSUtilities;
import app.data.loadouts.Loadout;
import app.data.loadouts.LoadoutManager;
import app.data.runescape.Item;
import app.ui.components.popups.addMultipleItems.AddMultipleItemsPopup;
import app.ui.components.popups.DialogBox;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.ui.components.items.containers.EquipmentContainerView;
import app.ui.components.items.containers.ItemContainerView;
import app.ui.components.items.ItemDragDropController;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * Page that can be used to show the loadout.
 * @author Brett Taylor
 */
public class LoadoutPage extends BasePage {
    /**
     * The loadout it is representing.
     */
    private Loadout loadout;

    /**
     * Holds both the equipment and inventory views.
     */
    private HBox itemContainersView;

    /**
     * The equipment tab view.
     */
    private ItemContainerView equipmentTab;

    /**
     * The inventory tab view.
     */
    private ItemContainerView inventoryTab;

    /**
     * The parent.
     */
    private AnchorPane parent;


    /**
     * Constructor
     * @[param loadout The loadout it is represnting.
     */
    public LoadoutPage(Loadout loadout) {
        super("/fxml/pages/LoadoutPage.fxml", true);
        this.loadout = loadout;

        parent = (AnchorPane) getParentElement();

        equipmentTab = new EquipmentContainerView(loadout.getEquipment());
        inventoryTab = new ItemContainerView(loadout.getInventory());

        ScrollPane scrollPane = (ScrollPane) lookup("#scrollPane");
        Objects.requireNonNull(scrollPane);
        VBox scrollPaneContents = (VBox) scrollPane.getContent();
        Objects.requireNonNull(scrollPaneContents);

        itemContainersView = (HBox) scrollPaneContents.lookup("#itemContainersView");
        Objects.requireNonNull(itemContainersView);
        itemContainersView.getChildren().addAll(equipmentTab, inventoryTab);

        HBox buttonContainer = (HBox) scrollPaneContents.lookup("#buttonContainer");
        Objects.requireNonNull(buttonContainer);

        SquareButton backButton = SquareButton.defaultButton();
        buttonContainer.getChildren().add(backButton);
        backButton.setSize(200, 30);
        backButton.setGlyph(FontAwesomeIcon.BACKWARD, CSSColorParser.parseColor("-text-muted-color"));
        backButton.setGlyphSize(20);
        backButton.setText("Back to Loadouts");
        backButton.setOnClicked(() -> OSRSUtilities.getWindow().showPage(new ViewLoadoutsPage()));

        SquareButton saveButton = SquareButton.defaultButton();
        buttonContainer.getChildren().add(saveButton);
        saveButton.setSize(200, 30);
        saveButton.setGlyph(FontAwesomeIcon.SAVE, CSSColorParser.parseColor("-text-muted-color"));
        saveButton.setGlyphSize(20);
        saveButton.setText("Save Loadout");
        saveButton.setOnClicked(this::onSaveButtonClicked);

        SquareButton addButton = SquareButton.defaultButton();
        buttonContainer.getChildren().add(addButton);
        addButton.setSize(220, 30);
        addButton.setFont(new Font("Open Sans", 13));
        addButton.setTextPadding(0, 0, 0, 6);
        addButton.setGlyphPadding(0, 0, 0, 6);
        addButton.setGlyph(FontAwesomeIcon.PLUS, CSSColorParser.parseColor("-text-muted-color"));
        addButton.setGlyphSize(20);
        addButton.setText("Add Multiple Inventory Items");
        addButton.setOnClicked(this::onAddItemsClicked);
    }

    @Override
    public void onLoaded() {
        ItemDragDropController.activate();
        setOnMouseReleased((e) -> ItemDragDropController.get().stopDragging());
    }

    @Override
    public void onRemoved() {
        ItemDragDropController.deactive();
    }

    /**
     * Called when the save button has been clicked.
     */
    private void onSaveButtonClicked() {
        DialogBox.showDialogBox("Saving the loadout");
        DialogBox.showingLoadingSpinner();
        new Thread(() -> {
            LoadoutManager.save(loadout);
            Platform.runLater(() -> {
                DialogBox.clearBody();
                DialogBox.showDialogBox("Loadout saved");
                DialogBox.showBodyLabel("Your loadout saved successfully.");
                CircularButton close = CircularButton.successButton();
                close.setGlyph(FontAwesomeIcon.CHECK, null);
                close.setOnClicked(DialogBox::close);
                DialogBox.addToButtonRow(close);
            });
        }).start();
    }

    /**
     * Called when the add items button has been clicked.
     */
    private void onAddItemsClicked() {
        AddMultipleItemsPopup addMultipleItemsPopup = new AddMultipleItemsPopup(null);
        addMultipleItemsPopup.show();
        addMultipleItemsPopup.setOnAddMultipleItemsSuccess((item, amount) -> inventoryTab.addMultipleItems(item, amount));
    }
}
