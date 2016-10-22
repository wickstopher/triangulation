package com.wicks.lineintersection;

import com.wicks.pointtools.Point;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Created by wickstopher on 10/8/16.
 */
public class EventQueue
{
    private TreeMap<Point, SweepLineEvent> queue;

    public EventQueue()
    {
        queue = new TreeMap<>();
    }

    public Entry<Point, SweepLineEvent> pop()
    {
        Entry<Point, SweepLineEvent> firstEntry = queue.pollFirstEntry();
        queue.remove(firstEntry.getKey());
        return firstEntry;
    }

    public void push(SweepLineEvent event)
    {
        queue.put(event.getKey(), event);
    }

    public void delete(Point eventPoint)
    {
        queue.remove(eventPoint);
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }
}
