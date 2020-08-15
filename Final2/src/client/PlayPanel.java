package client;

import client.controller.ControllerP1;
import client.controller.ControllerP2;
import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class PlayPanel extends JPanel implements Runnable{
    DynamicUIData dynamicUIData;
    LocalUIData localUIData;
    Point[] oldTail = new Point[2];

    public PlayPanel(DynamicUIData dynamicUIData, LocalUIData localUIData){
        this.dynamicUIData = dynamicUIData;
        this.localUIData = localUIData;

        ControllerP1 controllerP1 = new ControllerP1(dynamicUIData);
        ControllerP2 controllerP2 = new ControllerP2(dynamicUIData);
        if(localUIData.intPosition==0)
            addKeyListener(controllerP1);
        else
            addKeyListener(controllerP2);

        setLayout(null);
        localUIData.jLabelName.setBounds(830, 20, 180, 70);
        localUIData.jLabelName.setFont(LocalUIData.font3);
        localUIData.jLabelLives.setBounds(830, 100, 180, 70);
        localUIData.jLabelLives.setFont(LocalUIData.font3);
        localUIData.jLabelScores.setBounds(830, 180, 180, 70);
        localUIData.jLabelScores.setFont(LocalUIData.font3);
        localUIData.jLabelName2.setBounds(830, 450, 180, 70);
        localUIData.jLabelName2.setFont(LocalUIData.font3);
        localUIData.jLabelLives2.setBounds(830, 530, 180, 70);
        localUIData.jLabelLives2.setFont(LocalUIData.font3);
        localUIData.jLabelScores2.setBounds(830, 610, 180, 70);
        localUIData.jLabelScores2.setFont(LocalUIData.font3);
        add(localUIData.jLabelName);
        add(localUIData.jLabelName2);
        add(localUIData.jLabelLives);
        add(localUIData.jLabelLives2);
        add(localUIData.jLabelScores);
        add(localUIData.jLabelScores2);

        localUIData.jLabelInHole.setBounds(830, 320, 180, 70);
        localUIData.jLabelInHole.setFont(localUIData.font3);
        localUIData.jLabelInHole.setForeground(Color.RED);
        add(localUIData.jLabelInHole);
    }

    @Override
    public void run() {
        while(dynamicUIData.intLives[0] != 0  &&  dynamicUIData.intLives[1] != 0) {
            //如果沒有吃到身體或撞牆或吃到別人才動
            int k = localUIData.intPosition;
            if (!localUIData.booSnakeEatWall[k] && !localUIData.booSnakeEatBody[k] && !localUIData.booSnakeEatSnake[k] && !localUIData.booSnakeInHole[k]) {
                if (localUIData.intCurrentDirection[k] + dynamicUIData.intNewDirection.get(k) != 0) {
                    localUIData.intCurrentDirection[k] = dynamicUIData.intNewDirection.get(k);
                }
                synchronized (dynamicUIData.arrayListSnakeBody.get(k)) {
                    //去尾巴
                    oldTail[k] = dynamicUIData.arrayListSnakeBody.get(k).remove(dynamicUIData.arrayListSnakeBody.get(k).size() - 1);

                    //加頭
                    int x = dynamicUIData.arrayListSnakeBody.get(k).get(0).x;
                    int y = dynamicUIData.arrayListSnakeBody.get(k).get(0).y;
                    switch (localUIData.intCurrentDirection[k]) {
                        case LocalUIData.UP:
                            y--;
                            if (y < 0) {
                                y = LocalUIData.HEIGHT;
                            }
                            break;

                        case LocalUIData.DOWN:
                            y++;
                            if (y > LocalUIData.HEIGHT) {
                                y = 0;
                            }
                            break;

                        case LocalUIData.LEFT:
                            x--;
                            if (x < 0) {
                                x = LocalUIData.WIDTH;
                            }
                            break;

                        case LocalUIData.RIGHT:
                            x++;
                            if (x > LocalUIData.WIDTH) {
                                x = 0;
                            }
                            break;
                    }
                    Point newHead = new Point(x, y);
                    dynamicUIData.arrayListSnakeBody.get(k).add(0, newHead);

                    //每移動一次就傳給server
                    synchronized (dynamicUIData) {
                        try {
                            LocalUIData.doOutToServer.reset();
                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, "Snake move do out to server error.");
                        }
                    }
                }
                //每移動一次就判斷狀態
                judgeSnakeMoved();
            }
            //每移動一次就repaint一次
            repaint();
            localUIData.booSnakeEatFood[0] = false;
            localUIData.booSnakeEatFood[1] = false;
            try{Thread.sleep(dynamicUIData.intSpeed);} catch(Exception e){}

            if(localUIData.isEnd)
                break;
        }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(new ImageIcon("backGround.jpg").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawImage(new ImageIcon("playingBoard.jpg").getImage(), 0, 0, 1000, 820, null);

        drawWall(g);
        drawHole(g);
        drawFood(g);
        synchronized (dynamicUIData.arrayListSnakeBody) {
            drawSnake(g, localUIData.booSnakeInHole[0], localUIData.booSnakeEatFood[0], dynamicUIData.arrayListSnakeBody.get(0));
            drawSnake2(g, localUIData.booSnakeInHole[1], localUIData.booSnakeEatFood[1], dynamicUIData.arrayListSnakeBody.get(1));
        }

        if(localUIData.booSnakeEatWall[localUIData.intPosition] || localUIData.booSnakeEatBody[localUIData.intPosition] ||
                localUIData.booSnakeEatSnake[localUIData.intPosition]){
            g.drawImage(new ImageIcon("explosion.jpg").getImage(), 0, 0, 820, 820, null);
        }
    }

    //some methods
    private void drawWall(Graphics g){
        g.setColor(new Color(184, 115, 51));
        for(int i = dynamicUIData.pointWallRow.x; i <= dynamicUIData.pointWallRow.x + LocalUIData.intWallLength; i++){
            g.fill3DRect(i * LocalUIData.UNIT, dynamicUIData.pointWallRow.y * LocalUIData.UNIT, LocalUIData.UNIT, LocalUIData.UNIT, true);
        }
        for(int i = dynamicUIData.pointWallColumn.y; i <= dynamicUIData.pointWallColumn.y + LocalUIData.intWallLength; i++){
            g.fill3DRect(dynamicUIData.pointWallColumn.x * LocalUIData.UNIT, i * LocalUIData.UNIT, LocalUIData.UNIT, LocalUIData.UNIT, true);
        }
    }
    private void drawHole(Graphics g){
        int size = 4;
        g.setColor(new Color(171, 171, 171));
        g.fillOval(dynamicUIData.pointHoleOne.x * LocalUIData.UNIT - size, dynamicUIData.pointHoleOne.y * LocalUIData.UNIT - size,
                LocalUIData.UNIT + size*2, LocalUIData.UNIT + size*2);
        g.fillOval(dynamicUIData.pointHoleTwo.x * LocalUIData.UNIT - size, dynamicUIData.pointHoleTwo.y * LocalUIData.UNIT - size,
                LocalUIData.UNIT + size*2, LocalUIData.UNIT + size*2);

        g.setColor(new Color(0, 0, 0));
        g.fillOval(dynamicUIData.pointHoleOne.x * LocalUIData.UNIT, dynamicUIData.pointHoleOne.y * LocalUIData.UNIT ,
                LocalUIData.UNIT , LocalUIData.UNIT );
        g.fillOval(dynamicUIData.pointHoleTwo.x * LocalUIData.UNIT , dynamicUIData.pointHoleTwo.y * LocalUIData.UNIT ,
                LocalUIData.UNIT , LocalUIData.UNIT);
    }
    private void drawFood(Graphics g){
        if(!dynamicUIData.booFoodOneEaten) {
            int size = 2;
            g.setColor(new Color(205, 16, 118 ));
            g.fillOval(dynamicUIData.pointFoodOne.x * LocalUIData.UNIT - size, dynamicUIData.pointFoodOne.y * LocalUIData.UNIT - size,
                    LocalUIData.UNIT - size, LocalUIData.UNIT + size);

            g.setColor(new Color(255, 228, 225 ));
            g.fillOval(dynamicUIData.pointFoodOne.x * LocalUIData.UNIT + 5, dynamicUIData.pointFoodOne.y * LocalUIData.UNIT + 5,
                    LocalUIData.UNIT/2, LocalUIData.UNIT/2);
        }
        if(!dynamicUIData.booFoodTwoEaten) {
            int size = 2;
            g.setColor(new Color(205, 16, 118 ));
            g.fillOval(dynamicUIData.pointFoodTwo.x * LocalUIData.UNIT - size, dynamicUIData.pointFoodTwo.y * LocalUIData.UNIT - size,
                    LocalUIData.UNIT - size, LocalUIData.UNIT + size);

            g.setColor(new Color(255, 228, 225 ));
            g.fillOval(dynamicUIData.pointFoodTwo.x * LocalUIData.UNIT + 5, dynamicUIData.pointFoodTwo.y * LocalUIData.UNIT + 5,
                    LocalUIData.UNIT/2, LocalUIData.UNIT/2);
        }
    }
    private void drawSnake(Graphics g, boolean snakeInHole, boolean eatFood, ArrayList<Point> snakeBody) {
        if(!localUIData.booSnakeEatWall[0] && !localUIData.booSnakeEatBody[0] && !localUIData.booSnakeEatSnake[0]) {
            //head
            if (!snakeInHole) {
                g.setColor(new Color(25, 25, 112));
                if (eatFood) {
                    g.fillRoundRect(snakeBody.get(0).x * LocalUIData.UNIT - 5,
                            snakeBody.get(0).y * LocalUIData.UNIT - 5,
                            LocalUIData.UNIT + 10, LocalUIData.UNIT + 10, 15, 15);
                } else {
                    g.fillRoundRect(snakeBody.get(0).x * LocalUIData.UNIT,
                            snakeBody.get(0).y * LocalUIData.UNIT,
                            LocalUIData.UNIT, LocalUIData.UNIT, 15, 15);
                }
            }
            //body
            for (int i = 1; i < snakeBody.size(); i++) {
                g.setColor(new Color(0, 0, 255));
                //synchronized (snakeBody) {
                    g.fillRoundRect(snakeBody.get(i).x * LocalUIData.UNIT,
                            snakeBody.get(i).y * LocalUIData.UNIT,
                            LocalUIData.UNIT, LocalUIData.UNIT, 15, 15);
                //}
            }
        }
    }
    private void drawSnake2(Graphics g, boolean snakeInHole, boolean eatFood, ArrayList<Point> snakeBody) {
        if(!localUIData.booSnakeEatWall[1] && !localUIData.booSnakeEatBody[1] && !localUIData.booSnakeEatSnake[1]) {
            //head
            if (!snakeInHole) {
                g.setColor(new Color(112, 22, 22));
                if (eatFood) {
                    g.fillRoundRect(snakeBody.get(0).x * LocalUIData.UNIT - 5,
                            snakeBody.get(0).y * LocalUIData.UNIT - 5,
                            LocalUIData.UNIT + 10, LocalUIData.UNIT + 10, 15, 15);
                } else {
                    g.fillRoundRect(snakeBody.get(0).x * LocalUIData.UNIT,
                            snakeBody.get(0).y * LocalUIData.UNIT,
                            LocalUIData.UNIT, LocalUIData.UNIT, 15, 15);
                }
            }
            //body
            for (int i = 1; i < snakeBody.size(); i++) {
                g.setColor(new Color(255, 31, 28));
                //synchronized (snakeBody) {
                    g.fillRoundRect(snakeBody.get(i).x * LocalUIData.UNIT,
                            snakeBody.get(i).y * LocalUIData.UNIT,
                            LocalUIData.UNIT, LocalUIData.UNIT, 15, 15);
                //}
            }
        }
    }

    private void initialSnake(int position){
        localUIData.intBodyLength = 2;
        dynamicUIData.arrayListSnakeBody.get(position).clear();
        dynamicUIData.arrayListSnakeBody.get(position).add(new Point(80, 0));       //將頭放到遊戲場外
        try {
            LocalUIData.doOutToServer.reset();
            LocalUIData.doOutToServer.writeObject(dynamicUIData);
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, "Snake initial do out to server error.");
        }

        //間隔2秒後隨機從蛇洞出來
        Timer timerInitialSnake = new Timer();
        TimerTask timerTaskInitialSnake = new TimerTask() {
            @Override
            public void run() {
                if(position == localUIData.intPosition) {
                    ArrayList<Point> temp = new ArrayList<>();
                    temp.add(new Point(dynamicUIData.pointHoleOne.x + 1, dynamicUIData.pointHoleOne.y));
                    temp.add(dynamicUIData.pointHoleOne);
                    ArrayList<Point> temp2 = new ArrayList<>();
                    temp2.add(0, new Point(dynamicUIData.pointHoleTwo.x, dynamicUIData.pointHoleTwo.y - 1));
                    temp2.add(1, dynamicUIData.pointHoleTwo);
                    dynamicUIData.arrayListSnakeBody.get(position).clear();
                    if ((int) (Math.random() * 2) == 0) {
                        dynamicUIData.arrayListSnakeBody.add(position, temp);
                        dynamicUIData.intNewDirection.add(position, LocalUIData.RIGHT);
                    } else {
                        dynamicUIData.arrayListSnakeBody.add(position, temp2);
                        dynamicUIData.intNewDirection.add(position, LocalUIData.UP);
                    }

                    try {
                        LocalUIData.doOutToServer.reset();
                        LocalUIData.doOutToServer.writeObject(dynamicUIData);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Snake initial do out to server error.");
                    }

                    localUIData.booSnakeEatWall[position] = false;
                    localUIData.booSnakeEatSnake[position] = false;
                    localUIData.booSnakeEatBody[position] = false;
                }
                else{
                    localUIData.booSnakeEatWall[position] = false;
                    localUIData.booSnakeEatSnake[position] = false;
                    localUIData.booSnakeEatBody[position] = false;
                }
            }
        };
        timerInitialSnake.schedule(timerTaskInitialSnake, 2000);
    }

    private boolean isSnakeEatWall(int position){       //position判斷是哪一隻蛇
        //synchronized (dynamicUIData.arrayListSnakeBody.get(position)) {
            ArrayList<Point> snake = dynamicUIData.arrayListSnakeBody.get(position);
            Point p = snake.get(0);     //get snake head
            for (int i = dynamicUIData.pointWallRow.x; i <= dynamicUIData.pointWallRow.x + LocalUIData.intWallLength; i++) {
                Point point = new Point(i, dynamicUIData.pointWallRow.y);
                if (p.equals(point)) {
                    localUIData.booSnakeEatWall[position] = true;
                    return true;
                }
            }
            for (int i = dynamicUIData.pointWallColumn.y; i <= dynamicUIData.pointWallColumn.y + LocalUIData.intWallLength; i++) {
                Point point = new Point(dynamicUIData.pointWallColumn.x, i);
                if (p.equals(point)) {
                    localUIData.booSnakeEatWall[position] = true;
                    return true;
                }
            }
        //}
        return false;
    }
    private boolean isSnakeEatFoodOne(int position){
        //synchronized (dynamicUIData) {
            if (dynamicUIData.arrayListSnakeBody.get(position).get(0).equals(dynamicUIData.pointFoodOne) && !dynamicUIData.booFoodOneEaten) {
                return true;
            }
            else
                return false;
        //}
    }
    private boolean isSnakeEatFoodTwo(int position){
        //synchronized (dynamicUIData) {
            if (dynamicUIData.arrayListSnakeBody.get(position).get(0).equals(dynamicUIData.pointFoodTwo) && !dynamicUIData.booFoodTwoEaten) {
                return true;
            }
            return false;
        //}
    }
    private boolean isSnakeEatBody(int position){
        synchronized (dynamicUIData.arrayListSnakeBody.get(position)){
            for (int i = 1; i < dynamicUIData.arrayListSnakeBody.get(position).size(); i++) {
                if (dynamicUIData.arrayListSnakeBody.get(position).get(i).equals(dynamicUIData.arrayListSnakeBody.get(position).get(0))) {
                    localUIData.booSnakeEatBody[position] = true;
                    return true;
                }
            }
            return false;
        }
    }
    private boolean isSnakeEatSnake(int position){
        int rival = (position==0)? 1 : 0;
        //synchronized (dynamicUIData.arrayListSnakeBody.get(position)) {
            Point head = new Point(dynamicUIData.arrayListSnakeBody.get(position).get(0));
            for (int k = 0; k < dynamicUIData.arrayListSnakeBody.get(rival).size(); k++) {
                if (head.equals(dynamicUIData.arrayListSnakeBody.get(rival).get(k))) {
                    localUIData.booSnakeEatSnake[position] = true;
                    return true;
                }
            }
            return false;
        //}
    }
    private boolean isSnakeInHole(int position){
        if(dynamicUIData.arrayListSnakeBody.get(position).get(0).equals(dynamicUIData.pointHoleOne) ||
                dynamicUIData.arrayListSnakeBody.get(position).get(0).equals(dynamicUIData.pointHoleTwo)) {
            localUIData.booSnakeInHole[position] = true;
            return true;
        }
        else
            return false;
    }

    //Judge after snake moved
    private void judgeSnakeMoved(){
        //synchronized (dynamicUIData.arrayListSnakeBody) {
            int i = localUIData.intPosition;
                if (isSnakeEatWall(i)) {
                    dynamicUIData.intLives[i]--;
                    dynamicUIData.intScores[i] /= 2;
                    try {
                        LocalUIData.doOutToServer.reset();
                        LocalUIData.doOutToServer.writeObject(dynamicUIData);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Snake move do out to server error.");
                    }

                    if (dynamicUIData.intLives[i] != 0) {
                        initialSnake(i);
                    }
                }

                if (isSnakeEatFoodOne(i)) {
                    localUIData.booSnakeEatFood[i] = true;
                    dynamicUIData.intScores[i]++;
                    dynamicUIData.booFoodOneEaten = true;

                    //吃到食物就傳給server
                    try {
                        LocalUIData.doOutToServer.reset();
                        synchronized (dynamicUIData) {
                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                        }
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Snake eat food do out to server error.");
                    }

                    //如果吃到食物身體就加長，並傳data給server
                    if (i == localUIData.intPosition) {
                        localUIData.intBodyLength++;
                        synchronized (dynamicUIData.arrayListSnakeBody.get(localUIData.intPosition)) {
                            dynamicUIData.arrayListSnakeBody.get(localUIData.intPosition).add(dynamicUIData.arrayListSnakeBody.get(localUIData.intPosition).size(), oldTail[localUIData.intPosition]);
                        }
                        try {
                            LocalUIData.doOutToServer.reset();
                            synchronized (dynamicUIData) {
                                LocalUIData.doOutToServer.writeObject(dynamicUIData);
                            }
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "Snake eat food do out to server error.");
                        }
                    }
                }

                if (isSnakeEatFoodTwo(i)) {
                    localUIData.booSnakeEatFood[i] = true;
                    dynamicUIData.intScores[i]++;
                    dynamicUIData.booFoodTwoEaten = true;

                    //吃到食物就傳給server
                    try {
                        LocalUIData.doOutToServer.reset();
                        synchronized (dynamicUIData) {
                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                        }
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Snake eat food do out to server error.");
                    }

                    //如果吃到食物身體就加長，並傳data給server
                    if (i == localUIData.intPosition) {
                        localUIData.intBodyLength++;
                        synchronized (dynamicUIData.arrayListSnakeBody.get(localUIData.intPosition)) {
                            dynamicUIData.arrayListSnakeBody.get(localUIData.intPosition).add(dynamicUIData.arrayListSnakeBody.get(localUIData.intPosition).size(), oldTail[localUIData.intPosition]);
                        }
                        try {
                            LocalUIData.doOutToServer.reset();
                            synchronized (dynamicUIData) {
                                LocalUIData.doOutToServer.writeObject(dynamicUIData);
                            }
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "Snake eat food do out to server error.");
                        }
                    }
                }

                if (isSnakeEatBody(i)) {
                    dynamicUIData.intLives[i]--;
                    dynamicUIData.intScores[i] /= 2;
                    try {
                        LocalUIData.doOutToServer.reset();
                        LocalUIData.doOutToServer.writeObject(dynamicUIData);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Snake eat body do out to server error.");
                    }

                    if (dynamicUIData.intLives[i] != 0) {
                        initialSnake(i);
                    }
                }

                if (isSnakeEatSnake(i)) {
                    dynamicUIData.intLives[i]--;
                    dynamicUIData.intScores[i] /= 2;
                    try {
                        LocalUIData.doOutToServer.reset();
                        LocalUIData.doOutToServer.writeObject(dynamicUIData);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Snake eat snake do out to server error.");
                    }

                    if (dynamicUIData.intLives[i] != 0) {
                        initialSnake(i);
                    }
                }

                if(isSnakeInHole(i) && !localUIData.booJudgeSnakeInHoleCalled[i]){
                        localUIData.booJudgeSnakeInHoleCalled[i] = true;
                        Timer timer = new Timer();
                        Timer timer1 = new Timer();
                        TimerTask timerTask;
                        TimerTask timerTask1;

                        //隨機指定一個方向
                        int judgeDir = (int)(Math.random()*4);
                        switch (judgeDir){
                            case 0:
                                dynamicUIData.intNewDirection.set(i, LocalUIData.UP);
                                localUIData.intCurrentDirection[i] = LocalUIData.UP;
                                break;
                            case 1:
                                dynamicUIData.intNewDirection.set(i, LocalUIData.DOWN);
                                localUIData.intCurrentDirection[i] = LocalUIData.DOWN;
                                break;
                            case 2:
                                dynamicUIData.intNewDirection.set(i, LocalUIData.LEFT);
                                localUIData.intCurrentDirection[i] = LocalUIData.LEFT;
                                break;
                            case 3:
                                dynamicUIData.intNewDirection.set(i, LocalUIData.RIGHT);
                                localUIData.intCurrentDirection[i] = LocalUIData.RIGHT;
                                break;
                        }
                        //隨機指定一個蛇洞p
                        int judgeHole = (int)(Math.random()*2);
                        Point newHead = dynamicUIData.pointHoleOne;
                        if(judgeHole==0){
                            switch (judgeDir){
                                case 0:
                                    newHead.setLocation(dynamicUIData.pointHoleOne.x, dynamicUIData.pointHoleOne.y-1);
                                    break;
                                case 1:
                                    newHead.setLocation(dynamicUIData.pointHoleOne.x, dynamicUIData.pointHoleOne.y+1);
                                    break;
                                case 2:
                                    newHead.setLocation(dynamicUIData.pointHoleOne.x-1, dynamicUIData.pointHoleOne.y);
                                    break;
                                case 3:
                                    newHead.setLocation(dynamicUIData.pointHoleOne.x+1, dynamicUIData.pointHoleOne.y);
                                    break;
                            }
                        }
                        else{
                            switch (judgeDir){
                                case 0:
                                    newHead.setLocation(dynamicUIData.pointHoleTwo.x, dynamicUIData.pointHoleTwo.y-1);
                                    break;
                                case 1:
                                    newHead.setLocation(dynamicUIData.pointHoleTwo.x, dynamicUIData.pointHoleTwo.y+1);
                                    break;
                                case 2:
                                    newHead.setLocation(dynamicUIData.pointHoleTwo.x-1, dynamicUIData.pointHoleTwo.y);
                                    break;
                                case 3:
                                    newHead.setLocation(dynamicUIData.pointHoleTwo.x+1, dynamicUIData.pointHoleTwo.y);
                                    break;
                            }
                        }
                        Point newFirstBody = (judgeHole==0)? dynamicUIData.pointHoleOne : dynamicUIData.pointHoleTwo;

                        final int k = i;
                        timerTask1 = new TimerTask() {
                            //把身體加回來
                            public void run() {
                                synchronized (dynamicUIData.arrayListSnakeBody.get(k)) {
                                    if(localUIData.booSnakeInHole[k]){
                                        localUIData.booSnakeInHole[k] = false;
                                        //設定頭的位置
                                        dynamicUIData.arrayListSnakeBody.get(k).set(0, newHead);
                                        try {
                                            LocalUIData.doOutToServer.reset();
                                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                                        } catch (IOException e1) {
                                            JOptionPane.showMessageDialog(null, "Snake add head do out to server error.");
                                        }
                                        //加一個身體
                                        dynamicUIData.arrayListSnakeBody.get(k).add(newFirstBody);
                                        try {
                                            LocalUIData.doOutToServer.reset();
                                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                                        } catch (IOException e1) {
                                            JOptionPane.showMessageDialog(null, "Snake add body do out to server error.");
                                        }

                                    }
                                    else if(dynamicUIData.arrayListSnakeBody.get(k).size() < localUIData.intBodyLength){
                                        //加身體
                                        dynamicUIData.arrayListSnakeBody.get(k).add(newFirstBody);
                                        try {
                                            LocalUIData.doOutToServer.reset();
                                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                                        } catch (IOException e1) {
                                            JOptionPane.showMessageDialog(null, "Snake add body do out to server error.");
                                        }
                                    }
                                    else{
                                        localUIData.jLabelInHole.setText("");
                                        timer1.cancel();
                                        localUIData.booJudgeSnakeInHoleCalled[k] = false;
                                    }
                                }
                            }
                        };
                        timerTask = new TimerTask(){
                            public void run() {
                                localUIData.jLabelInHole.setText("Hole!!");
                                synchronized (dynamicUIData.arrayListSnakeBody.get(k)) {
                                    //把身體去掉
                                    if (dynamicUIData.arrayListSnakeBody.get(k).size() > 1) {
                                        dynamicUIData.arrayListSnakeBody.get(k).remove(dynamicUIData.arrayListSnakeBody.get(k).size() - 1);
                                        try {
                                            LocalUIData.doOutToServer.reset();
                                            LocalUIData.doOutToServer.writeObject(dynamicUIData);
                                        } catch (IOException e1) {
                                            JOptionPane.showMessageDialog(null, "Snake removing body do out to server error.");
                                        }
                                    }

                                    if (dynamicUIData.arrayListSnakeBody.get(k).size() == 1) {
                                        timer1.schedule(timerTask1, 2000, dynamicUIData.intSpeed / 2);
                                        timer.cancel();
                                        timer.purge();
                                    }
                                }
                            }
                        };
                        timer.schedule(timerTask, 0, dynamicUIData.intSpeed);
                }

        //}
    }

}
