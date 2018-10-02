package app.data.wiki;

import app.data.categories.DownloadCategories;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Used to get data from the different categories on the app.data.wiki.
 * @author Brett Taylor
 */
public class WikiList {
    /**
     * The app.data.wiki api call the list will use.
     */
    private WikiAPICall call;

    /**
     * The results of the api call. Returns the name and then url for the object.
     * Form of: <name, <id, value> >
     */
    private HashMap<String, HashMap<WikiListValues, Object>> objectsReturned = new HashMap<>();

    /**
     * Creates a app.data.wiki list.
     * @param category The category to get the list of.
     * @param amount The amount.
     */
    private WikiList(DownloadCategories category, int amount) {
        String api = "format=json&action=query&list=categorymembers&cmtitle=Category:" + category.getWikiCategoryName() + "&cmlimit=" + amount + "";
        call = new WikiAPICall(api);

        if (!call.isSuccessful())
            return;

        JsonArray array = call.getResult().getAsJsonObject().getAsJsonArray("query").getAsJsonObject().getAsJsonArray("categorymembers");
        for (JsonElement element : array) {
            JsonObject json = element.getAsJsonObject();
            HashMap<WikiListValues, Object> value = new HashMap<>();
            value.put(WikiListValues.TITLE, json.get(WikiListValues.TITLE.toString()).getAsString());
            value.put(WikiListValues.URL, json.get(WikiListValues.URL.toString()).getAsString());
            value.put(WikiListValues.ID, json.get(WikiListValues.ID.toString()).getAsInt());
            objectsReturned.put(json.get(WikiListValues.TITLE.toString()).getAsString(), value);
        }
    }

    /**
     * Get all objects in that category on the app.data.wiki.
     * @param category the category
     * @return the list of that category.
     */
    public static WikiList all(DownloadCategories category) {
        return new WikiList(category, 99999999);
    }

    /**
     * Get all objects in that category on the app.data.wiki.
     * @param category the category
     * @param amount the amount to grab.
     * @return the list of that category.
     */
    public static WikiList get(DownloadCategories category, int amount) {
        return new WikiList(category, amount);
    }

    /**
     * @return Returns the objects from the category.
     */
    public HashMap<String, HashMap<WikiListValues, Object>> getObjects() {
        return objectsReturned;
    }
}