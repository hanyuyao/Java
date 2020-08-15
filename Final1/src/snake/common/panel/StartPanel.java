package snake.common.panel;

import snake.common.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel{
    AllData d;
    Controller controller;
    Image backGround;
    JButton buttonStart;

    public StartPanel(AllData allData, Controller controller) {
        setPreferredSize(new Dimension(759, 925));

        d = allData;
        this.controller = controller;

        setLayout(null);
        backGround = new ImageIcon("startPage.jpg").getImage();

        //start button
        buttonStart = new JButton();
        buttonStart.setIcon(new ImageIcon("start.jpg"));
        buttonStart.setFocusPainted(false);
            //buttonStart.setOpaque(false);
            //buttonStart.setBackground(new Color(2, 2, 2));
        buttonStart.setContentAreaFilled(false);
        buttonStart.setBounds(180, 700, 400, 130);
        StartButtonHandler startButtonHandler = new StartButtonHandler();
        buttonStart.addActionListener(startButtonHandler);
        add(buttonStart);
    }
    public void paintComponent(Graphics g){
        g.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    class StartButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            d.card.show(d.pane, "panelPlay");
            d.playPanel.setFocusable(true);
            d.playPanel.requestFocus();

            //讓蛇開始移動
            controller.newGame();
            Thread t = new Thread(d.playPanel);
            t.start();
        }
    }
}
