/**
 * Created by wickstopher on 10/8/16.
 */
public class RightEndpointEvent extends SweepLineEvent
{
    public final Line line;

    public RightEndpointEvent(Line line)
    {
        super(line.b);
        this.line = line;
    }
}
