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

    @Test
    public void testObtuseExpectGreaterThan180()
    {
        Point a = new Point(2, 1);
        Point b = new Point(-1, 0.5);
        Point c = new Point(3, 0.5);

        assertTrue(a.getAngle(c, b) > 180.0);
        assertTrue(a.getAngle(b, c) > 180.0);
    }

    @Test
    public void testDistanceExpectOne()
    {
        Point a = new Point(0, 0);
        Point b = new Point(0, 1);

        assertEquals(1.0, a.distance(b), 0);
    }

    @Test
    public void testDistanceExpectSqrtTwo()
    {
        Point a = new Point(0, 0);
        Point b = new Point(1, 1);

        assertEquals(Math.sqrt(2.0), a.distance(b), 0);
    }
}