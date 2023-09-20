package org.apache.commons.collections4.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.collections4.ComparatorUtils;

/**
 * Reverses the order of another comparator by reversing the arguments
 * to its {@link #compare(Object, Object) compare} method.
 *
 * @since 2.0
 * @version $Id: ReverseComparator.java 1533984 2013-10-20 21:12:51Z tn $
 *
 * @see java.util.Collections#reverseOrder()
 */
public class ReverseComparator<E> implements Comparator<E>, Serializable {

    /** Serialization version from Collections 2.0. */
    private static final long serialVersionUID = 2858887242028539265L;

    /** The comparator being decorated. */
    private final Comparator<E> comparator;

    //-----------------------------------------------------------------------
    /**
     * Creates a comparator that compares objects based on the inverse of their
     * natural ordering.  Using this Constructor will create a ReverseComparator
     * that is functionally identical to the Comparator returned by
     * java.util.Collections.<b>reverseOrder()</b>.
     *
     * @see java.util.Collections#reverseOrder()
     */
    public ReverseComparator() {
        this(null);
    }

    /**
     * Creates a comparator that inverts the comparison
     * of the given comparator.  If you pass in <code>null</code>,
     * the ReverseComparator defaults to reversing the
     * natural order, as per {@link java.util.Collections#reverseOrder()}.
     *
     * @param comparator Comparator to reverse
     */
    @SuppressWarnings("unchecked")
    public ReverseComparator(final Comparator<? super E> comparator) {
        this.comparator = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : comparator;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares two objects in reverse order.
     *
     * @param obj1  the first object to compare
     * @param obj2  the second object to compare
     * @return negative if obj1 is less, positive if greater, zero if equal
     */
    public int compare(final E obj1, final E obj2) {
        return comparator.compare(obj2, obj1);
    }

    //-----------------------------------------------------------------------
    /**
     * Implement a hash code for this comparator that is consistent with
     * {@link #equals(Object) equals}.
     *
     * @return a suitable hash code
     * @since 3.0
     */
    @Override
    public int hashCode() {
        return "ReverseComparator".hashCode() ^ comparator.hashCode();
    }

    /**
     * Returns <code>true</code> iff <i>that</i> Object is
     * is a {@link Comparator} whose ordering is known to be
     * equivalent to mine.
     * <p>
     * This implementation returns <code>true</code>
     * iff <code><i>object</i>.{@link Object#getClass() getClass()}</code>
     * equals <code>this.getClass()</code>, and the underlying
     * comparators are equal.
     * Subclasses may want to override this behavior to remain consistent
     * with the {@link Comparator#equals(Object) equals} contract.
     *
     * @param object  the object to compare to
     * @return true if equal
     * @since 3.0
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (null == object) {
            return false;
        }
        if (object.getClass().equals(this.getClass())) {
            final ReverseComparator<?> thatrc = (ReverseComparator<?>) object;
            return comparator.equals(thatrc.comparator);
        }
        return false;
    }

}

