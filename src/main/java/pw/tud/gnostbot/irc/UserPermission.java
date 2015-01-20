package pw.tud.gnostbot.irc;

public enum UserPermission
{
    Admin(' '),
    OP('@'),
    Voice('+'),
    Default(' ')
    ;
    final char permission;
    UserPermission(char permission)
    {
        this.permission = permission;
    }

    public char getPermission()
    {
        return permission;
    }

    public UserPermission getUserPermission(IrcUser user)
    {
        for (UserPermission userPermission : values())
        {
            if (user.getName().charAt(0) == userPermission.getPermission())
            {
                return userPermission;
            }
        }
        return Default;
    }
}
