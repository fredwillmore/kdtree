import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;

//import java.lang.IllegalArgumentException;

import java.util.ArrayList;

public class PointSET {

    private SET<Point2D> set;

    public PointSET() {                               // construct an empty set of points

        set = new SET<Point2D>();
    }

    public boolean isEmpty() {                      // is the set empty?
        return set.isEmpty();
    }

    public int size() {                         // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();

        set.add(p);
    }

    public boolean contains(Point2D p) {            // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() {                         // draw all points to standard draw
        for (Point2D p: set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<Point2D>();
        for (Point2D p: set) {
            if (
                    p.x() <= rect.xmax() && p.x() >= rect.xmin()
                    &&
                    p.y() <= rect.ymax() && p.y() >= rect.ymin()
            ) {
                list.add(p);
            }
        }
        return list;
    }

    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        double distanceSquared = Double.POSITIVE_INFINITY;
        Point2D nearest = null;

        for (Point2D candidate: set) {
            if (p.distanceSquaredTo(candidate) < distanceSquared) {
                distanceSquared = p.distanceSquaredTo(candidate);
                nearest = candidate;
            }
        }

        return nearest;
    }

    public static void main(String[] args) {                  // unit testing of the methods (optional)
        RectHV r = new RectHV(0, 0, 0.5, 1);
        PointSET set = new PointSET();
        In in = new In(args[0]);      // input file
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            set.insert(p);
        }
        System.out.println("hello " + set.range(r));
        Point2D p = new Point2D(0.406, 0.99);
        System.out.println("nearest to point " + p.toString() + " is " + set.nearest(p));
//        set.draw();
    }
}
