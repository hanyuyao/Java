package snake.common.controller;

import snake.common.entity.Food;
import snake.common.entity.Hole;
import snake.common.entity.Snake;
import snake.common.entity.Wall;
import snake.common.panel.AllData;
import snake.common.panel.PlayPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends KeyAdapter{

    private Snake snake;
    private Food food;
    private Wall wall;
    private Hole hole;

    public Controller(Snake snake, Food food, Wall wall, Hole hole) {
        super();
        this.snake = snake;
        this.food = food;
        this.wall = wall;
        this.hole = hole;
        //this.playPanel = playPanel;
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                snake.changeDirection(Snake.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.changeDirection(Snake.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snake.changeDirection(Snake.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.changeDirection(Snake.RIGHT);
                break;
        }
    }
/*
    class GameThread implements Runnable{
        @Override
        public void run() {
            System.out.println("Game started!");
            while(snake.life){
                snake.move();
                try{Thread.sleep(AllData.speed1);} catch(Exception e){}
                if(food.foodOneEaten && food.foodTwoEaten) {
                    TimerTask timerTask = new TimerTask(){
                        public void run(){
                            food.init();
                        }
                    };
                    new Timer().schedule(timerTask, 2000);
                }
            }
        }
    }
*/
    public void newGame(){
        food.startFood();
        //new Thread(new GameThread()).start();
    }
}