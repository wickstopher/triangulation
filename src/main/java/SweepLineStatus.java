import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wickstopher on 10/8/16.
 */
public class SweepLineStatus
{
    private TreeMap<Double, Line> status;

    public SweepLineStatus()
    {
        status = new TreeMap<>();
    }

    public void add(Line segment)
    {
        status.put(segment.a.y, segment);
    }

    public Line pop()
    {
        return status.remove(status.firstKey());
    }

    public void swap(Double k1, Double k2)
    {
        Line valueOne = status.get(k1);
        Line valueTwo = status.put(k2, valueOne);
        status.put(k2, valueTwo);
    }

    public Map.Entry<Double, Line> getNext(double key)
    {
        return status.higherEntry(key);
    }

    public Map.Entry<Double, Line> getPrevious(double key)
    {
        return status.lowerEntry(key);
    }
}
