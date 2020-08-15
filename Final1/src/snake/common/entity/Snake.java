package snake.common.entity;

import snake.common.panel.AllData;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Snake{
    AllData allData;

    public static final int UP = -1;
    public static final int DOWN = 1;
    public static final int RIGHT = -2;
    public static final int LEFT = 2;

    public int oldDirection,newDirection;
    private Point oldTail;
    public ArrayList<Point> body = new ArrayList<>();
    public int bodyLength;
    public boolean life = true;
    public boolean eatWall = false;
    public boolean eatBody = false;
    public boolean eatFood = false;
    public boolean snakeInHole;

    public Snake(AllData allData){
        this.allData = allData;
        init();
    }

    public void init(){
        body.clear();
        bodyLength = 2;

        //隨機指定蛇的初始位置
        int x = (int)(Math.random()*AllData.Width);
        int y = (int)(Math.random()*AllData.Height);
        for(int i = 0; i < 2; i++){
            body.add(new Point(x--,y));
        }

        //隨機指定蛇的初始方向
        int direction = (int)(Math.random()*4);
        switch (direction){
            case 0:
                oldDirection = newDirection = UP;
                break;
            case 1:
                oldDirection = newDirection = DOWN;
                break;
            case 2:
                oldDirection = newDirection = RIGHT;
                break;
            case 3:
                oldDirection = newDirection = LEFT;
                break;
        }
    }

    public void die(){
        AllData.lives--;
        AllData.score -= 50;
        if(AllData.lives==0) life = false;
    }

    public void move(){
        if(oldDirection + newDirection != 0){
            oldDirection = newDirection;
        }
        synchronized (body) {
            //去尾巴
            oldTail = body.remove(body.size() - 1);

            //移動
            int x = body.get(0).x;
            int y = body.get(0).y;
            switch (oldDirection) {
                case UP:
                    y--;
                    if (y < 0) {
                        y = AllData.Height;
                    }
                    break;
                case DOWN:
                    y++;
                    if (y > AllData.Height) {
                        y = 0;
                    }
                    break;
                case LEFT:
                    x--;
                    if (x < 0) {
                        x = AllData.Width;
                    }
                    break;
                case RIGHT:
                    x++;
                    if (x > AllData.Width) {
                        x = 0;
                    }
                    break;
            }

            //加頭
            Point newHead = new Point(x, y);
            body.add(0, newHead);
        }
    }

    public void changeDirection(int direction){
        newDirection = direction;
    }

    public Point getHead(){
        return body.get(0);
    }

    public void eatFood(){
        eatFood = true;
        AllData.score += 10;
        bodyLength++;
        synchronized (body) {
            body.add(body.size(), oldTail);
        }
    }

    public boolean isEatBody(){
        synchronized (body) {
            for (int i = 1; i < body.size(); i++) {
                if (body.get(i).equals(body.get(0))) {
                    eatBody = true;
                    return true;
                }
            }
            return false;
        }
    }

    public void drawMe(Graphics g){
        //head
        if(!snakeInHole) {
            g.setColor(new Color(25, 25, 112));
            if (eatFood) {
                g.fillRoundRect(body.get(0).x * AllData.UNIT - 5, body.get(0).y * AllData.UNIT - 5,
                        AllData.UNIT + 10, AllData.UNIT + 10, 15, 15);
            } else {
                g.fillRoundRect(body.get(0).x * AllData.UNIT, body.get(0).y * AllData.UNIT,
                        AllData.UNIT, AllData.UNIT, 15, 15);
            }
        }
        //body
        for(int i = 1; i < body.size(); i++){
            g.setColor(new Color(0, 0, 255));
            synchronized (body) {
                g.fillRoundRect(body.get(i).x * AllData.UNIT, body.get(i).y * AllData.UNIT,
                        AllData.UNIT, AllData.UNIT, 15, 15);
            }
        }
    }
    /*
    public void startSnake(){
        new Thread(new SnakeThread()).start();
        System.out.println("Snake started!");
    }

    private class SnakeThread implements Runnable{
        public void run() {
            while(life){

            }
        }
    }
    */
}