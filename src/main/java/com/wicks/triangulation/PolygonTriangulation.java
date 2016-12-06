package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
import com.wicks.pointtools.Polygon;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wickstopher on 12/2/16.
 */
public class PolygonTriangulation extends PApplet
{
    private boolean debug = false;

    private int xDimension = 850;
    private int yDimension = 850;
    private float iconCenterY = 775;
    private float iconHalfHeight = 35;
    private float iconWidth = 57;
    private float iconSpacing = 75;
    private float panelTop = iconCenterY - iconHalfHeight - 20;

    // panel variables
    float playButtonX = 115;
    float nextButtonX = playButtonX + iconWidth + iconSpacing;
    float modeButtonX = nextButtonX + iconWidth + iconSpacing;
    float speedButtonX = modeButtonX + iconWidth + iconSpacing;
    float cancelButtonX = speedButtonX + iconWidth + iconSpacing;

    // state variables
    private boolean playing;
    private boolean waitingForInput;
    private boolean doReset;
    private boolean firstEvent;
    private int nextPolygonIndex;
    private DrawMode drawMode;
    private DrawSpeed drawSpeed;
    private List<Point> points;
    private List<Polygon> polygons;
    private List<Line> diagonals;
    private MonotonePolygonSubdivision subdivision;
    private MonotonePolygonTriangulation triangulation;
    private PolygonDrawState polygonDrawState;
    private HullVisualizationState hullState;
    private Line sweepLine;
    private Point eventPoint;
    private Point firstPoint;
    private Point previousPoint;

    public void settings()
    {
        size(xDimension, yDimension);

        // initialize state
        drawMode = DrawMode.POINT;
        drawSpeed = DrawSpeed.NORMAL;
        reset();
    }

    private void requestReset()
    {
        doReset = true;
    }

    private void reset()
    {
        playing = false;
        firstEvent = false;
        waitingForInput = false;
        doReset = false;
        points = new ArrayList<>();
        polygons = new ArrayList<>();
        diagonals = new ArrayList<>();
        hullState = null;
        polygonDrawState = null;
        subdivision = null;
        nextPolygonIndex = -1;
        sweepLine = null;
        eventPoint = null;
        firstPoint = null;
        previousPoint = null;
    }

