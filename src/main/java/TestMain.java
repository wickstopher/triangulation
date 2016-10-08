import processing.core.PApplet;

import java.util.TreeMap;

/**
 * Created by wickstopher on 10/8/16.
 */
public class TestMain extends PApplet
{
    public void settings()
    {
        size(200, 200);

        TreeMap<Integer, Integer> x = new TreeMap<>();
    }

    public void draw()
    {
        background(0);
        ellipse(mouseX, mouseY, 20, 20);
        point(2, 3);
    }

    public static void main(String[] args)
    {
        PApplet.main("TestMain");
    }
}
