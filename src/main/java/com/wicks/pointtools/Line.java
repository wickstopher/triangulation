package com.wicks.pointtools;

import java.util.Optional;
import Jama.*;

/**
 * Class to represent a 2D line.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class Line
{
    public final Point a;
    public final Point b;
    public final Double slope;
    public final Double intercept;
    public Double statusKey;

    /**
     * Construct a Line
     * @param a
     * @param b
     */
    public Line(Point a, Point b)
    {
        // We guarantee that the "leftmost" com.wicks.pointtools.Point is always going to be com.wicks.pointtools.Point a
        if (a.compareTo(b) > 0) {
            this.a = b;
            this.b = a;
        } else {
            this.a = a;
            this.b = b;
        }
        if (b.x == a.x) {
            this.slope = null;
            this.intercept = null;
        } else {
            this.slope = (this.b.y - this.a.y) / (this.b.x - this.a.x);
            this.intercept = this.a.y - (this.slope * this.a.x);
        }
        statusKey = this.a.y;
    }

    /**
     * Construct a Line
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public Line(double x1, double y1, double x2, double y2)
    {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    /**
     * Does this Line equal the other Object?
     * @param other
     * @return whether or not the Line is equal to the other Object
     */
    public boolean equals(Object other)
    {
        if (other == this) return true;

        if (other instanceof Line) {
            Line otherLine = (Line) other;
            return (a.equals(otherLine.a) && b.equals(otherLine.b));
        }
        return false;
    }

    /**
     * Is tjhis a vertical line?
     * @return
     */
    public boolean isVertical()
    {
        return (slope == null);
    }

    public boolean isCollinear(Line other)
    {
        if (isVertical()) {
            return a.x == other.a.x;
        }
        return (slope == other.slope && intercept == other.intercept);
    }

    public boolean hasIntersection(Line other)
    {
        double[][] coefficients = { {b.x - a.x, other.a.x - other.b.x}, {b.y - a.y, other.a.y - other.b.y} };
        double[][] constants = { {other.a.x - a.x}, {other.a.y - a.y} };
        try {
            Matrix solution = solve(coefficients, constants);
            double s = solution.get(0, 0);
            double t = solution.get(1, 0);
            return 0 <= s && s <= 1 && 0 <= t && t <= 1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get this Line's point of intersection with the other Line
     * @param other
     * @return The intersection point between the Lines
     */
    public Optional<Point> intersectionWith(Line other)
    {
        if (isCollinear(other)) {
            return Optional.empty();
        }
        if (a.equals(other.a) || a.equals(other.b)) {
            return Optional.of(a);
        }
        if (b.equals(other.a) || b.equals(other.b)) {
            return Optional.of(b);
        }

        if (!hasIntersection(other)) {
            return Optional.empty();
        }

        double[][] coefficients = { {-slope, 1}, {-other.slope, 1} };
        double[][] constants = { {intercept}, {other.intercept} };

        try {
            Matrix solution = solve(coefficients, constants);
            return Optional.of(new Point(solution.get(0, 0), solution.get(1, 0)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * @param x
     * @return The y-position on this line at the given x-coordinate
     */
    public double yPosition(double x)
    {
        return slope * x + intercept;
    }

    /**
     * @param other
     * @param x
     * @return whether or not this Line is above the other Line at the given x-coordinate.
     */
    public boolean isAbove(Line other, double x)
    {
        return yPosition(x) > other.yPosition(x);
    }

    /**
     * @return A String representation of the Line
     */
    public String toString()
    {
        return "[ " + a + " -- " + b + "]";
    }


    private Matrix solve(double[][] coefficients, double[][] constants)
    {
        Matrix coefficientMatrix = new Matrix(coefficients);
        Matrix constantMatrix = new Matrix(constants);
        Matrix solution = coefficientMatrix.solve(constantMatrix);
        return solution;
    }
}