    public void draw()
    {
        try {
            background(190);
            drawPanel();
            points.forEach(p -> drawInputPoint(p));
            polygons.forEach(polygon -> drawPolygon(polygon));
            diagonals.forEach(line -> drawPolygonEdge(line));
            drawSweepline();
            drawEventPoint();
            if (!playing && drawMode == DrawMode.POINT && previousPoint != null) {
                drawPolygonEdge(new Line(previousPoint, new Point(mouseX, mouseY)));
            }

            if (polygonDrawState != null) {
                if (polygonDrawState.hasNext()) {
                    drawPolygonEdge(polygonDrawState.getNextLine());
                    polygonDrawState.getProcessedEdges().forEach(line -> drawPolygonEdge(line));
                    delay(50);
                } else {
                    points = polygonDrawState.getVertices();
                    Polygon polygon = new Polygon(points);
                    polygons.add(polygon);
                    subdivision = new MonotonePolygonSubdivision(polygon);
                    polygonDrawState = null;
                    firstEvent = true;
                }
            } else if (hullState != null) {
                if (hullState.hasNext()) {
                    drawPolygonEdge(hullState.getNextLine());
                    hullState.getEdges().forEach(line -> drawPolygonEdge(line));
                    delay(50);
                } else {
                    points = hullState.getHull();
                    Polygon polygon = new Polygon(points);
                    polygons.add(polygon);
                    subdivision = new MonotonePolygonSubdivision(polygon);
                    hullState = null;
                    firstEvent = true;
                }
            } else if (subdivision != null) {
                if (subdivision.hasNextEvent()) {
                    if (!waitingForInput) {
                        if (firstEvent) {
                            firstEvent = false;
                        } else {
                            waitForUserInputOrDelay();
                            subdivision.processNextEvent();
                        }
                        sweepLine = subdivision.getSweepline();
                        eventPoint = subdivision.getCurrentVertex();
                        diagonals.addAll(subdivision.getNewDiagonals());
                    }
                } else {
                    polygons.addAll(subdivision.getPolygonSubdivison().getPolygons());
                    nextPolygonIndex = 1;
                    subdivision = null;
                }
            } else if (-1 < nextPolygonIndex && nextPolygonIndex < polygons.size() && triangulation == null) {
                Polygon polygon = polygons.get(nextPolygonIndex++);
                triangulation = new MonotonePolygonTriangulation(polygon);
            } else if (nextPolygonIndex == polygons.size()) {
                // final wait
                waitForUserInputOrDelay();
                nextPolygonIndex = -1;
            }
            else if (triangulation != null) {
                if (triangulation.hasNextStatus()) {
                    if (!waitingForInput) {
                        if (firstEvent) {
                            firstEvent = false;
                        } else {
                            waitForUserInputOrDelay();
                            triangulation.updateStatus();
                        }
                        sweepLine = triangulation.getSweepline();
                        eventPoint = triangulation.getEventPoint();
                        //diagonals.removeAll(triangulation.getDiagonals());
                        diagonals.addAll(triangulation.getDiagonals());
                    }
                } else {
                    waitForUserInputOrDelay();
                    triangulation = null;
                }
            }

            if (debug) {
                drawMousePosition();
                points.forEach(p -> {
                    textSize(12);
                    textAlign(TOP, RIGHT);
                    text("  ( " + p.x + ", " + p.y + " )", (float) p.x, (float) p.y);
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            requestReset();
        }
        if (doReset) {
            reset();
        }
    }

    private void play()
    {
        playing = true;
        polygons = new ArrayList<>();
        diagonals = new ArrayList<>();
        sweepLine = null;
        eventPoint = null;
        switch (drawMode) {
            case POINT:
                polygonDrawState = new PolygonDrawState(points);
                break;
            case HULL:
                hullState = new HullVisualizationState(Point.grahamsScan(points));
                break;
        }
    }

    public void mousePressed()
    {
        try {
            if (onButton(playButtonX)) {
                play();
            } else if (onButton(nextButtonX)) {
                waitingForInput = false;
            } else if (onButton(modeButtonX)) {
                cycleDrawMode();
            } else if (onButton(speedButtonX)) {
                cycleDrawSpeed();
            } else if (onButton(cancelButtonX)) {
                requestReset();
            } else if (!onPanel() && !playing) {
                switch (drawMode) {
                    case POINT:
                        Point p = new Point(mouseX, mouseY);
                        if (previousPoint != null) {
                            if (Math.abs(p.x - firstPoint.x) <= 15 && Math.abs(p.y - firstPoint.y) <= 15) {
                                p = firstPoint;
                            }
                            diagonals.add(new Line(p, previousPoint));
                        } else {
                            firstPoint = p;
                        }
                        previousPoint = p;
                        if (p == firstPoint && points.size() > 1) {
                            play();
                            break;
                        }
                    case HULL:
                        points.add(previousPoint);
                        break;
                    case DRAW:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            requestReset();
        }
    }

    public void keyPressed()
    {
        switch(key) {
            case 'D':
            case 'd':
                debug = !debug;
                break;
            default:
                break;
        }
    }

    private void drawPanel()
    {
        strokeWeight(5);
        stroke(87);
        line(0, panelTop, xDimension, panelTop);

        // "play button"
        strokeWeight(1);
        stroke(0);
        if (onButton(playButtonX)) {
            fill(123, 150, 75);
        } else {
            fill(123, 200, 75);
        }
        triangle(playButtonX, iconCenterY + iconHalfHeight,
                 playButtonX, iconCenterY - iconHalfHeight,
                 playButtonX + iconWidth, iconCenterY);

        // "next button"
        strokeWeight(8);
        if (onButton(nextButtonX)) {
            stroke(100, 25, 75);
            fill(100, 25, 120);
        } else {
            stroke(100, 25, 120);
            fill(100, 25, 120);
        }
        line(nextButtonX, iconCenterY, nextButtonX + iconWidth, iconCenterY);
        float triangleWidth = iconWidth / 6;
        float triangleLeftX = nextButtonX + (iconWidth - 10);
        strokeWeight(0);
        triangle(triangleLeftX, iconCenterY + triangleWidth + 5,
                 triangleLeftX, iconCenterY - triangleWidth  - 5,
                 nextButtonX + triangleWidth + iconWidth, iconCenterY);

        // "mode button"
        textSize(25);
        if (onButton(modeButtonX)) {
            fill(50);
        } else {
            fill(99);
        }
        textAlign(CENTER, CENTER);
        text(drawMode.toString().toLowerCase(), modeButtonX + (iconWidth / 2), iconCenterY);

        // "speed button"
        if (onButton(speedButtonX)) {
            fill(50);
        } else {
            fill(99);
        }
        text(drawSpeed.toString().toLowerCase(), speedButtonX + (iconWidth / 2), iconCenterY);

        // " cancel button"
        textSize(65);
        if (onButton(cancelButtonX)) {
            fill(150, 40, 40);
        } else {
            fill(200, 40, 40);
        }
        text("X", cancelButtonX + (iconWidth / 2), iconCenterY);
    }

    private boolean onButton(float buttonX)
    {
        float xMax = buttonX + iconWidth;
        float yMin = iconCenterY - iconHalfHeight;
        float yMax = iconCenterY + iconHalfHeight;

        return pointerInRegion(buttonX, xMax, yMin, yMax);
    }

    private boolean onPanel()
    {
        return pointerInRegion(0, xDimension, panelTop, yDimension);
    }

    private boolean pointerInRegion(float xMin, float xMax, float yMin, float yMax)
    {
        return xMin <= mouseX && mouseX <= xMax && yMin <= mouseY && mouseY <= yMax;
    }

    private void cycleDrawMode()
    {
        List<DrawMode> values = Arrays.asList(DrawMode.values());
        int index = values.indexOf(drawMode) + 1;
        index = index == values.size() ? 0 : index;
        drawMode = values.get(index);
    }

    private void cycleDrawSpeed()
    {
        List<DrawSpeed> values = Arrays.asList(DrawSpeed.values());
        int index = values.indexOf(drawSpeed) + 1;
        index = index == values.size() ? 0 : index;
        drawSpeed = values.get(index);
    }

    private void drawInputPoint(Point point)
    {
        fill(123);
        strokeWeight(1);
        stroke(0);
        drawPoint(point, 5);
    }

    private void drawEventPoint()
    {
        if (eventPoint != null) {
            fill(123, 50, 50);
            strokeWeight(1);
            stroke(0);
            drawPoint(eventPoint, 12);
        }
    }

    private void drawPoint(Point point, int diameter)
    {
        float x = (float) point.x;
        float y = (float) point.y;
        ellipse(x, y, diameter, diameter);
    }

    private void waitForUserInputOrDelay()
    {
        switch(drawSpeed) {
            case PAUSE:
                waitingForInput = true;
                break;
            case SLOW:
                delay(1000);
                break;
            case NORMAL:
                delay(500);
                break;
            case FAST:
                delay(50);
                break;
        }
    }

    private void drawPolygon(Polygon polygon)
    {
        polygon.getEdges().forEach(edge -> drawPolygonEdge(edge));
    }

    private void drawPolygonEdge(Line line)
    {
        strokeWeight(2);
        stroke(0);
        drawLine(line);
    }

    private void drawSweepline()
    {
        if (sweepLine != null) {
            strokeWeight(3);
            stroke(123, 50, 74);
            drawLine(sweepLine);
        }
    }

    private void drawLine(Line line)
    {
        float x1 = (float) line.a.x;
        float y1 = (float)line.a.y;
        float x2 = (float) line.b.x;
        float y2 = (float) line.b.y;
        line(x1, y1, x2, y2);
    }

    private void drawMousePosition() {
        fill(123);
        textSize(12);
        textAlign(TOP, RIGHT);
        String text;
        if (onButton(playButtonX)) {
            text = "on play button!";
        } else if (onButton(nextButtonX)) {
            text = "on next button!";
        } else if (onButton(modeButtonX)) {
            text = "on mode button!";
        } else if (onButton(speedButtonX)) {
            text = "on speed button!";
        } else if (onButton(cancelButtonX)) {
            text = "on cancel button!";
        } else if (onPanel()) {
            text = "on panel but not on button!";
        } else {
            text = "( " + mouseX + ", " + mouseY + " )";
        }
        text(text, mouseX, mouseY - 5);
    }

    public static void main(String[] args)
    {
        PApplet.main("com.wicks.triangulation.PolygonTriangulation");
    }

    private enum DrawMode
    {
        POINT, HULL, DRAW
    }

    private enum DrawSpeed
    {
        PAUSE, SLOW, NORMAL, FAST
    }
}
