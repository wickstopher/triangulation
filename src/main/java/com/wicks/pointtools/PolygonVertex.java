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
        return getEdge(true);
    }

    public PolygonEdge getLowerEdge()
    {
        return getEdge(false);
    }

    private PolygonEdge getEdge(boolean upper)
    {
        PolygonVertex a = nextEdge.getLeftEndpoint() == this ?
                nextEdge.getRightEndpoint() : nextEdge.getLeftEndpoint();
        PolygonVertex b = previousEdge.getLeftEndpoint() == this ?
                previousEdge.getRightEndpoint() : nextEdge.getLeftEndpoint();

        if (a == this || b == this) {
            throw new RuntimeException("That's bad");
        }
        if (upper) {
            return a.y > b.y ? nextEdge : previousEdge;
        } else {
            return a.y > b.y ? previousEdge : nextEdge;
        }
    }

    public VertexType getVertexType()
    {
        return vertexType;
    }
}
