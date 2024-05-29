package us.cubk.irc.server.handler.implemention;

import org.tinylog.Logger;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundConnectedPacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundDisconnectPacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundHandshakePacket;
import us.cubk.irc.server.interfaces.PacketHandler;
import us.cubk.irc.server.interfaces.Connection;
import us.cubk.irc.server.user.User;
import us.cubk.irc.server.user.UserManager;

public class HandshakeHandler implements PacketHandler<ServerBoundHandshakePacket> {
    @Override
    public void handle(ServerBoundHandshakePacket packet, Connection connection, UserManager userManager, User user) {
        Logger.info("User " + packet.getUsername() + " start handshake");

        // 你可以在这里实现验证Token
        // packet.getToken()

        if(userManager.getUser(connection.getSessionId()) != null){
            connection.sendPacket(new ClientBoundDisconnectPacket("你已经连接到了这个服务器！"));
        }else {
            userManager.putUser(connection.getSessionId(),new User(connection.getSessionId(),packet.getUsername(),packet.getToken()));
            connection.sendPacket(new ClientBoundConnectedPacket());
            Logger.info("Accepted user {} ({}/{})",connection.getSessionId(),connection.getIPAddress(),packet.getUsername());
        }
    }

    @Override
    public boolean allowNull() {
        return true;
    }
}
