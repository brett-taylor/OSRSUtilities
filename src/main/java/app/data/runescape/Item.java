package app.data.runescape;

import app.data.DataManager;
import app.data.DatabaseSQLErrorType;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.tables.ItemTable;
import app.ui.components.popups.DialogBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item from runescape.
 * @author Brett Taylor
 */
public class Item {
    /**
     * Name of the item.
     */
    private String name;

    /**
     * The name on the wiki used in the url of the article for this item.
     */
    private String wikiURL;

    /**
     * The wiki id of the item.
     */
    private Integer wikiID;

    /**
     * The stack size of the item.
     */
    private int stackSize;

    /**
     * Items stored inside of this item.
     */
    private List<Item> itemsInside = new ArrayList<>();

    /**
     * Creates a item.
     * @param name Name of the item.
     * @param wikiURL Wiki name of the item.
     * @param wikiID The app.data.wiki id of the item.
     */
    private Item(String name, String wikiURL, Integer wikiID) {
        this.name = name;
        this.wikiURL = wikiURL;
        this.wikiID  = wikiID;
        stackSize = 1;
    }

    /**
     * @return The item name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The items' wiki article id.
     */
    public int getArticleID() {
        return wikiID;
    }

    /**
     * @return The items wiki article url.
     */
    public String getWikiURLEnding() {
        return wikiURL;
    }

    /**
     * Creates an item with the information given and saves it to the data.
     * @param name Name of the item.
     * @param wikiName Wiki name of the item.
     * @param wikiID The app.data.wiki id of the item.
     */
    public static void create(String name, String wikiName, Integer wikiID) {
        if (wikiName.startsWith("/app/data/wiki/"))
            wikiName = wikiName.substring(6);

        SQLStatement sqlStatement = new SQLStatement("INSERT INTO tableName VALUES(itemName, itemWikiURL, itemWikiID);");
        sqlStatement.bindParam("tableName", ItemTable.TABLE_NAME);
        sqlStatement.bindStringParam("itemName", name);
        sqlStatement.bindStringParam("itemWikiURL", wikiName);
        sqlStatement.bindParam("itemWikiID", "" + wikiID);

        DatabaseSQLExecuteResult execute = DataManager.execute(sqlStatement);
        if (!execute.isSuccessful()) {
            if (execute.getErrorType() != DatabaseSQLErrorType.UNIQUE_FAILED) {
                DialogBox.showError("Failed to create Item: " + name + ". \n" + execute.getErrorMessage());
            }
        }
    }

    /**
     * Deletes all items that have the matching url.
     * @param url The url to match.
     */
    public static void deleteByMatchingWikiURL(String url) {
        SQLStatement sqlStatement = new SQLStatement("DELETE FROM tableName WHERE urlColumn LIKE urlGiven");
        sqlStatement.bindParam("tableName", ItemTable.TABLE_NAME);
        sqlStatement.bindParam("urlColumn", ItemTable.WIKI_URL_COLUMN);
        sqlStatement.bindStringParam("urlGiven", "%" + url + "%");

        DatabaseSQLExecuteResult execute = DataManager.execute(sqlStatement);
        if (!execute.isSuccessful()) {
            DialogBox.showError("Failed to delete Items with following url: " + url + ". \n" + execute.getErrorMessage());
        }
    }

    /**
     * Loads a item from the database with a given name.
     * @param name The name of the item.
     */
    public static Item load(String name) {
        return load(name, 1);
    }

    /**
     * Loads a item from the database with a given name.
     * @param name The name of the item.
     * @param amount The item stack size.
     */
    public static Item load(String name, int amount) {
        SQLStatement sqlStatement = new SQLStatement("SELECT nameColumn, wikiURLColumn, wikiIDColumn FROM tableName WHERE nameColumn = itemName;");
        sqlStatement.bindParam("tableName", ItemTable.TABLE_NAME);
        sqlStatement.bindParam("nameColumn", ItemTable.NAME_COLUMN_PK);
        sqlStatement.bindParam("wikiURLColumn", ItemTable.WIKI_URL_COLUMN);
        sqlStatement.bindParam("wikiIDColumn", ItemTable.WIKI_ID_COLUMN);
        sqlStatement.bindStringParam("itemName", name);

        DatabaseSQLExecuteResult execute = DataManager.execute(sqlStatement);
        if (!execute.isSuccessful()) {
            DialogBox.showError("Failed to load Item with following name: " + name + ". \n" + execute.getErrorMessage());
            return null;
        }

        try {
            if (execute.getResultSet().next()) {
                Item item = new Item(
                        execute.getResultSet().getString(ItemTable.NAME_COLUMN_PK),
                        execute.getResultSet().getString(ItemTable.WIKI_URL_COLUMN),
                        execute.getResultSet().getInt(ItemTable.WIKI_ID_COLUMN)
                );

                item.setStackSize(amount);
                return item;
            }
        } catch (SQLException e) {
            DialogBox.showError("Failed to load Item with following name: " + name + ". \n" + execute.getErrorMessage());
            return null;
        }

        return null;
    }

    /**
     * Gets all of the items that name matches the given pattern.
     * @param phrase The pattern
     * @return The list of items.
     */
    public static List<Item> loadAllItemsThatContainInName(String phrase) {
        SQLStatement sqlStatement = new SQLStatement("SELECT itemColumnName FROM tableName WHERE itemColumnName LIKE itemNamePhrase");
        sqlStatement.bindParam("tableName", ItemTable.TABLE_NAME);
        sqlStatement.bindParam("itemColumnName", ItemTable.NAME_COLUMN_PK);
        sqlStatement.bindStringParam("itemNamePhrase", "%" + phrase + "%");

        DatabaseSQLExecuteResult execute = DataManager.execute(sqlStatement);
        if (!execute.isSuccessful()) {
            DialogBox.showError("Failed to find items with following pattern in name. Pattern: " + phrase + ". \n" + execute.getErrorMessage());
            return null;
        }

        List<String> itemNames = new ArrayList<>();
        try {
            while (execute.getResultSet().next()) {
                itemNames.add(execute.getResultSet().getString(ItemTable.NAME_COLUMN_PK));
            }
        } catch (SQLException e) {
            DialogBox.showError("Failed to find items with following pattern in name. Pattern: " + phrase + ". \n" + execute.getErrorMessage());
            return null;
        }

        ArrayList<Item> items = new ArrayList<>();
        for (String itemName : itemNames) {
            Item item = load(itemName);
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    /**
     * @return The stack size of the item.
     */
    public int getStackSize() {
        return stackSize;
    }

    /**
     * Sets the stack size of the item.
     * @param newStackSize The new stack size.
     */
    public void setStackSize(int newStackSize) {
        stackSize = newStackSize;
    }

    /**
     * @return A list of items inside of this item.
     */
    public List<Item> getItemsInside() {
        return itemsInside;
    }
}
