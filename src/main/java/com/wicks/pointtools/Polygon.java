package com.wicks.pointtools;

import com.seisw.util.geom.Poly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wickstopher on 11/20/16.
 */
public class Polygon
{
    private List<PolygonVertex> vertices;
    private List<PolygonEdge> edges;

    public Polygon(List<Point> points)
    {
        initializeVertices(points);
        initializeEdges();
        determineVertexTypes();
    }

    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }

    public List<PolygonVertex> getSortedVertices()
    {
        List<PolygonVertex> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);
        return sortedVertices;
    }

    public List<PolygonVertex> getVertices()
    {
        return new ArrayList<>(vertices);
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
        boolean onLower = true;

        while (current != leftmost) {
            PolygonEdge previousEdge = current.getPreviousEdge();
            PolygonEdge nextEdge = current.getNextEdge();
            PolygonVertex.VertexType vertexType;

            if (current == previousEdge.getLeftEndpoint() && current == nextEdge.getLeftEndpoint()) {
                if (onLower) {
                    vertexType = PolygonVertex.VertexType.Start;
                } else {
                    vertexType = PolygonVertex.VertexType.Split;
                }
            } else if (current == previousEdge.getRightEndpoint() && current == nextEdge.getRightEndpoint()) {
                if (onLower) {
                    vertexType = PolygonVertex.VertexType.Merge;
                } else {
                    vertexType = PolygonVertex.VertexType.End;
                }
            } else if (current == previousEdge.getLeftEndpoint() && current == nextEdge.getRightEndpoint()) {
                onLower = true;
                vertexType = PolygonVertex.VertexType.Lower;
            } else if (current == previousEdge.getRightEndpoint() && current == nextEdge.getLeftEndpoint()) {
                onLower = false;
                vertexType = PolygonVertex.VertexType.Upper;
            } else {
                throw new RuntimeException("This should never happen.");
            }
            current.setVertexType(vertexType);
            current = current.getNext();
        }
    }
}
