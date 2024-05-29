package us.cubk.irc.server;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;
import org.tinylog.Logger;
import us.cubk.irc.packet.IRCPacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundDisconnectPacket;
import us.cubk.irc.packet.implemention.clientbound.ClientBoundMessagePacket;
import us.cubk.irc.processor.IRCProtocol;
import us.cubk.irc.server.handler.HandlerManager;
import us.cubk.irc.server.interfaces.Connection;
import us.cubk.irc.server.user.User;
import us.cubk.irc.server.user.UserManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IRCServer {
    private static IRCServer instance;
    private final UserManager userManager;
    private final Map<String,Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        new IRCServer();
    }

    public IRCServer() throws Exception{
        instance = this;
        Logger.info("Starting server...");

        userManager = new UserManager();
        HandlerManager handlerManager = new HandlerManager();
        IRCProtocol protocol = new IRCProtocol();

        MessageProcessor<IRCPacket> processor = new MessageProcessor<IRCPacket>() {
            @Override
            public void process(AioSession session, IRCPacket packet) {

                WriteBuffer outputStream = session.writeBuffer();
                if(connectionMap.get(String.valueOf(session.getSessionID())) == null){
                    connectionMap.put(String.valueOf(session.hashCode()),new Connection() {
                        @Override
                        public void sendPacket(IRCPacket packet) {
                            try {
                                byte[] bytes = protocol.encode(packet);
                                outputStream.writeInt(bytes.length);
                                outputStream.write(bytes);
                            }catch (Exception e){
                                Logger.error("Error while sending packet");
                                Logger.error(e);
                            }
                        }

                        @Override
                        public String getIPAddress() {
                            try {
                                return session.getRemoteAddress().getHostString();
                            } catch (IOException e) {
                                Logger.error("Error while reading address");
                                Logger.error(e);
                                return "";
                            }
                        }

                        @Override
                        public String getSessionId() {
                            return String.valueOf(session.hashCode());
                        }

                        @Override
                        public void disconnect(String message) {
                            ClientBoundDisconnectPacket packet = new ClientBoundDisconnectPacket(message);
                            sendPacket(packet);
                            disconnect();
                        }

                        @Override
                        public void disconnect() {
                            session.close();
                        }
                    });
                }

                Connection connection = connectionMap.get(String.valueOf(session.hashCode()));
                User user = userManager.getUser(String.valueOf(session.hashCode()));

                if(user != null || handlerManager.allowNull(packet)){
                    handlerManager.handlePacket(packet,connection,userManager,user);
                }
            }

            @Override
            public void stateEvent(AioSession session, StateMachineEnum stateMachineEnum, Throwable throwable) {
                MessageProcessor.super.stateEvent(session, stateMachineEnum, throwable);
                if(stateMachineEnum == StateMachineEnum.SESSION_CLOSED){
                    connectionMap.remove(String.valueOf(session.hashCode()));
                    User user = userManager.getUser(String.valueOf(session.hashCode()));
                    if(user != null){
                        userManager.removeUser(String.valueOf(session.hashCode()));
                        Logger.info("User {} disconnected.",user.getUsername());
                    }
                }
            }
        };

        AioQuickServer server = new AioQuickServer(8888, protocol, processor);
        server.setBannerEnabled(false);
        server.start();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = this::sendInGameUsername;
        scheduler.scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);

        Logger.info("Server started!");
    }

    public void boardCastMessage(ClientBoundMessagePacket packet){
        for (Connection value : connectionMap.values()) {
            value.sendPacket(packet);
        }
    }

    public void sendInGameUsername(){
        for (Connection value : connectionMap.values()) {
            userManager.syncUserList(value);
        }
    }

    public static IRCServer getInstance() {
        return instance;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
