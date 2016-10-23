package com.wicks.pointtools;

/**
 * Created by wickstopher on 10/8/16.
 */
public class Point implements Comparable
{
    public final double x;
    public final double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object other)
    {
        if (other == this) return true;
        if (other instanceof Point) {
            Point otherPoint = (Point) other;
            return (otherPoint.x == x && otherPoint.y == y);
        }
        return false;
    }

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
        // treat p2 and p3 vectors, with this as the origin
        double x1 = p2.x - x;
        double y1 = p2.y - y;
        double x2 = p3.x - x;
        double y2 = p3.y - y;

        double angle = Math.atan2(y2, x2) - Math.atan2(y1, x1);
        if (angle < 0) angle += 2 * Math.PI;

        return Math.toDegrees(angle);
    }

    public double distance(Point other)
    {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}