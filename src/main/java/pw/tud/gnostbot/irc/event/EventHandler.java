package pw.tud.gnostbot.irc.event;

public interface EventHandler<T extends Event>
{
    public void onEvent(T event);
}
