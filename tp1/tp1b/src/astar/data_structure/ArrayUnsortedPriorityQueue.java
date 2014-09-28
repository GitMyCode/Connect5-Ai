package astar.data_structure;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;


/**
 * This class implements the <code>OrderedCollection</code> interface
 *   with priority queue behavior: highest priority out.
 * It is backed by an unsorted array.
 * It makes a guarantee that the iteration order is in decreasing
 *   priority.
 * This class permits the <code>null</code> element.
 * This class offers constant time performance for many basic operations
 *   (<code>add</code> and <code>size</code>).
 * But, <code>remove</b>, <code>peekNext</code> require time
 *   proportional to the size of the priority queue.
 * Iterating over this set requires time proportional to the size of the
 *   priority queue times the log of its size.
 * <p>
 * Note that this implementation is not synchronized.
 * If multiple threads access a priority queue concurrently, and at
 *   least one of the threads modifies the priority queue, it must be
 *   synchronized externally.
 * This is typically accomplished by synchronizing on some object that
 *   naturally encapsulates the priority queue.
 * <p>
 * The iterators returned by this class's iterator method are fail-fast:
 *   if the priority queue is modified at any time after the iterator is
 *   created, in any way, the Iterator throws a
 *   <code>ConcurrentModificationException</code>.
 * Thus, in the face of concurrent modification, the iterator fails
 *   quickly and cleanly, rather than risking arbitrary,
 *   non-deterministic behavior at an undetermined time in the future. 
*/
public class ArrayUnsortedPriorityQueue<E> extends AbstractPriorityQueue<E> {

  /**
   * Constructs an empty, new priority queue with the capacity specified.
   *
   * @param initialCapacity    - the size of the initial backing array.
   * @param priorityComparator - the object used to determine priority.
   *
   * @throws IllegalArgumentException if <code>initialCapacity&lt;0</code>
  */
  @SuppressWarnings("unchecked")
	public ArrayUnsortedPriorityQueue (int                   initialCapacity,
                                     Comparator<? super E> priorityComparator)
  {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Capacity: "+  initialCapacity);
    this.priorityComparator = priorityComparator;
    pq = (E[])new Object[initialCapacity];
  }
  
  
  
  /**
   * Constructs an empty, new priority queue with the capacity specified.
   *
   * @param priorityComparator - the object used to determine priority.
  */
  public ArrayUnsortedPriorityQueue (Comparator<? super E> priorityComparator)
  {this(1, priorityComparator);}
  
  
  
  /**
   *Constructs a new priority queue containing the elements in the specified
   *   ordered collection.
   *
   * @param o - the ordered collection whose elements are to be placed
   *          into this priority queue.
   * @param priorityComparator - the object used to determine priority.
  */
  @SuppressWarnings("unchecked")
	public ArrayUnsortedPriorityQueue (OrderedCollection<E>  o,
  		                               Comparator<? super E> priorityComparator)
  {this((E[])o.toArray(), priorityComparator);}
  
  
  
  /**
   *Constructs a new priority queue containing the elements in the specified
   *   collection.
   *
   * @param c - the collection whose elements are to be placed into this
   *             priority queue.
   * @param priorityComparator - the object used to determine priority.
  */
  @SuppressWarnings("unchecked")
	public ArrayUnsortedPriorityQueue (Collection<E>         c,
  		                               Comparator<? super E> priorityComparator)
  {this((E[])c.toArray(), priorityComparator);}
  
 
  
  /**
   *Constructs a new priority queue containing the elements in the specified
   *   array.
   *
   * @param o - the array whose elements are to be placed into this
   *             priority queue.
   * @param priorityComparator - the object used to determine priority.
  */
  @SuppressWarnings("unchecked")
	public ArrayUnsortedPriorityQueue (E[]                   o,
  		                               Comparator<? super E> priorityComparator)
  {
    // Allow 10% room for growth
    this.priorityComparator = priorityComparator;
    pq = (E[])new Object[(int)Math.min((o.length*110L)/100,Integer.MAX_VALUE)]; 
    for (int i=0; i<o.length; i++)
      add(o[i]);
  }
  
  
  
  /**
   * Ensures that this priority queue contains the specified element. 
   * Returns <code>true</code> if this collection changed as a result of the
   *   call. 
   * This always happens because priority queues allow duplicates.
   *
   * @param o - the element to store in the priority queue
   *
   * @return <code>true</code> if this priority queue changed as a
   *   result of the call. 
  */
  public boolean add (E o)
  {
    ensureCapacity(objectCount+1);
    super.add(o);
    pq[objectCount-1] = o;
    return true;
  }
  
  
  
