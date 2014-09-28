package astar.data_structure;
import java.util.Iterator;
import java.util.Collection;



/**
 * The root interface in the ordered collection hierarchy.
 * An ordered collection represents a group of objects, known as its elements.
 * Ordered collections allow duplicate elements.
 * This interface is typically used to pass ordered collections around and
 *   manipulate them where maximum generality is desired.
 * <p>
 * All general-purpose OrderedCollection implementation classes (which
 *   typically implement OrdredCollection directly) should provide two
 *   "standard" constructors: a void (no arguments) constructor,
 *   which creates an empty ordred collection, and a constructor with a
 *   single argument of type OrdredCollection, which creates a new ordred
 *   collection with the same elements as its argument.
 * In effect, the latter constructor allows the user to copy any collection,
 *   producing an equivalent collection of the desired implementation type.
 * There is no way to enforce this convention (as interfaces cannot contain
 *   constructors) but all of the general-purpose OrderedCollection
 *   implementations provide here comply.
*/

public interface OrderedCollection<E> extends Iterable<E>{

  /**
   * Ensures that this ordered collection contains the specified element. 
   * Returns <code>true</code> if this collection changed as a result of the
   *   call. 
   * This always happens because ordered collections allow duplicates.
   *
   * @param o - the element to store in the ordered collection
   *
   * @return <code>true</code> if this collection changed as a result of the
   *   call. 
  */
  boolean add (E o);
  
  
  
  /**
   * Adds all of the elements in the specified ordered collection to
   *   this collection.
   * The behavior of this operation is undefined if the specified ordered
   *   collection is modified while the operation is in progress.
   * (This implies that the behavior of this call is undefined if the
   *  specified ordered collection is this collection, and this collection
   *  is nonempty.)
   *
   * @return <code>true</code> if this collection changed as a result of the
   *   call. 
   */
  boolean addAll (OrderedCollection<E> o);
  
  
  
  /**
   * Removes all of the elements from this ordered collection.
   * This collection will be empty after this method returns
  */
  void clear ();

  
  
  /**
   * Returns <code>true</code> if this ordered collection has no elements.
   *
   * @return <code>true</code> if this ordered collection has no elements.
  */
  boolean isEmpty();
  
  
  
  /**
   * Returns the number of elements in this ordered collection.
   *
   * @return the number of elements in this collection
  */
  int size ();


  
  /**
   * Returns/removes the next element in this ordered collection, according
   *   to how it is ordered.
   *
   * @return the next element in this ordered collection, according
   *   to how it is ordered.
   *
   * @throws if the ordered collection is empty
  */
  E remove ();


  
  /**
   * Returns (without removing) the next elements in this ordered collection,
   *   according to how it is ordered.
   *
   * @return the next element in this ordered collection, according
   *   to how it is ordered.
   *
   * @throwsd if the ordered collection is empty
  */
  E peek();


  
    /**
     * Compares the specified object with this collection for equality.
     * <p>
     * While the <code>Collection</code> interface adds no stipulations to
     *   the general contract for the <code>Object.equals</code>, programmers
     *   who implement the <code>Collection</code> interface "directly" (in
     *   other words, create a class that is a <code>Collection</code> but is
     *   not a <code>Stack</code> or a <code>Queuet</code> or 
     *   <code>PriorityQueue</code>) must exercise care if
     *   they choose to override the <code>Object.equals</code>.
     * It is not necessary to do so, and the simplest course of action is to
     *   rely on <code>Object</code>'s implementation, but the implementer
     *   may wish to implement a "value comparison" in place of the default
     *   "reference comparison."
     * <p>
     * The general contract for the <code>Object.equals</code> method
     *   states that equals must be symmetric (in other words,
     *   <code>a.equals(b)</code> if and only if <code>b.equals(a)</code>).
     * The contracts for <code>Stack.equals</code>, for instance, states
     *   that stacks are only equal to other stacks.
     *
     * @param o - Object to be compared for equality with this collection.
     *
     * @return <code>true</code> if the specified object is equal to this
     *   collection
     * 
     * @see Object#equals(Object)
    */
    boolean equals(Object o);


  
  /**
   * Returns the hash code value for this collection.
   * While the <code>Collection</code> interface adds no stipulations to
   *   the general contract for the <code>Object.hashCode</code> method,
   *   programmers should take note that any class that overrides the
   *   <code>Object.equals</code> method must also override the
   *   <code>Object.hashCode</code> method in order to satisfy the
   *   general contract for the <code>Object.hashCode</code>method.
   * In particular, <code>c1.equals(c2)</code> implies that
   *   <code>c1.hashCode()==c2.hashCode()</code>.
   *
   * @return the hash code value for this collection
   * 
   * @see Object#hashCode()
   * @see Object#equals(Object)
  */
  int hashCode();
  
  
  
  /**
   * Returns an <code>iterator</code> over the elements in this ordered
   *   collection.
   * There are no guarantees concerning the order in which the elements are
   *   returned (unless this ordered collection is an instance of some class
   *   that provides a guarantee).
   *
   * @return an <code>iterator</code> over the elements in this ordered
   *    collection
  */
  Iterator<E> iterator();


  
  /**
   * Returns an array containing all of the elements in this ordered
   *  collection, in order.
   * If the collection makes any guarantees as to what order its elements
   *   are returned by its iterator, this method must return the elements
   *   in the same order.
   * <p>
   * The returned array will be "safe" in that no references to it are
   *   maintained by this collection. (In other words, this method must
   *   allocate a new array even if this collection is backed by an array).
   * The caller is thus free to modify the returned array.
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   *
   * @return an array containing the elements of this ordered collection
  */
  Object[] toArray();


  
  /**
   * Returns an array containing all of the elements in this ordered
   *   collection, in order, whose runtime type is that of the specified
   *   array.
   * <p>
   * If the collection fits in the specified array, it is returned therein.
   * Otherwise, a new array is allocated with the runtime type of the
   *  specified array and the size of this collection.
   *
   * @return an array containing the elements of this ordred collection
  */
  <T> T[] toArray(T[] a);


  
  /**
   * Returns a collection containing all of the elements in this ordered
   *   collection.
   * If the collection makes any guarantees as to what order its elements
   *   are returned by its iterator, this method must return the elements
   *   in the same order.
   * <p>
   * This method acts as bridge between collection-based and ordred 
   *  collection-based APIs.
   *
   * @return a collection containing the elements of this ordred
   *    collection
  */
  Collection toCollection();
}
