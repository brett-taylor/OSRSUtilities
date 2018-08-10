package app.utils;

import app.ui.OSRSUtilitiesWindow;
import app.ui.components.DialogBox;
import javafx.css.CssParser;
import javafx.css.Rule;
import javafx.css.Stylesheet;
import javafx.css.converter.ColorConverter;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Parses a colour found in css into a javafx object
 * https://stackoverflow.com/questions/42609756/how-to-parse-map-javafx-css-file-to-retrieve-its-properties-and-values
 * @author Brett Taylor
 */
public class CSSColorParser {
    /**
     * Parses a colour found in css into a javafx object
     * @param name The name of the colour in css
     * @return The colour if successful if not it will return white.
     */
    public static Color parseColor(String name) {
        CssParser parser = new CssParser();
        try {
            Stylesheet css = parser.parse(CSSColorParser.class.getResource(OSRSUtilitiesWindow.CSS_LOCATION).toURI().toURL());
            final Rule rootRule = css.getRules().get(0); // .root
            return rootRule.getDeclarations().stream()
                    .filter(d -> d.getProperty().equals(name))
                    .findFirst()
                    .map(d -> ColorConverter.getInstance().convert(d.getParsedValue(), null))
                    .get();
        } catch (URISyntaxException | IOException error) {
            DialogBox.showError("Failed parsing css color: " + name + "\n" + error.getMessage());
        }
        return Color.WHITE;
    }
}
