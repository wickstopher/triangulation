package com.wicks.triangulation;

import com.wicks.pointtools.Point;

/**
 * Created by wickstopher on 10/23/16.
 */
public class RightEndpoint extends ReflexChainPoint
{
    public RightEndpoint(double x, double y)
    {
        super(x, y);
    }

    public RightEndpoint(Point point)
    {
        super(point);
    }

    @Override
    public ChainPosition getChainPosition()
    {
        return ChainPosition.RIGHT_ENDPOINT;
    }
}
