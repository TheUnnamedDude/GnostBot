package pw.tud.gnostbot.irc.event;

import pw.tud.gnostbot.irc.IrcUser;

public class ChannelMessageEvent extends Event
{
    private String channel;
    private IrcUser sender;
    private String message;

    public ChannelMessageEvent(String channel, IrcUser sender, String message)
    {
        this.channel = channel;
        this.sender = sender;
        this.message = message;
    }

    public String getChannel()
    {
        return channel;
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
