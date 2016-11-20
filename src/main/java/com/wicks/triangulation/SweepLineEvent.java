package com.wicks.triangulation;

import com.wicks.pointtools.PolygonEdge;
import com.wicks.pointtools.PolygonVertex;

/**
 * Created by wickstopher on 11/20/16.
 */
public class SweepLineEvent
{
    public enum EventType {
        SPLIT,
        MERGE,
        START,
        END,
        UPPER,
        LOWER
    }

    private PolygonVertex vertex;
    public final EventType eventType;

    public SweepLineEvent(PolygonVertex v)
    {
        vertex = v;
        eventType = determineEventType(v);
    }

    public PolygonVertex getVertex()
    {
        return vertex;
    }

    private EventType determineEventType(PolygonVertex v)
    {
        PolygonEdge e1 = v.getPreviousEdge();
        PolygonEdge e2 = v.getNextEdge();
        EventType eventType;

        // Split or Start
        if (v == e1.getLeftEndpoint() && v == e2.getLeftEndpoint()) {
            double angle = v.getAngle(e1.getRightEndpoint(), e2.getRightEndpoint());
            eventType = angle < 180 ? EventType.SPLIT : EventType.START;
        // Merge or End
        } else if (v == e1.getRightEndpoint() && v == e2.getRightEndpoint()) {
            double angle = v.getAngle(e1.getLeftEndpoint(), e2.getLeftEndpoint());
            eventType = angle < 180 ? EventType.END : EventType.MERGE;
        // Upper or Lower
        } else {
            eventType = v == e1.getRightEndpoint() ? EventType.LOWER : EventType.UPPER;
        }
        return eventType;
    }
}
