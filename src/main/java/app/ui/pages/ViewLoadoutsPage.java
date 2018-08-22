package app.ui.pages;

import app.data.loadouts.Loadout;
import app.data.loadouts.LoadoutManager;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SelectLoadoutButton;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Shows all of the loadouts.
 * @author Brett Taylor
 */
public class ViewLoadoutsPage extends BasePage {
    /**
     * The grid view.
     */
    private FlowPane gridView;

    /**
     * Constructor
     */
    public ViewLoadoutsPage() {
        super("/fxml/pages/ViewLoadoutsPage.fxml", true);

        CircularButton addNewLoadout = new CircularButton();
        addNewLoadout.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        addNewLoadout.setBackgroundHoverColor(CSSColorParser.parseColor("-background-color"));
        addNewLoadout.setGlyph(FontAwesomeIcon.PLUS, Color.WHITE);
        addNewLoadout.setSize(25d);
        addNewLoadout.setGlyphSize(27);
        addNewLoadout.setOnClicked(this::addNewLoadout);

        Pane addNewLoadoutHolder = (Pane) lookup("#addNewLoadoutHolder");
        Objects.requireNonNull(addNewLoadoutHolder);
        addNewLoadoutHolder.getChildren().add(addNewLoadout);

        ScrollPane scrollPane = (ScrollPane) lookup("#scrollPane");
        Objects.requireNonNull(scrollPane);

        gridView = (FlowPane) scrollPane.getContent();
        Objects.requireNonNull(gridView);

        loadAndDisplayAllLoadouts();
    }

    @Override
    public void onLoaded() {
    }

    @Override
    public void onRemoved() {
    }

    /**
     * Loads the loadouts and displays them.
     */
    private void loadAndDisplayAllLoadouts() {
        showCenteredLabel("Loading...");

        new Thread(() -> {
            List<Loadout> loadouts = LoadoutManager.getAllLoadoutsInDirectory();
            if (loadouts.isEmpty()) {
                Platform.runLater(() -> {
                    showCenteredLabel("No loadouts to display.");
                });
            } else {
                List<SelectLoadoutButton> loadoutViews = new ArrayList<>();
                for (Loadout loadout : loadouts) {
                    loadoutViews.add(new SelectLoadoutButton(loadout));
                }

                Platform.runLater(() -> {
                    gridView.getChildren().clear();
                    gridView.setAlignment(Pos.TOP_CENTER);
                    gridView.getChildren().addAll(loadoutViews);
                });
            }
        }).start();
    }

    /**
     * Called when the add new loadout button has been clicked.
     */
    private void addNewLoadout() {

    }

    /**
     * Shows a label on the screen in the center.
     * @param text The text to display.
     */
    private void showCenteredLabel(String text) {
        gridView.setAlignment(Pos.CENTER);
        gridView.getChildren().clear();
        Label label = new Label();
        label.setText(text);
        label.setFont(new Font("Open Sans", 16));
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        gridView.getChildren().add(label);
    }
}
