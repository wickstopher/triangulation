package com.wicks.pointtools;

import com.seisw.util.geom.Poly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wickstopher on 11/20/16.
 */
public class Polygon
{
    private List<PolygonVertex> vertices;

    public Polygon(List<Point> points)
    {
        initializeVertices(points);
    }

    public List<Line> getEdges()
    {
        List<Line> edges = new ArrayList<>();
        vertices.forEach(v -> edges.add(new Line(v, v.getNext())));
        return edges;
    }

    public List<PolygonVertex> getSortedVertices()
    {
        List<PolygonVertex> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);
        return sortedVertices;
    }

    public List<PolygonVertex> getVertices()
    {
        return new ArrayList<>(vertices);
    }

    private void initializeVertices(List<Point> points)
    {
        vertices = new ArrayList<>(points.size());
        points.forEach(p -> vertices.add(new PolygonVertex(p)));

        for (int i = 0; i < vertices.size() - 1; i++) {
            vertices.get(i).setNext(vertices.get(i + 1));
        }
        vertices.get(vertices.size() - 1).setNext(vertices.get(0));
    }
}

class PolygonVertex extends Point
{
    private PolygonVertex previous;
    private PolygonVertex next;

    public PolygonVertex(double x, double y, PolygonVertex previous, PolygonVertex next)
    {
        super(x, y);
        this.previous = previous;
        this.next = next;
    }

    public PolygonVertex getNext()
    {
        return next;
    }

    public PolygonVertex getPrevious()
    {
        return previous;
    }

    public PolygonVertex(Point p)
    {
        super(p.x, p.y);
        this.previous = null;
        this.next = null;
    }

    protected void setNext(PolygonVertex next)
    {
        this.next = next;
        next.previous = this;
    }
}