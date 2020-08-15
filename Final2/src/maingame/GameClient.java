package maingame;

import client.Client;
import client.StartPanel;
import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import java.awt.*;

public class GameClient extends JFrame{
    public GameClient(LocalUIData localUIData){
        super("Snakesss");

        //pane用card layout管理所有panel
        localUIData.pane = (JPanel) getContentPane();
        localUIData.pane.setPreferredSize(new Dimension(1000, 800));
        localUIData.card = new CardLayout();
        localUIData.pane.setLayout(localUIData.card);
        setContentPane(localUIData.pane);

        setMinimumSize(new Dimension(1022, 876));
        setIconImage(this.getToolkit().getImage("icon.png"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    public static void main(String[] args){
        DynamicUIData dynamicUIData = new DynamicUIData();
        LocalUIData localUIData = new LocalUIData();
        localUIData.gameClient = new GameClient(localUIData);

        localUIData.startPanel = new StartPanel(dynamicUIData, localUIData);
        localUIData.pane.add(localUIData.startPanel, "panelStart");

        localUIData.client = new Client(dynamicUIData, localUIData);

        JPanel jPanelTemp = new JPanel();
        localUIData.pane.add(jPanelTemp, "panelEnd");
    }
}
