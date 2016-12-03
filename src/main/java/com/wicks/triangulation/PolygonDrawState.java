package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.pointtools.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 12/3/16.
 */
public class PolygonDrawState
{
    Polygon polygon;
    List<Point> vertices;
    List<Line> edges;
    private int nextIndex;

    public PolygonDrawState(List<Point> vertices)
    {
        this.vertices = vertices;
        polygon = new Polygon(vertices);
        edges = polygon.getEdges();
        nextIndex = 0;
    }

    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }

    public List<Point> getVertices()
    {
        return new ArrayList<>(vertices);
    }

    public boolean hasNext()
    {
        return nextIndex < edges.size();
    }

    public Line getNextLine()
    {
        return edges.get(nextIndex++);
    }
}
