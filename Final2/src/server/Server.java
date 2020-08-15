package server;

import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JFrame{
    public final static int PORT = 5268;
    private ArrayList<Object> objects;

    public Server(){
        super("Server");
        setSize(800, 170);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        //告知用戶 server已經開始執行
        JLabel jLabelInformation1 = new JLabel("Server running.");
        JLabel jLabelInformation2 = new JLabel("To shut down server, close this window.");
        jLabelInformation1.setFont(LocalUIData.font1);
        jLabelInformation2.setFont(LocalUIData.font1);
        this.add(jLabelInformation1);
        this.add(jLabelInformation2);
        setVisible(true);

        objects = new ArrayList<>();        //用來存放線程
    }

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't create server socket.");
        }

        ArrayList<Socket> sockets = new ArrayList<>();

        while(true){
            Socket socketServer = null;
            try {
                socketServer = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Server socket can't accept");
            }
            sockets.add(socketServer);
            //接收到2個客戶端就生成一個group，並開一個線程執行
            if(sockets.size() == 2){
                ArrayList<Socket> groups = new ArrayList<>();
                for(int i = 0; i < 2; i++){
                    groups.add(sockets.get(i));
                }
                sockets.clear();
                Initialization initialization = new Initialization(groups);
                initialization.start();
                objects.add(initialization);
            }
        }
    }
}
