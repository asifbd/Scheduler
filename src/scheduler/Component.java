
package scheduler;

/** Class that represents a single element/component. */
public abstract class Component {

    final static int NULL_ID = Integer.MAX_VALUE;
    final static String NULL_NAME = "Null";

    /**Returns true if the given element is Null.*/
    public abstract boolean isNull();
}

