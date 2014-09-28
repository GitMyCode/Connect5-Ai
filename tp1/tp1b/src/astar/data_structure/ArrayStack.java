package astar.data_structure;

import java.util.Iterator;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;



/**
 * This class implements the <code>OrderedCollection</code> interface
 *   with LIFO behavior: last in/first out.
 * It is backed by an array.
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
public class ArrayStack<E> extends AbstractStack<E> {
    
  
  /**
   * Constructs an empty, new stack with the capacity specified.
   *
   * @param initialCapacity - the size of the initial backing array.
   *
   * @throws IllegalArgumentException if <code>initialCapacity&lt;0</code>
  */
  @SuppressWarnings("unchecked")
	public ArrayStack (int initialCapacity)
  {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Capacity: "+  initialCapacity);
    stack = (E[])new Object[initialCapacity];
  }
  
  

  /** 
    Constructs a new, empty stack.
  */
  public ArrayStack ()
  {this(1);}
  
  

  /**
   *Constructs a new stack containing the elements in the specified
   *   ordered collection.
   *
   * @param o - the ordered collection whose elements are to be placed
   *          into this stack.
  */
  @SuppressWarnings("unchecked")
	public ArrayStack (OrderedCollection<? extends E> o)
  {this((E[])o.toArray());}
  
  

  /**
   *Constructs a new stack containing the elements in the specified
   *   collection.
   *
   * @param c - the collection whose elements are to be placed into this
   *             stack.
  */
  @SuppressWarnings("unchecked")
	public ArrayStack (Collection<? extends E> c)
  {this((E[])c.toArray());}
  
  
  
  /**
   *Constructs a new stack containing the elements in the specified
   *   array.
   *
   * @param o - the array whose elements are to be placed into this
   *             stack.
  */
  @SuppressWarnings("unchecked")
	public ArrayStack (E[] o)
  {
    // Allow 10% room for growth
    stack = (E[])new Object[(int)Math.min((o.length*110L)/100,Integer.MAX_VALUE)]; 
    for (E e : o)
      add(e);
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
    ensureCapacity(objectCount+1);
    super.add(o);
    stack[objectCount-1] = o;
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
    E answer           = stack[objectCount];
    stack[objectCount] = null;
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
    return stack[objectCount-1];
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
	  int oldCapacity = stack.length;
	  if (minCapacity > oldCapacity) {
	    Object old[] = stack;
	    int newCapacity = (oldCapacity * 3)/2 + 1;
    	if (newCapacity < minCapacity)
		    newCapacity = minCapacity;
	    stack = (E[])new Object[newCapacity];
	    System.arraycopy(old,0,stack,0,objectCount);
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
	  int oldCapacity = stack.length;
	  if (objectCount < oldCapacity) {
	    Object old[] = stack;
	    stack = (E[])new Object[objectCount];
	    System.arraycopy(old,0,stack,0,objectCount);
    }
	}
  
  
  
  /**
   * Returns an <code>iterator</code> over the elements in this stack.
   * The order is guaranteed to be exhibit LIFO behavior.
   *
   * @return an <code>iterator</code> over the elements in this stack
  */
  public Iterator<E> iterator()
  {return new ArrayStackIterator();}
  
   

  /**
   * Returns a string representation of this stack.
   * The string representation consists of a count of the number of
   *   elements in the stack, the stack's length, and a list of the
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
  public String toString() {
    return "ArrayStack["+objectCount+"/"+stack.length+":"+super.toString()+"]";
  }



  /*
   * The backing array storing all the values in the stack.
  */
  private E[] stack;
  

  
  /*
   * The iterator for this queue implementation.
  */
  private class ArrayStackIterator implements Iterator<E> {
  
    ArrayStackIterator ()
    {
      iteratorIndex    = objectCount-1;
      expectedModCount = modCount;
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
      return stack[iteratorIndex--];
    }
   
   
     public void remove()
       throws UnsupportedOperationException
     {throw new UnsupportedOperationException();}
    
    private int expectedModCount;
    private int iteratorIndex;
  }
}
