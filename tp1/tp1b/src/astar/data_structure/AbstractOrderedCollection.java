
package astar.data_structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;



/**
 * This class provides a skeletal implementation of the OrdredCollection
 *   interface to minimize the effort required to implement this
 *   interface backed by a "random access" data store (such as an array).
 * The programmer should generally provide a void (no argument) and
 *   ordered collection constructor, as per the recommendation in the
 *   OrderedCollection interface specification.
 * The programmer has to provide an iterator implementation; the
 *   iterator is not implemented by this class
 * <p>
 * The documentation for each non-abstract methods in this class
 *   describes its implementation in detail.
 * Each of these methods may be overridden if the collection being
 *   implemented admits a more efficient implementation. 
*/
public  abstract class AbstractOrderedCollection<E> implements OrderedCollection<E> {

  /**
  * Sole constructor. (For invocation by subclass constructors,
  *   typically implicit.)
  */
  protected AbstractOrderedCollection()
  {}
  
 
  
  /**
   * Ensures that this ordered collection contains the specified element;
   *   partial implementation must be overridden. 
   * Returns <code>true</code> if this collection changed as a result of the
   *   call. 
   * This always happens because ordered collections allow duplicates.
   *
   * @return <code>true</code> if this collection changed as a result of the
   *   call. 
  */
  public boolean add (E o)
  {
    objectCount++;
    modCount++;
    return true;
  }
  
  
  
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
  public boolean addAll (OrderedCollection<E> o)
  {
    modCount++;
	  boolean modified = false;
	  for (E e : o)
	    if(add(e))
		    modified = true;
  	return modified;
  }
  
  
  
  /**
   * Removes all of the elements from this ordered collection.
   * This collection will be empty after this method returns.
  */
  public void clear ()
  {
    modCount++;
    while (!isEmpty())
      remove();
  }

  
  
  /**
   * Returns <code>true</code> if this ordered collection has no elements.
   *
   * @return <code>true</code> if this ordered collection has no elements.
  */
  public boolean isEmpty()
  {
    return objectCount == 0;
  }



  /**
   * Returns the number of elements in this ordered collection.
   *
   * @return the number of elements in this collection
  */
  public int size ()
  {return objectCount;}


  
  /**
   * Returns/removes the next element in this ordered collection, according
   *   to how it is ordered; partial implementation must be overridden. 
   *
   * @return the next element in this ordered collection, according
   *   to how it is ordered.
   *
   * @throws NoSuchElementException if the ordered collection is empty
  */
  public E remove ()
    throws NoSuchElementException
  {
    if (isEmpty())
      throw new NoSuchElementException();
    objectCount--;
    modCount++;
    return null;
  }
  
  
  
  /**
   * Returns (without removing) the next elements in this ordered collection,
   *   according to how it is ordered.
   *
   * @return the next element in this ordered collection, according
   *   to how it is ordered.
  */
  public abstract E peek();


  
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
  public abstract Iterator<E> iterator();
  
 
  
  /**
   * Returns the hash code value for this object.
   *
   * @return the hash code value for this object.
  */
  public int hashCode()
  {
  	int hashCode = 1;
   	for (E e : this)
	    hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
	  return hashCode;
  }



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
   * @return an array containing the elements of this collection
  */
  public Object[] toArray()
  {
  	Object[] result = new Object[size()];
	  Iterator<E> e = iterator();
	  for (int i=0; e.hasNext(); i++)
	    result[i] = e.next();
	  return result;
	}



  /**
   * Returns an array containing all of the elements in this ordered
   *   collection, in order, whose runtime type is that of the specified
   *   array.
   * <p>
   * If the collection fits in the specified array, it is returned therein.
   * Otherwise, a new array is allocated with the runtime type of the
   *  specified array and the size of this collection.
   *
   * @return an array containing the elements of this ordered collection
  */
  @SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a)
  {
    int size = size();
    if (a.length < size)
      a = (T[])
          java.lang.reflect.Array.newInstance(
             a.getClass().getComponentType(), size);

    Iterator<E> it=iterator();
    Object[] result = a;
    for (int i=0; i<size; i++)
      result[i] = it.next();

    if (a.length > size)
      a[size] = null;

    return a;
   }



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
  public Collection<E> toCollection ()
  {
    List<E> result = new ArrayList<E>();
	  for (E e : this)
	    result.add(e);
	  return result;
	}

 
     
  /**
   * Returns a string representation of this collection.
   * The string representation consists of a list of the collection's
   *   elements in the order they are returned by its iterator,
   *   enclosed in square brackets ("[]").
   * Adjacent elements are separated by the characters ", " (comma and 
   *   space).
   * Elements are converted to strings as by String.valueOf(Object).
   * <p>
   * This implementation creates an empty string buffer, appends a left
   *   square bracket, and iterates over the collection appending the
   *   string representation of each element in turn.
   * After appending each element except the last, the string ", " is
   *   appended.
   * Finally a right bracket is appended.
   * A string is obtained from the string buffer, and returned.
   *
   * @return an array containing the elements of this collection
  */
  public String toString()
  {
  	StringBuffer buf = new StringBuffer();
 
    Iterator<E> i = iterator();
    boolean hasNext = i.hasNext();
    while (hasNext) {
      E o = i.next();
      buf.append(o == this ? "(this Collection)" : String.valueOf(o));
      hasNext = i.hasNext();
      if (hasNext)
        buf.append(", ");
    }

	  return buf.toString();
	}
  
  
  
  
  /*
   * The number of objects currently in this ordered collection.
  */
  protected int objectCount = 0;


  /*
   * The number of times this ordered collection has been structurally
   *   modified.
  */
protected transient volatile int modCount = 0;
}
