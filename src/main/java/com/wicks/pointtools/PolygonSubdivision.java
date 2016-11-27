package com.wicks.pointtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 11/27/16.
 */
public class PolygonSubdivision
{
    List<Polygon> polygons;

    public PolygonSubdivision(Polygon polygon)
    {
        polygons = new ArrayList<Polygon>();
        polygons.add(polygon);
    }

    public void addDiagonal(Line diagonal)
    {
        Polygon polygon = null;
        PolygonVertex a = null, b = null;

        for (Polygon p : polygons) {
            List<PolygonVertex> vertices = p.getVertices();
            int indexA, indexB;
            if ((indexA = vertices.indexOf(diagonal.a)) >= 0 && (indexB = vertices.indexOf(diagonal.b)) >= 0) {
                a = vertices.get(indexA);
                b = vertices.get(indexB);
                polygon = p;
                break;
            }
        }
        if (polygon == null) {
            throw new IllegalArgumentException("No such vertices found in this polygon subdivision.");
        }
        polygons.remove(polygon);
        polygons.addAll(polygon.split(a, b));
    }

    public List<Polygon> getPolygons()
    {
        return new ArrayList<>(polygons);
    }
}
