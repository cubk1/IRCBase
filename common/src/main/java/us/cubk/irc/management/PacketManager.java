package us.cubk.irc.management;

import com.google.gson.JsonObject;
import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundConnectedPacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundDisconnectPacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundMessagePacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundUpdateUserListPacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundHandshakePacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundMessagePacket;
import us.cubk.irc.packet.implemention.serverbound.ServerBoundUpdateIgnPacket;
import us.cubk.irc.util.UnsafeReflect;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {
    private final Map<Integer,Class<? extends IRCPacket>> idToPacketMap = new HashMap<>();
    private final Map<Class<? extends IRCPacket>,Integer> packetToIdMap = new HashMap<>();
    private int id;

    public PacketManager(){
        // client bound

        register(ClientBoundDisconnectPacket.class, ClientBoundConnectedPacket.class, ClientBoundUpdateUserListPacket.class, ClientBoundMessagePacket.class);

        // server bound

        register(ServerBoundHandshakePacket.class,ServerBoundUpdateIgnPacket.class, ServerBoundMessagePacket.class);
    }

    @SafeVarargs
    private final void register(Class<? extends IRCPacket>... classes){
        for (Class<? extends IRCPacket> clazz : classes) {
            idToPacketMap.put(id, clazz);
            packetToIdMap.put(clazz,id);
            id++;
        }
    }

    public IRCPacket readPacket(JsonObject object){
        if(object.has("id") && object.has("cxt")){
            int id = object.get("id").getAsInt();
            IRCPacket packet = create(id);
            packet.readPacket(object.get("cxt").getAsJsonObject());
            return packet;
        }else {
            throw new RuntimeException("Unknown packet");
        }
    }

    public JsonObject writePacket(IRCPacket packet){
        JsonObject jsonObject = new JsonObject();
        JsonObject packetJson = packet.writePacket();
        jsonObject.addProperty("id",packetToIdMap.get(packet.getClass()));
        jsonObject.add("cxt",packetJson);
        return jsonObject;
    }

    public IRCPacket create(int id){
        if(!idToPacketMap.containsKey(id)){
            throw new IllegalArgumentException("Unknown packet: " + id);
        }
        Class<? extends IRCPacket> clazz = idToPacketMap.get(id);
        return create(clazz);
    }

    public IRCPacket create(Class<? extends IRCPacket> clazz){
        try {
            return (IRCPacket) UnsafeReflect.theUnsafe.allocateInstance(clazz);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
