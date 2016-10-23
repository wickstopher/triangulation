package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class LowerChainPoint extends ReflexChainPoint
{
    public LowerChainPoint(double x, double y)
    {
        super(x, y);
    }

    public LowerChainPoint(Point point)
    {
        super(point);
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.LOWER_CHAIN;
    }
}
