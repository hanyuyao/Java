package server;

import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BroadcastTimer{
    //要兩玩家都開始才開始broadcast
    ArrayList<ObjectOutputStream> outputs;
    DynamicUIData dynamicUIData;

    public BroadcastTimer(ArrayList<ObjectOutputStream> outputs, DynamicUIData dynamicUIData){
        this.outputs = outputs;
        this.dynamicUIData = dynamicUIData;
    }

    public void start() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (dynamicUIData.booIsStart.get(0) && dynamicUIData.booIsStart.get(1)) {
                    for(int i = 0; i < 2; i++) {
                        try {
                            outputs.get(i).reset();
                            //synchronized (dynamicUIData) {
                                outputs.get(i).writeObject(dynamicUIData);
                            //}
                        } catch (IOException e) {
                            timer.cancel();
                        }
                    }
                }
            }
        };
        timer.schedule(timerTask, 10, 20);
    }
}
