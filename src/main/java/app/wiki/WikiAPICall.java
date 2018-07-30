package app.wiki;

import app.OSRSUtilities;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Will be used to
 * @author Brett Taylor
 */
public class WikiAPICall {
    /**
     * Holds the osrs app.wiki address.
     */
    private final static String WIKI_API_ADDRESS = OSRSUtilities.WIKI_ADDRESS + "api/v1/Articles/";

    /**
     * Holds any error messages that were created from attempting to call the api.
     */
    private String errorMessage = "";

    /**
     * Whether it got to fully execute the whole api call or not.
     */
    private boolean isSuccessful = false;

    /**
     * The result of the api call.
     */
    private JsonElement result = null;

    /**
     * Constructs and calls an api to the OSRS app.wiki.
     * @param api the ending of the api call. The address of the app.wiki and /api/v1/Articles/ is inserted automatically.
     */
    public WikiAPICall(String api) {
        URL url;
        try {
            url = new URL(WIKI_API_ADDRESS + api);
        } catch (MalformedURLException e) {
            errorMessage = "MalformedURLException: " + e.getMessage();
            return;
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            errorMessage = "IOException: " + e.getMessage();
            return;
        }

        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            errorMessage = "ProtocolException: " + e.getMessage();
            return;
        }

        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            result = new JsonParser().parse(rd.readLine());
            rd.close();
        } catch (IOException e) {
            errorMessage = "IOException: " + e.getMessage();
            return;
        }

        isSuccessful = true;
    }

    /**
     * @return True if the api call was successful. This does not mean the api returned a correct result just it
     * connected and executed fine.
     */
    public boolean isSuccessful() {
        return isSuccessful;
    }

    /**
     * @return If the api call was successful returns the result otherwise null. Check if the api call
     * was successful before using this.
     */
    public JsonElement getResult() {
        return isSuccessful() ? result : null;
    }

    /**
     * @return If the api call was not successful - returns the error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}