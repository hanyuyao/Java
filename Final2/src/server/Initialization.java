package server;

import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Initialization{
    ArrayList<Socket> sockets;      //分別與兩個client連接的socket
    DynamicUIData dynamicUIData = new DynamicUIData();
    public Initialization(ArrayList<Socket> groups){
        sockets = groups;
    }

    public void initializeUIData(){
        //initial wall location
        int judgeWallLocation = (int)(Math.random()*5);
        switch(judgeWallLocation){
            case 0:
                dynamicUIData.pointWallRow = new Point(0, 5);
                dynamicUIData.pointWallColumn = new Point(30, 20);
                break;
            case 1:
                dynamicUIData.pointWallRow = new Point(5, 15);
                dynamicUIData.pointWallColumn = new Point(25, 20);
                break;
            case 2:
                dynamicUIData.pointWallRow = new Point(10, 20);
                dynamicUIData.pointWallColumn = new Point(15, 25);
                break;
            case 3:
                dynamicUIData.pointWallRow = new Point(15, 30);
                dynamicUIData.pointWallColumn = new Point(5, 10);
                break;
            case 4:
                dynamicUIData.pointWallRow = new Point(20, 35);
                dynamicUIData.pointWallColumn = new Point(5, 15);
                break;
            default:
                dynamicUIData.pointWallRow = new Point(0, 5);
                dynamicUIData.pointWallColumn = new Point(30, 20);
        }

        //initialize hole location
        int judgeHoleLocation = (int)(Math.random()*3);
        if(judgeWallLocation==0){
            switch (judgeHoleLocation){
                case 0:
                    dynamicUIData.pointHoleOne = new Point(10, 20);
                    dynamicUIData.pointHoleTwo = new Point(20, 35);
                    break;
                case 1:
                    dynamicUIData.pointHoleOne = new Point(10, 35);
                    dynamicUIData.pointHoleTwo = new Point(20, 15);
                    break;
                case 2:
                    dynamicUIData.pointHoleOne = new Point(15, 25);
                    dynamicUIData.pointHoleTwo = new Point(10, 30);
                    break;
            }
        }
        else if(judgeWallLocation==1){
            switch (judgeHoleLocation){
                case 0:
                    dynamicUIData.pointHoleOne = new Point(10, 5);
                    dynamicUIData.pointHoleTwo = new Point(10, 30);
                    break;
                case 1:
                    dynamicUIData.pointHoleOne = new Point(15, 35);
                    dynamicUIData.pointHoleTwo = new Point(40, 20);
                    break;
                case 2:
                    dynamicUIData.pointHoleOne = new Point(30, 10);
                    dynamicUIData.pointHoleTwo = new Point(5, 30);
                    break;
            }
        }
        else if(judgeWallLocation==2){
            switch (judgeHoleLocation){
                case 0:
                    dynamicUIData.pointHoleOne = new Point(20, 5);
                    dynamicUIData.pointHoleTwo = new Point(40, 20);
                    break;
                case 1:
                    dynamicUIData.pointHoleOne = new Point(5, 30);
                    dynamicUIData.pointHoleTwo = new Point(35, 10);
                    break;
                case 2:
                    dynamicUIData.pointHoleOne = new Point(25, 5);
                    dynamicUIData.pointHoleTwo = new Point(39, 15);
                    break;
            }
        }
        else if(judgeWallLocation==3){
            switch (judgeHoleLocation){
                case 0:
                    dynamicUIData.pointHoleOne = new Point(20, 10);
                    dynamicUIData.pointHoleTwo = new Point(30, 20);
                    break;
                case 1:
                    dynamicUIData.pointHoleOne = new Point(15, 35);
                    dynamicUIData.pointHoleTwo = new Point(35, 20);
                    break;
                case 2:
                    dynamicUIData.pointHoleOne = new Point(25, 5);
                    dynamicUIData.pointHoleTwo = new Point(30, 35);
                    break;
            }
        }
        else{               //judgeWallLocation==4
            switch (judgeHoleLocation){
                case 0:
                    dynamicUIData.pointHoleOne = new Point(10, 10);
                    dynamicUIData.pointHoleTwo = new Point(30, 20);
                    break;
                case 1:
                    dynamicUIData.pointHoleOne = new Point(15, 20);
                    dynamicUIData.pointHoleTwo = new Point(25, 10);
                    break;
                case 2:
                    dynamicUIData.pointHoleOne = new Point(15, 39);
                    dynamicUIData.pointHoleTwo = new Point(25, 20);
                    break;
            }
        }

        //initialize snake body (length = 2)
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(new Point(dynamicUIData.pointHoleOne.x+1, dynamicUIData.pointHoleOne.y));
        temp.add(dynamicUIData.pointHoleOne);
        dynamicUIData.arrayListSnakeBody.add(0, temp);
        dynamicUIData.intNewDirection.add(0, LocalUIData.RIGHT);

        ArrayList<Point> temp2 = new ArrayList<>();
        temp2.add(0, new Point(dynamicUIData.pointHoleTwo.x, dynamicUIData.pointHoleTwo.y-1));
        temp2.add(1, dynamicUIData.pointHoleTwo);
        dynamicUIData.arrayListSnakeBody.add(1, temp2);
        dynamicUIData.intNewDirection.add(1, LocalUIData.UP);

        //initial food
        initializeFood();

        //others
        dynamicUIData.booIsStart.add(0, false);
        dynamicUIData.booIsStart.add(1, false);
        dynamicUIData.booFoodOneEaten = false;
        dynamicUIData.booFoodTwoEaten = false;
        dynamicUIData.intSpeed = 60;
        dynamicUIData.intScores[0] = 0;
        dynamicUIData.intScores[1] = 0;
        dynamicUIData.intLives[0] = 3;
        dynamicUIData.intLives[1] = 3;
        dynamicUIData.booLife[0] = true;
        dynamicUIData.booLife[1] = true;


    }

    private boolean judgeFoodCoincidence(){
        //判斷有無撞蛇頭
        if(dynamicUIData.arrayListSnakeBody.get(0).get(0).equals(dynamicUIData.pointFoodOne) ||
                dynamicUIData.arrayListSnakeBody.get(0).get(0).equals(dynamicUIData.pointFoodTwo) ||
                dynamicUIData.arrayListSnakeBody.get(1).get(0).equals(dynamicUIData.pointFoodOne) ||
                dynamicUIData.arrayListSnakeBody.get(1).get(0).equals(dynamicUIData.pointFoodTwo))
            return true;
        //判斷有無撞牆
        for(int i = dynamicUIData.pointWallRow.x; i <= dynamicUIData.pointWallRow.x + LocalUIData.intWallLength; i++){
            Point point = new Point(i, dynamicUIData.pointWallRow.y);
            if(dynamicUIData.pointFoodOne.equals(point) || dynamicUIData.pointFoodTwo.equals(point)){
                return true;
            }
        }
        for(int i = dynamicUIData.pointWallColumn.y; i <= dynamicUIData.pointWallColumn.y + LocalUIData.intWallLength; i++){
            Point point = new Point(dynamicUIData.pointWallColumn.x, i);
            if(dynamicUIData.pointFoodOne.equals(point) || dynamicUIData.pointFoodTwo.equals(point)){
                return true;
            }
        }
        //判斷有無撞洞
        if(dynamicUIData.pointFoodOne.equals(dynamicUIData.pointHoleOne) || dynamicUIData.pointFoodOne.equals(dynamicUIData.pointFoodTwo) ||
                dynamicUIData.pointFoodTwo.equals(dynamicUIData.pointHoleOne) || dynamicUIData.pointFoodTwo.equals(dynamicUIData.pointHoleTwo)) {
            return true;
        }
        //判斷兩蛋是否重合
        if(dynamicUIData.pointFoodOne.equals(dynamicUIData.pointFoodTwo))
            return true;

        return false;
    }

    private class FoodThread extends Thread{
        public void run() {
            while(!Thread.currentThread().isInterrupted() && dynamicUIData.intLives[0]!=0 && dynamicUIData.intLives[1]!=0){
                if(dynamicUIData.booFoodOneEaten && dynamicUIData.booFoodTwoEaten) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }

                    synchronized (dynamicUIData) {
                        initializeFood();
                    }

                    try {
                        Thread.sleep(200);

                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    private void initializeFood(){
        dynamicUIData.booFoodOneEaten = false;
        dynamicUIData.booFoodTwoEaten = false;
        do {
            int foodOneX = (int) (Math.random() * LocalUIData.WIDTH);
            int foodOneY = (int) (Math.random() * LocalUIData.HEIGHT);
            int foodTwoX = (int) (Math.random() * LocalUIData.WIDTH);
            int foodTwoY = (int) (Math.random() * LocalUIData.HEIGHT);
            dynamicUIData.pointFoodOne = new Point(foodOneX, foodOneY);
            dynamicUIData.pointFoodTwo = new Point(foodTwoX, foodTwoY);

        } while (judgeFoodCoincidence());
    }

    public void start(){
        //initialize dynamicUIData
        initializeUIData();

        ArrayList<ObjectOutputStream> outputs  = new ArrayList<>();
        for(int i = 0; i < sockets.size(); i++){
            try{
                ObjectInputStream brInFromClient = new ObjectInputStream(sockets.get(i).getInputStream());
                ObjectOutputStream doOutToClient = new ObjectOutputStream(sockets.get(i).getOutputStream());
                //broadcast UI
                    //transmit what you new (the initial play board) to client
                dynamicUIData.intPosition = i;
                doOutToClient.writeObject(dynamicUIData);
                outputs.add(doOutToClient);
                    //you'll new two the updaters (for two clients)
                Updater updater = new Updater(brInFromClient, dynamicUIData, i);
                updater.start();
            } catch(Exception e){
                JOptionPane.showMessageDialog(null, "new object stream error.");
            }
        }
        BroadcastTimer broadcastTimer = new BroadcastTimer(outputs, dynamicUIData);
        broadcastTimer.start();

        FoodThread foodThread = new FoodThread();
        foodThread.start();
    }
}
