package us.cubk.irc.client;

public interface IRCHandler {
    void onMessage(String sender,String message);
    void onDisconnected(String message);
    void onConnected();
    String getInGameUsername();
}
