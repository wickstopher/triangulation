package com.wicks.pointtools;

/**
 * Created by wickstopher on 11/20/16.
 */
public class PolygonVertex extends Point
{
    private PolygonVertex previous;
    private PolygonVertex next;
    private PolygonEdge previousEdge;
    private PolygonEdge nextEdge;

    public PolygonVertex(double x, double y, PolygonVertex previous, PolygonVertex next)
    {
        super(x, y);
        this.previous = previous;
        this.next = next;
    }

    public PolygonVertex(Point p)
    {
        super(p.x, p.y);
        this.previous = null;
        this.next = null;
    }

    public PolygonVertex getNext()
    {
        return next;
    }

    public PolygonVertex getPrevious()
    {
        return previous;
    }

    protected void setNext(PolygonVertex next)
    {
        this.next = next;
        next.previous = this;
    }

    protected void setNextEdge(PolygonEdge next)
    {
        nextEdge = next;
    }

    protected void setPreviousEdge(PolygonEdge previous)
    {
        previousEdge = previous;
    }

    public PolygonEdge getNextEdge()
    {
        return nextEdge;
    }

    public PolygonEdge getPreviousEdge()
    {
        return previousEdge;
    }
}
