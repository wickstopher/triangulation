package com.wicks.triangulation;

import com.wicks.pointtools.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Class to hold the state of one run of the Monotone Polygon Subdivision algorithm.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class MonotonePolygonSubdivision
{
    private Polygon polygon;
    private PolygonSubdivision polygonSubdivision;
    private TreeMap<Double, PolygonEdge> sweepLineStatus;
    private List<Line> newDiagonals;
    private List<SweepLineEvent> events;
    private PolygonVertex currentVertex;
    private int nextIndex;
    private double xPosition;

    /**
     * Initialize the algorithm with the given Polygon.
     * @param polygon
     */
    public MonotonePolygonSubdivision(Polygon polygon)
    {
        this.polygon = polygon;
        this.newDiagonals = new ArrayList<>();
        this.events = new ArrayList<>();
        polygon.getSortedVertices().forEach(v -> events.add(SweepLineEvent.createEvent(v)));
        this.nextIndex = 0;
        sweepLineStatus = new TreeMap<>();
    }

    /**
     * @return Is there another event to process?
     */
    public boolean hasNextEvent()
    {
        return nextIndex < events.size();
    }

    /**
     * Process the next event and adjust the state of the subdivision.
     */
    public void processNextEvent()
    {
        SweepLineEvent event = events.get(nextIndex++);
        PolygonVertex v = event.getVertex();
        currentVertex = v;
        xPosition = v.x;
        reorderSweepLine(v.x);

        if (event instanceof SplitEvent) {
            PolygonEdge e = getEdgeAbove(v);
            newDiagonals.add(new Line(v, e.helper));
            insertVertexEdges(v);
            v.getLowerEdge().helper = v;
            e.helper = v;
        } else if (event instanceof MergeEvent) {
            sweepLineStatus.remove(v.getNextEdge().statusKey);
            sweepLineStatus.remove(v.getPreviousEdge().statusKey);
            PolygonEdge above = getEdgeAbove(v);
            fixUp(v, above);
            fixUp(v, v.getLowerEdge());
            above.helper = v;
        } else if (event instanceof StartEvent) {
            insertVertexEdges(v);
            v.getUpperEdge().helper = v;
        } else if (event instanceof EndEvent) {
            fixUp(v, v.getUpperEdge());
            sweepLineStatus.remove(v.getNextEdge().statusKey);
            sweepLineStatus.remove(v.getPreviousEdge().statusKey);
        } else if (event instanceof UpperEvent) {
            fixUp(v, v.getPreviousEdge());
            sweepLineStatus.remove(v.getPreviousEdge().statusKey);
            insertStatusEdge(v.getNextEdge(), v.y);
            v.getNextEdge().helper = v;
        } else if (event instanceof LowerEvent) {
            fixUp(v, v.getNextEdge());
            sweepLineStatus.remove(v.getNextEdge().statusKey);
            insertStatusEdge(v.getPreviousEdge(), v.y);
            v.getPreviousEdge().helper = v;
        }
    }

    /**
     * @return The current vertex (associated with the most recently processed event)
     */
    public PolygonVertex getCurrentVertex()
    {
        return currentVertex;
    }

    /**
     * @return The PolygonSubdivision of the Polygon with which the algorithm was initialized.
     */
    public PolygonSubdivision getPolygonSubdivison()
    {
        if (polygonSubdivision == null) {
            while (hasNextEvent()) {
                processNextEvent();
            }
            polygonSubdivision = new PolygonSubdivision(polygon);
            newDiagonals.forEach(diagonal -> polygonSubdivision.addDiagonal(diagonal));
        }
        return polygonSubdivision;
    }

    /**
     * @return The sweepline at the most recently processed event.
     */
    public Line getSweepline()
    {
        if (!sweepLineStatus.isEmpty()) {
            Point a = new Point(xPosition, sweepLineStatus.firstEntry().getValue().yPosition(xPosition));
            Point b = new Point(xPosition, sweepLineStatus.lastEntry().getValue().yPosition(xPosition));

            if (a.y > currentVertex.y) {
                a = currentVertex;
            } else if (b.y < currentVertex.y) {
                b = currentVertex;
            }
            return new Line(a, b);
        }
        List<PolygonVertex> vertices = polygon.getSortedVertices();
        Point p = vertices.get(vertices.size() - 1);
        return new Line(p, p);
    }

    /**
     * @return All new diagonals that have been added to the Polygon thus far.
     */
    public List<Line> getNewDiagonals()
    {
        return new ArrayList<>(newDiagonals);
    }

    private void insertVertexEdges(PolygonVertex v)
    {
        PolygonEdge upper = v.getUpperEdge();
        PolygonEdge lower = v.getLowerEdge();

        if (upper == lower) {
            throw new RuntimeException("Upper edge equals lower, this should never happen.");
        }

        insertStatusEdge(lower, v.y);
        insertStatusEdge(upper, v.y + 0.0000001);
    }

    private PolygonEdge getEdgeAbove(PolygonVertex v)
    {
       for (PolygonEdge e : sweepLineStatus.values()) {
           if (e.statusKey > v.y) {
               return e;
           }
       }
       throw new RuntimeException("No Edge found above!");
    }

    private void fixUp(PolygonVertex v, PolygonEdge e)
    {
        if (e.helper != null && e.helper.getVertexType() == PolygonVertex.VertexType.Merge) {
            newDiagonals.add(new Line(v, e.helper));
        }
    }

    private void insertStatusEdge(PolygonEdge edge, double yPosition)
    {
        edge.statusKey = yPosition;
        sweepLineStatus.put(edge.statusKey, edge);
    }

    private void reorderSweepLine(double xPosition)
    {
        TreeMap<Double, PolygonEdge> newStatus = new TreeMap<>();
        TreeMap<Double, PolygonEdge> oldStatus = sweepLineStatus;
        sweepLineStatus = newStatus;
        PolygonEdge e1 = null;
        PolygonEdge e2 = null;

        if (oldStatus != null) {
            for (PolygonEdge e : oldStatus.values()) {
                if (e.getLeftEndpoint().x == xPosition || e.getRightEndpoint().x == xPosition) {
                    if (e1 == null) {
                        e1 = e;
                    } else if (e2 == null) {
                        e2 = e;
                    } else {
                        throw new RuntimeException("More than 2 endpoints match?!");
                    }
                } else {
                    insertStatusEdge(e, e.yPosition(xPosition));
                }
            }
        }
        if (e1 != null) {
            if (e2 == null) {
                insertStatusEdge(e1, e1.yPosition(xPosition));
            } else {
                PolygonVertex v = e1.getLeftEndpoint().x == xPosition ? e1.getLeftEndpoint() : e1.getRightEndpoint();
                insertStatusEdge(v.getLowerEdge(), v.y);
                insertStatusEdge(v.getUpperEdge(), v.y + 0.000001);
            }
        }
    }
}
