package client;

import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client{
    public final static int PORT = 5268;

    public LocalUIData localUIData;
    //用dynamicUIDataFromServer來更新dynamicUIData
    public DynamicUIData dynamicUIData;
    public DynamicUIData dynamicUIDataFromServer;

    public Client(DynamicUIData dynamicUIData, LocalUIData localUIData){
        this.dynamicUIData = dynamicUIData;
        this.localUIData = localUIData;
    }

    public void connect(String stringIP){            //按下connect鍵做的事
        Socket socketClient = null;
        //如果client輸入錯誤的IP就繼續讀IP
        try {
            socketClient = new Socket(stringIP, PORT);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Wrong IP, Try again.");
        }

        if(socketClient != null) {
            LocalUIData.isConnected = true;
            //若已連接就移除button connect的listener
            localUIData.buttonConnect.removeActionListener(localUIData.buttonConnect.getActionListeners()[0]);
            //告知client已連接成功
            JOptionPane.showMessageDialog(null, "Connected!");

            try {
                LocalUIData.doOutToServer = new ObjectOutputStream(socketClient.getOutputStream());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "new objectOutputStream error.");
            }

            try {
                LocalUIData.brInFromServer = new ObjectInputStream(socketClient.getInputStream());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "new objectInputStream error.");
            }

            //先讀一次初始化client的dynamicUIData
                    //server會等2個client都連接上了才會開始傳資料，在那之前client會一直讀，直到讀到為止
                    try {
                while (true) {
                    dynamicUIDataFromServer = (DynamicUIData)LocalUIData.brInFromServer.readObject();
                    if (dynamicUIDataFromServer != null) {
                        break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Can't read object from server.");
            }
            initialDynamicUIData();
            listenServer();

            //Client接收server傳來的資料一次，PlayPanel才new
            localUIData.playPanel = new PlayPanel(dynamicUIData, localUIData);
            localUIData.pane.add(localUIData.playPanel, "panelPlay");

            //不斷讀取從server傳來的資料，不斷更新本地資料
            ClientUpdater clientUpdater = new ClientUpdater();
            clientUpdater.start();
        }
    }

    public void initialDynamicUIData(){
        localUIData.intPosition = dynamicUIDataFromServer.intPosition;
        localUIData.intRivalPosition = (localUIData.intPosition==0)? 1 : 0;

        //只初始arraylist
        dynamicUIData.booIsStart.add(0, false);
        dynamicUIData.booIsStart.add(1, false);

        dynamicUIData.arrayListSnakeBody.add(0, dynamicUIDataFromServer.arrayListSnakeBody.get(0));
        dynamicUIData.arrayListSnakeBody.add(1, dynamicUIDataFromServer.arrayListSnakeBody.get(1));

        dynamicUIData.intNewDirection.add(0, dynamicUIDataFromServer.intNewDirection.get(0));
        dynamicUIData.intNewDirection.add(1, dynamicUIDataFromServer.intNewDirection.get(1));
    }

    public void listenServer(){
        //根據 dynamicUIDataFromServer 去更新 dynamicUIData(本地)
        dynamicUIData.pointWallRow = dynamicUIDataFromServer.pointWallRow;
        dynamicUIData.pointWallColumn = dynamicUIDataFromServer.pointWallColumn;

        dynamicUIData.pointHoleOne = dynamicUIDataFromServer.pointHoleOne;
        dynamicUIData.pointHoleTwo = dynamicUIDataFromServer.pointHoleTwo;

        dynamicUIData.pointFoodOne = dynamicUIDataFromServer.pointFoodOne;
        dynamicUIData.pointFoodTwo = dynamicUIDataFromServer.pointFoodTwo;

        dynamicUIData.booFoodOneEaten = dynamicUIDataFromServer.booFoodOneEaten;
        dynamicUIData.booFoodTwoEaten = dynamicUIDataFromServer.booFoodTwoEaten;

        dynamicUIData.intSpeed = dynamicUIDataFromServer.intSpeed;
        dynamicUIData.booIsStart.set(0, dynamicUIDataFromServer.booIsStart.get(0));
        dynamicUIData.booIsStart.set(1, dynamicUIDataFromServer.booIsStart.get(1));

        dynamicUIData.arrayListSnakeBody.set(localUIData.intRivalPosition, dynamicUIDataFromServer.arrayListSnakeBody.get(localUIData.intRivalPosition));

        dynamicUIData.intNewDirection.set(localUIData.intRivalPosition, dynamicUIDataFromServer.intNewDirection.get(localUIData.intRivalPosition));

        localUIData.jLabelScores.setText(dynamicUIDataFromServer.intScores[localUIData.intPosition] + "分");
        localUIData.jLabelScores2.setText(dynamicUIDataFromServer.intScores[localUIData.intRivalPosition] + "分");
        localUIData.jLabelLives.setText(dynamicUIDataFromServer.intLives[localUIData.intPosition] + "命");
        localUIData.jLabelLives2.setText(dynamicUIDataFromServer.intLives[localUIData.intRivalPosition] + "命");

        if((dynamicUIDataFromServer.intLives[0]==0 || dynamicUIDataFromServer.intLives[1]==0) && !localUIData.isEnd){
            localUIData.endPanel = new EndPanel(localUIData);
            localUIData.pane.add(localUIData.endPanel, "panelEnd");

            localUIData.jLabelName.setFont(LocalUIData.font2);
            localUIData.jLabelLives.setFont(LocalUIData.font2);
            localUIData.jLabelScores.setFont(LocalUIData.font2);
            localUIData.jLabelName2.setFont(LocalUIData.font2);
            localUIData.jLabelLives2.setFont(LocalUIData.font2);
            localUIData.jLabelScores2.setFont(LocalUIData.font2);

            if(dynamicUIDataFromServer.intScores[localUIData.intPosition] > dynamicUIDataFromServer.intScores[localUIData.intRivalPosition] )
                localUIData.jLabelEnd.setText("You win...");
            else if(dynamicUIDataFromServer.intScores[localUIData.intPosition] < dynamicUIDataFromServer.intScores[localUIData.intRivalPosition] )
                localUIData.jLabelEnd.setText("You lose!!!");
            else
                localUIData.jLabelEnd.setText("HA! HA! Even...");

            localUIData.card.show(localUIData.pane,"panelEnd");
            localUIData.isEnd = true;
        }
    }

    class ClientUpdater extends Thread{
        @Override
        public void run() {
            while(true) {
                try {
                    dynamicUIDataFromServer = (DynamicUIData) LocalUIData.brInFromServer.readObject();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Server had closed.");
                    break;
                }
                if (dynamicUIDataFromServer != null) {
                    synchronized (dynamicUIData) {
                        listenServer();
                    }
                }
            }
        }
    }

}

