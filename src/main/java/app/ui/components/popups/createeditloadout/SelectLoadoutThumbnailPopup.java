package app.ui.components.popups.createeditloadout;

import app.data.loadouts.Loadout;
import app.data.loadouts.LoadoutManager;
import app.ui.components.buttons.CircularButton;
import app.ui.components.popups.PopupMenu;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Popup where a loadout can be created or edited.
 */
public class CreateEditLoadoutPopup extends PopupMenu {
    /**
     * Layout container.
     */
    private VBox layout;

    /**
     * Textfield containing the name of the loadout.
     */
    private TextField nameLoadoutField;

    /**
     * Loadout it may be representing.
     */
    private Loadout loadout;

    /**
     * The submit button.
     */
    private CircularButton submitButton;

    /**
     * Boolean checking if the button can be submitted or not
     */
    private boolean canBeSubmitted = false;

    /**
     * Creates a popup that allows the user to create or edit a loadout.
     * @param loadout If a loadout is given it will edit the current loadout rather than create a new one.
     */
    public CreateEditLoadoutPopup(Loadout loadout) {
        this.loadout = loadout;
        heading.setText(loadout == null ? "Create Loadout" : "Modify Loadout: " + loadout.getName());

        CircularButton byeButton = CircularButton.failedButton();
        buttonRow.getChildren().add(byeButton);
        byeButton.setOnClicked(this::startByeAnimation);

        submitButton = CircularButton.successButton();
        buttonRow.getChildren().add(submitButton);
        submitButton.setOnClicked(() -> {
            if (canBeSubmitted) {
                startByeAnimation();
                if (loadout == null) {
                    Loadout newLoadout = new Loadout(nameLoadoutField.getText().trim());
                    LoadoutManager.save(newLoadout);
                } else {
                    System.out.println("Add code to modify a loadout.");
                }
            }
        });

        layout = new VBox();
        mainBody.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);
        AnchorPane.setLeftAnchor(layout, 0d);
        layout.setFillWidth(true);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(5, 5, 5,5 ));

        addNameRow();
        checkSubmitStatus();
    }

    /**
     * Checks to see if the dialog can be submitted or not.
     */
    private void checkSubmitStatus() {
        canBeSubmitted = true;

        if (nameLoadoutField.getText().trim().isEmpty()) {
            canBeSubmitted = false;
        }

        if (LoadoutManager.doesLoadoutExist(nameLoadoutField.getText())) {
            canBeSubmitted = false;
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
     * Adds the row dedicated to editing the name of the loadout.
     */
    private void addNameRow() {
        Label label = new Label();
        label.setFont(new Font("Open Sans", 13));
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        label.setText("Name of loadout");
        layout.getChildren().add(label);
        label.setPadding(new Insets(5, 5, 2,5 ));

        nameLoadoutField = new TextField();
        nameLoadoutField.setAlignment(Pos.CENTER);
        nameLoadoutField.getStyleClass().add("nice-textfield");
        nameLoadoutField.setId("dark-searchbox");
        nameLoadoutField.setPrefHeight(30d);
        nameLoadoutField.prefWidthProperty().bind(layout.widthProperty());
        nameLoadoutField.setPromptText("Name");
        nameLoadoutField.setText(loadout == null ? "" : loadout.getName());
        nameLoadoutField.setOnKeyTyped((e) -> checkSubmitStatus());
        layout.getChildren().add(nameLoadoutField);
    }
}
