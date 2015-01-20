package pw.tud.gnostbot.irc;

public enum ChannelMode
{
    PRIVATE('*'),
    SECRET('@'),
    PUBLIC('=');

    private final char mode;
    ChannelMode(char mode)
    {
        this.mode = mode;
    }

    public char getModeChar()
    {
        return mode;
    }

    public static ChannelMode parse(char c)
    {
        for (ChannelMode channelMode : values())
        {
            if (channelMode.mode == c)
                return channelMode;
        }
        return null;
    }
}
