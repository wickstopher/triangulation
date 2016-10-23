package com.wicks.triangulation;

import com.google.common.collect.TreeMultiset;
import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wickstopher on 10/22/16.
 */
public class MonotonePolygonTriangulation
{
    ArrayList<ReflexChainPoint> reflexChain;

    public MonotonePolygonTriangulation()
    {

    }

    public void triangulatePolygon(List<Point> polygon)
    {
        reflexChain = translateInput(polygon);
        List<Line> diagonals = new ArrayList<>();
        ReflexChainPoint u = reflexChain.get(0);
        ReflexChainPoint vPrev = u;
        ReflexChainPoint v;

        for (int i = 1; i < reflexChain.size(); i++) {
            v = reflexChain.get(i);
            ReflexChainPoint.ChainPosition vPosition = v.getChainPosition();
            ReflexChainPoint.ChainPosition vPrevPosition= vPrev.getChainPosition();

            // Right/Left endpoints can be considered part of either the upper or lower chain (TODO: verify this)
            if (vPosition != vPrevPosition && vPrevPosition != ReflexChainPoint.ChainPosition.LEFT_ENDPOINT && vPosition != ReflexChainPoint.ChainPosition.RIGHT_ENDPOINT) {
                int j = i - 2;
                while (vPrev != u) {    // reference comparison should be sufficient here
                    diagonals.add(new Line(v, vPrev));
                    vPrev = reflexChain.get(j--);
                }
                u = reflexChain.get(i - 1);
            } else {

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

        output.add(new LeftEndpoint(polygon.get(minIndex)));

        for (int i = minIndex + 1; i != minIndex; i++) {
            if (i == polygon.size()) {
                i = 0;
                if (minIndex == 0) break;
            }

            Point point = polygon.get(i);

            if (i == maxIndex) {
                onUpperChain = false;
                output.add(new RightEndpoint(point));
            } else {
                if (onUpperChain) output.add(new UpperChainPoint(point));
                else output.add(new LowerChainPoint(point));
            }
        }
        return new ArrayList(output);
    }
}
