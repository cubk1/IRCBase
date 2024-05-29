package us.cubk.irc.server.handler.implemention;

import org.tinylog.Logger;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundMessagePacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundMessagePacket;
import us.cubk.irc.server.IRCServer;
import us.cubk.irc.server.interfaces.Connection;
import us.cubk.irc.server.interfaces.PacketHandler;
import us.cubk.irc.server.user.User;
import us.cubk.irc.server.user.UserManager;

public class MessageHandler implements PacketHandler<ServerBoundMessagePacket> {
    @Override
    public void handle(ServerBoundMessagePacket packet, Connection connection, UserManager userManager, User user) {

        IRCServer.getInstance().boardCastMessage(new ClientBoundMessagePacket(user.getUsername(),packet.getMessage()));

        Logger.info("Chat Message: {} >> {}",user.getUsername(),packet.getMessage());
    }
}
