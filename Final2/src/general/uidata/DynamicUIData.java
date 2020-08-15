package general.uidata;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class DynamicUIData implements Serializable {
    //private static final long serialVersionUID = 5950169519310163575L;
    //只傳一次(不更新)
        //兩玩家相同的data
    public Point pointWallRow, pointWallColumn;
    public Point pointHoleOne, pointHoleTwo;
        //兩玩家不同的data
    public int intPosition;
    public ArrayList<Boolean> booIsStart = new ArrayList<>(LocalUIData.PERSON);


    //會更新
        //兩玩家相同的data
    public Point pointFoodOne, pointFoodTwo;
    public boolean booFoodOneEaten, booFoodTwoEaten;
    public int intSpeed;
        //兩玩家不同的data
    public ArrayList<ArrayList<Point>> arrayListSnakeBody = new ArrayList<>(LocalUIData.PERSON);
    public ArrayList<Integer> intNewDirection = new ArrayList<>(LocalUIData.PERSON);

    public int[] intScores = {0, 0};
    public int[] intLives = {3, 3};
    public boolean[] booLife = {true, true};
}
