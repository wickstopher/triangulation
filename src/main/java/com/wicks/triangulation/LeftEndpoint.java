package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Class to represent a Left Endpoint event for the MonotonePolygonTriangulation.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class LeftEndpoint extends ReflexChainEvent
{
    public LeftEndpoint(Point point, Point previous, Point next)
    {
        super(point, previous, next);
        isReflexVertex = false;
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.LEFT_ENDPOINT;
    }
}
