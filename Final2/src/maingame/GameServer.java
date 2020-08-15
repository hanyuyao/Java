package maingame;

import general.uidata.LocalUIData;
import server.Server;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameServer {
    public static void main(String[] args){
        try {
            InetAddress myComputer = InetAddress.getLocalHost();
            String IP = myComputer.getHostAddress();
            String notify = "Your server IP: " + IP + "\n" +
                    "You have to click OK button first and then clients can connect.";
            UIManager.put("OptionPane.messageFont", LocalUIData.font1);
            JOptionPane.showMessageDialog(null, notify, "Server IP", JOptionPane.INFORMATION_MESSAGE);
        } catch(UnknownHostException e){
            JOptionPane.showMessageDialog(null, "Unknown host exception.");
        }


        Server server = new Server();
        server.run();
    }
}
