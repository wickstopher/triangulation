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
            v.getUpperEdge().helper = v;
            e.helper = v;
        } else if (event instanceof MergeEvent) {
            sweepLineStatus.remove(v.getNextEdge().statusKey);
            sweepLineStatus.remove(v.getPreviousEdge().statusKey);
            fixUp(v, getEdgeAbove(v));
            fixUp(v, v.getLowerEdge());
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
            insertStatusEdge(v.getNextEdge(), v.x);
            v.getNextEdge().helper = v;
        } else if (event instanceof LowerEvent) {
            fixUp(v, v.getNextEdge());
            sweepLineStatus.remove(v.getNextEdge().statusKey);
            insertStatusEdge(v.getPreviousEdge(), v.x);
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

        insertStatusEdge(v.getLowerEdge(), v.x);
        insertStatusEdge(v.getUpperEdge(), v.x + 0.0000001);
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

    private void insertStatusEdge(PolygonEdge edge, double xPosition)
    {
        edge.statusKey = edge.yPosition(xPosition);
        sweepLineStatus.put(edge.statusKey, edge);
    }

    private void reorderSweepLine(double xPosition)
    {
        TreeMap<Double, PolygonEdge> newStatus = new TreeMap<>();

        if (sweepLineStatus != null) {
            for (PolygonEdge e : sweepLineStatus.values()) {
                e.statusKey = e.yPosition(xPosition);
                newStatus.put(e.statusKey, e);
            }
        }
        sweepLineStatus = newStatus;
    }

    public List<Line> getNewDiagonals()
    {
        return new ArrayList<>(newDiagonals);
    }
}
