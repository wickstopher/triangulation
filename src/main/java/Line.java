import java.util.Optional;
import Jama.*;

/**
 * Created by wickstopher on 10/8/16.
 */
public class Line
{
    public final Point a;
    public final Point b;
    public final Double slope;
    public final Double intercept;

    public Line(Point a, Point b)
    {
        // We guarantee that the "leftmost" Point is always going to be Point a
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
    }

    public Line(double x1, double y1, double x2, double y2)
    {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    public boolean equals(Object other)
    {
        if (other == this) return true;

        if (other instanceof Line) {
            Line otherLine = (Line) other;
            return (a.equals(otherLine.a) && b.equals(otherLine.b));
        }
        return false;
    }

    public boolean isVertical()
    {
        return (slope == null);
    }

    public Optional<Point> intersectionWith(Line other)
    {
        double[][] coefficients = { {-slope, 1}, {-other.slope, 1} };
        double[][] constants = { {intercept}, {other.intercept} };

        try {
            Matrix coefficientMatrix = new Matrix(coefficients);
            Matrix constantMatrix = new Matrix(constants);
            Matrix solution = coefficientMatrix.solve(constantMatrix);
            return Optional.of(new Point(solution.get(0, 0), solution.get(1, 0)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
