package pw.tud.gnostbot.irc;

public enum UserMode
{
    Away('a', (byte)0x80),
    Invisible('i', (byte)0x40),
    Wallops('w', (byte)0x20),
    Restricted('r', (byte)0x10),
    Operator('o', (byte)0x08),
    LocalOperator('O', (byte)0x04)

    ;
    private char mode;
    private byte bitmask;
    UserMode(char mode, byte bitmask)
    {
        this.mode = mode;
        this.bitmask = bitmask;
    }

    public byte getBitmask()
    {
        return bitmask;
    }

    public char getModeChar()
    {
        return mode;
    }

    public static int addMode(byte current, UserMode mode)
    {
        current &= mode.getBitmask();
        return current;
    }
}
