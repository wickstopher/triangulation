package com.wicks.pointtools;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent a Polygon subdivision.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class PolygonSubdivision
{
    private List<Polygon> polygons;

    /**
     * Initialize the PolygonSubdivision with a polygon
     * @param polygon
     */
    public PolygonSubdivision(Polygon polygon)
    {
        polygons = new ArrayList<Polygon>();
        polygons.add(polygon);
    }

    /**
     * Add a new diagonal, splitting the Polygon to which it belongs along that diagonal.
     * @param diagonal
     */
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

    /**
     * Get all of the Polygons in this subdivision.
     * @return a List of the Polygons in this subdivision
     */
    public List<Polygon> getPolygons()
    {
        return new ArrayList<>(polygons);
    }
}
