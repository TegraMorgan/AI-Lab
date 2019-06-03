package com.AILab5.Entity;
import java.util.Iterator;

/**
    Set only for values between 0 to n - 1.
    Worst case time complexity of each one of the operations is O(1).
    Can be used as a Colors-Domain for the arc consistency algorithm.
 */
public class RangeSet implements Iterable<Integer>
{
    private int count;
    private Node[] nodes;
    private final Node list;

    public RangeSet (int n, boolean isFull)
    {
        if (n <= 1)
        {
            System.err.println("n must be bigger than 1 (n = " + n + ").");
            throw new IllegalArgumentException();
        }
        nodes = new Node[n];
        list = new Node(null, -1, new Node(-1));
        count = 0;
        if (isFull)
        {
            for (int i = 0; i < n; i++)
            {
                add(i);
            }
        }
    }
    public int size()
    {
        return count;
    }
    public boolean contains(int value)
    {
        return nodes[value] != null;
    }
    public void add(int value)
    {
        if (nodes[value] == null)
        {
            nodes[value] = new Node(list, value, list.next);
            count++;
        }
    }
    public void remove(int value)
    {
        Node node = nodes[value];
        if(node != null)
        {
            node.previous.next = node.next;
            node.next.previous = node.previous;
            nodes[value] = null;
            count--;
        }
    }
    public Iterator<Integer> iterator()
    {
        return new SetIterator(this);
    }
    private class Node
    {
        int value;
        Node previous, next;
        Node (int value)
        {
            this.value = value;
        }
        Node (Node p, int value, Node n)
        {
            this.previous = p;
            this.value = value;
            this.next = n;
            if (n != null)
            {
                n.previous = this;
            }
            if (p != null)
            {
                p.next = this;
            }
        }
    }
    private class SetIterator implements Iterator<Integer>
    {
        Node iterator;
        final RangeSet set;
        SetIterator (RangeSet set)
        {
            iterator = set.list;
            this.set = set;
        }
        public boolean hasNext()
        {
            return iterator.next.value != -1;
        }
        public Integer next()
        {
            return (iterator = iterator.next).value;
        }
        public void remove()
        {
            set.remove(iterator.value);
        }
    }
}