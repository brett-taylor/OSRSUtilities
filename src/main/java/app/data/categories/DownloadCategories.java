package app.data.categories;

import app.data.DataManager;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.OnCategoryDownloadUpdateEvent;
import app.data.tables.CategoryTable;
import app.data.wiki.WikiBlacklist;
import app.ui.components.popups.DialogBox;
import app.data.wiki.WikiList;
import app.data.wiki.WikiListValues;

import java.sql.SQLException;
import java.util.*;

/**
 * All the different categories on the app.data.wiki the api allows us to access.
 * @author Brett Taylor
 */
public enum DownloadCategories {
    BOSSES("Bosses", "Bosses", "Bosses", DownloadCategoriesType.MONSTER),
    SLAYER_MONSTERS("Slayer_monsters", "Slayer Beasts", "Slayer_monsters", DownloadCategoriesType.MONSTER),
    MAGIC_ARMOUR("Magic_armour", "Magic Armour", "Magic_armour", DownloadCategoriesType.ITEM),
    MELEE_ARMOUR("Melee_armour", "Melee Armour", "Melee_armour", DownloadCategoriesType.ITEM),
    RANGE_ARMOUR("Ranged_armour", "Range Armour", "Ranged_armour", DownloadCategoriesType.ITEM),
    QUEST_ITEMS("Quest_items", "Quest Items", "Quest_items", DownloadCategoriesType.ITEM),
    POTIONS("Potions", "Potions", "Potions", DownloadCategoriesType.ITEM),
    FOOD("Food", "Food", "Food", DownloadCategoriesType.ITEM),
    CURRENCY("Currency", "Currency", "Currency", DownloadCategoriesType.ITEM),
    HERBS("Herbs", "Herbs", "Herbs", DownloadCategoriesType.ITEM),
    TOOLS("Tools", "Tools", "Tools", DownloadCategoriesType.ITEM),
    ORES("Ores", "Ores", "Ores", DownloadCategoriesType.ITEM),
    RUNES("Runes", "Runes", "Runes", DownloadCategoriesType.ITEM),
    AMMO("Ammunition", "Ammo", "Ammunition", DownloadCategoriesType.ITEM),
    STORAGE_ITEMS("Storage_items", "Storage Items", "Storage_items", DownloadCategoriesType.ITEM),
    SKILL_CAPES("Capes_of_Accomplishment", "Skillcapes", "Capes_of_Accomplishment", DownloadCategoriesType.ITEM),

    ALL_HELMETS("Head_slot_items", "Helmets", "Head_slot_items", DownloadCategoriesType.ITEM),
    ALL_CAPES("Cape_slot_items", "Capes", "Cape_slot_items", DownloadCategoriesType.ITEM),
    ALL_NECKLACES("Neck_slot_items", "Necklaces", "Neck_slot_items", DownloadCategoriesType.ITEM),
    ALL_AMMO("Ammunition_slot_items", "Quiver Items", "Ammunition_slot_items", DownloadCategoriesType.ITEM),
    ALL_WEAPONS("Weapon_slot_items", "1H Weapons", "Weapon_slot_items", DownloadCategoriesType.ITEM),
    ALL_TWO_HANDED_WEAPONS("Two-handed_slot_items", "2H Weapons", "Two-handed_slot_items", DownloadCategoriesType.ITEM),
    ALL_BODY("Body_slot_items", "Body Items", "Body_slot_items", DownloadCategoriesType.ITEM),
    ALL_SHIELD("Shield_slot_items", "Shield Items", "Shield_slot_items", DownloadCategoriesType.ITEM),
    ALL_LEGS("Leg_slot_items", "Leg Items", "Leg_slot_items", DownloadCategoriesType.ITEM),
    ALL_GLOVES("Hand_slot_items", "Gloves", "Hand_slot_items", DownloadCategoriesType.ITEM),
    ALL_BOOTS("Feet_slot_items", "Boots", "Feet_slot_items", DownloadCategoriesType.ITEM),
    ALL_RINGS("Ring_slot_items", "Rings", "Ring_slot_items", DownloadCategoriesType.ITEM),

