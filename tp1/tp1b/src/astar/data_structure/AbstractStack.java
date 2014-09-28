


package astar.data_structure;

import java.util.Iterator;



/**
 * This class extends a skeletal implementation of
 *   AbstractOrderedCollection with the equals method.
*/
public abstract class AbstractStack<E> extends AbstractOrderedCollection<E> {

  /**
   * Sole constructor. (For invocation by subclass constructors,
   *   typically implicit.)
  */
  protected AbstractStack()
  {}
  
  

  /**
   * Compares the specified object with this stack for equality.
   * Returns <code>true</code> if and only if the specified object is
   *   also a stack, both stacks have the same size, and all
   *  corresponding pairs of elements in the two stacks are equal.
   *  (Two elements e1 and e2 are equal if
   *  <pre><b>  (e1==null ? e2==null : e1.equals(e2))</pre></b>
   *  In other words, two stacks are defined to be equal if they
   *    contain the same elements in the same order.
   * <p>
   * This implementation first checks if the specified object is this
   *   stack.
   * If so, it returns <code>true</code>; if not, it checks if the
   *   specified object is a stack.
   * If not, it returns <code>false</code>; if so, it iterates over
   *    both stacks, comparing corresponding pairs of elements.
   * If any comparison returns <code>false</code>, this method
   *   returns <code>false</code>.
   * Ootherwise it returns <code>true</code> when the iterations
   *   complete.
   *
   * @return <code>true</code> if the specified object is equal to this
   *         stack.
  */
  public boolean equals(Object o) {
	  if (o == this)
	     return true;
	  if (!(o instanceof AbstractStack))
	    return false;

	  Iterator<E> e1 = iterator();
	  Iterator    e2 = ((AbstractStack) o).iterator();
	  if ( size() != ((AbstractStack) o).size() )
	    return false;
	    
	  while(e1.hasNext() && e2.hasNext()) {
	    E      o1 = e1.next();
	    Object o2 = e2.next();
	    if (!(o1==null ? o2==null : o1.equals(o2)))
		    return false;
	  }
	 
	  return true;
  }


  
}
