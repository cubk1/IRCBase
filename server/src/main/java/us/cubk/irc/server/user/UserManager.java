package us.cubk.irc.server.user;

import us.cubk.irc.packet.implemention.clientbound.ClientBoundUpdateUserListPacket;
import us.cubk.irc.server.interfaces.Connection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private final Map<String,User> sessionToUserMap = new ConcurrentHashMap<>();

    public void removeUser(String session){
        sessionToUserMap.remove(session);
    }

    public User getUser(String session){
        return sessionToUserMap.get(session);
    }

    public void putUser(String session,User user){
        sessionToUserMap.put(session,user);
    }

    public void syncUserList(Connection connection){
        Map<String,String> userList = new LinkedHashMap<>();
        for (User value : sessionToUserMap.values()) {
            userList.put(value.getUsername(),value.getIgn());
        }
        ClientBoundUpdateUserListPacket packet = new ClientBoundUpdateUserListPacket(userList);
        connection.sendPacket(packet);
    }
}
