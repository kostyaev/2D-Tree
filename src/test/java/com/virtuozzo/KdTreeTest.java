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
    
    Object obj1;
    Object obj2;
    Object obj3;
    Object obj4;
    Object obj5;  
    

    @Before
    public void initGuards() {
        obj1 = new Object();
        obj2 = new Object();
        obj3 = new Object();
        obj4 = new Object();
        obj5 = new Object(); 

        node1 = new Node<>(obj1, 0, 0);
        node2 = new Node<>(obj2, 10, 10);
        node3 = new Node<>(obj3, 0, 10);
        node4 = new Node<>(obj4, 10, 0);
        node5 = new Node<>(obj5, 20, 20);

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

    @Test
    public void testInsert() {
        KdTree<Object> tree = new KdTree<>();
        tree.insert(obj1, 0, 0);
        tree.insert(obj2, 10, 10);
        tree.insert(obj3, 0, 10);
        tree.insert(obj4, 10, 0);
        tree.insert(obj5, 20, 20);

        Object node = tree.findNearestNeighbour(0, 0);
        assertEquals(obj1, node);

        node = tree.findNearestNeighbour(8, 8);
        assertEquals(obj2, node);

        node = tree.findNearestNeighbour(0, 8);
        assertEquals(obj3, node);

        node = tree.findNearestNeighbour(9, 0);
        assertEquals(obj4, node);

        node = tree.findNearestNeighbour(20, 20);
        assertEquals(obj5, node);

    }


    @Test
    public void testRangeSearch() {
        KdTree<Object> tree = new KdTree<>();

        tree.insert(obj1, 0, 0);
        tree.insert(obj3, 0, 10);
        tree.insert(obj5, 20, 20);

        List<Object> result = tree.rangeSearch(0, 0, 9);
        assertEquals(1, result.size());

        result = tree.rangeSearch(0, 0, 10);
        assertEquals(2, result.size());


        result = tree.rangeSearch(0, 0, 30);
        assertEquals(3, result.size());
    }
}
