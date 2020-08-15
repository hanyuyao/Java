package snake.main;

import snake.common.controller.Controller;
import snake.common.entity.Food;
import snake.common.entity.Hole;
import snake.common.entity.Snake;
import snake.common.entity.Wall;
import snake.common.panel.AllData;
import snake.common.panel.EndPanel;
import snake.common.panel.PlayPanel;
import snake.common.panel.StartPanel;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    AllData data;
    Controller controller;
    Snake snake;
    Wall wall;
    Food food;
    Hole hole;
    Game(AllData allData, Controller controller, Snake snake, Wall wall, Food food, Hole hole){
        super("Snakesss");
        //setSize(new Dimension(765, 960));
        //setBounds(100, 100, 765, 960);
        //setResizable(false);
        setIconImage(this.getToolkit().getImage("icon.png"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        data = allData;
        this.controller = controller;
        this.snake = snake;
        this.wall = wall;
        this.food = food;
        this.hole = hole;

        //pane用card layout管理所有panel
        data.pane = (JPanel) getContentPane();
        data.pane.setPreferredSize(new Dimension(759, 925));
        data.card = new CardLayout();
        data.pane.setLayout(data.card);
        data.startPanel  = new StartPanel(allData, controller);
        data.playPanel = new PlayPanel(allData, controller, snake, wall, food, hole);
        data.endPanel = new EndPanel(allData);
        data.pane.add(data.startPanel, "panelStart");
        data.pane.add(data.playPanel, "panelPlay");
        data.pane.add(data.endPanel, "panelEnd");
        setContentPane(data.pane);


        //setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    public static void main(String[] args){
        AllData allData = new AllData();
        Snake snake = new Snake(allData);
        Wall wall = new Wall();
        Food food = new Food(snake);
        Hole hole = new Hole(snake);
        Controller controller = new Controller(snake, food, wall, hole);
        Game game = new Game(allData, controller, snake, wall, food, hole);
    }
}

