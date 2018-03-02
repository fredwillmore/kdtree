import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {

    private Node root;              // root of BST
    private Point2D nearest = null; // used in nearest()

    private class Node {
        private Point2D point;
        private int size;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            if (point != null)
                this.point = point;
        }

        private int pointCompare(Point2D p1, Point2D p2, int level) {
            if (level % 2 == 0)
                // compare by y
                return Double.compare(p1.y(), p2.y());
            else
                // compare by x
                return Double.compare(p1.x(), p2.x());
        }

        private boolean pointEquals(Point2D p1, Point2D p2) {
            return p1.x() == p2.x() && p1.y() == p2.y();
        }

        private Node insert(Point2D p, int level) {
            Node node;
            size++;
            if (point == null) {
                point = p;
                node = this;
            } else if (pointCompare(p, point, level) < 0) {
                if (left == null)
                    left = new Node(null);
                node = left.insert(p, level+1);
            } else {
                if (right == null)
                    right = new Node(null);
                node = right.insert(p, level+1);
            }
            return node;
        }

        private boolean contains(Point2D p, int level ) {
            if (point == null)
                return false;
            if (pointEquals(point, p))
                return true;
            else if (pointCompare(p, point, level) < 0)
                if (left == null)
                    return false;
                else
                    return this.left.contains(p, level+1);
            else
                if (right == null)
                    return false;
                else
                    return this.right.contains(p, level+1);
        }

        private void draw() {
            point.draw();
            if(left != null)
                left.draw();
            if(right != null)
                right.draw();
        }
    }

    public KdTree() {
    }

    public boolean isEmpty() {                      // is the set empty?
        return root == null;
    }

    public int size() {                         // number of points in the set
        return isEmpty() ? 0 : root.size;
    }

    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        if(root == null)
            root = new Node(null);
        if (contains(p))
            return;
        root.insert(p, 1);

    }

    public boolean contains(Point2D p) {            // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        if (root == null)
            return false;
        return root.contains(p, 1);
    }

    public void draw() {                         // draw all points to standard draw
        if (root == null)
            return;
        root.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        pointsInRange(root, rect, list);
        return list;
    }

    private void pointsInRange(Node localRoot, RectHV rect, ArrayList<Point2D> list) {
        if (localRoot == null)
            return;
        if (rect.contains(localRoot.point))
            list.add(localRoot.point);
        // TODO: this is still a brute force algorithm - need to implement the following rule for deciding which tree(s) to follow:
        // do not check whether the point is inside the query rectangle unless
        // the rectangle corresponding to the node intersects the query rectangle
        pointsInRange(localRoot.left, rect, list);
        pointsInRange(localRoot.right, rect, list);
    }

    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        findNearest(root, p);
        return nearest;
    }

    private void findNearest(Node localRoot, Point2D p) {
        double distanceSquared = (nearest == null) ? Double.POSITIVE_INFINITY : p.distanceSquaredTo(nearest);
        if (localRoot == null)
            return;
        if (p.distanceSquaredTo(localRoot.point) < distanceSquared)
            nearest = localRoot.point;
        // TODO: this is still a brute force algorithm - need to determine rule for deciding which tree(s) to follow
        findNearest(localRoot.left, p);
        findNearest(localRoot.right, p);
    }

    public static void main(String[] args) {                  // unit testing of the methods (optional)
        RectHV r = new RectHV(0, 0, 1, 1);
        KdTree kdTree = new KdTree();
        In in = new In(args[0]);      // input file
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }
        System.out.println("hello " + kdTree.range(r));
        Point2D p = new Point2D(0.49, 0.74);
        System.out.println("nearest to point " + p.toString() + " is " + kdTree.nearest(p));
        kdTree.draw();
    }
}
