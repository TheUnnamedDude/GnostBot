package pw.tud.gnostbot.irc.event.error;

import pw.tud.gnostbot.irc.event.Event;

public class ErrorEvent extends Event
{
    private final String errorId;
    private final String message;

    public ErrorEvent(String errorId, String message)
    {
        this.errorId = errorId;
        this.message = message;
    }

    public String getErrorId()
    {
        return errorId;
    }

    public String getMessage()
    {
        return message;
    }
}
