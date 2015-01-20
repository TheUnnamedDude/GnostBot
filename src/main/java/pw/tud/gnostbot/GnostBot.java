package pw.tud.gnostbot;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pw.tud.gnostbot.irc.IrcBot;
import pw.tud.gnostbot.irc.event.ChannelMessageEvent;
import pw.tud.gnostbot.irc.event.ConnectedEvent;

public class GnostBot
{
    Logger logger = LogManager.getLogger(GnostBot.class);
    IrcBot bot = new IrcBot();

    public static void main(String[] args)
    {
        new GnostBot().connect(args[0], "TudBot2001", null);
    }

    public void connect(String server, String username, String oauthToken)
    {
        bot.setLogger(logger);
        bot.addEventHandler(ConnectedEvent.class, (e) -> bot.joinChannel("#TheUnnamedDude"));
        bot.addEventHandler(ChannelMessageEvent.class, (e) -> {
            String command = null;
            String[] args = new String[0];
            if (e.getMessage().startsWith("!"))
            {
                command = e.getMessage().substring(1, e.getMessage().indexOf(' '));
                args = e.getMessage().substring(e.getMessage().indexOf(' ') + 1).split(" ");
            }
            else if (e.getMessage().startsWith(bot.getNick()))
            {
                command = e.getMessage().substring(e.getMessage().indexOf(' ') + 1);
                args = command.substring(command.indexOf(' ') + 1).split(" ");
                command = command.substring(0, command.indexOf(' '));
            }
            else if (e.getMessage().startsWith("u ar suk"))
            {
                nick = (bot.getNick())
                command = "/ban " + nick;
            }
            if ("nick".equals(command))
            {
                if (args.length > 0)
                {
                    bot.changeNick(args[0]);
                }
            }
        });
        bot.connect(server, 6667, username, oauthToken);
    }
}
