package us.cubk.irc.server.handler.implemention;

import org.tinylog.Logger;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundUpdateIgnPacket;
import us.cubk.irc.server.IRCServer;
import us.cubk.irc.server.interfaces.Connection;
import us.cubk.irc.server.interfaces.PacketHandler;
import us.cubk.irc.server.user.User;
import us.cubk.irc.server.user.UserManager;

public class UpdateIGNHandler implements PacketHandler<ServerBoundUpdateIgnPacket> {
    @Override
    public void handle(ServerBoundUpdateIgnPacket packet, Connection connection, UserManager userManager, User user) {
        String prevIgn = user.getIgn();
        if(!prevIgn.equals(packet.getName())){
            user.setIgn(packet.getName());
            IRCServer.getInstance().sendInGameUsername();
            Logger.info("User {} updated in-game-username {} -> {}",user.getUsername(),prevIgn,user.getIgn());
        }
    }
}
