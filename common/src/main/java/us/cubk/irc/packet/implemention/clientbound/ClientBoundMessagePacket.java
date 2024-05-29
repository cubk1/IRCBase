package us.cubk.irc.packet.implemention.clientbound;

import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.packet.annotations.ProtocolField;

public class ClientBoundMessagePacket implements IRCPacket {
    @ProtocolField("s")
    private final String sender;

    @ProtocolField("m")
    private final String message;

    public ClientBoundMessagePacket(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
