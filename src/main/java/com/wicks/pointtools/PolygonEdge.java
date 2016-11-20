package com.wicks.pointtools;

/**
 * Created by wickstopher on 11/20/16.
 */
public class PolygonEdge extends Line
{
    public final PolygonVertex previousVertex;
    public final PolygonVertex nextVertex;

    public PolygonEdge(PolygonVertex a, PolygonVertex b)
    {
        super(a, b);
        previousVertex = a;
        nextVertex = b;
        previousVertex.setNextEdge(this);
        nextVertex.setPreviousEdge(this);
    }
}
