package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Class to represent a Lower Chain Point for the MonotonePolygonTriangulation.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class LowerChainPoint extends ReflexChainEvent
{
    public LowerChainPoint(Point point, Point previous, Point next)
    {
        super(point, previous, next);
        isReflexVertex = point.getAngle(previous, next)  < 180;
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.LOWER_CHAIN;
    }
}
