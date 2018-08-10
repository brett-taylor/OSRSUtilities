package app.data.tables;

/**
 * The name of the column and tables for the Monster table.
 * @author Brett Taylor
 */
public class MonsterTable {
    /**
     * The table name
     */
    public static final String TABLE_NAME = "Monsters";

    /**
     * The name column - Primary key in this table.
     */
    public static final String NAME_COLUMN_PK = "name";

    /**
     * The wiki url status column - Unique in this table
     */
    public static final String WIKI_URL_COLUMN = "wikiURL";

    /**
     * The wiki ID status column - Unique in this table
     */
    public static final String WIKI_ID_COLUMN = "wikiID";
}
