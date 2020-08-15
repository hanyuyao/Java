package snake.common.panel;

import javax.swing.*;
import java.awt.*;

public class AllData {
    public CardLayout card;
    public JPanel pane;            //pane管理所有其他panel
    public StartPanel startPanel;
    public PlayPanel playPanel;
    public EndPanel endPanel;
    public JLabel lebelScore = new JLabel();

    public static int score = 0;
    public static int lives = 3;

    public static Point wallRow, wallColumn;
    public static int wallLength = 15;
    public static Point foodOne, foodTwo;
    public static Point holeOne, holeTwo;

    public static Font font1 = new Font("Arial", Font.BOLD, 36);
    public static Font font2 = new Font("STHupo", Font.BOLD, 72);
    public static int speed1 = 80;

    public static int Width = 37, Height = 35, UNIT = 20;
}
