package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.pointtools.Polygon;
import com.wicks.pointtools.PolygonEdge;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 10/23/16.
 */
public class OldPolygonTriangulation extends PApplet
{
    private ArrayList<Point> points;
    private MonotonePolygonTriangulation triangulation;
    private MonotonePolygonSubdivision subdivision;
    private HullVisualizationState hullState;
    private List<Polygon> polygons;
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
        subdivision = null;
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
                startTriangulationVisualization();
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
                triangulation = new MonotonePolygonTriangulation(new Polygon(hullState.getHull()));
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
                while (subdivision.hasNextEvent()) {
                    subdivision.processNextEvent();
                }
                polygons = subdivision.getPolygonSubdivison().getPolygons();
                triangulation = new MonotonePolygonTriangulation(polygonDrawState.polygon);
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
                triangulationVisualize = false;
            }
            visualizationPaused = true;
        }
        for (Point p : points) {
            text("x: " + p.x + "y: " + p.y, (float) p.x, (float) p.y);
        }
        if (subdivision != null) {
            for (Line e : subdivision.getNewDiagonals()) {
                drawLine(e);
            }
        }
    }

    private void startTriangulationVisualization()
    {
        if (points.size() < 2) {
            System.out.println("Please add some more points!");
            return;
        }
        defaults();
        hullState = new HullVisualizationState(Point.grahamsScan(points));
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
        PApplet.main("com.wicks.triangulation.OldPolygonTriangulation");
    }
}

