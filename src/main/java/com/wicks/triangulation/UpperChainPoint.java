package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class UpperChainPoint extends ReflexChainPoint
{
    public UpperChainPoint(double x, double y)
    {
        super(x, y);
    }

    public UpperChainPoint(Point point)
    {
        super(point);
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.UPPER_CHAIN;
    }
}
