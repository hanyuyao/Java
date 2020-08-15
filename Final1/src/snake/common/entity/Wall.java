package snake.common.entity;

import snake.common.panel.AllData;

import java.awt.*;

public class Wall {
    public Wall(){
        int rowX = (int)(Math.random()*(AllData.Width - AllData.wallLength));
        int rowY = (int)(Math.random()*(AllData.Height));
        int columnX = (int)(Math.random()*(AllData.Width));
        int columnY = (int)(Math.random()*(AllData.Height - AllData.wallLength));
        AllData.wallRow = new Point(rowX, rowY);
        AllData.wallColumn = new Point(columnX, columnY);
    }

    public boolean isSnakeEatWall(Snake snake){
        Point p = snake.getHead();
        for(int i = AllData.wallRow.x; i < AllData.wallRow.x + AllData.wallLength; i++){
            Point point = new Point(i, AllData.wallRow.y);
            if(p.equals(point)){
                snake.eatWall = true;
                return true;
            }
        }
        for(int i = AllData.wallColumn.y; i < AllData.wallColumn.y + AllData.wallLength; i++){
            Point point = new Point(AllData.wallColumn.x, i);
            if(p.equals(point)){
                snake.eatWall = true;
                return true;
            }
        }
        return false;
    }

    public void drawMe(Graphics g) {
        g.setColor(new Color(184, 115, 51));

        for(int i = AllData.wallRow.x; i < AllData.wallRow.x + AllData.wallLength; i++){
            g.fill3DRect(i * AllData.UNIT, AllData.wallRow.y * AllData.UNIT, AllData.UNIT, AllData.UNIT, true);
        }
        for(int i = AllData.wallColumn.y; i < AllData.wallColumn.y + AllData.wallLength; i++){
            g.fill3DRect(AllData.wallColumn.x * AllData.UNIT, i * AllData.UNIT, AllData.UNIT, AllData.UNIT, true);
        }
    }
}
