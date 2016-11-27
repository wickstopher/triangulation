package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Polygon;
import com.wicks.pointtools.PolygonEdge;
import com.wicks.pointtools.PolygonVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by wickstopher on 11/20/16.
 */
public class MonotonePolygonSubdivision
{
    private Polygon polygon;
    private TreeMap<Double, PolygonEdge> sweepLineStatus;
    private List<Line> newDiagonals;
    private List<SweepLineEvent> events;
    private int nextIndex;

    public MonotonePolygonSubdivision(Polygon polygon)
    {
        this.polygon = polygon;
        this.newDiagonals = new ArrayList<>();
        this.events = new ArrayList<>();
        polygon.getSortedVertices().forEach(v -> events.add(SweepLineEvent.createEvent(v)));
        this.nextIndex = 0;
    }

    public boolean hasNextEvent()
    {
        return nextIndex < events.size();
    }

    public void processNextEvent()
    {
        SweepLineEvent event = events.get(nextIndex++);
        PolygonVertex v = event.getVertex();
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

    public List<Line> getNewDiagonals()
    {
        return new ArrayList<>(newDiagonals);
    }
}
