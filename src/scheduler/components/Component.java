
package scheduler.components;

/** Class that represents a single element/component. */
public abstract class Component {

    public final static int NULL_ID = Integer.MAX_VALUE;
    public final static String NULL_NAME = "Null";

    /**Returns true if the given element is Null.
     * @return true if the given element is null, false otherwise
     */
    public abstract boolean isNull();
}

