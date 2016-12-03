package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class RightEndpoint extends ReflexChainPoint
{
    public RightEndpoint(Point point, Point previous, Point next)
    {
        super(point, previous, next);
        isReflexVertex = false;
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.RIGHT_ENDPOINT;
    }
}
