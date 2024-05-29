package us.cubk.irc.packet.implemention.serverbound;

import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.packet.annotations.ProtocolField;

public class ServerBoundMessagePacket implements IRCPacket {
    @ProtocolField("m")
    private final String message;

    public ServerBoundMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
