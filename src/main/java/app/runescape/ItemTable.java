package app.runescape;

import app.data.DatabaseTable;
import app.utils.pairs.StringPair;

import java.util.ArrayList;
import java.util.List;

/**
 * The item table in the data.
 * @author Brett Taylor
 */
public class ItemTable extends DatabaseTable {
    /**
     * The name of the table.
     */
    public final static String TABLE_NAME = "Items";

    /**
     * The name of the column that stores the name of the item.
     */
    public final static String NAME_FIELD = "name";

    /**
     * The name of the column that stores the name on the app.wiki of the item.
     */
    public final static String WIKI_NAME_FIELD = "wiki_name";

    /**
     * The name of the column that stores the type of the item.
     */
    public final static String TYPE_FIELD = "type";

    /**
     * The id of the article.
     */
    public final static String WIKI_ID_FIELD = "wiki_id";

    public ItemTable() {
        super(TABLE_NAME);

        List<StringPair> columns = new ArrayList<StringPair>();
        columns.add(new StringPair(NAME_FIELD, "varchar(255)"));
        columns.add(new StringPair(WIKI_NAME_FIELD, "varchar(255)"));
        columns.add(new StringPair(TYPE_FIELD, "varchar(255)"));
        columns.add(new StringPair(WIKI_ID_FIELD, "integer"));

        setColumns(columns);
    }
}
