package com.wicks.pointtools;

/**
 * Created by wickstopher on 11/20/16.
 */
public class PolygonVertex extends Point
{
    public enum VertexType {
        Split, Merge, Start, End, Upper, Lower
    }

    private PolygonVertex previous;
    private PolygonVertex next;
    private PolygonEdge previousEdge;
    private PolygonEdge nextEdge;
    private VertexType vertexType;

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

    protected void setVertexType(VertexType type)
    {
        vertexType = type;
    }

    public PolygonEdge getNextEdge()
    {
        return nextEdge;
    }

    public PolygonEdge getPreviousEdge()
    {
        return previousEdge;
    }

    public PolygonEdge getUpperEdge()
    {
        double x = getYComparisonPoint(nextEdge, previousEdge);
        return nextEdge.yPosition(x) >= previousEdge.yPosition(x) ? nextEdge : previousEdge;
    }

    public PolygonEdge getLowerEdge()
    {
        return nextEdge == getUpperEdge() ? previousEdge : nextEdge;
    }

    private double getYComparisonPoint(PolygonEdge a, PolygonEdge b) {
        Point aEndpoint, bEndpoint;

        if (this == a.getLeftEndpoint() && this == b.getLeftEndpoint()) {
            aEndpoint = a.getRightEndpoint();
            bEndpoint = b.getRightEndpoint();
        } else if (this == a.getRightEndpoint() && this == b.getRightEndpoint()) {
            aEndpoint = a.getLeftEndpoint();
            bEndpoint = b.getLeftEndpoint();
        } else {
            aEndpoint = a.getRightEndpoint() == this ? a.getLeftEndpoint() : a.getRightEndpoint();
            bEndpoint = b.getRightEndpoint() == this ? b.getLeftEndpoint() : b.getRightEndpoint();
        }

        if (aEndpoint.x < bEndpoint.x) {
            return aEndpoint.x;
        }
        return bEndpoint.x;
    }

    public VertexType getVertexType()
    {
        return vertexType;
    }
}
