package us.cubk.irc.server.interfaces;

import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.server.user.User;
import us.cubk.irc.server.user.UserManager;

public interface PacketHandler<T extends IRCPacket> {
    void handle(T packet, Connection connection, UserManager userManager, User user);

    default boolean allowNull(){
        return false;
    }
}
