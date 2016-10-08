/**
 * Created by wickstopher on 10/8/16.
 */
public class LeftEndpointEvent extends SweepLineEvent
{
    public final Line line;

    public LeftEndpointEvent(Line line)
    {
        super(line.a);
        this.line = line;
    }
}
