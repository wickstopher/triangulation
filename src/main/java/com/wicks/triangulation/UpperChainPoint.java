package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class UpperChainPoint extends ReflexChainEvent
{
    public UpperChainPoint(Point point, Point previous, Point next)
    {
        super(point, previous, next);
        isReflexVertex = point.getAngle(previous, next) > 180;
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.UPPER_CHAIN;
    }
}
