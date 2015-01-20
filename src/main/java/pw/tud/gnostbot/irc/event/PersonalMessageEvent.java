package pw.tud.gnostbot.irc.event;

import pw.tud.gnostbot.irc.IrcUser;

public class PersonalMessageEvent extends Event
{
    IrcUser sender;
    String message;

    public PersonalMessageEvent(IrcUser sender, String message)
    {
        this.sender = sender;
        this.message = message;
    }

    public IrcUser getSender()
    {
        return sender;
    }

    public String getMessage()
    {
        return message;
    }
}
