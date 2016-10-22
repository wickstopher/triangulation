package com.wicks.lineintersection;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/8/16.
 */
public abstract class SweepLineEvent
{
    private Point key;

    public SweepLineEvent(Point key)
    {
        this.key = key;
    }

    public Point getKey()
    {
        return key;
    }
}
