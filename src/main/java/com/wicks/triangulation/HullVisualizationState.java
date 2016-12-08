package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a convex hull visualization state.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class HullVisualizationState
{
    private List<Point> convexHull;
    private List<Line> edges;
    private Point prev;

    private int nextIndex;
    private boolean hasNext;

    /**
     * Given a List of Points, construct a new visualization state.
     * @param convexHull
     */
    public HullVisualizationState(List<Point> convexHull)
    {
        if (convexHull.size() < 2) {
            throw new IllegalStateException("Convex hull must contain at least 2 points!");
        }
        edges = new ArrayList<>();
        this.convexHull = convexHull;
        nextIndex = 1;
        prev = convexHull.get(0);
        hasNext = true;
    }

    /**
     * @return The next line to visualize
     */
    public Line getNextLine()
    {
        Line nextLine;
        if (nextIndex < convexHull.size()) {
            Point next = convexHull.get(nextIndex++);
            nextLine = new Line(prev, next);
            prev = next;
        }
        else {
            hasNext = false;
            nextLine = new Line(prev, convexHull.get(0));
        }
        edges.add(nextLine);
        return nextLine;
    }

    /**
     * @return Whether or not there is a line that has not yet been provided.
     */
    public boolean hasNext()
    {
        return hasNext;
    }

    /**
     * @return The Convex Hull associated with this HullVisualizationState
     */
    public List<Point> getHull()
    {
        return new ArrayList<>(convexHull);
    }

    /**
     * @return The edges that have been visualized thus far.
     */
    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }
}
