package com.wicks.triangulation;

import com.wicks.pointtools.PolygonVertex;

/**
 * Created by wickstopher on 11/20/16.
 */
public abstract class SweepLineEvent
{
    private final PolygonVertex vertex;

    protected SweepLineEvent(PolygonVertex v)
    {
        vertex = v;
    }

    public PolygonVertex getVertex()
    {
        return vertex;
    }

    public static SweepLineEvent createEvent(PolygonVertex v)
    {
        switch(v.getVertexType()) {
            case Split:
                return new SplitEvent(v);
            case Merge:
                return new MergeEvent(v);
            case Start:
                return new StartEvent(v);
            case End:
                return new EndEvent(v);
            case Upper:
                return new UpperEvent(v);
            case Lower:
                return new LowerEvent(v);
            default:
                throw new RuntimeException("This should never happen");
        }
    }

    public String toString()
    {
        return this.getClass() + ": " + vertex.toString();
    }
}

class SplitEvent extends SweepLineEvent
{
    public SplitEvent(PolygonVertex v)
    {
        super(v);
    }
}

class MergeEvent extends SweepLineEvent
{
    public MergeEvent(PolygonVertex v)
    {
        super(v);
    }
}

class StartEvent extends SweepLineEvent
{
    public StartEvent(PolygonVertex v)
    {
        super(v);
    }
}

class EndEvent extends SweepLineEvent
{
    public EndEvent(PolygonVertex v)
    {
        super(v);
    }
}

class UpperEvent extends SweepLineEvent
{
    public UpperEvent(PolygonVertex v)
    {
        super(v);
    }
}


class LowerEvent extends SweepLineEvent
{
    public LowerEvent(PolygonVertex v)
    {
        super(v);
    }
}