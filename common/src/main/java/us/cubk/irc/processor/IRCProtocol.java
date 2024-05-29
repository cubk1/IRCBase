package us.cubk.irc.processor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import us.cubk.irc.management.PacketManager;
import us.cubk.irc.packet.IRCPacket;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class IRCProtocol implements Protocol<IRCPacket> {
    private final PacketManager packetManager = new PacketManager();
    private final Gson gson = new Gson();

    public byte[] encode(IRCPacket packet){
        return gson.toJson(packetManager.writePacket(packet)).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public IRCPacket decode(ByteBuffer readBuffer, AioSession session) {
        int remaining = readBuffer.remaining();
        if (remaining < Integer.BYTES) {
            return null;
        }
        readBuffer.mark();
        int length = readBuffer.getInt();
        if (length > readBuffer.remaining()) {
            readBuffer.reset();
            return null;
        }
        byte[] b = new byte[length];
        readBuffer.get(b);
        readBuffer.mark();

        String text = new String(b);

        // 你可以在这里实现加密

        try {
            JsonObject object = JsonParser.parseString(text).getAsJsonObject();
            return packetManager.readPacket(object);
        }catch (Exception e){
            e.printStackTrace();
            session.close();
            return null;
        }
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }
}
