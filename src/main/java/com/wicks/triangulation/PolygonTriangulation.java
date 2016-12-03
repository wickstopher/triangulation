package com.wicks.triangulation;

import com.wicks.pointtools.Line;
import com.wicks.pointtools.Point;
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
    private DrawMode drawMode;
    private DrawSpeed drawSpeed;
    private ArrayList<Point> points;
    private HullVisualizationState hullState;

    public void settings()
    {
        size(xDimension, yDimension);

        // initialize state
        drawMode = DrawMode.POINT;
        drawSpeed = DrawSpeed.NORMAL;
        reset();
    }

    private void reset()
    {
        points = new ArrayList<>();
        hullState = null;
    }

    public void draw()
    {
        background(190);
        drawPanel();
        points.forEach(p -> drawPoint(p));

        if (hullState != null) {
            if (hullState.hasNext()) {
                drawPolygonEdge(hullState.getNextLine());
                delay(50);
            }
            hullState.getEdges().forEach(line -> drawPolygonEdge(line));
        }

        if (debug) drawMousePosition();
    }

    public void mousePressed()
    {
        if (onButton(playButtonX)) {
            switch(drawMode) {
                case HULL:
                    hullState = new HullVisualizationState(Point.grahamsScan(points));
                    break;
            }
        }
        else if (onButton(modeButtonX)) {
            cycleDrawMode();
        } else if (onButton(speedButtonX)) {
            cycleDrawSpeed();
        } else if (onButton(cancelButtonX)) {
            reset();
        } else if (!onPanel()) {
            switch(drawMode) {
                case POINT:
                case HULL:
                    points.add(new Point(mouseX, mouseY));
                    break;
                case DRAW:
                   break;
            }
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

    private void drawPoint(Point point)
    {
        float x = (float) point.x;
        float y = (float) point.y;
        fill(123);
        strokeWeight(1);
        stroke(0);
        ellipse(x, y, 5, 5);
    }

    private void drawPolygonEdge(Line line)
    {
        float x1 = (float) line.a.x;
        float y1 = (float)line.a.y;
        float x2 = (float) line.b.x;
        float y2 = (float) line.b.y;

        strokeWeight(2);
        stroke(0);
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
