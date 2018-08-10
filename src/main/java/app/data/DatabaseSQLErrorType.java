package app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL errors will be assigned one of these types.
 * @author Brett Taylor
 */
public enum DatabaseSQLErrorType {
    UNIQUE_FAILED("UNIQUE constraint failed"),
    UNKNOWN("THIS_IS_A_UNKNOWN_ERROR_WHOOPS"); // Fake data so unknown is never detected automatically. UNKNOWN should be return manually.

    /**
     * The type of sql error is assigned based if this string is found in that error message.
     */
    private String sqlErrorContains;

    /**
     * Constructor
     * @param sqlErrorContains The type of sql error is assigned based if this string is found in that error message.
     */
    DatabaseSQLErrorType(String sqlErrorContains) {
        this.sqlErrorContains = sqlErrorContains;
    }

    /**
     * Returns all of the error types except from Unknown.
     * @return The possible types of errors.
     */
    public static List<DatabaseSQLErrorType> getPossibleErrorTypes() {
        List<DatabaseSQLErrorType> errors = new ArrayList<>();
        for (DatabaseSQLErrorType errorType : DatabaseSQLErrorType.values()) {
            if (errorType != UNKNOWN)
                errors.add(errorType);
        }

        return errors;
    }

    /**
     * @return The string that if found in the sql error will decide the type of error.
     */
    public String getSqlErrorContains() {
        return sqlErrorContains;
    }
}
