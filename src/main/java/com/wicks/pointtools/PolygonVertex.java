package com.wicks.pointtools;

/**
 * A class to represent a 2D Polygon vertex.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
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

    /**
     * Construct a PolygonVertex
     * @param x
     * @param y
     * @param previous
     * @param next
     */
    public PolygonVertex(double x, double y, PolygonVertex previous, PolygonVertex next)
    {
        super(x, y);
        this.previous = previous;
        this.next = next;
    }

    /**
     * Construct a PolygonVertex
     * @param p
     */
    public PolygonVertex(Point p)
    {
        super(p.x, p.y);
        this.previous = null;
        this.next = null;
    }

    /**
     * @return If this Vertex is part of a Polygon, return the next vertex in the orientation, or null if it is not.
     */
    public PolygonVertex getNext()
    {
        return next;
    }

    /**
     * @return If this Vertex is part of a Polygon, return the previous vertex in the orientation, or null if it is not.
     */
    public PolygonVertex getPrevious()
    {
        return previous;
    }

    /**
     * @return If this Vertex is part of a Polygon, return the next Edge in the orientation, or null if it is not.
     */
    public PolygonEdge getNextEdge()
    {
        return nextEdge;
    }

    /**
     * @return If this Vertex is part of a Polygon, return the previous Edge in the orientation, or null if it is not.
     */
    public PolygonEdge getPreviousEdge()
    {
        return previousEdge;
    }

    /**
     * @return the upper of this vertice's two incident edges
     */
    public PolygonEdge getUpperEdge()
    {
        double x = getYComparisonPoint(nextEdge, previousEdge);
        return nextEdge.yPosition(x) >= previousEdge.yPosition(x) ? nextEdge : previousEdge;
    }

    /**
     * @return the lower of this vertice's two incident edges
     */
    public PolygonEdge getLowerEdge()
    {
        return nextEdge == getUpperEdge() ? previousEdge : nextEdge;
    }

    /**
     * @return The VertexType of thie Vertex
     */
    public VertexType getVertexType()
    {
        return vertexType;
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
}
