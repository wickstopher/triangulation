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

    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
