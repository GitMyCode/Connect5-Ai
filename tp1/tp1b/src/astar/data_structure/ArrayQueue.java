package astar.data_structure;
import java.util.Iterator;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;



/**
 * This class implements the <code>OrderedCollection</code> interface
 *   with FIFO behavior: first in/first out.
 * It is backed by an array (used circularly).
 * It makes a guarantee that the iteration order is FIFO.
 * This class permits the <code>null</code> element.
 * This class offers constant time performance for the basic operations
 *   (<code>add</code>, <code>remove</code>, <code>peek</code>, and
 *   <code>size</code>).
 * Iterating over this set requires time proportional to the size of the
 *    queue.
 * <p>
 * Note that this implementation is not synchronized.
 * If multiple threads access a queue concurrently, and at least one of
 *   the threads modifies the queue, it must be synchronized externally.
 * This is typically accomplished by synchronizing on some object that
 *   naturally encapsulates the queue.
 * <p>
 * The iterators returned by this class's iterator method are fail-fast:
 *   if the queue is modified at any time after the iterator is created,
 *   in any way, the Iterator throws a
 *   <code>ConcurrentModificationException</code>.
 * Thus, in the face of concurrent modification, the iterator fails
 *   quickly and cleanly, rather than risking arbitrary,
 *   non-deterministic behavior at an undetermined time in the future. 
*/
public class ArrayQueue<E> extends AbstractQueue<E> {
    
  
  /**
   * Constructs an empty, new queue with the capacity specified.
   *
   * @param initialCapacity - the size of the initial backing array.
   *
   * @throws IllegalArgumentException if <code>initialCapacity&lt;0</code>
  */
  @SuppressWarnings("unchecked")
	public ArrayQueue (int initialCapacity)
  {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Capacity: "+  initialCapacity);
    q = (E[])new Object[initialCapacity];
  }
  
  
  
  /** 
    Constructs a new, empty queue.
  */
  public ArrayQueue ()
  {this(1);}
  
  
  
  /**
   *Constructs a new queue containing the elements in the specified
   *   ordered collection.
   *
   * @param o - the ordered collection whose elements are to be placed
   *          into this queue.
  */
  @SuppressWarnings("unchecked")
	public ArrayQueue (OrderedCollection<? extends E> o)
  {this((E[])o.toArray());}
  
  
  
  /**
   *Constructs a new queue containing the elements in the specified
   *   collection.
   *
   * @param c - the collection whose elements are to be placed into this
   *             queue.
  */
  @SuppressWarnings("unchecked")
	public ArrayQueue (Collection<? extends E> c)
  {this((E[])c.toArray());}
  
  
  
  /**
   *Constructs a new queue containing the elements in the specified
   *   array.
   *
   * @param o - the array whose elements are to be placed into this
   *             queue.
  */
  @SuppressWarnings("unchecked")
	public ArrayQueue (E[] o)
  {
    // Allow 10% room for growth
    q = (E[])new Object[(int)Math.min((o.length*110L)/100,Integer.MAX_VALUE)]; 
    for (E e : o)
      add(e);
  }
  
  
  
  /**
   * Ensures that this queue contains the specified element. 
   * Returns <code>true</code> if this collection changed as a result of the
   *   call. 
   * This always happens because queues allow duplicates.
   *
   * @param o - the element to store in the queue
   *
   * @return <code>true</code> if this queue collection changed as a
   *   result of the call. 
  */
  public boolean add (E o)
  {
    ensureCapacity(objectCount+1);
    super.add(o);
    last = (last+1)%q.length;
    q[last] = o;
    return true;
  }
  

  
  /**
   * Returns/removes the next element in this queue, according
   *   to FIFO behavior.
   *
   * @return the next element in this queue, according
   *   to FIFO behavior.
   *
   * @throws NoSuchElementException if the queue is empty
  */
  public E remove ()
    throws NoSuchElementException
  {
    super.remove();
    E answer = q[first];
    q[first] = null;
    first    = (first+1)%q.length;
    return answer;
  }

  
  
  /**
   * Returns (without removing) the next elements in this queue,
   *   according to FIFO behavior.
   *
   * @return the next elements in this queue,
   *   according to FIFO behavior.
   *
   * @throws NoSuchElementException if the queue is empty
  */
  public E peek ()
    throws NoSuchElementException
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return q[first];
  }


  /**
   * Ensures that the backing array can store at least
   *   <code>minCapacity</b> elements.
   *
   * @param minCapacity - the minimum length of the backing array
  */
  @SuppressWarnings("unchecked")
	public void ensureCapacity (int minCapacity)
  {
    modCount++;
	  int oldCapacity = q.length;
	  if (minCapacity > oldCapacity) {
	    E[] old = q;
	    int newCapacity = (oldCapacity * 3)/2 + 1;
    	if (newCapacity < minCapacity)
		    newCapacity = minCapacity;
	    q = (E[])new Object[newCapacity];
	    for (int i=0; i<objectCount; i++)
	      q[i] = old[(first+i)%old.length];
      first = 0;
      last  = objectCount-1;
	  }
	} 
  


  /**
   * Ensures that the backing array is exactly big enough to
   *   store only the elements it currently stores.
  */
  @SuppressWarnings("unchecked")
	public void trimToSize () {
	  modCount++;
	  int oldCapacity = q.length;
	  if (objectCount < oldCapacity) {
	    E[] old = q;
	    q = (E[])new Object[objectCount];
	    for (int i=0; i<objectCount; i++)
	      q[i] = old[(first+i)%old.length];
      first = 0;
      last  = objectCount-1;
    }
	}
  
  
  
  /**
   * Returns an <code>iterator</code> over the elements in this queue.
   * The order is guaranteed to be exhibit FIFO behavior.
   *
   * @return an <code>iterator</code> over the elements in this queue
  */
  public Iterator<E> iterator()
  {return new ArrayQueueIterator();}
  
   
   
  /**
   * Returns a string representation of this queue.
   * The string representation consists of a count of the number of
   *   elements in the queue, the queue's length, and a list of the
   *   queue's elements in the order they are returned by its iterator,
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
   * @return an array containing the elements of this queue
  */
  public String toString()
  {return "ArrayQueue["+objectCount+"/"+q.length+":"+super.toString()+"]";}



  /*
   * The index of the first element in the queue.
  */
  private int first = 0;


  /*
   * The index of the last element in the queue.
  */
  private int last = -1;


  /*
   * The backing array storing all the values in the queue.
  */
  private E[] q;
  

  
  /*
   * The iterator for this queue implementation.
  */
  private class ArrayQueueIterator implements Iterator<E> {
  
    ArrayQueueIterator ()
    {
      iteratorIndex    = first;
      leftToIterate    = objectCount;
      expectedModCount = modCount;
    }
   
    public boolean hasNext()
    {return leftToIterate != 0;}
   
    
    public E next()
      throws ConcurrentModificationException,NoSuchElementException
    {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (leftToIterate == 0)
        throw new NoSuchElementException();
      E answer = q[iteratorIndex];
      iteratorIndex = (iteratorIndex+1)%q.length;
      leftToIterate--;
      return answer;
    }
   
   
    public void remove()
      throws UnsupportedOperationException
    {throw new UnsupportedOperationException();}
    
    private int expectedModCount;
    private int iteratorIndex;
    private int leftToIterate;
  }
}
