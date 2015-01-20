package pw.tud.gnostbot.irc.event;

public class ConnectedEvent extends Event
{
    public ConnectedEvent(String message)
    {
        this.message = message;
    }

    private final String message;

    public String getMessage()
    {
        return message;
    }
}
