import processing.core.PApplet;

import java.util.Optional;

/**
 * Created by wickstopher on 10/8/16.
 */
public class TestMain extends PApplet
{
    public void settings()
    {
        size(1000, 1000);
    }

    public void draw()
    {
        Line line1 = new Line(0, 200, 400, 500);
        Line line2 = new Line(200, 400, 5, 40);

        line(line1);
        line(line2);

        Optional<Point> point = line1.intersectionWith(line2);
        point.ifPresent(p -> point(p));
    }

    public static void main(String[] args)
    {
        //PApplet.main("TestMain");

        Line line1 = new Line(0, 200, 400, 500);
        Line line2 = new Line(0, 200, 403, 504);
        System.out.println(line1.intersectionWith(line2));
    }

    public void line(Line line)
    {
        float x1 = (float) line.a.x;
        float y1 = (float)line.a.y;
        float x2 = (float) line.b.x;
        float y2 = (float) line.b.y;
        line(x1, y1, x2, y2);
    }

    public void point(Point point)
    {
        float x = (float) point.x;
        float y = (float) point.y;
        ellipse(x, y, 5, 5);
    }
}
