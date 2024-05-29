import us.cubk.irc.client.IRCHandler;
import us.cubk.irc.client.IRCTransport;

import java.io.IOException;
import java.util.Scanner;

public class IRCTest {
    public static void main(String[] args) {
        try {
            IRCTransport transport = new IRCTransport("localhost", 8888, new IRCHandler() {
                @Override
                public void onMessage(String sender,String message) {
                    System.out.println(sender + ": " +message);
                }

                @Override
                public void onDisconnected(String message) {
                    System.out.println("Disconnected: " + message);
                }

                @Override
                public void onConnected() {
                    System.out.println("Connected");
                }

                @Override
                public String getInGameUsername() {
                    return "cubk";
                }
            });
            transport.connect("cubk","123");
            Scanner scanner = new Scanner(System.in);
            while (true){
                transport.sendChat(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
