package us.cubk.irc.server.handler;

import org.tinylog.Logger;
import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundHandshakePacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundMessagePacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundUpdateIgnPacket;
import us.cubk.irc.server.handler.implemention.HandshakeHandler;
import us.cubk.irc.server.handler.implemention.MessageHandler;
import us.cubk.irc.server.handler.implemention.UpdateIGNHandler;
import us.cubk.irc.server.interfaces.PacketHandler;
import us.cubk.irc.server.interfaces.Connection;
import us.cubk.irc.server.user.User;
import us.cubk.irc.server.user.UserManager;

import java.util.HashMap;
import java.util.Map;

public class HandlerManager {
    private final Map<Class<? extends IRCPacket>, PacketHandler> classToHandlerMap = new HashMap<>();

    public HandlerManager(){
        classToHandlerMap.put(ServerBoundHandshakePacket.class,new HandshakeHandler());
        classToHandlerMap.put(ServerBoundUpdateIgnPacket.class,new UpdateIGNHandler());
        classToHandlerMap.put(ServerBoundMessagePacket.class,new MessageHandler());
    }

    public boolean allowNull(IRCPacket packet){
        if(!classToHandlerMap.containsKey(packet.getClass())){
            Logger.warn("No " + packet.getClass().getSimpleName() + " handler found.");
            return false;
        }
        return classToHandlerMap.get(packet.getClass()).allowNull();
    }

    public void handlePacket(IRCPacket packet, Connection connection, UserManager userManager, User user){
        if(!classToHandlerMap.containsKey(packet.getClass())){
            Logger.warn("No " + packet.getClass().getSimpleName() + " handler found.");
            return;
        }
        classToHandlerMap.get(packet.getClass()).handle(packet, connection,userManager,user);
    }
}
