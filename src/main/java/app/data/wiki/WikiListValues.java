package app.data.wiki;

/**
 * The values that exist inside a wiki list.
 * @author Brett Taylor
 */
public enum WikiListValues {
    TITLE("title"),
    URL("url"),
    ID("id");

    /**
     * The name of the object they are representing.
     */
    private String name;

    /**
     * Constructor
     * @param name The name of the object they are representing.
     */
    WikiListValues(String name) {
        this.name=  name;
    }

    @Override
    public String toString() {
        return name;
    }
}
