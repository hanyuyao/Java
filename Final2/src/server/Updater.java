package server;

import general.uidata.DynamicUIData;

import javax.swing.*;
import java.io.ObjectInputStream;

//Updater uses brInFromClient to update the dynamicUIData
public class Updater extends Thread {
    ObjectInputStream brInFromClient;
    DynamicUIData dynamicUIData;
    int intPosition;
    public DynamicUIData dynamicUIDataFromClient;
    public Updater(ObjectInputStream brInFromClient, DynamicUIData dynamicUIDataint, int intPosition){
        this.brInFromClient = brInFromClient;
        this.dynamicUIData = dynamicUIDataint;
        this.intPosition = intPosition;
    }


    @Override
    public void run() {
        //一直讀取client傳的資料，並更新dynamicUIData (Server端的)
        while(true) {
            try {
                dynamicUIDataFromClient = (DynamicUIData) brInFromClient.readObject();
                synchronized (dynamicUIData) {
                    updateData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Client " + intPosition + " disconnected...",
                        "Updater", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void updateData(){
        dynamicUIData.booFoodOneEaten = dynamicUIDataFromClient.booFoodOneEaten;
        dynamicUIData.booFoodTwoEaten = dynamicUIDataFromClient.booFoodTwoEaten;
        dynamicUIData.booIsStart.set(intPosition, dynamicUIDataFromClient.booIsStart.get(intPosition));
        dynamicUIData.intNewDirection.set(intPosition, dynamicUIDataFromClient.intNewDirection.get(intPosition));
        dynamicUIData.arrayListSnakeBody.set(intPosition, dynamicUIDataFromClient.arrayListSnakeBody.get(intPosition));
        dynamicUIData.intSpeed = dynamicUIDataFromClient.intSpeed;

        dynamicUIData.intScores[intPosition] = dynamicUIDataFromClient.intScores[intPosition];
        dynamicUIData.intLives[intPosition] = dynamicUIDataFromClient.intLives[intPosition];
        dynamicUIData.booLife[intPosition] = dynamicUIDataFromClient.booLife[intPosition];
    }

}

