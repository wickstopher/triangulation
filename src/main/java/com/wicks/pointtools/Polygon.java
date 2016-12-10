package com.wicks.pointtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent a 2D Polygon.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class Polygon
{
    private List<PolygonVertex> vertices;
    private List<PolygonEdge> edges;

    /**
     * Construct a Polygon given a counter-clockwise list of Points
     * @param points
     */
    public Polygon(List<Point> points)
    {
        if (points.size() < 3) {
            throw new RuntimeException("Polygons need at least 3 vertices!");
        }
        initializeVertices(fixOrientation(points));
        initializeEdges();
        determineVertexTypes();
    }

    /**
     * @return the edges of this Polygon
     */
    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }

    /**
     * @return a sorted list of this Polygon's vertices
     */
    public List<PolygonVertex> getSortedVertices()
    {
        List<PolygonVertex> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);
        return sortedVertices;
    }

    /**
     * @return The vertices of this Polygon in the order it was constructed
     */
    public List<PolygonVertex> getVertices()
    {
        return new ArrayList<>(vertices);
    }

    /**
     * Given two vertices, return the two Polygons that would be constructed by splitting along the edge connecting
     * those two vertices.
     * @param a
     * @param b
     */
    public List<Polygon> split(PolygonVertex a, PolygonVertex b)
    {
        List<Polygon> newPolygons = new ArrayList<>(2);
        List<Point> aPoints = new ArrayList<>();
        List<Point> bPoints = new ArrayList<>();
        PolygonVertex current = a;

        while (current != b) {
            aPoints.add(current);
            current = current.getNext();
        }
        aPoints.add(current);

        while (current != a) {
            bPoints.add(current);
            current = current.getNext();
        }
        bPoints.add(current);

        newPolygons.add(new Polygon(aPoints));
        newPolygons.add(new Polygon(bPoints));

        return newPolygons;
    }

    private List<Point> fixOrientation(List<Point> points)
    {
        List<Point> newPoints = new ArrayList<>(points);

        int sum = 0;
        for (int i = 0; i < newPoints.size(); i++) {
            Point p = newPoints.get(i);
            Point next = i < newPoints.size() - 1 ? newPoints.get(i+1) : newPoints.get(0);

            sum += ( (next.x - p.x) * (next.y + p.y) );
        }
        if (sum > 0) {
            // it's clockwise, need to fix it
            Collections.reverse(newPoints);
        }
        return newPoints;
    }

    private void initializeEdges()
    {
        edges = new ArrayList<>();
        vertices.forEach(v -> edges.add(new PolygonEdge(v, v.getNext())));
    }

    private void initializeVertices(List<Point> points)
    {
        vertices = new ArrayList<>(points.size());
        points.forEach(p -> vertices.add(new PolygonVertex(p)));

        for (int i = 0; i < vertices.size() - 1; i++) {
            vertices.get(i).setNext(vertices.get(i + 1));
        }
        vertices.get(vertices.size() - 1).setNext(vertices.get(0));
    }

    private void determineVertexTypes()
    {
        PolygonVertex leftmost = Collections.min(vertices);
        leftmost.setVertexType(PolygonVertex.VertexType.Start);
        PolygonVertex current = leftmost.getNext();

        while (current != leftmost) {

            PolygonEdge previousEdge = current.getPreviousEdge();
            PolygonEdge nextEdge = current.getNextEdge();
            PolygonVertex.VertexType vertexType;

            // Two left endpoints means a Start vetex or a Split vertex
            if (current == previousEdge.getLeftEndpoint() && current == nextEdge.getLeftEndpoint()) {
                if (current.getUpperEdge() == previousEdge) {
                    vertexType = PolygonVertex.VertexType.Start;
                } else {
                    vertexType = PolygonVertex.VertexType.Split;
                }
            // Two right endpoints means an End vertex or a Merge vertex
            } else if (current == previousEdge.getRightEndpoint() && current == nextEdge.getRightEndpoint()) {
                if (current.getLowerEdge() == previousEdge) {
                    vertexType = PolygonVertex.VertexType.End;
                } else {
                    vertexType = PolygonVertex.VertexType.Merge;
                }
            // Lower
            } else if (current == previousEdge.getLeftEndpoint() && current == nextEdge.getRightEndpoint()) {
                vertexType = PolygonVertex.VertexType.Lower;
            // Upper
            } else if (current == previousEdge.getRightEndpoint() && current == nextEdge.getLeftEndpoint()) {
                vertexType = PolygonVertex.VertexType.Upper;
            } else {
                throw new RuntimeException("This should never happen.");
            }
            current.setVertexType(vertexType);
            current = current.getNext();
        }
    }
}
