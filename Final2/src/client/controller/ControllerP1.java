package client.controller;

import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

//control client 0
public class ControllerP1 extends KeyAdapter {
    DynamicUIData dynamicUIData;

    public ControllerP1(DynamicUIData dynamicUIData){
        this.dynamicUIData = dynamicUIData;
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(0, LocalUIData.UP);
                //}
                break;
            case KeyEvent.VK_DOWN:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(0, LocalUIData.DOWN);
                //}
                break;
            case KeyEvent.VK_LEFT:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(0, LocalUIData.LEFT);
                //}
                break;
            case KeyEvent.VK_RIGHT:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(0, LocalUIData.RIGHT);
                //}
                break;
            case KeyEvent.VK_SHIFT:
                if(dynamicUIData.intSpeed < 90)
                    dynamicUIData.intSpeed += 10;
                try {
                    LocalUIData.doOutToServer.reset();
                    LocalUIData.doOutToServer.writeObject(dynamicUIData);
                } catch (IOException e1) {
                    //JOptionPane.showMessageDialog(null, "Snake move do out to server error.");
                }
                break;
            case KeyEvent.VK_ENTER:
                if(dynamicUIData.intSpeed > 50)
                    dynamicUIData.intSpeed -= 10;
                try {
                    LocalUIData.doOutToServer.reset();
                    LocalUIData.doOutToServer.writeObject(dynamicUIData);
                } catch (IOException e1) {
                    //JOptionPane.showMessageDialog(null, "Snake move do out to server error.");
                }
                break;
        }
    }
}
