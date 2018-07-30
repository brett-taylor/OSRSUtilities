package app.ui.pages;

import app.OSRSUtilities;
import app.data.DatabaseManager;
import com.jfoenix.controls.JFXSpinner;
import io.datafx.controller.FXMLController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;

@FXMLController("/fxml/pages/PopulateDatabase.fxml")
public class PopulateDatabaseController {
    /**
     * The spinner
     */
    @FXML
    private JFXSpinner spinner;

    /**
     * Used to show the macro state of populating the data. E.g. grabbing helmets.
     */
    @FXML
    private Label heading;

    /**
     * Used to show the micro state of populating the data. E.g grabbing slayer helmet.
     */
    @FXML
    private Label subheading;

    @PostConstruct
    public void init() {
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                heading.setText(DatabaseManager.getCurrentMacroStep());
                subheading.setText(DatabaseManager.getCurrentMicroStep());
                if (DatabaseManager.getCurrentMacroStep().equals("done")) {
                    OSRSUtilities.getWindow().showPage(CharacterSelectionController.class);
                    stop();
                }
            }
        }.start();
    }
}
