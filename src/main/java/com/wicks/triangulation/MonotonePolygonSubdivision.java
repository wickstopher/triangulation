package com.wicks.triangulation;

import com.wicks.pointtools.Polygon;
import com.wicks.pointtools.PolygonVertex;

import java.util.List;

/**
 * Created by wickstopher on 11/20/16.
 */
public class MonotonePolygonSubdivision
{
    private Polygon polygon;
    private List<PolygonVertex> events;

    public MonotonePolygonSubdivision(Polygon polygon)
    {
        this.polygon = polygon;
        events = polygon.getSortedVertices();
    }

}
