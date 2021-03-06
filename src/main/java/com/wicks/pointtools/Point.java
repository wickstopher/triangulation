package com.wicks.pointtools;

import Jama.Matrix;

import java.util.*;

/**
 * A class to represent a 2D Point.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class Point implements Comparable
{
    public final double x;
    public final double y;

    /**
     * Construct a new Point
     * @param x
     * @param y
     */
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * @param other
     * @return Whether or not this Point equals the other Object
     */
    public boolean equals(Object other)
    {
        if (other == this) return true;
        if (other instanceof Point) {
            Point otherPoint = (Point) other;
            return (otherPoint.x == x && otherPoint.y == y);
        }
        return false;
    }

    /**
     * Compare this Point to the other Object
     * @param other
     * @return
     */
    public int compareTo(Object other)
    {
        if (!(other instanceof Point)) {
            throw new RuntimeException("Not a point!");
        }
        Point otherPoint = (Point) other;
        double n1, n2;
        if (x == otherPoint.x) {
            n1 = y;
            n2 = otherPoint.y;
        } else {
            n1 = x;
            n2 = otherPoint.x;
        }
        if (n1 == n2) {
            return 0;
        } else if (n1 < n2) {
            return -1;
        }
        return 1;
    }

    /**
     * Considering this Point as the vertex, get the angle created by drawing vectors from this point to p2 and p3
     * @param p2
     * @param p3
     * @return
     */
    public double getAngle(Point p2, Point p3)
    {
        // be agnostic with respect to parameter order
        if (p2.compareTo(p3) < 0) {
            Point temp = p2;
            p2 = p3;
            p3 = temp;
        }

        // treat p2 and p3 as vectors, with this as the origin
        double x1 = p2.x - x;
        double y1 = p2.y - y;
        double x2 = p3.x - x;
        double y2 = p3.y - y;

        double angle = Math.atan2(y2, x2) - Math.atan2(y1, x1);
        if (angle < 0) angle += 2 * Math.PI;

        return Math.toDegrees(angle);
    }

    /**
     * @param other
     * @return The distance between this Point and the other Point
     */
    public double distance(Point other)
    {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    /**
     * @param q
     * @param r
     * @return The orientation of p -> q -> r
     */
    public double orientation(Point q, Point r)
    {
        double[][] m = { {1, x, y}, {1, q.x, q.y}, {1, r.x, r.y} };
        Matrix matrix = new Matrix(m);
        return matrix.det();
    }

    /**
     * @return A String representation of this Point.
     */
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Given a list of points, compute and return the convex hull.
     * @param points
     * @return The convex hull in a counter-clockwise orientation
     */
    public static List<Point> grahamsScan(List<Point> points)
    {
        List<Point> upperHull = computeUpperHull(points);
        List<Point> lowerHull = computeLowerHull(points);

        upperHull = upperHull.subList(1, upperHull.size());
        lowerHull = lowerHull.subList(1, lowerHull.size());
        lowerHull.addAll(upperHull);
        return lowerHull;
    }

    private static ArrayList<Point> computePartialHull(ArrayList<Point> points)
    {
        ArrayList<Point> partialHull = new ArrayList<Point>();
        while (!points.isEmpty()) {
            Point p = points.remove(points.size() - 1);
            while (partialHull.size() >= 2) {
                Point q = partialHull.get(partialHull.size() - 1);
                Point r = partialHull.get(partialHull.size() - 2);
                if (p.orientation(q, r) >= 0) {
                    partialHull.remove(partialHull.size() - 1);
                }
                else {
                    break;
                }
            }
            partialHull.add(p);
        }
        return partialHull;
    }

    private static ArrayList<Point> computeUpperHull(List<Point> points)
    {
        ArrayList<Point> copy = new ArrayList<>(points);
        Collections.sort(copy);
        return computePartialHull(copy);
    }

    private static ArrayList<Point> computeLowerHull(List<Point> points)
    {
        ArrayList<Point> copy = new ArrayList<>(points);
        Collections.sort(copy);
        Collections.reverse(copy);
        return computePartialHull(copy);
    }
}