    ALL_STACKABLE_ITEMS("Stackable_items", "Stackable Items", "Stackable_items", DownloadCategoriesType.ITEM),
    ALL_ITEMS("Items", "All Items", "Items", DownloadCategoriesType.ITEM),
    ALL_MONSTERS("Bestiary", "All Beasts", "Bestiary", DownloadCategoriesType.MONSTER);

    /**
     * The category the enum represents on the app.data.wiki.
     */
    private String wikiCategoryName;

    /**
     * The name of the category.
     */
    private String niceName;

    /**
     * The image name of the category in the default images folder.
     */
    private String imageName;


    /**
     * The type the category is.
     */
    private DownloadCategoriesType type;

    /**
     * The download status.
     */
    private DownloadCategoriesStatus downloadStatus;

    /**
     * The listeners that are listening to this category about changes.
     */
    private List<OnCategoryDownloadUpdateEvent> onCategoryDownloadUpdateEventListeners = new ArrayList<>();

    /**
     * Constructor.
     * @param wikiCategoryName The category the enum represents on the app.data.wiki.
     * @param niceName The name of the category.
     * @param imageName The image name of the category in the default images folder.
     * @param type The type the category is.
     */
    DownloadCategories(String wikiCategoryName, String niceName, String imageName, DownloadCategoriesType type) {
        this.wikiCategoryName = wikiCategoryName;
        this.niceName = niceName;
        this.imageName = imageName;
        this.type = type;
    }

    /**
     * @return The category the enum represents on the app.data.wiki.
     */
    public String getWikiCategoryName() {
        return wikiCategoryName;
    }

    /**
     * @return The nice name
     */
    public String getNiceName() {
        return niceName;
    }

    /**
     * @return The image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @return the current download state.
     */
    public DownloadCategoriesStatus getDownloadStatus() {
        return downloadStatus;
    }

    /**
     * @return the current download state.
     */
    public void resendStatusUpdateEvent() {
        if (downloadStatus == null) {
            setDownloadStatus(getDownloadStatusFromDatabase());
        } else {
            setDownloadStatus(downloadStatus);
        }
    }

    /**
     * Sets the download status
     * @param newDownloadStatus The new status.
     */
    public void setDownloadStatus(DownloadCategoriesStatus newDownloadStatus) {
        DownloadCategoriesStatus oldStatus = downloadStatus;
        downloadStatus = newDownloadStatus;

        for (OnCategoryDownloadUpdateEvent listener : onCategoryDownloadUpdateEventListeners)
            listener.statusUpdate(this, oldStatus, newDownloadStatus);
    }

    /**
     * @return the current download state.
     */
    private DownloadCategoriesStatus getDownloadStatusFromDatabase() {
        SQLStatement sqlStatement = new SQLStatement("SELECT downloadColumn FROM tableName WHERE nameColumn = wikiCategory;");
        sqlStatement.bindParam("tableName", CategoryTable.TABLE_NAME);
        sqlStatement.bindParam("downloadColumn", CategoryTable.DOWNLOADED_COLUMN);
        sqlStatement.bindParam("nameColumn", CategoryTable.NAME_COLUMN_PK);
        sqlStatement.bindStringParam("wikiCategory", this.toString());

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        try {
            if (result.getResultSet().next()) {
                if (result.getResultSet().getInt(CategoryTable.DOWNLOADED_COLUMN) == 1) {
                    return DownloadCategoriesStatus.DOWNLOADED;
                }
            } else {
                createCategoryRowInDatabase();
            }
        } catch (SQLException e) {
            DialogBox.showError("Failed to get download status for " + getNiceName() + ". \n" + e.getMessage());
        }

        return DownloadCategoriesStatus.NOT_DOWNLOADED;
    }

