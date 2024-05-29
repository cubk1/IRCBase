package us.cubk.irc.server.interfaces;

import us.cubk.irc.packet.IRCPacket;

public interface Connection {
    void sendPacket(IRCPacket packet);
    String getIPAddress();
    String getSessionId();
    void disconnect(String message);
    void disconnect();
}
