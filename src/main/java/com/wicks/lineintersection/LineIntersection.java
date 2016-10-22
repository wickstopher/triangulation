package com.wicks.lineintersection;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wickstopher on 10/8/16.
 */
public class LineIntersection
{

    private EventQueue events;
    private SweepLineStatus status;
    private List<Line> lines;
    private List<IntersectionEvent> intersections;
    private double xPosition;

    public List<IntersectionEvent> computeIntersections(List<Line> lines)
    {
        this.lines = lines;
        intersections = new ArrayList<>();
        events = new EventQueue();
        status = new SweepLineStatus();

        for (Line line : lines) {
            if (!line.isVertical()) {
                events.push(new LeftEndpointEvent(line));
            }
            events.push(new RightEndpointEvent(line));
        }

        while (!events.isEmpty()) {
            handle(events.pop());
        }

        return intersections;
    }

    private void handle(Map.Entry<Point, SweepLineEvent> eventEntry)
    {
        this.xPosition = eventEntry.getKey().x;
        SweepLineEvent event = eventEntry.getValue();

        if (event instanceof LeftEndpointEvent)
            handleEvent((LeftEndpointEvent) event);
        else if (event instanceof RightEndpointEvent)
            handleEvent((RightEndpointEvent) event);
        else if (event instanceof IntersectionEvent)
            handleEvent((IntersectionEvent) event);
        else
            throw new IllegalArgumentException();
    }

    private void handleEvent(LeftEndpointEvent event)
    {
        status.add(event.line, xPosition, events);
    }

    private void handleEvent(RightEndpointEvent event)
    {
        status.remove(event.line.statusKey, events).ifPresent(events -> intersections.addAll(events));
    }

    private void handleEvent(IntersectionEvent event)
    {
        status.swap(event.line1.statusKey, event.line2.statusKey, xPosition, events);
        intersections.add(event);
    }
}
