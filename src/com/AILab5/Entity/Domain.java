package com.AILab5.Entity;

import java.util.Arrays;
import java.util.Iterator;

public class Domain implements Iterable<Integer>
{
    private int count = 0;
    private ColorNode[] nodes;
    private final ColorNode list;

    public Domain(int nColors)
    {
        if(nColors <= 1)
        {
            System.err.println("nColors must be bigger than 1 (nColors = " + nColors + ").");
        }
        nodes = new ColorNode[nColors];
        Arrays.setAll(nodes, ColorNode::new);
        nodes[0].next = nodes[1];
        nodes[nColors - 1].previous = nodes[nColors - 2];
        for (int i = nColors - 2; i >= 1; i--)
        {
            nodes[i].previous = nodes[i - 1];
            nodes[i].next = nodes[i + 1];
        }
        list = new ColorNode(null, -1, nodes[0]);
    }
    public int size()
    {
        return count;
    }
    public boolean contains(int color)
    {
        return nodes[color] != null;
    }
    public void add(int color)
    {
        if (nodes[color] == null)
        {
            nodes[color] = new ColorNode(list, color, list.next);
            count++;
        }
    }
    public void remove(int color)
    {
        ColorNode node = nodes[color];
        if(node != null)
        {
            node.previous.next = node.next;
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
            return iterator.next != null;
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