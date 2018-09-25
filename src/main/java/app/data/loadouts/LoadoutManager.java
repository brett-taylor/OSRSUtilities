package app.data.loadouts;

import app.data.DataManager;
import app.data.runescape.Item;
import app.ui.components.popups.DialogBox;
import com.google.gson.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving loadouts from and to json files on the disk.
 * @author Brett Taylor
 */
public class LoadoutManager {
    /**
     * Location of the data files. Should end with a slash.
     */
    private final static String LOADOUTS_LOCATION = DataManager.DATA_LOCATION + "Loadouts/";

    /**
     * Creates the directory if it currently does not exist where the loadouts will be stored.
     */
    private static void createDirectoryIfNeeded() {
        File directory = new File(LOADOUTS_LOCATION);
        if (!directory.exists() || !directory.isDirectory()) {
            new File(LOADOUTS_LOCATION).mkdirs();
        }
    }

    /**
     * Gets all the loadouts in the loadout directory.
     * @return A list containing all of the loadouts.
     */
    public static List<Loadout> getAllLoadoutsInDirectory() {
        createDirectoryIfNeeded();
        FilenameFilter textFilter = (dir, name) -> name.toLowerCase().endsWith(".json");
        File[] files = new File(LOADOUTS_LOCATION).listFiles(textFilter);

        ArrayList<Loadout> loadouts = new ArrayList<>();
        for (File f : files) {
            Loadout loadout = load(f.getName());
            if (loadout != null) {
                loadouts.add(loadout);
            }
        }

        return loadouts;
    }

    /**
     * Checks to see if a loadout already exists.
     * @param name The name of the loadout to check.
     * @return True if a loadout with that name already exists.
     */
    public static boolean doesLoadoutExist(String name) {
        File file = new File(LOADOUTS_LOCATION + name + ".json");
        return file.exists();
    }

    /**
     * Loads a loadout from a file.
     * @param fileName The name of the file to load it from.
     * @return The loadout.
     */
    public static Loadout load(String fileName) {
        if (!fileName.endsWith(".json")) {
            fileName = fileName + ".json";
        }

        File file = new File(LOADOUTS_LOCATION + fileName);
        if (!file.exists())
            return  null;

        String jsonString = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
            jsonString = new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            DialogBox.showError("Failed to load loadout. Corrupted json. \n" + e.getMessage());
            return null;
        }

        if (jsonString.isEmpty()) {
            DialogBox.showError("Failed to load loadout. Corrupted json.");
            return null;
        }

        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        String name = fileName.replace(".json", "");
        Loadout loadout = new Loadout(name);
        loadout.getInventory().placeItems(loadItemsFromJsonArray(json.getAsJsonArray("inventory")));
        loadout.getEquipment().placeItems(loadItemsFromJsonArray(json.getAsJsonArray("equipment")));

        JsonElement thumbnailType = json.get("thumbnailtype");
        if (thumbnailType != null) {
            try {
                LoadoutThumbnailType type =  LoadoutThumbnailType.valueOf(thumbnailType.getAsString());
                loadout.setThumbnailType(type);
                loadout.setThumbnailName(json.get("thumbnailname").getAsString());
            } catch (IllegalArgumentException e) {
                DialogBox.showError("Failed to load thumbnail information for loadout: " + loadout.getName() +" \n" + e.getMessage());
            }
        }

        return loadout;
    }

    /**
     * Loads a an array of items from a json array.
     * @param itemContainer The json array.
     * @return The array of items.
     */
    private static Item[] loadItemsFromJsonArray(JsonArray itemContainer) {
        JsonArray contents = itemContainer.get(0).getAsJsonArray();

        Item[] items = new Item[contents.size()];
        int positionInArray = 0;
        for (JsonElement element : contents) {
            JsonObject object = element.getAsJsonObject();
            items[positionInArray] = object.size() == 0 ? null : loadItemFromJsonObject(object);
            positionInArray++;
        }

        return items;
    }

    /**
     * Loads a item from a json object.
     * @param jsonObject the json obhject.
     * @return The associated item loaded.
     */
    private static Item loadItemFromJsonObject(JsonObject jsonObject) {
        String itemName = jsonObject.get("name").getAsString();
        int stackSize = jsonObject.get("stack").getAsInt();
        Item i = Item.load(itemName);
        i.setStackSize(stackSize);

        JsonElement itemsWithin  = jsonObject.get("itemsInside");
        if (itemsWithin != null) {
            List<Item> itemsInside = new ArrayList<>();
            for (JsonElement arrayElement : itemsWithin.getAsJsonArray()) {
                JsonObject possibleItem = arrayElement.getAsJsonObject();
                itemsInside.add(loadItemFromJsonObject(possibleItem));
            }
            i.getItemsInside().addAll(itemsInside);
        }
        return i;
    }

    /**
     * Saves a loadout.
     * @param loadout The loadout to save.
     */
    public static void save(Loadout loadout) {
        JsonObject json = new JsonObject();

        // Misc
        json.addProperty("thumbnailtype", loadout.getThumbnailType().toString());
        json.addProperty("thumbnailname", loadout.getThumbnailName());

        // Equipment
        JsonArray equipmentItems = new JsonArray();
        equipmentItems.add(generateItemContainerJSON(loadout.getEquipment().getItems()));
        json.add("equipment", equipmentItems);
        JsonArray inventoryItems = new JsonArray();
        inventoryItems.add(generateItemContainerJSON(loadout.getInventory().getItems()));
        json.add("inventory", inventoryItems);

        try (PrintWriter out = new PrintWriter(LOADOUTS_LOCATION + loadout.getName() + ".json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJsonString = gson.toJson(json);
            out.println(prettyJsonString);
        } catch (FileNotFoundException e) {
            DialogBox.showError("Failed to save Loadout. \n" + e.getMessage());
        }
    }

    /**
     * Generates a item array to a json array.
     * @param items The item array.
     * @returns the json object.
     */
    private static JsonArray generateItemContainerJSON(Item[] items) {
        JsonArray itemsArray = new JsonArray();
        for (Item item : items) {
            if (item != null) {
                itemsArray.add(generateItemJSON(item));
            } else {
                itemsArray.add(new JsonObject());
            }
        }

        return itemsArray;
    }

    /**
     * Generates the json object for a item
     * @param item The item the json object will be generated for.
     * @return The item encoded as a json object.
     */
    private static JsonObject generateItemJSON(Item item) {
        JsonObject itemObject = new JsonObject();
        itemObject.addProperty("name", item.getName());
        itemObject.addProperty("stack", item.getStackSize());

        if (!item.getItemsInside().isEmpty()) {
            JsonArray itemsInside = new JsonArray();
            for (Item itemInside :item.getItemsInside()) {
                itemsInside.add(generateItemJSON(itemInside));
            }

            itemObject.add("itemsInside", itemsInside);
        }

        return itemObject;
    }
}
