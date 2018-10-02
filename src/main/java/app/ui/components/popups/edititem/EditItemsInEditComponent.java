package app.ui.components.popups.edititem;

import app.data.runescape.Item;
import app.ui.components.buttons.SquareButton;
import app.ui.components.items.ItemHotspot;
import app.ui.components.items.ItemSprite;
import app.ui.components.popups.searchitem.SelectItemPopup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class EditItemsInEditComponent extends ScrollPane {
    /**
     * The grid container.
     */
    private FlowPane gridView;

    /**
     * The edit item popup that created it.
     */
    private EditItemPopup editItemPopup;

    /**
     * The add new item hotspot button.
     */
    private SquareButton addNewHotspotButton;

    /**
     * Keeps track of all the item hotspots.
     */
    private List<ItemHotspot> itemHotspots;

    /**
     * The most recent item hotspot.
     */
    private ItemHotspot mostRecentHotspot;

    /**
     * Constructor
     * @param items Items that are already contained inside.
     */
    public EditItemsInEditComponent(EditItemPopup editItemPopup, List<Item> items) {
        this.editItemPopup = editItemPopup;
        itemHotspots = new ArrayList<>();

        setPadding(new Insets(5, 5, 5,5 ));
        setFitToWidth(true);
        getStyleClass().add("invis-scrollpane");
        getStyleClass().add("edge-to-edge");

        gridView = new FlowPane();
        setContent(gridView);
        gridView.setAlignment(Pos.CENTER);
        gridView.setHgap(5d);
        gridView.setVgap(5d);

        for (Item i : items) {
            ItemHotspot ih = createNewItemHotspot();
            ih.attachItem(new ItemSprite(i));
            gridView.getChildren().add(ih);
        }

        addNewHotspotButton = SquareButton.defaultButton();
        addNewHotspotButton.setGlyph(FontAwesomeIcon.PLUS, Color.WHITE);
        addNewHotspotButton.setGlyphSize(18);
        addNewHotspotButton.setText("Add Item");
        addNewHotspotButton.setSize(120d, ItemHotspot.HEIGHT);
        gridView.getChildren().add(addNewHotspotButton);
        addNewHotspotButton.setOnClicked(this::addNewItemHotspot);
    }

    /**
     * Called when the add new hotspot is clicked.
     */
    private void addNewItemHotspot() {
        ItemHotspot ih = createNewItemHotspot();
        gridView.getChildren().add(ih);

        gridView.getChildren().remove(addNewHotspotButton);
        mostRecentHotspot = ih;
    }

    /**
     * Creates a item hotspot with the correct behaviour.
     */
    private ItemHotspot createNewItemHotspot() {
        ItemHotspot ih = new ItemHotspot();
        ih.setItemDragging(false);
        ih.setShouldShowItemDetailsWhenHovered(false);
        ih.setOnMouseClicked((e) -> {
            if (ih.getAttachedItem() == null) {
                editItemPopup.startByeAnimation();
                SelectItemPopup selectItemPopup = new SelectItemPopup();
                selectItemPopup.startHelloAnimation();
                selectItemPopup.setOnSelectItemCancelled(() -> editItemPopup.startHelloAnimation());
                selectItemPopup.setOnSelectItemConfirmed((newItem) -> {
                    editItemPopup.startByeAnimation();
                    createEditItemPopup(ih, newItem, newItem.getStackSize());
                });
            } else {
                editItemPopup.startByeAnimation();
                createEditItemPopup(ih, ih.getAttachedItem().getItem(), ih.getAttachedItem().getItem().getStackSize());
            }
        });

        itemHotspots.add(ih);
        return ih;
    }

    /**
     * Creates the edit item popup.
     * @param ih Item hotspot it will edit.
     * @param item The item to be default selected.
     * @param stackSize The stack size.
     */
    private void createEditItemPopup(ItemHotspot ih, Item item, int stackSize) {
        EditItemPopup editSubItem = new EditItemPopup(item, stackSize, false, true);
        editSubItem.startHelloAnimation();
        editSubItem.setOnEditItemFailed(() -> editItemPopup.startHelloAnimation());
        editSubItem.setOnEditItemSuccess((newItem) -> {
            ih.unattachItem();
            ih.attachItem(new ItemSprite(newItem));
            editItemPopup.startHelloAnimation();

            if (mostRecentHotspot != null && ih == mostRecentHotspot && !gridView.getChildren().contains(addNewHotspotButton)) {
                gridView.getChildren().add(addNewHotspotButton);
            }
        });
        editSubItem.setOnRequestToDestroy(() -> {
            editItemPopup.startHelloAnimation();
            itemHotspots.remove(ih);
            gridView.getChildren().remove(ih);
            if (mostRecentHotspot != null && ih == mostRecentHotspot && !gridView.getChildren().contains(addNewHotspotButton)) {
                gridView.getChildren().add(addNewHotspotButton);
            }
        });
    }

    /**
     * @return A list of items that are in the edit items inside item component.
     */
    public List<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        for (ItemHotspot ih : itemHotspots) {
            if (ih.getAttachedItem() != null) {
                items.add(ih.getAttachedItem().getItem());
            }
        }

        return items;
    }
}
