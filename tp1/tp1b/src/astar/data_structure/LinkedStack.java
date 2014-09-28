package astar.data_structure;

import java.util.Iterator;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;




/**
 * This class implements the <code>OrderedCollection</code> interface
 *   with LIFO behavior: last in/first out.
 * It is backed by a linked list.
 * It makes a guarantee that the iteration order is LIFO.
 * This class permits the <code>null</code> element.
 * This class offers constant time performance for the basic operations
 *   (<code>add</code>, <code>remove</code>, <code>peek</code>, and
 *   <code>size</code>).
 * Iterating over this set requires time proportional to the size of the
 *    stack.
 * <p>
 * Note that this implementation is not synchronized.
 * If multiple threads access a stack concurrently, and at least one of
 *   the threads modifies the stack, it must be synchronized externally.
 * This is typically accomplished by synchronizing on some object that
 *   naturally encapsulates the stack.
 * <p>
 * The iterators returned by this class's iterator method are fail-fast:
 *   if the stack is modified at any time after the iterator is created,
 *   in any way, the Iterator throws a
 *   <code>ConcurrentModificationException</code>.
 * Thus, in the face of concurrent modification, the iterator fails
 *   quickly and cleanly, rather than risking arbitrary,
 *   non-deterministic behavior at an undetermined time in the future. 
*/
public class LinkedStack<E> extends AbstractStack<E> {
    
  
  /** 
    Constructs a new, empty stack.
  */
  public LinkedStack ()
  {}
  
  

  /**
   *Constructs a new stack containing the elements in the specified
   *   ordered collection.
   *
   * @param o - the ordered collection whose elements are to be placed
   *          into this stack.
  */
  @SuppressWarnings("unchecked")
	public LinkedStack (OrderedCollection<E> o)
  {this((E[])o.toArray());}
  
  

  /**
   *Constructs a new stack containing the elements in the specified
   *   collection.
   *
   * @param c - the collection whose elements are to be placed into this
   *             stack.
  */
  @SuppressWarnings("unchecked")
	public LinkedStack (Collection<E> c)
  {this((E[])c.toArray());}
  
  
  
  /**
   *Constructs a new stack containing the elements in the specified
   *   array.
   *
   * @param o - the array whose elements are to be placed into this
   *             stack.
  */
  public LinkedStack (E[] o)
  {
    // Allow 10% room for growth
    for (E e : o)
      add(e);
  }
  
  
  
  /**
   * Removes all of the elements from this stack.
   * This stack will be empty after this method returns.
   * This method overrides the one in
   *   <code>AbstractOrderedCollection</code> and is more efficient.
  */
  public void clear ()
  {
    modCount++;
    objectCount = 0;
    top = null;
  }
  

  
  /**
   * Ensures that this stack contains the specified element. 
   * Returns <code>true</code> if this collection changed as a result of the
   *   call. 
   * This always happens because stacks allow duplicates.
   *
   * @param o - the element to store in the stack
   *
   * @return <code>true</code> if this stack changed as a
   *   result of the call. 
  */
  public boolean add (E o)
  {
    super.add(o);
    top = new StackNode<E>(o,top);
    return true;
  }



  /**
   * Returns/removes the next element in this stack, according
   *   to LIFO behavior.
   *
   * @return the next element in this stack, according
   *   to LIFO behavior
   *
   * @throws NoSuchElementException if the stack is empty
  */
  public E remove ()
  {
    super.remove();
    E answer = top.value;
    top = top.next;
    return answer;
  }



  /**
   * Returns (without removing) the next elements in this stack,
   *   according to LIFO behavior.
   *
   * @return the next elements in this stack,
   *   according to LIFO behavior.
   *
   * @throws NoSuchElementException if the stack is empty
  */
  public E peek ()
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return top.value;
  }



  /**
   * Returns an <code>iterator</code> over the elements in this stack.
   * The order is guaranteed to be exhibit LIFO behavior.
   *
   * @return an <code>iterator</code> over the elements in this stack
  */
  public Iterator<E> iterator()
  {return new LinkedStackIterator();}
  
   

  /**
   * Returns a string representation of this stack.
   * The string representation consists of a count of the number of
   *   elements in the stack and a list of the
   *   stack's elements in the order they are returned by its iterator,
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
   * @return an array containing the elements of this stack
  */
  public String toString()
  {return "LinkedStack["+objectCount+":"+super.toString()+"]";}



  /*
   * A reference to the top element in the stack.
  */
  private StackNode<E> top;


  /*
   * Nested class for queue's linked list.
  */
  private static class StackNode<E> {
    E            value;
    StackNode<E> next;
    
    StackNode (E v, StackNode<E> n)
    {
      value = v;
      next  = n;
    }
    
    public String toString()
    {return ""+value;}
  }



  /*
   * The iterator for this stack implementation.
  */
  private class LinkedStackIterator implements Iterator<E> {
  
    LinkedStackIterator ()
    {
      iteratorCursor   = top;
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
    private StackNode<E> iteratorCursor;
  }
}
