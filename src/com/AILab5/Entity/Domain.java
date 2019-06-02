package com.AILab5.Entity;

import java.util.Arrays;
import java.util.Iterator;

public class Domain implements Iterable<Integer>
{
    private int count;
    private ColorNode[] nodes;
    private final ColorNode list;

    public Domain(int n)
    {
        if (n <= 0)
        {
            System.err.println("n must be positive (n = " + n + ").");
        }
        nodes = new ColorNode[n];
        Arrays.setAll(nodes, ColorNode::new);
        count = n;
        if(n > 1)
        {
            nodes[0].next = nodes[1];
            nodes[n - 1].previous = nodes[n - 2];
            for (int i = n - 2; i >= 1; i--)
            {
                nodes[i].previous = nodes[i - 1];
                nodes[i].next = nodes[i + 1];
            }
        }
        list = new ColorNode(null, -1, nodes[0]); //set first node to -1
        new ColorNode(nodes[n - 1], -1, null); //set last node to -1
    }
    public int size()
    {
        return count;
    }
    public boolean contains(int c)
    {
        return nodes[c] != null;
    }
    public void add(int c)
    {
        if (nodes[c] == null)
        {
            nodes[c] = new ColorNode(list, c, list.next);
            count++;
        }
    }
    public void remove(int color)
    {
        ColorNode node = nodes[color];
        if(node != null)
        {
            node.previous.next = node.next;
            node.next.previous = node.previous;
            nodes[color] = null;
            count--;
        }
    }
    public Iterator<Integer> iterator()
    {
        return new DomainIterator(this);
    }
    private class ColorNode
    {
        int color;
        ColorNode previous, next;
        ColorNode(int color)
        {
            this.color = color;
        }
        ColorNode(ColorNode p, int color, ColorNode n)
        {
            this.previous = p;
            this.color = color;
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
    private class DomainIterator implements Iterator<Integer>
    {
        ColorNode iterator;
        final Domain domain;
        DomainIterator (Domain domain)
        {
            iterator = domain.list;
            this.domain = domain;
        }
        public boolean hasNext()
        {
            return iterator.next.color != -1;
        }
        public Integer next()
        {
            return (iterator = iterator.next).color;
        }
        public void remove()
        {
            domain.remove(iterator.color);
        }
    }
}