package app.ui.pages;

import app.data.wiki.WikiBlacklist;
import app.ui.components.BlackListView;
import app.ui.components.popups.DialogBox;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.utils.CSSColorParser;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The blacklist can edited by a user here.
 * @author Brett Taylor
 */
public class BlacklistPage extends BasePage {
    /**
     * The searchItem result area.
     */
    private VBox resultArea;

    /**
     * The thread that the searchItem will work on.
     */
    private Thread searchThread;

    /**
     * Constructor
     */
    public BlacklistPage() {
        super("/fxml/pages/BlacklistPage.fxml", true);

        CircularButton addNewURL = new CircularButton();
        addNewURL.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        addNewURL.setBackgroundHoverColor(CSSColorParser.parseColor("-background-color"));
        addNewURL.setGlyph(FontAwesomeIcon.PLUS, Color.WHITE);
        addNewURL.setSize(25d);
        addNewURL.setGlyphSize(27);
        addNewURL.setOnClicked(this::addNewUrlClicked);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a URL");
        searchBar.getStyleClass().add("searchbox");
        searchBar.setPrefWidth(400d);
        searchBar.setPrefHeight(40d);

        SquareButton searchButton = SquareButton.defaultButton();
        searchButton.setSize(40d, 40d);
        searchButton.setText("");
        searchButton.setGlyph(FontAwesomeIcon.SEARCH, Color.WHITE);
        searchButton.setGlyphSize(18);
        searchButton.setBackgroundColor(CSSColorParser.parseColor("-good-color"));
        searchButton.setBackgroundHoverColor(CSSColorParser.parseColor("-good-secondary-color"));
        searchButton.setOnClicked(() -> searchAndShow(searchBar.getText()));

        Pane addButtonHolder = (Pane) lookup("#addButtonHolder");
        Objects.requireNonNull(addButtonHolder);
        addButtonHolder.getChildren().add(addNewURL);

        HBox searchBoxArea = (HBox) lookup("#searchBoxArea");
        Objects.requireNonNull(searchBoxArea);
        searchBoxArea.getChildren().add(searchBar);
        searchBoxArea.getChildren().add(searchButton);

        ScrollPane scrollPane = (ScrollPane) lookup("#resultArea");
        Objects.requireNonNull(scrollPane);
        resultArea = (VBox) scrollPane.getContent();

        searchAndShow("");
    }

    @Override
    public void onLoaded() {
        getParentElement().requestFocus();
    }

    @Override
    public void onRemoved() {
    }

    /**
     * Called when the button associated with adding a new url is called.
     */
    private void addNewUrlClicked() {
        DialogBox.showDialogBox("Blacklist a new URL?");

        Label helpLabel = new Label();
        helpLabel.setText("Write in the url in the box below.");
        helpLabel.setFont(new Font("Open Sans", 12));
        helpLabel.setTextFill(Color.WHITE);
        helpLabel.setAlignment(Pos.CENTER_LEFT);

        TextField textField = new TextField();
        textField.getStyleClass().add("nice-textfield");
        DialogBox.showMenu(helpLabel, textField);

        CircularButton addButton = CircularButton.successButton();
        CircularButton nothingButton = CircularButton.failedButton();
        nothingButton.setOnClicked(DialogBox::close);
        DialogBox.addToButtonRow(nothingButton);
        DialogBox.addToButtonRow(addButton);

        addButton.setOnClicked(() -> {
            String textContents = textField.getText();
            if (!textContents.isEmpty()) {
                DialogBox.clearBody();
                DialogBox.showingLoadingSpinner();
                new Thread(() -> {
                    WikiBlacklist.add(textContents);
                    Platform.runLater(() -> {
                        resultArea.getChildren().add(new BlackListView(textContents));
                        DialogBox.close();
                    });
                }).start();
            }
        });
    }

    /**
     * Called when the searchItem button is clicked.
     * @param phrase The phrase that should be in the url.
     */
    private void searchAndShow(String phrase) {
        if (searchThread != null) {
            searchThread.interrupt();
            searchThread = null;
        }

        searchThread = new Thread(() -> {
            Platform.runLater(() -> {
                resultArea.getChildren().clear();
                resultArea.setAlignment(Pos.TOP_LEFT);
                showLoadingSpinner();
            });

            List<String> urls = WikiBlacklist.getAllURLSBlacklisted(phrase);
            if (urls.isEmpty()) {
                Platform.runLater(this::showNoURLFound);
            } else {
                List<BlackListView> urlView = new ArrayList<>();
                for (String url : urls) {
                    urlView.add(new BlackListView(url));
                }

                Platform.runLater(() ->  {
                    resultArea.getChildren().clear();
                    resultArea.setAlignment(Pos.TOP_LEFT);
                    resultArea.getChildren().addAll(urlView);
                });
            }
        });
        searchThread.start();
    }

    /**
     * Shows a loading spinner in the center of the screen.
     */
    private void showLoadingSpinner() {
        resultArea.getChildren().clear();
        resultArea.setAlignment(Pos.CENTER);
        JFXSpinner spinner = new JFXSpinner();
        resultArea.getChildren().add(spinner);
    }

    /**
     * Shows a label in the center of the screen saying that no urls can be found.
     */
    private void showNoURLFound() {
        resultArea.getChildren().clear();
        resultArea.setAlignment(Pos.CENTER);
        Label label = new Label();
        label.setText("No blacklisted urls found");
        label.setFont(new Font("Open Sans", 24));
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        resultArea.getChildren().add(label);
    }
}
