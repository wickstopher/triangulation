import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Created by wickstopher on 10/8/16.
 */
public class TestMain extends PApplet
{
    private boolean proceed;
    private LineIntersection interComp;
    private List<IntersectionEvent> intersections;
    private List<Line> lines;

    public void settings()
    {
        interComp = new LineIntersection();
        size(1000, 1000);
    }

    public void draw()
    {
         if (lines != null && intersections != null) {
             background(255);
             lines.forEach(l -> line(l));
             intersections.forEach(i -> point(i.point));
         }
    }

    public void mouseClicked()
    {
        try {
            lines = getLines(10);
            intersections = interComp.computeIntersections(lines);
        } catch (Exception e) {
            background(255);
            lines.forEach(l ->  {
                line(l);
                System.out.println(l);
            });
            delay(500);
            throw e;
        }
    }

    public static void main(String[] args)
    {
        PApplet.main("TestMain");
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

    public static List<Line> getLines(int n)
    {
        List<Line> lines = new ArrayList<>(n);
        IntStream.range(0, n).forEach(i -> lines.add(getRandomLine(i)));
        return lines;
    }

    public static Line getRandomLine(int i)
    {
        Random random = new Random();

        return new Line(
            random.nextInt(1000),
            random.nextInt(1000),
            random.nextInt(1000),
            random.nextInt(1000)
        );
    }
}
