package app.wiki;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Used to get data from the different categories on the app.wiki.
 * @author Brett Taylor
 */
public class WikiList {
    /**
     * The app.wiki api call the list will use.
     */
    private WikiAPICall call = null;

    /**
     * The results of the api call. Returns the name and then url for the object.
     * Form of: <name, <id, value> >
     */
    private HashMap<String, HashMap<String, Object>> objectsReturned = new HashMap<>();

    /**
     * Creates a app.wiki list.
     * @param category The category to get the list of.
     * @param amount The amount.
     */
    private WikiList(WikiCategories category, int amount) {
        String api = "List?category=" + category.getWikiCategoryName() + "&limit=" + amount + "";
        call = new WikiAPICall(api);

        if (!call.isSuccessful())
            return;

        JsonArray array = call.getResult().getAsJsonObject().getAsJsonArray("items");
        for (JsonElement element : array) {
            JsonObject json = element.getAsJsonObject();
            HashMap<String, Object> value = new HashMap<>();
            value.put("title", json.get("title").getAsString());
            value.put("url", json.get("url").getAsString());
            value.put("id", json.get("id").getAsInt());
            objectsReturned.put(json.get("title").getAsString(), value);
        }
    }

    /**
     * Get all objects in that category on the app.wiki.
     * @param category the category
     * @return the list of that category.
     */
    public static WikiList all(WikiCategories category) {
        return new WikiList(category, 99999999);
    }

    /**
     * Get all objects in that category on the app.wiki.
     * @param category the category
     * @param amount the amount to grab.
     * @return the list of that category.
     */
    public static WikiList get(WikiCategories category, int amount) {
        return new WikiList(category, amount);
    }

    /**
     * @return Returns the objects from the category.
     */
    public HashMap<String, HashMap<String, Object>> getObjects() {
        return objectsReturned;
    }
}