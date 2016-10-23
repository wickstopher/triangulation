package com.wicks.triangulation;

import com.google.common.collect.TreeMultiset;
import com.wicks.pointtools.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wickstopher on 10/23/16.
 */
public class SampleMain
{
    public static void main(String[] args)
    {
        List<Point> polygon = new ArrayList<>();

        polygon.add(new Point(1, 2));
        polygon.add(new Point(2, 3));
        polygon.add(new Point(3, 3));
        polygon.add(new Point(4, 4));
        polygon.add(new Point(5, 3));
        polygon.add(new Point(4.5, 1.5));
        polygon.add(new Point(3, 1));
        polygon.add(new Point(2, 1));

        MonotonePolygonTriangulation mpt = new MonotonePolygonTriangulation();

        //TreeMultiset<ReflexChainPoint> output = mpt.translateInput(polygon);
    }
}
