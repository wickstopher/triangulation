package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class LeftEndpoint extends ReflexChainPoint
{

    public LeftEndpoint(Point point, Point previous, Point next)
    {
        super(point, previous, next);
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.LEFT_ENDPOINT;
    }
}
