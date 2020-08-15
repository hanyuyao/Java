package client.controller;

import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

//control client 1
public class ControllerP2 extends KeyAdapter {
    DynamicUIData dynamicUIData;

    public ControllerP2(DynamicUIData dynamicUIData){
        this.dynamicUIData = dynamicUIData;
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(1, LocalUIData.UP);
                //}
                break;
            case KeyEvent.VK_S:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(1, LocalUIData.DOWN);
                //}
                break;
            case KeyEvent.VK_A:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(1, LocalUIData.LEFT);
                //}
                break;
            case KeyEvent.VK_D:
                //synchronized (dynamicUIData) {
                    dynamicUIData.intNewDirection.set(1, LocalUIData.RIGHT);
                //}
                break;
            case KeyEvent.VK_F:
                if(dynamicUIData.intSpeed < 90)
                    dynamicUIData.intSpeed += 10;
                try {
                    LocalUIData.doOutToServer.reset();
                    LocalUIData.doOutToServer.writeObject(dynamicUIData);
                } catch (IOException e1) {
                    //JOptionPane.showMessageDialog(null, "Snake move do out to server error.");
                }
                break;
            case KeyEvent.VK_G:
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
