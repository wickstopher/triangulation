import java.util.TreeMap;

/**
 * Created by wickstopher on 10/8/16.
 */
public class EventQueue
{
    private TreeMap<Point, SweepLineEvent> queue;

    public EventQueue()
    {
        queue = new TreeMap<>();
    }

    public SweepLineEvent pop()
    {
        return queue.remove(queue.firstKey());
    }

    public void push(SweepLineEvent event)
    {
        queue.put(event.getKey(), event);
    }

    public void delete(Point eventPoint)
    {
        queue.remove(eventPoint);
    }
}
