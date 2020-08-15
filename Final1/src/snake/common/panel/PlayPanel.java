package snake.common.panel;

import snake.common.controller.Controller;
import snake.common.entity.Food;
import snake.common.entity.Hole;
import snake.common.entity.Snake;
import snake.common.entity.Wall;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PlayPanel extends JPanel implements Runnable {
    AllData data;
    Controller controller;
    Snake snake;
    Wall wall;
    Food food;
    Hole hole;
    Image board, eatWall;
    JLabel lebelScore, lebelLives;
    public PlayPanel(AllData allData, Controller controller, Snake snake, Wall wall, Food food, Hole hole) {
        setPreferredSize(new Dimension(759, 925));

        data = allData;
        this.snake = snake;
        this.wall = wall;
        this.food = food;
        this.hole = hole;
        this.controller = controller;

        addKeyListener(controller);

        board = new ImageIcon("playBoard.jpg").getImage();
        eatWall = new ImageIcon("explosion.png").getImage();
        this.setLayout(null);
        lebelScore = new JLabel("Scores: " + AllData.score);
        lebelScore.setFont(AllData.font2);
        lebelScore.setBounds(180, 690, 600, 150);
        this.add(lebelScore);
        lebelLives = new JLabel("Lives: " + AllData.lives);
        lebelLives.setFont(AllData.font2);
        lebelLives.setBounds(230, 760, 400, 200);
        this.add(lebelLives);
    }
    public void paintComponent(Graphics g){
        g.drawImage(board, 0, 0, this.getWidth(), this.getHeight(), this);
        snake.drawMe(g);
        wall.drawMe(g);
        food.drawMe(g);
        hole.drawMe(g);

        if(snake.eatWall){
            g.drawImage(eatWall, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        if(snake.eatBody){
            g.drawImage(eatWall, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        if(snake.snakeInHole){
            g.setFont(AllData.font1);
            g.setColor(Color.red);
            g.drawString("Snake in hole!", 100, 100);
        }
    }

    public void judegSnakeMoved(Snake snake) {
        //調整速度
        if(snake.bodyLength < 5)
            AllData.speed1 = 100;
        else if(snake.bodyLength >= 5 && snake.bodyLength <10)
            AllData.speed1 = 80;
        else if(snake.bodyLength >= 10 && snake.bodyLength <15)
            AllData.speed1 = 60;
        else if(snake.bodyLength >= 15 && snake.bodyLength <20)
            AllData.speed1 = 40;
        else
            AllData.speed1 = 30;

        if(AllData.score > 100 && AllData.score < 200)
            AllData.speed1 -= 5;
        if(AllData.score >= 200 && AllData.score < 300)
            AllData.speed1 -= 10;
        if(AllData.score >= 300 && AllData.score < 350)
            AllData.speed1 = 20;
        if(AllData.score >= 350)
            AllData.speed1 = 10;

        if(food.isSnakeEatFood(snake)){
            snake.eatFood();
            lebelScore.setText("Scores: " + AllData.score);
        }
        if(snake.isEatBody()){
            snake.die();
            lebelScore.setText("Scores: " + AllData.score);
            lebelLives.setText("Lives: " + AllData.lives);
            if(AllData.lives != 0){
                snake.init();
            }
        }
        if(wall.isSnakeEatWall(snake)){
            snake.die();
            lebelScore.setText("Scores: " + AllData.score);
            lebelLives.setText("Lives: " + AllData.lives);
            if(AllData.lives != 0){
                snake.init();
            }
        }
        if(hole.isSnakeInHole() && !hole.judgeSnakeInHoleCalled){
            if((int)(Math.random()*2)==1)
                AllData.score += 10;
            lebelScore.setText("Scores: " + AllData.score);
            hole.judgeSnakeInHoleCalled = true;
            Timer timer = new Timer();
            Timer timer1 = new Timer();
            TimerTask timerTask;
            TimerTask timerTask1;

            //隨機指定一個方向
            int judgeDir = (int)(Math.random()*4);
            switch (judgeDir){
                case 0: snake.newDirection = Snake.UP; snake.oldDirection = Snake.UP; break;
                case 1: snake.newDirection = Snake.DOWN; snake.oldDirection = Snake.DOWN; break;
                case 2: snake.newDirection = Snake.LEFT; snake.oldDirection = Snake.LEFT; break;
                case 3: snake.newDirection = Snake.RIGHT; snake.oldDirection = Snake.RIGHT; break;
            }
            //隨機指定一個蛇洞p
            int judgeHole = (int)(Math.random()*2);
            Point newHead = new Point(AllData.holeOne.x, AllData.holeOne.y);
            if(judgeHole==0){
                switch (judgeDir){
                    case 0: newHead.setLocation(AllData.holeOne.x, AllData.holeOne.y-1); break;
                    case 1: newHead.setLocation(AllData.holeOne.x, AllData.holeOne.y+1); break;
                    case 2: newHead.setLocation(AllData.holeOne.x-1, AllData.holeOne.y); break;
                    case 3: newHead.setLocation(AllData.holeOne.x+1, AllData.holeOne.y); break;
                }
            }
            else{
                switch (judgeDir){
                    case 0: newHead.setLocation(AllData.holeTwo.x, AllData.holeTwo.y-1); break;
                    case 1: newHead.setLocation(AllData.holeTwo.x, AllData.holeTwo.y+1); break;
                    case 2: newHead.setLocation(AllData.holeTwo.x-1, AllData.holeTwo.y); break;
                    case 3: newHead.setLocation(AllData.holeTwo.x+1, AllData.holeTwo.y); break;
                }
            }
            Point newFirstBody = (judgeHole==0)? AllData.holeOne : AllData.holeTwo;

            timerTask1 = new TimerTask() {
                //把身體加回來
                public void run() {
                    synchronized (snake.body) {
                        if(snake.snakeInHole){
                            snake.snakeInHole = false;
                            //設定頭的位置
                            snake.body.set(0, newHead);
                            //加一個身體
                            snake.body.add(newFirstBody);
                        }
                        else if(snake.body.size() < snake.bodyLength){
                            //加身體
                            snake.body.add(newFirstBody);
                        }
                        else{
                            timer1.cancel();
                            hole.judgeSnakeInHoleCalled = false;
                        }
                    }
                }
            };
            timerTask = new TimerTask(){
                public void run(){
                    //把身體去掉
                    if(snake.body.size() > 1){
                        snake.body.remove(snake.body.size()-1);
                    }
                    if(snake.body.size()==1){
                        timer1.schedule(timerTask1, 2000, AllData.speed1/2);
                        timer.cancel();
                        timer.purge();
                    }
                }
            };
            timer.schedule(timerTask, 0, AllData.speed1);
        }
    }

    @Override
    public void run() {
        while(snake.life){
            if(!snake.snakeInHole) {
                //蛇不在洞才移動
                snake.move();
                judegSnakeMoved(snake);

                try {
                    Thread.sleep(AllData.speed1);
                } catch (Exception e) {}
            }
            repaint();
            //每隔speed1 ms重畫一次

            snake.eatWall = false;
            snake.eatBody = false;
            snake.eatFood = false;
        }
        //life == false 時換到結束頁面
        data.lebelScore.setText("Scores: " + AllData.score);
        data.card.show(data.pane, "panelEnd");
    }
}
