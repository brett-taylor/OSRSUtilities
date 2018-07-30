package app.utils.pairs;

/**
 * A abstract pair of variables where they are the same type.
 * @author Brett Taylor
 */
public class SamePair<A> extends Pair<A, A> {
    /**
     * Constructor.
     * @param one variable one
     * @param two variable two
     */
    public SamePair(A one, A two) {
        super(one, two);
    }
}
