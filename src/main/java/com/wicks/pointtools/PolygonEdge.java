package com.wicks.pointtools;

/**
 * Created by wickstopher on 11/20/16.
 */
public class PolygonEdge extends Line
{
    public final PolygonVertex previousVertex;
    public final PolygonVertex nextVertex;
    public PolygonVertex helper;

    public PolygonEdge(PolygonVertex prev, PolygonVertex next)
    {
        super(prev, next);
        previousVertex = prev;
        nextVertex = next;
        previousVertex.setNextEdge(this);
        nextVertex.setPreviousEdge(this);
    }

    public PolygonVertex getLeftEndpoint()
    {
        return (PolygonVertex) a;
    }

    public PolygonVertex getRightEndpoint()
    {
        return (PolygonVertex) b;
    }
}
