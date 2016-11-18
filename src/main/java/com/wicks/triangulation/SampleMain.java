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
    private ArrayList<Point> points;
    private List<Point> hull;
    private List<Line> triangulation;
    private MonotonePolygonTriangulation mpt;
    private Random random;

    public void settings()
    {
        random = new Random();
        points = new ArrayList<>();
        IntStream.range(0, 200).forEach(i -> points.add(new Point(random.nextInt(450) + 200, random.nextInt(450) + 200)));
        hull = Point.grahamsScan(points);
        mpt = new MonotonePolygonTriangulation();
        triangulation = mpt.triangulatePolygon(hull);
        size(750, 750);
    }

    public void draw()
    {
        //triangulation = mpt.triangulatePolygon(polygon);
        hull.forEach(p -> point(p));
        triangulation.forEach(l -> line(l));
    }

    public static void main(String[] args)
    {
//        List<Point> polygon = new ArrayList<>();
//        polygon.add(new Point(1, 2));
//        polygon.add(new Point(2, 3));
//        polygon.add(new Point(3, 3));
//        polygon.add(new Point(4, 4));
//        polygon.add(new Point(5, 3));
//        polygon.add(new Point(4.5, 1.5));
//        polygon.add(new Point(3, 1));
//        polygon.add(new Point(2, 1));
//        MonotonePolygonTriangulation mpt = new MonotonePolygonTriangulation();
//        List<Line> triangulation = mpt.triangulatePolygon(polygon);
//        triangulation.forEach(line -> System.out.println(line));
        PApplet.main("com.wicks.triangulation.SampleMain");
    }

    public void line(Line line)
    {
        float x1 = (float) line.a.x;
        float y1 = (float)line.a.y;
        float x2 = (float) line.b.x;
        float y2 = (float) line.b.y;
        line(x1, y1, x2, y2);
    }

    public void point(Point point)
    {
        float x = (float) point.x;
        float y = (float) point.y;
        ellipse(x, y, 5, 5);
    }

}
