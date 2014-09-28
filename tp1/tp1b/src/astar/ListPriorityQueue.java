package astar;
import java.util.*;

public class ListPriorityQueue<E> extends LinkedHashSet<E> {

    private Node head;
    private int size;

    private CompareEtat compareEtat = new CompareEtat();

    private class Node {
        private E value;
        private Node next;
        public Node(E value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private class NodeIter implements Iterator<E> {
        private E[] arr;
        private int n;

        public NodeIter() {
            n = size;
            arr = (E[]) new Object[n];
            Node p = head;
            for (int i = 0; i < n; ++i) {
                arr[i] = p.value;
                p = p.next;
            }
        }
        public boolean hasNext() {
            return n > 0;
        }
        public E next() {
            if (n == 0)
                throw new NoSuchElementException();
            return arr[--n];
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public ListPriorityQueue() {
        this.head = null;
        this.size = 0;
    }

    public boolean insert(E object) {
        Etat etat = (Etat) object;

        if (isFull())
            return false;
        if (head == null) {
            head = new Node(object, null);
        }
        else if(compareEtat.compare(etat,(Etat)head.value) < 0){

            head = new Node(object, head);
        }else {
             Node p = head;
            while (p.next != null && (compareEtat.compare(etat,(Etat)p.next.value) >= 0)) { // Comparable<E>)object).compareTo(p.next.value) >= 0) {
                p = p.next; //or equal to preserve FIFO on equal items
            }
            p.next = new Node(object, p.next);
        }
        ++size;
        return true;

       /*
        else if (((Comparable<E>)object).compareTo(head.value) < 0) {
            head = new Node(object, head);
        }
        else {
            Node p = head;
            while (p.next != null && ((Comparable<E>)object).compareTo(p.next.value) >= 0) {
                p = p.next; //or equal to preserve FIFO on equal items
            }
            p.next = new Node(object, p.next);
        }
        ++size;
        return true;*/
    }

    public E remove() {
        if (isEmpty())
            return null;
        E value = head.value;
        head = head.next;
        --size;
        return value;
    }

    public E peek() {
        if (isEmpty())
            return null;
        return head.value;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        Node p = head;
        while (p != null) {
            if(compareEtat.compare((Etat) o,(Etat) p.value) ==0){
                return true;
            }
            p = p.next;

            /*if (((Comparable<E>) o).compareTo(p.value) == 0)
                return true;
            p = p.next;*/
        }
        return false;
    }
/*

    @Override
    public boolean contains(E object) {
        Node p = head;
        while (p != null) {
            if (((Comparable<E>)object).compareTo(p.value) == 0)
                return true;
            p = p.next;
        }
        return false;
    }
*/

    public Iterator<E> iterator() {
        if (isEmpty())
            return null;
        return new NodeIter();
    }

    public void clear() {
        head = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return false; //size == DEFAULT_MAX_CAPACITY;
    }

    private class CompareEtat implements Comparator<Etat> {

        @Override
        public int compare(Etat a, Etat b){


            if(a.f < b.f){
                return -1;
            }
            if( a.f > b.f){
                return 1;
            }

            if(a.equals(b)){
                return 0;
            }
            return a.compareTo(b);


        }
    }

}
