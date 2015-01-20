package pw.tud.gnostbot.irc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import pw.tud.gnostbot.irc.event.*;

public class IrcHandler extends ChannelInboundHandlerAdapter
{
    IrcBot bot;

    public IrcHandler(IrcBot bot)
    {
        this.bot = bot;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String line = (String) msg;
        bot.getLogger().trace(line);
        if (line.startsWith("PING"))
        {
            bot.writeLine("PONG " + line.substring(6));
        }
        if (line.startsWith(":"))
        {
            int hostNameLength = line.indexOf(' ') + 1;
            String hostname = line.substring(1, hostNameLength);
            int commandLength = line.indexOf(' ', hostNameLength);
            String serverCommand = line.substring(hostNameLength, commandLength);
            String content = line.substring(line.indexOf(' ', commandLength) + 1);
            String[] args;
            String extra = null;
            if (content.contains(" :"))
            {
                extra = content.substring(content.indexOf(" :") + 2);
                args = content.substring(0, content.indexOf(" :")).split(" ");
            }
            else
            {
                args = content.split(" ");
            }

            switch (serverCommand)
            {
                case "PRIVMSG":
                    String sender = parseSenderName(hostname);
                    if (args[0].startsWith("#"))
                    {
                        String channel = args[0];
                        ChannelMessageEvent event = new ChannelMessageEvent(channel, bot.getUser(sender), extra);
                        bot.executeEvent(ChannelMessageEvent.class, event);
                    }
                    else
                    {
                        PersonalMessageEvent event = new PersonalMessageEvent(bot.getUser(sender), extra);
                        bot.executeEvent(PersonalMessageEvent.class, event);
                    }
                    break;
                case "NOTICE":
                    //TODO
                    break;
                case "MODE":
                    //TODO
                    break;
                case "PART":
                    bot.executeEvent(PartEvent.class, new PartEvent(parseSenderName(hostname), args[0], extra));
                    break;
                case "001":
                    bot.executeEvent(ConnectedEvent.class, new ConnectedEvent(extra));
                    break;
                case "353":
                    //TODO: Update user list
                    ChannelMode mode = ChannelMode.parse(args[0].charAt(0));
                    // 2 - args.length should be user names now
                    for (int i = 2; i < args.length; i++)
                    {

                    }
                    bot.executeEvent(ChannelJoinedEvent.class, new ChannelJoinedEvent(args[1], mode));
                    break;
            }
        }
    }

    public String parseSenderName(String hostString)
    {
        if (hostString.contains("!"))
        {
            return hostString.substring(0, hostString.indexOf('!'));
        }
        return hostString;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        bot.getLogger().trace("Connecting...");
        bot.writeLine(
                "NICK " + bot.getUsername(),
                String.format("USER %1$s %1$s %1$s %1$s", bot.getUsername())
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
    }
}
