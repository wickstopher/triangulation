package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 12/3/16.
 */
public class HullVisualizationState
{
    private List<Point> convexHull;
    private List<Line> edges;
    private Point prev;

    private int nextIndex;
    private boolean hasNext;

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

    public boolean hasNext()
    {
        return hasNext;
    }

    public List<Point> getHull()
    {
        return new ArrayList<>(convexHull);
    }

    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }
}