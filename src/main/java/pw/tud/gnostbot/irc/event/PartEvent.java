package pw.tud.gnostbot.irc.event;

public class PartEvent extends Event
{
    private final String username;
    private final String channel;
    private final String message;

    public PartEvent(String username, String channel, String message)
    {
        this.username = username;
        this.channel = channel;
        this.message = message;
    }

    public String getUsername()
    {
        return username;
    }

    public String getChannel()
    {
        return channel;
    }

    public String getMessage()
    {
        return message;
    }
}
