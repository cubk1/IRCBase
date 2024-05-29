package us.cubk.irc.server.user;

public class User {
    private final String sessionId;
    private final String username;
    private final String token;
    private String ign;

    public User(String sessionId, String username, String token) {
        this.sessionId = sessionId;
        this.username = username;
        this.token = token;
        this.ign = "Unknown";
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getIgn() {
        return ign;
    }

    public void setIgn(String ign) {
        this.ign = ign;
    }
}
