package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public abstract class ReflexChainEvent extends Point
{
    public enum ChainPosition { LEFT_ENDPOINT, RIGHT_ENDPOINT, UPPER_CHAIN, LOWER_CHAIN }

    protected boolean isReflexVertex;

    public ReflexChainEvent(Point point, Point previous, Point next)
    {
        super(point.x, point.y);
    }

    public abstract ChainPosition getChainPosition();

    public boolean isReflexVertex()
    {
        return isReflexVertex;
    }
}
