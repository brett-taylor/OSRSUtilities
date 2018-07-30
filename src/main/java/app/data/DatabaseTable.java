package app.data;

import app.utils.pairs.StringPair;

import java.util.Iterator;
import java.util.List;

/**
 * Holds information about what each table needs.
 * @author Brett Taylor
 */
public abstract class DatabaseTable {
    /**
     * The name of the table.
     */
    private String name;

    /**
     * The columns of the table.
     * Form of: <table name, data type>
     */
    private List<StringPair> columns = null;

    /**
     * Constructor
     * @param name The name of the table.
     */
    protected DatabaseTable(String name) {
        this.name = name;
    }

    /**
     * Updates the columns of the table.
     * @param columns the new columns.
     */
    protected void setColumns(List<StringPair> columns) {
        this.columns = columns;
    }

    /**
     * Creates and updates the table.
     */
    public void createAndUpdate() {
        if (columns == null)
            return;

        // Create the table if needed
        if (!DatabaseManager.doesTableExist(name)) {
            DatabaseManager.execute(getCreateString());
        }

        // Add any extra columns that currently don't exist.
        for (StringPair column : columns) {
            if (!DatabaseManager.doesTableHaveColumn(name, column.getOne())) {
                DatabaseManager.execute(getInsertNewColumnString(column.getOne(), column.getTwo()));
            }
        }
    }

    /**
     * @return the sql statement to create the table.
     */
    private String getCreateString() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(name);
        sql.append(" (");

        Iterator<StringPair> it = columns.iterator();
        while (it.hasNext()) {
            StringPair column = it.next();
            sql.append(column.getOne());
            sql.append(" ");
            sql.append(column.getTwo());

            if (it.hasNext()) {
                sql.append(", ");
            }
        }

        sql.append(");");
        return sql.toString();
    }

    /**
     * Generates a sql string that can be used to insert a new column
     * @param columnName the name of the new column
     * @param columnType the data type of the new column
     * @return the generated sql statement.
     */
    private String getInsertNewColumnString(String columnName, Object columnType) {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ");
        sql.append(name);
        sql.append(" ADD ");
        sql.append(columnName);
        sql.append(" ");
        sql.append(columnType);
        sql.append(";");

        return sql.toString();
    }
}

