import java.util.*;
import java.util.Map.Entry;

/**
 * Created by wickstopher on 10/8/16.
 */
public class SweepLineStatus {
    private TreeMap<Double, Line> status;

    public SweepLineStatus() {
        status = new TreeMap<>();
    }

    public void add(Line segment, double xPosition, EventQueue events) {

        double key = segment.yPosition(xPosition);
        Entry<Double, Line> above = above(key);
        Entry<Double, Line> below = below(key);
        List<Line> removed = new ArrayList<>();

        put(key, segment);

        while (above != null && !above.getValue().isAbove(segment, xPosition)) {
            status.remove(above.getKey());
            removed.add(above.getValue());
            above = above(key);
            below = below(key);
        }

        while (below != null && !segment.isAbove(below.getValue(), xPosition)) {
            status.remove(below.getKey());
            removed.add(below.getValue());
            above = above(key);
            below = below(key);
        }

        if (above != null) {
            checkForIntersectionsAndInsert(above.getValue(), segment, xPosition, events);
        }
        if (below != null) {
            checkForIntersectionsAndInsert(segment, below.getValue(), xPosition, events);
        }
        if (above != null && below != null) {
            checkForIntersectionsAndDelete(above.getValue(), below.getValue(), xPosition, events);
        }

        for (Line l : removed) {
            add(l, xPosition, events);
        }
    }

    public Optional<List<IntersectionEvent>> remove(Double key, EventQueue events)
    {
        List<IntersectionEvent> intersections = null;
        Entry<Double, Line> above = above(key);
        Entry<Double, Line> below = below(key);
        Line line = status.remove(key);

        if (line == null) {
            System.out.println("WTF!!!");
            return Optional.empty();
        }

        if (line.isVertical()) {
            intersections = new ArrayList<>();
            double yMin = Math.min(line.a.y, line.b.y);
            double yMax = Math.max(line.a.y, line.b.y);

            for (Line l : status.values()) {
                double ly = l.slope * key * l.intercept;
                if (yMin <= ly && ly <= yMax) {
                    intersections.add(new IntersectionEvent(line, l, new Point(key, ly)));
                }
            }
        } else if (above != null && below != null) {
            checkForIntersectionsAndInsert(above.getValue(), below.getValue(), key, events);
        }
        return Optional.ofNullable(intersections);
    }

    public void swap(Double k1, Double k2, double xPosition, EventQueue events)
    {
        Line valueOne = status.get(k1);
        Line valueTwo = status.get(k2);

        Entry<Double, Line> nowAboveTwo = above(k1);
        Entry<Double, Line> nowBelowOne = below(k2);

        if (status.higherKey(k2) != k1) {
            Double intermediate = k1;
            k1 = k2;
            k2 = k1;
        }

        put(k1, valueTwo);
        put(k2, valueOne);

        if (nowAboveTwo != null) {
            checkForIntersectionsAndInsert(nowAboveTwo.getValue(), valueTwo, xPosition, events);
            checkForIntersectionsAndDelete(valueOne, nowAboveTwo.getValue(), xPosition, events);
        }

        if (nowBelowOne != null) {
            checkForIntersectionsAndInsert(valueOne, nowBelowOne.getValue(), xPosition, events);
            checkForIntersectionsAndDelete(valueTwo, nowBelowOne.getValue(), xPosition, events);
        }
    }

    public void put(Double key, Line value)
    {
        status.put(key, value);
        value.statusKey = key;
        if (!status.get(value.statusKey).equals(value)) throw new RuntimeException();
    }

    public Entry<Double, Line> above(double key) {
        return status.higherEntry(key);
    }

    public Entry<Double, Line> below(double key) {
        return status.lowerEntry(key);
    }

    private void checkForIntersectionsAndInsert(final Line l1, final Line l2, double xPosition, EventQueue events)
    {
        l1.intersectionWith(l2).ifPresent(p -> {
            if (p.x > xPosition) {
                events.push(new IntersectionEvent(l1, l2, p));
            }
        });
    }

    private void checkForIntersectionsAndDelete(final Line l1, final Line l2, double xPosition, EventQueue events)
    {
        l1.intersectionWith(l2).ifPresent(p -> {
            if (p.x > xPosition) {
                events.delete(p);
            }
        });
    }
}
