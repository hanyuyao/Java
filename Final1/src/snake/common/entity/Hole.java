package snake.common.entity;

import snake.common.panel.AllData;
import java.awt.*;

import static snake.common.panel.AllData.*;

public class Hole {
    Snake snake;
    public boolean judgeSnakeInHoleCalled = false;

    public Hole(Snake snake){
        this.snake = snake;
        init();
    }
    public void init(){
        snake.snakeInHole = false;

        do{
            int holeOneX = (int) (Math.random() * AllData.Width);
            int holeOneY = (int) (Math.random() * AllData.Height);
            int holeTwoX = (int) (Math.random() * AllData.Width);
            int holeTwoY = (int) (Math.random() * AllData.Height);
            holeOne = new Point(holeOneX, holeOneY);
            holeTwo = new Point(holeTwoX, holeTwoY);
        }while(judgeCoincidence(snake, holeOne, holeTwo));
    }

    private boolean judgeCoincidence(Snake snake, Point holeOne, Point holeTwo){
        //判斷有無撞蛇頭
        if(snake.getHead().equals(AllData.foodOne) || snake.getHead().equals(AllData.foodTwo))
            return true;
        //判斷有無撞牆
        for(int i = AllData.wallRow.x; i < AllData.wallRow.x + AllData.wallLength; i++){
            Point point = new Point(i, AllData.wallRow.y);
            if(holeOne.equals(point) || holeTwo.equals(point)){
                return true;
            }
        }
        for(int i = AllData.wallColumn.y; i < AllData.wallColumn.y + AllData.wallLength; i++){
            Point point = new Point(AllData.wallColumn.x, i);
            if(holeOne.equals(point) || holeTwo.equals(point)){
                return true;
            }
        }
        //判斷有無撞蛋
        if(foodOne.equals(holeOne) || foodOne.equals(holeTwo) || foodTwo.equals(holeOne) || foodTwo.equals(holeTwo))
            return true;
        //判斷兩洞是否重合
        if(holeOne.equals(holeTwo))
            return true;

        return false;
    }

    public boolean isSnakeInHole(){
        if(snake.getHead().equals(holeOne) || snake.getHead().equals(holeTwo)) {
            snake.snakeInHole = true;
            return true;
        }
        else
            return false;
    }

    public void drawMe(Graphics g) {
        int size = 4;

        g.setColor(new Color(171, 171, 171));
        g.fillOval(holeOne.x * AllData.UNIT - size, holeOne.y * AllData.UNIT - size,
                AllData.UNIT + size*2, AllData.UNIT + size*2);
        g.fillOval(holeTwo.x * AllData.UNIT - size, holeTwo.y * AllData.UNIT - size,
                AllData.UNIT + size*2, AllData.UNIT + size*2);

        g.setColor(new Color(0, 0, 0));
        g.fillOval(holeOne.x * AllData.UNIT, holeOne.y * AllData.UNIT ,
                AllData.UNIT , AllData.UNIT );
        g.fillOval(holeTwo.x * AllData.UNIT , holeTwo.y * AllData.UNIT ,
                AllData.UNIT , AllData.UNIT);

    }
}