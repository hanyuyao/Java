package general.uidata;

import client.Client;
import client.EndPanel;
import client.PlayPanel;
import client.StartPanel;
import maingame.GameClient;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LocalUIData {
    //不變的量
    public static final int PERSON = 2;
    public static final int WIDTH = 40, HEIGHT = 40;
    public static final int UNIT = 20;
    public static final int intWallLength = 15;
    public static final int UP = -1;
    public static final int DOWN = 1;
    public static final int RIGHT = -2;
    public static final int LEFT = 2;



    public static Font font1 = new Font("Arial", Font.BOLD, 36);
    public static Font font2 = new Font("微軟正黑體",  Font.BOLD, 60);
    public static Font font3 = new Font("微軟正黑體", Font.BOLD, 44);

    public GameClient gameClient;
    public Client client;
    public CardLayout card;
    public JPanel pane;            //pane管理所有其他panel
    public static boolean isConnected = false;

    //Start panel
    public StartPanel startPanel;
    public String stringName;
    public int intPosition;
    public int intRivalPosition;
    public JButton buttonConnect;

    //Play panel
    public PlayPanel playPanel;
    public JLabel jLabelName = new JLabel();
    public JLabel jLabelScores = new JLabel();
    public JLabel jLabelLives = new JLabel();
    public JLabel jLabelName2 = new JLabel("對手:");
    public JLabel jLabelScores2 = new JLabel();
    public JLabel jLabelLives2 = new JLabel();
    public JLabel jLabelInHole = new JLabel();
        //snake parameter
    public boolean[] booSnakeInHole = {false, false};
    public boolean[] booJudgeSnakeInHoleCalled = {false, false};
    public boolean[] booSnakeEatFood = {false, false};
    public boolean[] booSnakeEatWall = {false, false};
    public boolean[] booSnakeEatBody = {false, false};
    public boolean[] booSnakeEatSnake = {false, false};
    public int[] intCurrentDirection = {RIGHT, UP};
    public int intBodyLength = 2;

    //End panel
    public EndPanel endPanel;
    public JLabel jLabelEnd = new JLabel();
    public boolean isEnd = false;

    public static ObjectOutputStream doOutToServer;
    public static ObjectInputStream brInFromServer;
}
