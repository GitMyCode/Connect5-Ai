package astar.data_structure;

import java.util.Iterator;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;



/**
 * This class implements the <code>OrderedCollection</code> interface
 *   with FIFO behavior: first in/first out.
 * It is backed by a linked list.
 * It makes a guarantee that the iteration order is FIFO.
 * This class permits the <code>null</code> element.
 * This class offers constant time performance for the basic operations
 *   (<code>add</code>, <code>remove</code>, <code>peekNext</code>, and
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
public class LinkedQueue<E> extends AbstractQueue<E> {


   /** 
    Constructs a new, empty queue.
  */
  public LinkedQueue ()
  {}
  
  

  /**
   *Constructs a new queue containing the elements in the specified
   *   ordered collection.
   *
   * @param o - the ordered collection whose elements are to be placed
   *          into this queue.
  */
  @SuppressWarnings("unchecked")
	public LinkedQueue (OrderedCollection<E> o)
  {this((E[])o.toArray());}
  
  
  
  /**
   *Constructs a new queue containing the elements in the specified
   *   collection.
   *
   * @param c - the collection whose elements are to be placed into this
   *             queue.
  */
  @SuppressWarnings("unchecked")
	public LinkedQueue (Collection<E> c)
  {this((E[])c.toArray());}
  
  

  /**
   *Constructs a new queue containing the elements in the specified
   *   array.
   *
   * @param o - the array whose elements are to be placed into this
   *             queue.
  */
  public LinkedQueue (E[] o)
  {
    for (E e : o)
      add(e);
  }
  
 
  
  /**
   * Removes all of the elements from this queue.
   * This queue will be empty after this method returns.
   * This method overrides the one in
   *   <code>AbstractOrderedCollection</code> and is more efficient.
  */
  public void clear ()
  {
    modCount++;
    objectCount = 0;
    first = last = null;
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
    super.add(o);
    if (first == null)
      first = last = new QueueNode<E>(o);
    else
      last = last.next = new QueueNode<E>(o);
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
    E answer = first.value;
    first = first.next;
    return answer;
  }

  

  /**
   * Returns (without removing) the next elements in this queue,
   *   according to FIFO behavior.
   *
   * @return the next element in this queue, according
   *   to FIFO behavior.
   *
   * @throws NoSuchElementException if the queue is empty
  */
  public E peek()
    throws NoSuchElementException
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return first.value;
  }



  /**
   * Returns an <code>iterator</code> over the elements in this queue.
   * The ordered is guaranteed to exhibit FIFO behavior.
   *
   * @return an <code>iterator</code> over the elements in this queue
  */
  public Iterator<E> iterator()
  {return new LinkedQueueIterator();}
  
   

  /**
   * Returns a string representation of this queue.
   * The string representation consists of a count of the number of
   *   elements in the queue and a list of the
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
  {return "LinkedQueue["+objectCount+":"+super.toString()+"]";}




  /*
   * A reference to the first element in the queue.
  */
  private QueueNode<E> first;


  /*
   * A reference to the last element in the queue.
  */
  private QueueNode<E> last;   //access only if first != null
  

  /*
   * Nested class for queue's linked list.
  */
  private static class QueueNode<E> {
    E            value;
    QueueNode<E> next  = null;
    
    QueueNode (E v)
    {value = v;}
    
    public String toString()
    {return ""+value;}
  }
      
  
  
  /*
   * The iterator for this queue implementation.
  */
  private class LinkedQueueIterator implements Iterator<E> {
  
    LinkedQueueIterator ()
    {
      iteratorCursor   = first;
      expectedModCount = modCount;
    }
   
    public boolean hasNext()
    {return iteratorCursor != null;}
   
    
    public E next()
      throws ConcurrentModificationException,NoSuchElementException
    {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (iteratorCursor == null)
        throw new NoSuchElementException();
      E answer = iteratorCursor.value;
      iteratorCursor = iteratorCursor.next;
      return answer;
    }
   
   
    public void remove()
      throws UnsupportedOperationException
    {throw new UnsupportedOperationException();}
    
    private int          expectedModCount;
    private QueueNode<E> iteratorCursor;
  }
}
