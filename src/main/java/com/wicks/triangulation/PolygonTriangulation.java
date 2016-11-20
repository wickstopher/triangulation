package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.pointtools.Polygon;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 10/23/16.
 */
public class PolygonTriangulation extends PApplet
{
    private ArrayList<Point> points;
    private MonotonePolygonTriangulation triangulation;
    private MonotonePolygonSubdivision subdivision;
    private HullVisualizationState hullState;
    private PolygonDrawState polygonDrawState;

    private boolean hullVisualize;
    private boolean polygonVisualize;
    private boolean triangulationVisualize;
    private boolean visualizationPaused;

    public void settings()
    {
        size(750, 750);
        points = new ArrayList<>();
        hullVisualize = false;
        triangulationVisualize = false;
        visualizationPaused = false;
    }

    private void reset()
    {
        points = new ArrayList<>();
        defaults();
    }


    public void mousePressed()
    {
        if (hullVisualize || polygonVisualize) {
            // do nothing for now
        } else if (triangulationVisualize) {
            visualizationPaused = false;
        } else {
            if (mouseY < 650) {
                points.add(new Point(mouseX, mouseY));
            } else if (mouseX < (750 / 2)) {
                try {
                    startTriangulationVisualization();
                } catch (Exception e) {
                }
            } else {
                reset();
            }
        }
    }

    public void draw()
    {
        if (!hullVisualize && !polygonVisualize && !triangulationVisualize) {
            defaults();
            fill(123);
            text("x: " + mouseX + "y: " + mouseY, mouseX, mouseY);
            points.forEach(p -> drawPoint(p));
        }
        if (hullVisualize && !visualizationPaused) {
            if (hullState.hasNext()) {
                drawLine(hullState.getNextLine());
                delay(50);
            } else {
                hullVisualize = false;
                triangulation = new MonotonePolygonTriangulation(hullState.getHull());
                triangulationVisualize = true;
            }
        }

        if (polygonVisualize && !visualizationPaused) {
            if (polygonDrawState.hasNext()) {
                drawLine(polygonDrawState.getNextLine());
                delay(50);
            } else {

                polygonVisualize = false;
                subdivision = new MonotonePolygonSubdivision(polygonDrawState.polygon);
                triangulation = new MonotonePolygonTriangulation(polygonDrawState.getVertices());
                triangulationVisualize = true;
            }
        }

        if (triangulationVisualize) {
            if (triangulation.hasNextStatus()) {

                if (!visualizationPaused) {
                    triangulation.updateStatus();
                }
                defaults();
                text("x: " + mouseX + "y: " + mouseY, mouseX, mouseY);
                //hullState.getEdges().forEach(line -> drawLine(line));
                polygonDrawState.getEdges().forEach(line -> drawLine(line));
                triangulation.getDiagonals().forEach(line -> drawLine(line));
                drawSweepline(triangulation.getXPosition());
            } else {
                System.out.printf("Total number of diagonals: %d\n", triangulation.getDiagonals().size());
                triangulationVisualize = false;
            }
            visualizationPaused = true;
        }
    }

    private void startTriangulationVisualization()
    {
        if (points.size() < 2) {
            System.out.println("Please add some more points!");
            return;
        }
        defaults();
        //hullState = new HullVisualizationState(Point.grahamsScan(points));
        //hullVisualize = true;
        visualizationPaused = false;
        polygonDrawState = new PolygonDrawState(points);
        polygonVisualize = true;
    }

    private void defaults()
    {
        background(223);
    }

    private void drawLine(Line line)
    {
        float x1 = (float) line.a.x;
        float y1 = (float)line.a.y;
        float x2 = (float) line.b.x;
        float y2 = (float) line.b.y;
        line(x1, y1, x2, y2);
        redraw();
    }

    private void drawPoint(Point point)
    {
        float x = (float) point.x;
        float y = (float) point.y;
        ellipse(x, y, 5, 5);
        redraw();
    }

    private void drawSweepline(double x)
    {
        line((float) x, 1000, (float) x, -1000);
    }

    public static void main(String[] args)
    {
        PApplet.main("com.wicks.triangulation.PolygonTriangulation");
    }
}

class PolygonDrawState
{
    Polygon polygon;
    List<Point> vertices;
    List<Line> edges;
    private int nextIndex;

    public PolygonDrawState(List<Point> vertices)
    {
        this.vertices = vertices;
        polygon = new Polygon(vertices);
        edges = polygon.getEdges();
        nextIndex = 0;
    }

    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }

    public List<Point> getVertices()
    {
        return new ArrayList<>(vertices);
    }

    public boolean hasNext()
    {
        return nextIndex < edges.size();
    }

    public Line getNextLine()
    {
        return edges.get(nextIndex++);
    }
}

class HullVisualizationState
{
    private List<Point> convexHull;
    private List<Line> edges;
    private Point prev;

    private int nextIndex;
    private boolean hasNext;

    public HullVisualizationState(List<Point> convexHull)
    {
        if (convexHull.size() < 2) {
            throw new IllegalStateException("Convex hull must contain at least 2 points!");
        }
        edges = new ArrayList<>();
        this.convexHull = convexHull;
        nextIndex = 1;
        prev = convexHull.get(0);
        hasNext = true;
    }

    public Line getNextLine()
    {
        Line nextLine;
        if (nextIndex < convexHull.size()) {
            Point next = convexHull.get(nextIndex++);
            nextLine = new Line(prev, next);
            prev = next;
        }
        else {
            hasNext = false;
            nextLine = new Line(prev, convexHull.get(0));
        }
        edges.add(nextLine);
        return nextLine;
    }

    public boolean hasNext()
    {
        return hasNext;
    }

    public List<Point> getHull()
    {
        return new ArrayList<>(convexHull);
    }

    public List<Line> getEdges()
    {
        return new ArrayList<>(edges);
    }
}