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
        double numerator = Math.pow(distance(p2), 2) + Math.pow(distance(p3), 2) + Math.pow(p2.distance(p3), 2);
        double denominator = 2 * distance(p2) * distance(p3);
        return Math.acos(numerator / denominator);
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
