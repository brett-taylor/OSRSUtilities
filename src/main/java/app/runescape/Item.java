package app.runescape;

import app.data.DatabaseManager;
import app.data.DatabaseSQLExecuteResult;
import utils.ItemType;

import java.sql.ResultSet;
import java.sql.SQLException;

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
     * The name on the app.wiki used in the url of the article for this item.
     */
    private String wikiName;

    /**
     * The type of the item
     */
    private ItemType type;

    /**
     * The app.wiki id of the item.
     */
    private Integer wikiID;

    /**
     * Constructor.
     * @param name Name of the item.
     * @param wikiName Wiki name of the item.
     * @param type The item type.
     * @param wikiID The app.wiki id of the item.
     */
    private Item(String name, String wikiName, ItemType type, Integer wikiID) {
        this.name = name;
        this.wikiName = wikiName;
        this.type = type;
        this.wikiID  = wikiID;
    }

    /**
     * Creates an item with the information given and saves it to the data.
     * @param name Name of the item.
     * @param wikiName Wiki name of the item.
     * @param type The item type.
     * @param wikiID The app.wiki id of the item.
     */
    public static void create(String name, String wikiName, ItemType type, Integer wikiID) {
        if (wikiName.startsWith("/app/wiki/"))
            wikiName = wikiName.substring(6);

        wikiName = wikiName.replace("%27", "''");
        name = name.replace("'", "''");
        String s = "INSERT INTO " + ItemTable.TABLE_NAME + " VALUES ('" + name + "', '" + wikiName + "', '" + type.toString() + "', '" + wikiID.toString() + "');";
        DatabaseSQLExecuteResult execute = DatabaseManager.execute(s);
        System.out.println("SQL QUERY: " + s);
        if (!execute.isSuccessful())
            System.out.println("ERROR SQL: " + execute.getErrorMessage());
    }

    /**
     * Loads a item fromt he data.
     * @param name The name of the item.
     */
    public static Item load(String name) {
        String tempName = name;
        tempName = tempName.replace("'", "''");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ItemTable.NAME_FIELD).append(", ").append(ItemTable.WIKI_NAME_FIELD).append(", ").append(ItemTable.TYPE_FIELD).append(", ").append(ItemTable.WIKI_ID_FIELD);
        sql.append(" FROM ").append(ItemTable.TABLE_NAME).append(" WHERE ").append(ItemTable.NAME_FIELD).append(" = '").append(tempName).append("';");

        DatabaseSQLExecuteResult execute = DatabaseManager.execute(sql.toString());
        if (execute == null || !execute.isSuccessful()) {
            System.out.println("ERROR SQL: " + execute.getErrorMessage());
            return null;
        }

        ResultSet set = execute.getResultSet();
        try {
            if (set.next()) {
                return new Item(set.getString(ItemTable.NAME_FIELD), set.getString(ItemTable.WIKI_NAME_FIELD), ItemType.valueOf(set.getString(ItemTable.TYPE_FIELD)), set.getInt(ItemTable.WIKI_ID_FIELD));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * @return The item name
     */
    public String getItemName() {
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
        return wikiName;
    }
}
