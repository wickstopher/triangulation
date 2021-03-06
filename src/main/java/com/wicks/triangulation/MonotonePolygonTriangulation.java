package com.wicks.triangulation;

import com.google.common.collect.TreeMultiset;
import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.pointtools.Polygon;
import com.wicks.pointtools.PolygonVertex;
import com.wicks.triangulation.ReflexChainEvent.ChainPosition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wickstopher on 10/22/16.
 */
public class MonotonePolygonTriangulation
{
    private List<ReflexChainEvent> points;
    private Stack<ReflexChainEvent> reflexChain;
    private List<Line> diagonals;
    private ReflexChainEvent u;
    private ReflexChainEvent vPrev;
    private int nextIndex;
    private Polygon polygon;

    public MonotonePolygonTriangulation(Polygon polygon)
    {
        this.polygon = polygon;
        points = translateInput(polygon.getVertices());
        reflexChain = new Stack<>();
        diagonals = new ArrayList<>();
        u = points.get(0);
        vPrev = u;
        reflexChain.push(u);
        nextIndex = 0;
    }

    public boolean hasNextStatus()
    {
        return nextIndex < points.size();
    }

    public double getXPosition()
    {
        return vPrev.x;
    }

    public Line getSweepline()
    {
        double x = getXPosition();
        List<Double> yPositions = polygon.getEdges().stream().
                filter(edge -> edge.a.x <= x && x <= edge.b.x).
                map(edge -> edge.yPosition(x)).collect(Collectors.toList());

        Point a = new Point(x, Collections.max(yPositions));
        Point b = new Point(x, Collections.min(yPositions));
        return new Line(a, b);
    }

    public List<Line> getDiagonals()
    {
        return new ArrayList<>(diagonals);
    }

    public void updateStatus()
    {
        ReflexChainEvent v = points.get(nextIndex++);

        if (nextIndex == 1) {
            return;
        }

        ChainPosition vPosition = v.getChainPosition();
        ChainPosition vPrevPosition = vPrev.getChainPosition();

        if (vPrevPosition != vPosition) {
            while (!reflexChain.isEmpty() && reflexChain.peek() != u) { // reference comparison should suffice here
                ReflexChainEvent prev = reflexChain.pop();
                if ((vPosition != ChainPosition.RIGHT_ENDPOINT && vPosition != ChainPosition.LEFT_ENDPOINT)
                        || prev != vPrev) {
                    diagonals.add(new Line(v, prev));
                }
            }
            u = reflexChain.push(vPrev);
        } else {
            if (!vPrev.isReflexVertex()) {
                ReflexChainEvent prev = reflexChain.pop();
                if (!reflexChain.isEmpty()) {
                    ReflexChainEvent twoPrev = reflexChain.peek();
                    boolean onUpper = vPrevPosition == ChainPosition.UPPER_CHAIN;
                    while ((onUpper && prev.getAngle(twoPrev, v) < 180) || (!onUpper && prev.getAngle(twoPrev, v) > 180)) {
                        if (twoPrev.getChainPosition() != ChainPosition.LEFT_ENDPOINT
                                && twoPrev.getChainPosition() != prev.getChainPosition()) break;
                        diagonals.add(new Line(v, prev));
                        prev = reflexChain.pop();
                        if (reflexChain.isEmpty()) break;
                        twoPrev = reflexChain.peek();
                    }
                    if (twoPrev == u) {
                        diagonals.add(new Line(v, u));
                        u = prev;
                    }
                }
                reflexChain.push(prev); // last visible vertex
            }
        }
        // in either case, v is added to the reflex chain
        reflexChain.push(v);
        vPrev = v;
    }

    public ReflexChainEvent getEventPoint()
    {
        return vPrev;
    }

    /**
     * Assumes that polygon is a list of points in counter-clockwise vertex order representing a monotone polygon.
     * @param vertices
     * @return
     */
    private static ArrayList<ReflexChainEvent> translateInput(List<PolygonVertex> vertices)
    {
        int minIndex = vertices.indexOf(Collections.min(vertices));
        int maxIndex = vertices.indexOf(Collections.max(vertices));
        TreeMultiset<ReflexChainEvent> output = TreeMultiset.create();

        boolean onUpperChain = true;

        output.add(new LeftEndpoint(vertices.get(minIndex), vertices.get(computePreviousIndex(minIndex, vertices.size())),
                vertices.get(computeNextIndex(minIndex, vertices.size()))));

        for (int i = minIndex + 1; i != minIndex; i++) {
            if (i == vertices.size()) {
                i = 0;
                if (minIndex == 0) break;
            }

            Point previous = vertices.get(computePreviousIndex(i, vertices.size()));
            Point next = vertices.get(computeNextIndex(i, vertices.size()));

            Point point = vertices.get(i);

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
