package com.wicks.triangulation;

import com.google.common.collect.TreeMultiset;
import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;

import java.util.*;

/**
 * Created by wickstopher on 10/22/16.
 */
public class MonotonePolygonTriangulation
{
    ArrayList<ReflexChainPoint> sortedPoints;

    public MonotonePolygonTriangulation()
    {

    }

    public void triangulatePolygon(List<Point> polygon)
    {
        sortedPoints = translateInput(polygon);
        List<Line> diagonals = new ArrayList<>();
        Stack<ReflexChainPoint> reflexChain = new Stack<>();
        ReflexChainPoint u = reflexChain.get(0);
        ReflexChainPoint vPrev = u;
        reflexChain.push(u);

        for (ReflexChainPoint v : sortedPoints.subList(1, sortedPoints.size())) {

            ReflexChainPoint.ChainPosition vPosition = v.getChainPosition();
            ReflexChainPoint.ChainPosition vPrevPosition= vPrev.getChainPosition();

            // Right/Left endpoints can be considered part of either the upper or lower chain (TODO: verify this)
            if (vPosition != vPrevPosition && vPrevPosition != ReflexChainPoint.ChainPosition.LEFT_ENDPOINT && vPosition != ReflexChainPoint.ChainPosition.RIGHT_ENDPOINT) {

                while (reflexChain.peek() != u) { // reference comparison should be sufficient here
                    diagonals.add(new Line(v, reflexChain.pop()));
                }
                u = reflexChain.push(vPrev);
            } else {
                if (!vPrev.isReflexVertex()) {
                    diagonals.add(new Line(1, 2, 3, 4));
                } else {

                }
            }
            vPrev = v;
        }
    }

    /**
     * Assumes that polygon is a list of points in counter-clockwise vertex order representing a monotone polygon.
     * @param polygon
     * @return
     */
    public ArrayList<ReflexChainPoint> translateInput(List<Point> polygon)
    {
        int minIndex = polygon.indexOf(Collections.min(polygon));
        int maxIndex = polygon.indexOf(Collections.max(polygon));
        TreeMultiset<ReflexChainPoint> output = TreeMultiset.create();

        boolean onUpperChain = true;

        output.add(new LeftEndpoint(polygon.get(minIndex), polygon.get(computePreviousIndex(minIndex, polygon.size())),
                polygon.get(computeNextIndex(minIndex, polygon.size()))));

        for (int i = minIndex + 1; i != minIndex; i++) {
            if (i == polygon.size()) {
                i = 0;
                if (minIndex == 0) break;
            }

            Point previous = polygon.get(computePreviousIndex(i, polygon.size()));
            Point next = polygon.get(computeNextIndex(i, polygon.size()));

            Point point = polygon.get(i);

            if (i == maxIndex) {
                onUpperChain = false;
                output.add(new RightEndpoint(point, previous, next));
            } else {
                if (onUpperChain) output.add(new UpperChainPoint(point, previous, next));
                else output.add(new LowerChainPoint(point, previous, next));
            }
        }
        return new ArrayList(output);
    }

    private int computeNextIndex(int i, int size) {
        if (i == size - 1) {
            return 0;
        }
        return i + 1;
    }

    private int computePreviousIndex(int i, int size) {
        if (i == 0) {
            return size - 1;
        }
        return i - 1;
    }
}
