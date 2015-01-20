package pw.tud.gnostbot.irc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pw.tud.gnostbot.irc.event.Event;
import pw.tud.gnostbot.irc.event.EventHandler;

import java.nio.charset.Charset;
import java.util.*;

public class IrcBot
{
    private ChannelPipeline pipeline;
    private Charset charset = CharsetUtil.UTF_8;
    private final EventLoopGroup group = new NioEventLoopGroup(1);
    private Logger logger = LogManager.getLogger(IrcBot.class);
    private HashMap<Class<? extends Event>, ArrayList<EventHandler>> eventHandlers = new HashMap<>();
    private String username;
    private String password;
    private String nick;

    public void connect(String host, int port, String username, String password, String charset)
    {
        if (Charset.isSupported(charset))
        {
            this.charset = Charset.forName(charset);
        }
        connect(host, port, username, password);
    }

    public void connect(String host, int port, String username, String password, Charset charset)
    {
        this.charset = charset;
        connect(host, port, username, password);
    }

    public void connect(String host, int port, String username)
    {
        connect(host, port, username, null);
    }

    public void connect(String host, int port, String username, String password)
    {
        getLogger().info("Creating bootstrap");
        if (nick == null)
        {
            nick = username;
        }
        this.username = username;
        this.password = password;
        try
        {
            Bootstrap client = new Bootstrap()
                    .group(group)
                    .handler(new IrcClientInitializer(this))
                    .channel(NioSocketChannel.class);
            ChannelFuture f = client.connect(host, port).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }

    public <T extends Event> void addEventHandler(Class<T> event, EventHandler<T> handler)
    {
        getEventHandlers(event).add(handler);
    }

    public <T extends Event> ArrayList<EventHandler<T>> getEventHandlers(Class<T> event)
    {
        ArrayList<T> handlers = (ArrayList) eventHandlers.get(event);
        if (handlers == null)
        {
            handlers = new ArrayList<>();
        }
        eventHandlers.put(event, (ArrayList) handlers);
        return (ArrayList) eventHandlers.get(event);
    }

    /*public ArrayList<EventHandler> getEventHandlers(Class<? extends Event> event)
    {
        ArrayList<EventHandler> eventHandlers = this.eventHandlers.get(event);
        if (eventHandlers == null)
        {
            eventHandlers = new ArrayList<>();
            this.eventHandlers.put(event, eventHandlers);
        }
        return eventHandlers;
    }*/

    public Logger getLogger()
    {
        return logger;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    void writeLine(String... lines)
    {
        for (String line : lines)
        {
            writeLine(line);
        }
    }

    void writeLine(String line)
    {
        pipeline.writeAndFlush(line + "\r\n");
        getLogger().trace("client> " + line);
    }

    public void joinChannel(String channel)
    {
        writeLine("JOIN " + channel);
    }

    public void partChannel(String channel)
    {
        writeLine("PART " + channel);
    }

    public void joinChannel(String channel, String password)
    {
        writeLine("JOIN " + channel + " " + password);
    }

    public void changeNick(String nick)
    {
        writeLine("NICK " + nick);
        this.nick = nick; // TODO: Do this properly
    }

    public void sendMessage(String channelOrUser, String message)
    {
        writeLine("PRIVMSG " + channelOrUser + " " + message);
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getNick()
    {
        return nick;
    }

    public <T extends Event> void executeEvent(Class<T> eventType, T event)
    {
        getEventHandlers(eventType).forEach((e) -> e.onEvent(event));
    }

    public IrcUser getUser(String nick)
    {
        return new IrcUser(nick);
    }

    public static class IrcClientInitializer extends ChannelInitializer<SocketChannel>
    {
        private static ByteBuf NEWLINE = Unpooled.wrappedBuffer(new byte[]{0x0D, 0x0A});
        IrcBot bot;

        public IrcClientInitializer(IrcBot bot)
        {
            this.bot = bot;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception
        {
            ch.pipeline().addLast("newline-decoder", new DelimiterBasedFrameDecoder(512, NEWLINE));
            ch.pipeline().addLast("string-decoder", new StringDecoder(bot.charset));

            ch.pipeline().addLast("string-encoder", new StringEncoder(bot.charset));
            ch.pipeline().addLast("handler", new IrcHandler(bot));
            bot.pipeline = ch.pipeline();
        }
    }
}
