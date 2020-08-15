package snake.common.entity;

import snake.common.panel.AllData;
import java.awt.*;
import static snake.common.panel.AllData.foodOne;
import static snake.common.panel.AllData.foodTwo;

public class Food{
    Snake snake;
    public boolean foodOneEaten, foodTwoEaten;
    public Food(Snake snake){
        this.snake = snake;
        init();
    }

    public void init(){
        foodOneEaten = false;
        foodTwoEaten = false;

        do{
            int foodOneX = (int) (Math.random() * AllData.Width);
            int foodOneY = (int) (Math.random() * AllData.Height);
            int foodTwoX = (int) (Math.random() * AllData.Width);
            int foodTwoY = (int) (Math.random() * AllData.Height);
            foodOne = new Point(foodOneX, foodOneY);
            foodTwo = new Point(foodTwoX, foodTwoY);
        }while(judgeCoincidence(snake, foodOne, foodTwo));
    }

    private boolean judgeCoincidence(Snake snake, Point foodOne, Point foodTwo){
        //判斷有無撞蛇
        if(snake.getHead().equals(foodOne) || snake.getHead().equals(foodTwo))
            return true;
        //判斷有無撞牆
        for(int i = AllData.wallRow.x; i < AllData.wallRow.x + AllData.wallLength; i++){
            Point point = new Point(i, AllData.wallRow.y);
            if(foodOne.equals(point) || foodTwo.equals(point)){
                return true;
            }
        }
        for(int i = AllData.wallColumn.y; i < AllData.wallColumn.y + AllData.wallLength; i++){
            Point point = new Point(AllData.wallColumn.x, i);
            if(foodOne.equals(point) || foodTwo.equals(point)){
                return true;
            }
        }
        //判斷有無撞洞
        if(foodOne.equals(AllData.holeOne) || foodOne.equals(AllData.holeTwo) ||
                foodTwo.equals(AllData.holeOne) || foodTwo.equals(AllData.holeTwo)) {
            return true;
        }
        //判斷兩蛋是否重合
        if(foodOne.equals(foodTwo))
            return true;

        return false;
    }

    public boolean isSnakeEatFood(Snake snake){
        if(snake.getHead().equals(foodOne) && foodOneEaten==false){
            foodOneEaten = true;
            return true;
        }
        if(snake.getHead().equals(foodTwo) && foodTwoEaten==false){
            foodTwoEaten = true;
            return true;
        }
        else
            return false;
    }

    public void drawMe(Graphics g) {
        if(!foodOneEaten) {
            int size = 2;
            g.setColor(new Color(205, 16, 118 ));
            g.fillOval(foodOne.x * AllData.UNIT - size, foodOne.y * AllData.UNIT - size,
                    AllData.UNIT - size, AllData.UNIT + size);

            g.setColor(new Color(255, 228, 225 ));
            g.fillOval(foodOne.x * AllData.UNIT + 5, foodOne.y * AllData.UNIT + 5,
                    AllData.UNIT/2, AllData.UNIT/2);
        }
        if(!foodTwoEaten) {
            int size = 2;
            g.setColor(new Color(205, 16, 118 ));
            g.fillOval(foodTwo.x * AllData.UNIT - size, foodTwo.y * AllData.UNIT - size,
                    AllData.UNIT - size, AllData.UNIT + size);

            g.setColor(new Color(255, 228, 225 ));
            g.fillOval(foodTwo.x * AllData.UNIT + 5, foodTwo.y * AllData.UNIT + 5,
                    AllData.UNIT/2, AllData.UNIT/2);
        }
    }

    public void startFood(){
        new Thread(new FoodThread()).start();
    }

    private class FoodThread implements Runnable{
        public void run() {
            while(snake.life){
                System.out.print("");
                if(foodOneEaten && foodTwoEaten) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}
                    init();
                }
            }
        }
    }
}