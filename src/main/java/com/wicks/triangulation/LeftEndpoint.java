package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class LeftEndpoint extends ReflexChainPoint
{
    public LeftEndpoint(double x, double y)
    {
        super(x, y);
    }

    public LeftEndpoint(Point point)
    {
        super(point);
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.LEFT_ENDPOINT;
    }
}
