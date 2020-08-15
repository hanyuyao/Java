package client;

import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    LocalUIData localUIData;
    public EndPanel(LocalUIData localUIData){
        this.localUIData = localUIData;
        setLayout(new GridLayout(5, 1));

        localUIData.jLabelEnd.setFont(new Font("STHupo", Font.PLAIN, 100));
        this.add(localUIData.jLabelName);
        this.add(localUIData.jLabelScores);
        this.add(localUIData.jLabelEnd);
        this.add(localUIData.jLabelName2);
        this.add(localUIData.jLabelScores2);
    }
    public void paintComponent(Graphics g){
        g.drawImage(new ImageIcon("playingBoard.jpg").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
