package app.data.models;

import app.data.DataManager;
import app.data.DatabaseSQLErrorType;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.tables.ItemTable;
import app.ui.components.DialogBox;

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
     * Creates a item.
     * @param name Name of the item.
     * @param wikiURL Wiki name of the item.
     * @param wikiID The app.data.wiki id of the item.
     */
    private Item(String name, String wikiURL, Integer wikiID) {
        this.name = name;
        this.wikiURL = wikiURL;
        this.wikiID  = wikiID;
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
}
