package com.virtuozzo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class KdTreeTest {


    Node<Object> node1;
    Node<Object> node2;
    Node<Object> node3;
    Node<Object> node4;
    Node<Object> node5;


    @Before
    public void initGuards() {

        node1 = new Node<>(new Object(), 0, 0);
        node2 = new Node<>(new Object(), 10, 10);
        node3 = new Node<>(new Object(), 0, 10);
        node4 = new Node<>(new Object(), 10, 0);
        node5 = new Node<>(new Object(), 20, 20);

    }

    @Test
    public void testBuildTree1() {
        List<Node<Object>> nodes = new ArrayList<>();
        
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        KdTree<Object> tree = new KdTree<>(nodes);

        Object Object = tree.findNearestNeighbour(0, 0);
        assertEquals(node1.getObject(), Object);

        Object = tree.findNearestNeighbour(8, 8);
        assertEquals(node2.getObject(), Object);

        Object = tree.findNearestNeighbour(0, 8);
        assertEquals(node3.getObject(), Object);

        Object = tree.findNearestNeighbour(9, 0);
        assertEquals(node4.getObject(), Object);

        Object = tree.findNearestNeighbour(20, 20);
        assertEquals(node5.getObject(), Object);

    }
}
