package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public abstract class ReflexChainPoint extends Point
{
    public enum ChainPosition { LEFT_ENDPOINT, RIGHT_ENDPOINT, UPPER_CHAIN, LOWER_CHAIN }

    public ReflexChainPoint(double x, double y)
    {
        super(x, y);
    }

    public ReflexChainPoint(Point point)
    {
        super(point.x, point.y);
    }

    public abstract ChainPosition getChainPosition();
}
