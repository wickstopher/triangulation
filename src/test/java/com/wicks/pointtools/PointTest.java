package com.wicks.pointtools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wickstopher on 10/23/16.
 */
public class PointTest
{
    @Test
    public void testRightAngleExpect90()
    {
        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(0, 1);

        assertEquals(90.0, a.getAngle(b, c), 0);
    }

    @Test
    public void testStraightLineExpect180()
    {
        Point a = new Point(-1, 0);
        Point b = new Point(0, 0);
        Point c = new Point(1, 0);

        assertEquals(180.0, b.getAngle(a, c), 0);
    }

    @Test
    public void testStraightLineExpect0()
    {
        Point a = new Point(-1, 0);
        Point b = new Point(0, 0);
        Point c = new Point(1, 0);

        assertEquals(0, a.getAngle(b, c), 0);
    }
}