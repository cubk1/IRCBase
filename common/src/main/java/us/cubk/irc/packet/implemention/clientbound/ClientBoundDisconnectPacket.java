package us.cubk.irc.packet.implemention.clientbound;

import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.packet.annotations.ProtocolField;

public class ClientBoundDisconnectPacket implements IRCPacket {
    @ProtocolField("r")
    private final String reason;

    public ClientBoundDisconnectPacket(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
