package com.virtuozzo;

import java.awt.geom.Point2D;


public class Node<T> {

    protected Point2D.Double coords;

    protected Node<T> left;

    protected Node<T> right;

    protected T object;

    public Node() {

    }

    public Node(T object, double x, double y) {
        this.coords = new Point2D.Double(x,y);
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }


    public Point2D.Double getCoords() {
        return coords;
    }

    public Double getX() {
        return coords.x;
    }

    public Double getY() {
        return coords.y;
    }


    @Override
    public String toString() {
        return "Node{" +
                "coords=" + coords +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}