  /**
   * Returns the index of the the highest priority element in this
   *   priority queue.
   *
   * @return the index of the the highest priority element in this
   *   priority queue
  */
  private int maxIndex()
  {
    int maxI = 0;
    for (int i=1; i<objectCount; i++)
      if (priorityComparator.compare(pq[i],pq[maxI]) >= 0 )
        maxI = i;
    return maxI;
  }
  
  
  
  /**
   * Returns/removes the highest priority element in this priority queue.
   *
   * @return the highest priority element in this priority queue
   *
   * @throws NoSuchElementException if the priority queue is empty
  */
  public E remove ()
    throws NoSuchElementException
  {
    if (isEmpty())
      throw new NoSuchElementException();
    int maxI   = maxIndex();
    E   answer = pq[maxI];
    pq[maxI]          = pq[objectCount-1];
    pq[objectCount-1] = null;
    super.remove();
    return answer;
  }

  

  /**
   * Returns (without removing) the highest priority element in this
   *   priority queue.
   *
   * @return the highest priority element in this
   *   priority queue.
   *
   * @throws NoSuchElementException if the priority queue is empty
  */
  public E peek ()
    throws NoSuchElementException
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return pq[maxIndex()];
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
	  int oldCapacity = pq.length;
	  if (minCapacity > oldCapacity) {
	    Object old[] = pq;
	    int newCapacity = (oldCapacity * 3)/2 + 1;
    	if (newCapacity < minCapacity)
		    newCapacity = minCapacity;
	    pq = (E[])new Object[newCapacity];
	    System.arraycopy(old,0,pq,0,objectCount);
	  }
	} 
  
  
  
  /**
   * Ensures that the backing array is exactly big enough to
   *   store only the elements it currently stores.
  */
  @SuppressWarnings("unchecked")
	public void trimToSize ()
  {
	  modCount++;
	  int oldCapacity = pq.length;
	  if (objectCount < oldCapacity) {
	    E old[] = pq;
	    pq = (E[]) new Object[objectCount];
	    System.arraycopy(old,0,pq,0,objectCount);
    }
	}


  
  /**
   * Returns an <code>iterator</code> over the elements in this priority
   *   queue.
   * The order is guaranteed to be highest to lowest priority.
   *
   * @return an <code>iterator</code> over the elements in this priority
   *    queue
  */
  public Iterator<E> iterator()
  {return new ArrayUnsortedPriorityQueueIterator();}

  
   
  /**
   * Returns a string representation of this priority queue.
   * The string representation consists of a count of the number of
   *   elements in the priority queue, the priority queue's length, and
   *   a list of the priority queue's elements in the order they are
   *   returned by its iterator, enclosed in square brackets ("[]").
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
   * @return an array containing the elements of this priority queue
  */
  public String toString()
  {return "ArrayUnsortedPriorityQueue["+objectCount+"/"+pq.length+":"+super.toString()+"]";}


  /*
   * The backing array storing all the values in the priority queue, not
   *   in any particular order.
  */
  private E[] pq;

  
  /*
   * The prioritizer (see <code>add</code>).
  */
  private Comparator<? super E> priorityComparator;
  

  
  /*
   * The iterator for this queue implementation.
   * It uses its own array, sorted.
  */
  private class ArrayUnsortedPriorityQueueIterator implements Iterator<E> {
  
    @SuppressWarnings("unchecked")
		ArrayUnsortedPriorityQueueIterator ()
    {
      iteratorIndex    = objectCount-1;
      expectedModCount = modCount;
      copy = (E[]) new Object[size()];
      for (int i=0; i<objectCount; i++)
        copy[i] = pq[i];
      Arrays.sort(copy,priorityComparator);
    }
   
   
    public boolean hasNext()
    {return iteratorIndex != -1;}
   
    
    public E next()
      throws ConcurrentModificationException,NoSuchElementException
    {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (iteratorIndex == -1)
        throw new NoSuchElementException();
      return copy[iteratorIndex--];
    }
   
   
    public void remove()
      throws UnsupportedOperationException
    {throw new UnsupportedOperationException();}

    
    private int expectedModCount;
    private int iteratorIndex;
    private E[] copy;
  }
}
