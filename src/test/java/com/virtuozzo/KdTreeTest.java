package com.virtuozzo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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


    @Test
    public void testFastDelete() {
        List<Node<Object>> nodes = new ArrayList<>();

        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        KdTree<Object> tree = new KdTree<>(nodes);

        assertTrue(tree.findNearestNeighbour(10, 10).equals(obj2));
        tree.fastDelete(obj2, node2.getX(), node2.getY());
        assertTrue(!tree.findNearestNeighbour(10, 10).equals(obj2));

        assertTrue(tree.findNearestNeighbour(0, 10).equals(obj3));
        tree.fastDelete(obj3, node3.getX(), node3.getY());
        assertTrue(!tree.findNearestNeighbour(0, 10).equals(obj3));

        assertTrue(tree.findNearestNeighbour(10, 0).equals(obj4));
        tree.fastDelete(obj4, node4.getX(), node4.getY());
        assertTrue(!tree.findNearestNeighbour(10, 0).equals(obj4));

    }

    @Test
    public void testDelete() {
        KdTree<Object> tree = new KdTree<>();
        tree.insert(obj1, 0, 0);
        tree.insert(obj2, 10, 10);
        tree.insert(obj3, 0, 10);
        tree.insert(obj4, 10, 0);
        tree.insert(obj5, 20, 20);

        tree.delete(obj3);
        assertEquals(4, tree.getSize());

        tree.delete(obj1);
        assertEquals(3, tree.getSize());

        tree.delete(obj4);
        assertEquals(2, tree.getSize());

        tree.delete(obj2);
        assertEquals(1, tree.getSize());

        assertEquals(obj5, tree.findNearestNeighbour(0, 0));

    }

    @Test
    public void testUpdate() {
        KdTree<Object> tree = new KdTree<>();

        tree.insert(obj1, 0, 0);
        tree.insert(obj2, 10, 10);

        assertEquals(obj1, tree.findNearestNeighbour(0, 0));

        tree.insertOrUpdate(obj1, 50, 50);
        assertEquals(obj2, tree.findNearestNeighbour(0, 0));

        tree.insertOrUpdate(obj5, 20, 20);
        assertEquals(obj5, tree.findNearestNeighbour(20, 20));

        tree.insertOrUpdate(obj5, 60, 60);
        assertNotSame(obj5, tree.findNearestNeighbour(20, 20));


    }


    public boolean isTheSame(List<Object> etalon, List<Object> target) {
        if (etalon.size() != target.size())
            return false;
        for(int i = 0; i<target.size(); i++) {
            if (!etalon.get(i).equals(target.get(i)))
                return false;
        }
        return true;
    }

    @Test
    public void testFindNearestNeighbours() {
        KdTree<Object> tree = new KdTree<>();
        tree.insert(obj3, 10, 10);
        tree.insert(obj2, 0, 5);
        tree.insert(obj1, 0, 0);
        tree.insert(obj5, 100, 100);
        tree.insert(obj4, 50, 50);


        assertEquals(5, tree.getSize());

        List<Object> result = tree.findNearestNeighbours(0, 0, 1);
        assertEquals(1, result.size());
        List<Object> etalon = new LinkedList<>();
        etalon.add(obj1);
        assertTrue(isTheSame(etalon, result));


        result = tree.findNearestNeighbours(0, 0, 2);
        assertEquals(2, result.size());
        etalon = new LinkedList<>();
        etalon.add(obj1);
        etalon.add(obj2);
        assertTrue(isTheSame(etalon, result));

        result = tree.findNearestNeighbours(0, 0, 3);
        assertEquals(3, result.size());
        etalon = new LinkedList<>();
        etalon.add(obj1);
        etalon.add(obj2);
        etalon.add(obj3);
        assertTrue(isTheSame(etalon, result));

        result = tree.findNearestNeighbours(0, 0, 4);
        assertEquals(4, result.size());
        etalon = new LinkedList<>();
        etalon.add(obj1);
        etalon.add(obj2);
        etalon.add(obj3);
        etalon.add(obj4);
        assertTrue(isTheSame(etalon, result));


        result = tree.findNearestNeighbours(0, 0, 5);
        assertEquals(5, result.size());
        etalon = new LinkedList<>();
        etalon.add(obj1);
        etalon.add(obj2);
        etalon.add(obj3);
        etalon.add(obj4);
        etalon.add(obj5);
        assertTrue(isTheSame(etalon, result));

    }


    @Test
    public void testDistanceCalculator() {
        double EPS = 1e-2;

        double mephiLat = 55.649371;
        double mephiLon = 37.663750;
        double metroLat = 55.655132;
        double metroLon = 37.648986;

        double distance = EarthDistance.calculate(mephiLat, mephiLon, metroLat, metroLon);
        double rightAnswer = 1.12;

        assertTrue(Math.abs(distance - rightAnswer) < EPS);

    }


    @Test
    public void testRangeSearchKm() {
        KdTree<Object> tree = new KdTree<>();

        double posLat1 = 55.649371;
        double posLon1 = 37.663750;
        double posLat2 = 55.655132;
        double posLon2 = 37.648986;

        tree.insert(new Object(), posLat1, posLon1);

        assertEquals(0, tree.rangeSearchKilometers(posLat2, posLon2, 1.0).size());
        assertEquals(1, tree.rangeSearchKilometers(posLat1, posLon1, 1.3).size());

    }


}