    /**
     * Creates the category row in the database.
     */
    private void createCategoryRowInDatabase() {
        SQLStatement sqlStatement = new SQLStatement("INSERT INTO tableName VALUES(name, downloadStatus);");
        sqlStatement.bindParam("tableName", CategoryTable.TABLE_NAME);
        sqlStatement.bindParam("downloadStatus", "0");
        sqlStatement.bindStringParam("name", this.toString());

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to get create download status for " + getNiceName() + ". \n" + result.getErrorMessage());
        }
    }

    /**
     * Saves the new download status
     */
    public void saveDownloadStatus() {
        if (downloadStatus == null) {
            return;
        }

        int newDownloadStatus = 0;
        if (downloadStatus == DownloadCategoriesStatus.DOWNLOADED)
            newDownloadStatus = 1;

        SQLStatement sqlStatement = new SQLStatement("UPDATE tableName SET downloadColumn = downloadStatus WHERE nameColumn = categoryName;");
        sqlStatement.bindParam("tableName", CategoryTable.TABLE_NAME);
        sqlStatement.bindParam("downloadColumn", CategoryTable.DOWNLOADED_COLUMN);
        sqlStatement.bindParam("nameColumn", CategoryTable.NAME_COLUMN_PK);
        sqlStatement.bindParam("downloadStatus", "" + newDownloadStatus);
        sqlStatement.bindStringParam("categoryName", this.toString());

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to get update download status for " + getNiceName() + ". \n" + result.getErrorMessage());
        }
    }

    /**
     * Starts listening to the OnCategoryDownloadUpdate event.
     * @param event The object to start listening.
     */
    public void listenToOnCategoryDownloadUpdateEvent(OnCategoryDownloadUpdateEvent event) {
        onCategoryDownloadUpdateEventListeners.add(event);
    }

    /**
     * Stops listening to the OnCategoryDownloadUpdate event.
     * @param event The object to start listening.
     */
    public void stopListeningToOnCategoryDownloadUpdateEvent(OnCategoryDownloadUpdateEvent event) {
        onCategoryDownloadUpdateEventListeners.remove(event);
    }

    /**
     * Clears all listeners to the OnCategoryDownloadUpdate event.
     */
    public void clearAllListenersToOnCategoryDownloadUpdateEvent() {
        onCategoryDownloadUpdateEventListeners.clear();
    }

    /**
     * Downloads the category.
     * Should be called on a separate thread.
     */
    public void download() {
        setDownloadStatus(DownloadCategoriesStatus.DOWNLOADING);
        for (OnCategoryDownloadUpdateEvent listener : onCategoryDownloadUpdateEventListeners) {
            listener.requestToStartDownload(this);
        }

        // Get list and remove all links in blacklist.
        WikiList list = WikiList.all(this);
        Iterator<HashMap<WikiListValues, Object>> it = list.getObjects().values().iterator();
        while (it.hasNext()) {
            HashMap<WikiListValues, Object> urlObject = it.next();
            String url = (String) urlObject.get(WikiListValues.URL);
            if (WikiBlacklist.isURLBlacklisted(url)) {
                it.remove();
            }
        }

        for (OnCategoryDownloadUpdateEvent listener : onCategoryDownloadUpdateEventListeners) {
            listener.receivedWikiAPI(this);
        }

        int total = list.getObjects().size();
        int current = 0;
        for (HashMap<WikiListValues, Object> pair : list.getObjects().values()) {
            current++;
            type.save((String) pair.get(WikiListValues.TITLE), (String) pair.get(WikiListValues.URL), (Integer) pair.get(WikiListValues.ID));
            for (OnCategoryDownloadUpdateEvent listener : onCategoryDownloadUpdateEventListeners) {
                listener.installObject(this, current, total);
            }
        }

        for (OnCategoryDownloadUpdateEvent listener : onCategoryDownloadUpdateEventListeners)
            listener.doneDownload(this);
    }
}
