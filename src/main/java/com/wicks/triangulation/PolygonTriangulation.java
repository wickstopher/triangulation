package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 10/23/16.
 */
public class PolygonTriangulation extends PApplet
{
    private enum State {
        ADDING_POINTS;
    }

    private ArrayList<Point> points;
    private MonotonePolygonTriangulation mpt;
    private List<Line> triangulation;
    private HullVisualizationState hullState;
    private boolean hullVisualize;

    public void settings()
    {
        points = new ArrayList<>();
        mpt = new MonotonePolygonTriangulation();
        hullVisualize = false;
        size(750, 750);
    }

    private void reset()
    {
        points = new ArrayList<>();
        clear();
    }

    public void mousePressed()
    {
        if (mouseY < 650) {
            addUserPoint();
        } else if (mouseX < (750 / 2)) {
            try {
                startTriangulationVisualization();
            } catch (Exception e) {}
        } else {
            reset();
        }
    }

    public void draw()
    {
        if (hullVisualize) {
            if (hullState.hasNext()) {
                drawLine(hullState.getNextLine());
                delay(50);
            } else {
                hullVisualize = false;
                triangulation = mpt.triangulatePolygon(hullState.getHull());
                triangulation.forEach(line -> drawLine(line));
            }
        }
    }

    private void startTriangulationVisualization()
    {
        if (points.size() < 2) {
            System.out.println("Please add some more points!");
            return;
        }
        clear();

        hullState = new HullVisualizationState(Point.grahamsScan(points));
        hullVisualize = true;
    }

    public void clear()
    {
        super.clear();
        defaults();
    }

    private void defaults()
    {
        background(223);
    }

    private void addUserPoint()
    {
        Point p = new Point(mouseX, mouseY);
        points.add(p);
        drawPoint(p);
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

    public static void main(String[] args)
    {
        PApplet.main("com.wicks.triangulation.PolygonTriangulation");
    }
}

class HullVisualizationState
{
    private List<Point> convexHull;
    private Point prev;

    private int nextIndex;
    private boolean hasNext;

    public HullVisualizationState(List<Point> convexHull)
    {
        if (convexHull.size() < 2) {
            throw new IllegalStateException("Convex hull must contain at least 2 points!");
        }
        this.convexHull = convexHull;
        nextIndex = 1;
        prev = convexHull.get(0);
        hasNext = true;
    }

    public Line getNextLine()
    {
        if (nextIndex < convexHull.size()) {
            Point next = convexHull.get(nextIndex++);
            Line nextLine = new Line(prev, next);
            prev = next;
            return nextLine;
        }
        else {
            hasNext = false;
            return new Line(prev, convexHull.get(0));
        }
    }

    public boolean hasNext()
    {
        return hasNext;
    }

    public List<Point> getHull()
    {
        return convexHull;
    }
}