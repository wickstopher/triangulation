package com.wicks.triangulation;

import com.google.common.collect.TreeMultiset;
import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by wickstopher on 10/23/16.
 */
public class SampleMain extends PApplet
{
    private enum State {
        ADDING_POINTS;
    }

    private ArrayList<Point> points;
    private MonotonePolygonTriangulation mpt;
    private List<Line> triangulation;

    public void settings()
    {
        points = new ArrayList<>();
        mpt = new MonotonePolygonTriangulation();
        size(750, 750);
    }

    private void reset()
    {
        points = new ArrayList<>();
        clear();
    }

    public void draw()
    {
    }

    public void mousePressed()
    {
        if (mouseY < 650) {
            addUserPoint();
        } else if (mouseX < (750 / 2)) {
            startTriangulationVisualization();
        } else {
            reset();
        }
    }

    private void startTriangulationVisualization()
    {
        clear();
        List<Point> convexHull = Point.grahamsScan(points);
        Point prev = convexHull.get(0);
        for (Point p : convexHull.subList(0, convexHull.size())) {
            drawLine(new Line(prev, p));
            prev = p;
        }
        drawLine(new Line(prev, convexHull.get(0)));
        triangulation = mpt.triangulatePolygon(convexHull);
        triangulation.forEach(line -> drawLine(line));
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
    }

    private void drawPoint(Point point)
    {
        float x = (float) point.x;
        float y = (float) point.y;
        ellipse(x, y, 5, 5);
    }

    public static void main(String[] args)
    {
        PApplet.main("com.wicks.triangulation.SampleMain");
    }
}
