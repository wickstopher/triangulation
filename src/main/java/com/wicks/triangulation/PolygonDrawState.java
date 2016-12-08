package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.pointtools.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the state of drawing a Polygon on the screen.
 *
 * @author Christopher R. Wicks <wickstopher@gmail.com>
 */
public class PolygonDrawState
{
    Polygon polygon;
    List<Point> vertices;
    List<Line> edges;
    List<Line> processedEdges;
    private int nextIndex;

    /**
     * Initialize the draw state with a list of Points.
     * @param vertices
     */
    public PolygonDrawState(List<Point> vertices)
    {
        this.vertices = vertices;
        polygon = new Polygon(vertices);
        edges = polygon.getEdges();
        processedEdges = new ArrayList<>(edges.size());
        nextIndex = 0;
    }

    /**
     * @return the edges of the Polygon that was created at initialization
     */
    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }

    /**
     * @return The vertices that this state was initialized with
     */
    public List<Point> getVertices()
    {
        return new ArrayList<>(vertices);
    }

    /**
     * @return is there another Line to draw?
     */
    public boolean hasNext()
    {
        return nextIndex < edges.size();
    }

    /**
     * @return get the next Line to draw
     */
    public Line getNextLine()
    {
        processedEdges.add(edges.get(nextIndex));
        return edges.get(nextIndex++);
    }

    /**
     * @return The edges that have been processed thus far
     */
    public List<Line> getProcessedEdges()
    {
        return new ArrayList<>(processedEdges);
    }
}
