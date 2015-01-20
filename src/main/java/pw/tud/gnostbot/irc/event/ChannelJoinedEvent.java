package pw.tud.gnostbot.irc.event;

import pw.tud.gnostbot.irc.ChannelMode;

public class ChannelJoinedEvent extends Event
{
    private final String channel;
    private final ChannelMode channelMode;

    public ChannelJoinedEvent(String channel, ChannelMode channelMode)
    {
        this.channel = channel;
        this.channelMode = channelMode;
    }

    public String getChannel()
    {
        return channel;
    }

    public ChannelMode getChannelMode()
    {
        return channelMode;
    }
}
