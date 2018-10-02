package app.ui.components.popups.searchitem;

import app.data.runescape.Item;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.ui.components.popups.PopupMenu;
import app.utils.CSSColorParser;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 * Popup that allows the user to select a item.
 * @author Brett Taylor
 */
public class SelectItemPopup extends PopupMenu {
    /**
     * The maximum amount of items to show.
     */
    private static int TOTAL_AMOUNT_OF_ITEMS_TO_SHOW = 50;

    /**
     * Layout container.
     */
    private VBox layout;

    /**
     * The button that will choose the currently selected item.
     */
    private CircularButton submitButton;

    /**
     * Boolean checking if the button can be submitted or not
     */
    private boolean submitStatus = false;

    /**
     * Grid view.
     */
    private FlowPane grid;

    /**
     * The listener to the OnSelectItemConfirmed event.
     */
    private OnSelectItemConfirmed onSelectItemConfirmed;

    /**
     * The listener to the OnSelectItemCancelled event.
     */
    private OnSelectItemCancelled onSelectItemCancelled;

    /**
     * The thread that searching for items will execute on.
     */
    private Thread searchThread;

    /**
     * The ItemSearchResult that has currently been selected.
     */
    private ItemSearchResult currentItemSearchResultSelected;

    /**
    * Creates a popup that allows the user to choose a item.
    */
    public SelectItemPopup() {
        heading.setText("Select a item");
        setSize(700, 500);

        CircularButton byeButton = CircularButton.failedButton();
        buttonRow.getChildren().add(byeButton);
        Runnable onCancelled = () -> {
            startByeAnimation();
            if (onSelectItemCancelled != null) {
                onSelectItemCancelled.onCancelled();
            }
        };
        byeButton.setOnClicked(onCancelled);
        setOnShortcutFailed(onCancelled);

        submitButton = CircularButton.successButton();
        buttonRow.getChildren().add(submitButton);
        Runnable onSuccess = () -> {
            if (submitStatus) {
                startByeAnimation();
                if (onSelectItemConfirmed != null) {
                    onSelectItemConfirmed.onSuccess(currentItemSearchResultSelected.getItem());
                }
            }
        };
        submitButton.setOnClicked(onSuccess);

        enableSubmitButton(false);

        layout = new VBox();
        mainBody.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);
        AnchorPane.setLeftAnchor(layout, 0d);
        layout.setFillWidth(true);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(5, 5, 5,5 ));

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a Item");
        searchBar.getStyleClass().add("searchbox");
        searchBar.setId("dark-searchbox");
        searchBar.setPrefWidth(Integer.MAX_VALUE);
        searchBar.setPrefHeight(40d);

        SquareButton searchButton = SquareButton.defaultButton();
        searchButton.setSize(40d, 40d);
        searchButton.setText("");
        searchButton.setGlyph(FontAwesomeIcon.SEARCH, Color.WHITE);
        searchButton.setGlyphSize(18);
        searchButton.setBackgroundColor(CSSColorParser.parseColor("-good-color"));
        searchButton.setBackgroundHoverColor(CSSColorParser.parseColor("-good-secondary-color"));
        Runnable onSearchButtonClicked = () -> search(searchBar.getText().trim());
        searchButton.setOnClicked(onSearchButtonClicked);

        HBox searchBoxContainer = new HBox();
        Objects.requireNonNull(searchBoxContainer);
        layout.getChildren().add(searchBoxContainer);
        searchBoxContainer.getChildren().add(searchBar);
        searchBoxContainer.getChildren().add(searchButton);

        ScrollPane scrollPane = new ScrollPane();
        layout.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getStyleClass().add("edge-to-edge");
        scrollPane.getStyleClass().add("invis-scrollpane");

        grid = new FlowPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5d);
        grid.setVgap(5d);
        grid.setPadding(new Insets(5, 0, 0,0));

        showCeneterLabel("Search for a item");

        setOnShortcutSuccess(() -> {
            if (searchBar.isFocused()) {
                onSearchButtonClicked.run();
                layout.requestFocus();
            } else {
                onSuccess.run();
            }
        });
    }

    /**
     * Sets the listener to the OnSelectItemConfirmed event.
     * @param onSelectItemConfirmed the listener.
     */
    public void setOnSelectItemConfirmed(OnSelectItemConfirmed onSelectItemConfirmed) {
        this.onSelectItemConfirmed = onSelectItemConfirmed;
    }

    /**
     * Sets the listener to the OnSelectItemCancelled event.
     * @param onSelectItemCancelled the listener.
     */
    public void setOnSelectItemCancelled(OnSelectItemCancelled onSelectItemCancelled) {
        this.onSelectItemCancelled = onSelectItemCancelled;
    }

    /**
     * Enables or disables the submit button.
     * @param submitStatus True if the submit button should be enabled.
     */
    private void enableSubmitButton(boolean submitStatus) {
        this.submitStatus = submitStatus;
        if (this.submitStatus) {
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
     * Shows a center label.
     * @param message the message to show
     */
    private void showCeneterLabel(String message) {
        resetSelectedItemSearchResult();
        grid.getChildren().clear();
        grid.setAlignment(Pos.CENTER);

        Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Open Sans", 13));
        label.setWrapText(true);
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        grid.getChildren().add(label);
    }

    /**
     * Shows a loading spinner.
     */
    private void showLoadingSpinner() {
        resetSelectedItemSearchResult();
        grid.getChildren().clear();
        grid.setAlignment(Pos.CENTER);

        JFXSpinner spinner = new JFXSpinner();
        grid.getChildren().add(spinner);
    }

    /**
     * Called when the text changes on the searchitem box.
     * @param phrase The phrase to searchitem for.
     */
    private void search(String phrase) {
        if (searchThread != null) {
            searchThread.interrupt();
            searchThread = null;
        }

        resetSelectedItemSearchResult();
        if (phrase.isEmpty()) {
            showCeneterLabel("Search for a item");
            return;
        } else {
            showLoadingSpinner();
        }

        searchThread = new Thread(() -> {
            List<Item> items = Item.loadAllItemsThatContainInName(phrase);
            if (items == null) {
                Platform.runLater(() -> showCeneterLabel("A error occurred."));
                return;
            }

            int currentAmountOfItems = 0;
            List<Node> searchResults = new ArrayList<>();
            boolean firstItem = true;
            for (Item item : items) {

                ItemSearchResult itemSearchResult = new ItemSearchResult(item);
                itemSearchResult.setOnItemSearchResultClicked(this::onItemSearchResultClicked);
                searchResults.add(itemSearchResult);

                currentAmountOfItems++;
                if (currentAmountOfItems > TOTAL_AMOUNT_OF_ITEMS_TO_SHOW) {
                    Label label = new Label();
                    label.setText("Showing maximum amount of items - please research with a more specific term.");
                    label.setAlignment(Pos.CENTER);
                    label.setFont(new Font("Open Sans", 13));
                    label.setWrapText(true);
                    label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
                    searchResults.add(label);
                    break;
                }

                if (firstItem) {
                    Platform.runLater(() -> onItemSearchResultClicked(itemSearchResult, itemSearchResult.getItem()));
                    firstItem = false;
                }
            }

            Platform.runLater(() -> {
                grid.getChildren().clear();
                grid.setAlignment(Pos.CENTER);
                grid.getChildren().addAll(searchResults);
            });
        });
        searchThread.start();
    }

    /**
     * Called when a item searchitem result when it was clicked.
     * @param self The item searchitem result that was clicked.
     * @param item The item the item searchitem was representing.
     */
    private void onItemSearchResultClicked(ItemSearchResult self, Item item) {
        if (currentItemSearchResultSelected == self) {
            resetSelectedItemSearchResult();
            return;
        }

        if (currentItemSearchResultSelected != null) {
            currentItemSearchResultSelected.setNormalBorder();
        }
        self.setSelectedBorder();
        currentItemSearchResultSelected = self;
        enableSubmitButton(true);
    }

    /**
     * Resets the currently selected item searchitem result.
     */
    private void resetSelectedItemSearchResult() {
        if (currentItemSearchResultSelected != null) {
            currentItemSearchResultSelected.setNormalBorder();
        }

        currentItemSearchResultSelected = null;
        enableSubmitButton(false);
    }
}