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
        polygon.getSortedVertices().forEach(v -> events.add(new SweepLineEvent(v)));
        this.nextIndex = 0;
    }

    public boolean hasNextEvent()
    {
        return nextIndex < events.size();
    }

    public void processNextEvent()
    {
        SweepLineEvent event = events.get(nextIndex++);
    }

    private void fixUp(PolygonVertex v, PolygonEdge e)
    {
        if (isMergeVertex(e.helper)) {
            newDiagonals.add(new Line(v, e.helper));
        }
    }

    private boolean isMergeVertex(PolygonVertex v)
    {
        // TODO: LOGIC
        return false;
    }

    private void insertStatusEdge(PolygonEdge edge, double xPosition)
    {
        TreeMap<Double, PolygonEdge> newStatus = new TreeMap<>();

        if (sweepLineStatus != null) {
            for (PolygonEdge e : sweepLineStatus.values()) {
                e.statusKey = e.yPosition(xPosition);
                newStatus.put(e.statusKey, e);
            }
        }
        edge.statusKey = edge.yPosition(xPosition);
        newStatus.put(edge.statusKey, edge);
        sweepLineStatus = newStatus;
    }
}
