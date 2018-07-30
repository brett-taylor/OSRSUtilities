package app.utils.pairs;

/**
 * A abstract pair of variables.
 * @author Brett Taylor
 */
public class Pair<A, B> {
    /**
     * The first variable.
     */
    private final A one;
    /**
     * The second variable.
     */
    private final B two;

    /**
     * Constructor.
     * @param one variable one
     * @param two variable two
     */
    public Pair(A one, B two) {
        this.one = one;
        this.two = two;
    }

    /**
     * @return the first variable
     */
    public A getOne() {
        return one;
    }

    /**
     * @return the second variable.
     */
    public B getTwo() {
        return two;
    }
}