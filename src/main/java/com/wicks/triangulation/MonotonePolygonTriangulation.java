package com.wicks.triangulation;

import com.google.common.collect.TreeMultiset;
import com.sun.net.httpserver.Filter;
import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.triangulation.ReflexChainPoint.ChainPosition;

import java.util.*;

/**
 * Created by wickstopher on 10/22/16.
 */
public class MonotonePolygonTriangulation
{
    private List<ReflexChainPoint> points;
    private Stack<ReflexChainPoint> reflexChain;
    private ReflexChainPoint u;
    private ReflexChainPoint vPrev;
    private int nextIndex;

    public MonotonePolygonTriangulation(List<Point> polygon)
    {
        if (polygon.size() < 2) {
            throw new IllegalStateException("Polygon size must be at least 2!");
        }
        points = translateInput(polygon);
        reflexChain = new Stack<>();
        u = points.get(0);
        vPrev = u;
        reflexChain.push(u);
        nextIndex = 1;
    }

    public boolean hasNext()
    {
        return nextIndex < points.size();
    }

    public List<Line> getNextDiagonals()
    {
        ReflexChainPoint v = points.get(nextIndex++);
        List<Line> diagonals = new ArrayList<>();

        ChainPosition vPosition = v.getChainPosition();
        ChainPosition vPrevPosition = vPrev.getChainPosition();

        if (!onTheSameChain(vPrevPosition, vPosition)) {
            while (!reflexChain.isEmpty() && reflexChain.peek() != u) { // reference comparison should suffice here
                diagonals.add(new Line(v, reflexChain.pop()));
            }
            u = reflexChain.push(vPrev);
        } else {
            if (!vPrev.isReflexVertex()) {
                ReflexChainPoint prev = reflexChain.pop();
                if (!reflexChain.isEmpty()) {
                    ReflexChainPoint twoPrev = reflexChain.peek();
                    while (prev.getAngle(twoPrev, v) < 180) {
                        diagonals.add(new Line(v, prev));
                        prev = reflexChain.pop();
                        if (reflexChain.isEmpty()) break;
                        twoPrev = reflexChain.peek();
                    }
                }
                reflexChain.push(prev); // last visible vertex
            }
        }
        // in either case, v is added to the reflex chain
        reflexChain.push(v);
        vPrev = v;
        return diagonals;
    }

    private boolean onTheSameChain(ChainPosition a, ChainPosition b)
    {
        // Right/Left endpoints can be considered part of either the upper or lower chain
        return a == b || a == ChainPosition.LEFT_ENDPOINT || b == ChainPosition.LEFT_ENDPOINT
                || a == ChainPosition.RIGHT_ENDPOINT || b == ChainPosition.RIGHT_ENDPOINT;
    }

    /**
     * Assumes that polygon is a list of points in counter-clockwise vertex order representing a monotone polygon.
     * @param polygon
     * @return
     */
    private static ArrayList<ReflexChainPoint> translateInput(List<Point> polygon)
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

    private static int computeNextIndex(int i, int size) {
        if (i == size - 1) {
            return 0;
        }
        return i + 1;
    }

    private static int computePreviousIndex(int i, int size) {
        if (i == 0) {
            return size - 1;
        }
        return i - 1;
    }
}
