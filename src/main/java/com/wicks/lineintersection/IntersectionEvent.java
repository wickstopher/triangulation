package com.wicks.lineintersection;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/8/16.
 */
public class IntersectionEvent extends SweepLineEvent
{
    public final Line line1;
    public final Line line2;
    public final Point point;

    public IntersectionEvent(Line line1, Line line2, Point point)
    {
        super(point);
        this.point = point;
        this.line1 = line1;
        this.line2 = line2;
    }
}
