package com.virtuozzo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KdTree<T> {

    private final Comparator<Node<T>> cmpX = new Comparator<Node<T>>() {

        @Override
        public int compare(Node<T> p1, Node<T> p2) {
            return Double.compare(p1.getX(), p2.getY());
        }
    };

    private final Comparator<Node<T>> cmpY = new Comparator<Node<T>>() {

        @Override
        public int compare(Node<T> p1, Node<T> p2) {
            return Double.compare(p1.getY(), p2.getY());
        }
    };

    private Node<T> root;

    public KdTree() {

    }

    public KdTree(List<Node<T>> nodes) {
        buildTree(nodes);
    }

    public void buildTree(List<Node<T>> nodes) {
        root = new Object() {
            Node<T> buildTree(boolean divX, List<Node<T>> nodes) {
                if (nodes == null || nodes.isEmpty())
                    return null;
                Collections.sort(nodes, divX ? cmpX : cmpY);
                int mid = nodes.size() >> 1;
                Node<T> node = new Node<>();
                node.coords = nodes.get(mid).coords;
                node.object = nodes.get(mid).object;
                node.left = buildTree(!divX, nodes.subList(0, mid));
                if (mid + 1 < nodes.size() - 1)
                    node.right = buildTree(!divX, nodes.subList(mid+1, nodes.size()));
                return node;
            }
        }.buildTree(true, nodes);
    }

    public T findNearestNeighbour(double x, double y) {
        return findNearestNeighbour(root, x, y, true);
    }

    private T findNearestNeighbour(Node<T> fromNode, final double x, final double y, boolean divX) {
        return new Object() {

            double closestDist = Double.POSITIVE_INFINITY;

            T closestNode = null;

            T findNearestNeighbour(Node<T> node, boolean divX) {
                if (node == null)
                    return null;
                double d = Point2D.distanceSq(node.coords.x, node.coords.y, x, y);
                if (closestDist > d) {
                    closestDist = d;
                    closestNode = node.object;
                }
                double delta = divX ? x - node.coords.x : y - node.coords.y;
                double delta2 = delta * delta;
                Node<T> node1 = delta < 0 ? node.left : node.right;
                Node<T> node2 = delta < 0 ? node.right : node.left;
                findNearestNeighbour(node1, !divX);
                if (delta2 < closestDist) {
                    findNearestNeighbour(node2, !divX);
                }
                return closestNode;
            }
        }.findNearestNeighbour(fromNode, divX);
    }


    public List<T> rangeSearch(final double x, final double y, final double dist) {
        return new Object() {
            List<T> result = new LinkedList<>();
            double radius = dist * dist;
            List<T> rangeSearch(Node<T> node, boolean divX) {
                if (node == null)
                    return result;
                double d = Point2D.distanceSq(node.coords.x, node.coords.y, x, y);

                if (radius >= d)
                    result.add(node.object);

                double delta = divX ? x - node.coords.x : y - node.coords.y;
                double delta2 = delta * delta;
                Node<T> node1 = delta < 0 ? node.left : node.right;
                Node<T> node2 = delta < 0 ? node.right : node.left;
                rangeSearch(node1, !divX);
                if (delta2 < radius) {
                    rangeSearch(node2, !divX);
                }
                return result;
            }
        }.rangeSearch(root, true);
    }

    public List<T> rangeSearchKilometers(final double x, final double y, final double dist) {
        return new Object() {
            List<T> result = new LinkedList<>();
            List<T> rangeSearch(Node<T> node, boolean divX) {
                if (node == null)
                    return result;
                double d = EarthDistance.calculate(node.coords.x, node.coords.y, x, y);
                if (dist >= d)
                    result.add(node.object);
                double delta = divX ? x - node.coords.x : y - node.coords.y;
                double delta2 = divX ? EarthDistance.calculate(x, 0, node.coords.x, 0)
                        : EarthDistance.calculate(0, y, 0, node.coords.y);
                Node<T> node1 = delta < 0 ? node.left : node.right;
                Node<T> node2 = delta < 0 ? node.right : node.left;
                rangeSearch(node1,!divX);
                if (delta2 < dist) {
                    rangeSearch(node2, !divX);
                }
                return result;
            }
        }.rangeSearch(root, true);
    }


    public List<T> findNearestNeighbours(final double x, final double y, final int limit) {
        final List<T> result = new LinkedList<>();
        final List<Node<T>> closestNodes  = new ArrayList<>();
        final Comparator<Node<T>> cmp = new Comparator<Node<T>>() {
            @Override
            public int compare(Node<T> node1, Node<T> node2) {
                Double p1 = Point2D.distanceSq(node1.coords.x, node1.coords.y, x, y);
                Double p2 = Point2D.distanceSq(node2.coords.x, node2.coords.y, x, y);
                return Double.compare(p1, p2);
            }
        };

        new Object() {
            double closestDist = Double.POSITIVE_INFINITY;
            int size = 0;

            void findNearestNeighbours(Node<T> node, boolean divX) {
                if (node == null)
                    return ;
                double d = Point2D.distanceSq(node.coords.x, node.coords.y, x, y);
                if (closestDist > d) {
                    closestDist = d;
                }
                closestNodes.add(node);
                size++;
                double delta = divX ? x - node.coords.x : y - node.coords.y;
                double delta2 = delta * delta;
                Node<T> node1 = delta < 0 ? node.left : node.right;
                Node<T> node2 = delta < 0 ? node.right : node.left;
                findNearestNeighbours(node1, !divX);
                if (delta2 < closestDist || size < limit) {
                    findNearestNeighbours(node2, !divX);
                }
            }

        }.findNearestNeighbours(root, true);

        Collections.sort(closestNodes, cmp);

        int cnt = 0;
        for(Node<T> node : closestNodes) {
            if (cnt >= limit)
                break;
            result.add(node.object);
            cnt++;
        }

        return result;
    }


    public void insert(T object, double x, double y) {
        Node<T> node = new Node<>(object, x, y);
        if (root == null)
            root = node;
        else
            insert(node, root, true);
    }

    private void insert(Node<T> node, Node<T> currentNode, boolean divX) {
        if (node == null)
            return;
        int cmpResult = (divX ? cmpX : cmpY).compare(node, currentNode);
        if (cmpResult == -1)
            if(currentNode.left == null)
                currentNode.left = node;
            else
                insert(node, currentNode.left, !divX);
        else
        if(currentNode.right == null)
            currentNode.right = node;
        else
            insert(node, currentNode.right, !divX);
    }

    public T findMin() {
        Node<T> node = findMin(root, true);
        if(node != null)
            return node.object;
        return null;
    }


    private Node<T> findMin(Node<T> node, boolean divX) {
        if (node == null)
            return null;
        if (divX) {
            if (node.left == null)
                return node;
            else
                return findMin(node.left, false);
        }
        else {
            List<Node<T>> list = new LinkedList<>();
            list.add(findMin(node, true));

            if(node.left != null)
                list.add(findMin(node.left, true));

            if(node.right != null)
                list.add(findMin(node.right, true));

            Collections.sort(list, cmpY);

            return list.get(0);

        }
    }

    public Node<T> fastDelete(final T target, double x, double y) {
        root = new Object() {
            Node<T> delete(T targetObject, double x, double y, Node<T> node, boolean divX) {
                if(node == null)
                    return null;
                if (targetObject.equals(node.object)) {
                    if(node.right != null) {
                        Node<T> minNode = findMin(node.right, !divX);
                        node.object = minNode.object;
                        node.coords = minNode.coords;
                        node.right = delete(node.object, node.getX(), node.getY(), node.right, !divX);
                    }
                    else if (node.left != null) {
                        Node<T> minNode = findMin(node.left, !divX);
                        node.object = minNode.object;
                        node.coords = minNode.coords;
                        node.left = delete(node.object, node.getX(), node.getY(), node.left, !divX);
                    }
                    else
                        node = null;
                }
                else {
                    double delta = divX ? x - node.coords.x : y - node.coords.y;
                    if(delta < 0)
                        node.left = delete(targetObject, x, y, node.left, !divX);
                    else
                        node.right = delete(targetObject, x, y, node.right, !divX);
                }
                return node;
            }
        }.delete(target, x, y, root, true);

        return root;
    }

    public Node<T> delete(final T target) {
        root = new Object() {
            Node<T> delete(T targetObject, Node<T> node, boolean divX) {
                if(node == null)
                    return null;
                if (targetObject.equals(node.object)) {
                    if(node.right != null) {
                        Node<T> minNode = findMin(node.right, !divX);
                        node.object = minNode.object;
                        node.coords = minNode.coords;
                        node.right = delete(node.object, node.right, !divX);
                    }
                    else if (node.left != null) {
                        Node<T> minNode = findMin(node.left, !divX);
                        node.object = minNode.object;
                        node.coords = minNode.coords;
                        node.left = delete(node.object, node.left, !divX);
                    }
                    else
                        node = null;
                }
                else {
                    node.left = delete(targetObject, node.left, !divX);
                    node.right = delete(targetObject, node.right, !divX);
                }
                return node;
            }
        }.delete(target, root, true);

        return root;
    }


    public void insertOrUpdate(T target, double x, double y) {
        delete(target);
        insert(target, x, y);
    }

    public int getSize() {
        return new Object() {
            int cnt = 0;
            int getSize(Node<T> node) {
                if (node == null)
                    return cnt - 1;
                if (node.left != null) {
                    cnt++;
                    getSize(node.left);
                }
                if (node.right != null) {
                    cnt++;
                    getSize(node.right);
                }
                return cnt;
            }
        }.getSize(root) + 1;
    }

    public List<T> getAll() {
        final List<T> result = new LinkedList<>();
        new Object() {
            void fillList(Node<T> node) {
                if(node == null)
                    return;
                result.add(node.getObject());
                if(node.left != null)
                    fillList(node.left);
                if(node.right != null)
                    fillList(node.right);
            }
        }.fillList(root);
        return result;
    }

}