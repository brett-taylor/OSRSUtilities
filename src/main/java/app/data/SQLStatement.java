package app.data;

/**
 * Creates a sql statement.
 * @author Brett Taylor
 */
public class SQLStatement {
    /**
     * The current sql statement.
     */
    private String sql;

    /**
     * Creates a SQLStatement.
     * @param sqlStatement the base statement formatted with its parameters.
     * e.g. "Select %a FROM %b;"
     */
    public SQLStatement(String sqlStatement) {
        sql = sqlStatement;
    }

    /**
     * Binds a parameter in the sql statement.
     * @param placeholder the placeholder that is currently in the sql string.
     * @param replaceText What the placeholder will be replaced with.
     */
    public void bindParam(String placeholder, String replaceText) {
        if (!sql.contains(placeholder))
            System.out.println("[SQL STATEMENT] placeholder not found. Placeholder: " + placeholder + " || ReplaceText: " + replaceText);
        sql = sql.replace(placeholder, replaceText);
    }

    /**
     * Binds a parameter in the sql statement and puts the parameter in apostrophes.
     * @param placeholder the placeholder that is currently in the sql string.
     * @param replaceText What the placeholder will be replaced with.
     */
    public void bindStringParam(String placeholder, String replaceText) {
        replaceText = replaceText.replace("'", "''");
        replaceText = replaceText.replace("%27", "''");
        bindParam(placeholder, "'" + replaceText + "'");
    }

    /**
     * @return The sql statement at the current time.
     */
    public String getSQLString() {
        return sql;
    }

    /**
     * @return True if this type of sql statement will be a query rather than a update.
     */
    public boolean willUseQuery() {
        return sql.startsWith("SELECT");
    }
}
