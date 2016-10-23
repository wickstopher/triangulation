package com.wicks.triangulation;

import com.wicks.pointtools.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wickstopher on 10/22/16.
 */
public class MonotonePolygonTriangulation
{
    public MonotonePolygonTriangulation()
    {

    }

    /**
     * Assumes the polygon is a list of points in counter-clockwise vertex order
     * @param polygon
     * @return
     */
    public List<ReflexChainPoint> translateInput(List<Point> polygon)
    {
        int minIndex = polygon.indexOf(Collections.min(polygon));
        int maxIndex = polygon.indexOf(Collections.max(polygon));
        List<ReflexChainPoint> output = new ArrayList<>();

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
        return output;
    }


}
