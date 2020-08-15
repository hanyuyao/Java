package client;

import general.uidata.DynamicUIData;
import general.uidata.LocalUIData;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;

public class StartPanel extends JPanel {
    LocalUIData localUIData;
    DynamicUIData dynamicUIData;

    JLabel jLabelFrontCover;
    JLabel jLabelIP, jLabelName;
    JTextField jTextFieldIP, jTextFieldName;
    Image backGround;
    JButton buttonStart;

    public StartPanel(DynamicUIData dynamicUIData, LocalUIData localUIData) {
        this.dynamicUIData = dynamicUIData;
        this.localUIData = localUIData;
        setPreferredSize(new Dimension(1000, 800));
        setLayout(new GridBagLayout());
        GridBagConstraints c0 = new GridBagConstraints();

        backGround = new ImageIcon("black.png").getImage();
        jLabelFrontCover = new JLabel(new ImageIcon("frontCover.jpg"));
        c0.gridx = 0;
        c0.gridy = 0;
        c0.gridwidth = 10;
        c0.gridheight = 3;
        c0.weightx = 1;
        c0.weighty = 1;
        c0.fill = GridBagConstraints.BOTH;
        c0.anchor = GridBagConstraints.CENTER;
        this.add(jLabelFrontCover, c0);

        //jLebel and jTextArea
        UIManager.put("Label.font", localUIData.font1);
        UIManager.put("Label.foreground", Color.yellow);
        UIManager.put("TextField.font", localUIData.font1);

        jLabelIP = new JLabel("IP:");
        jLabelName = new JLabel("Your name:");
        jTextFieldIP = new JTextField();
        jTextFieldIP.setColumns(8);
        jTextFieldName = new JTextField("snake");
        jTextFieldName.setColumns(8);

        JTextFieldLimit limitDocument = new JTextFieldLimit(5);     //只允許長度為5
        jTextFieldName.setDocument(limitDocument);

        c0.gridx = 1;
        c0.gridy = 3;
        c0.gridwidth = 1;
        c0.gridheight = 1;
        c0.weightx = 1;
        c0.weighty = 0;
        c0.fill = GridBagConstraints.VERTICAL;
        c0.anchor = GridBagConstraints.EAST;
        c0.insets = new Insets(0, 0, 60, 5);
        this.add(jLabelIP, c0);

        c0.gridx = 2;
        c0.gridy = 3;
        c0.gridwidth = 3;
        c0.gridheight = 1;
        c0.weightx = 1;
        c0.weighty = 0;
        c0.fill = GridBagConstraints.VERTICAL;
        c0.anchor = GridBagConstraints.WEST;
        c0.insets = new Insets(0, 5, 60, 20);
        this.add(jTextFieldIP, c0);

        c0.gridx = 5;
        c0.gridy = 3;
        c0.gridwidth = 2;
        c0.gridheight = 1;
        c0.weightx = 1;
        c0.weighty = 0;
        c0.fill = GridBagConstraints.VERTICAL;
        c0.anchor = GridBagConstraints.EAST;
        c0.insets = new Insets(0, 0, 60, 5);
        this.add(jLabelName, c0);

        c0.gridx = 7;
        c0.gridy = 3;
        c0.gridwidth = 3;
        c0.gridheight = 1;
        c0.weightx = 1;
        c0.weighty = 0;
        c0.fill = GridBagConstraints.VERTICAL;
        c0.anchor = GridBagConstraints.WEST;
        c0.insets = new Insets(0, 5, 60, 0);
        this.add(jTextFieldName, c0);


        //button
        buttonStart = new JButton();
        buttonStart.setIcon(new ImageIcon("start.jpg"));
        buttonStart.setFocusPainted(false);
        buttonStart.setContentAreaFilled(false);
        buttonStart.setPreferredSize(new Dimension(300, 80));
        StartButtonHandler startButtonHandler = new StartButtonHandler();
        buttonStart.addActionListener(startButtonHandler);

        localUIData.buttonConnect = new JButton();
        localUIData.buttonConnect.setIcon(new ImageIcon("connect.jpg"));
        localUIData.buttonConnect.setFocusPainted(false);
        localUIData.buttonConnect.setContentAreaFilled(false);
        localUIData.buttonConnect.setPreferredSize(new Dimension(300, 80));
        ConnectButtonHandler connectButtonHandler = new ConnectButtonHandler();
        localUIData.buttonConnect.addActionListener(connectButtonHandler);

        c0.gridx = 0;
        c0.gridy = 4;
        c0.gridwidth = 5;
        c0.gridheight = 1;
        c0.weightx = 0;
        c0.weighty = 0;
        c0.fill = GridBagConstraints.NONE;
        c0.anchor = GridBagConstraints.NORTH;
        c0.insets = new Insets(10, 0, 80, 0);
        this.add(localUIData.buttonConnect, c0);

        c0.gridx = 5;
        c0.gridy = 4;
        c0.gridwidth = 5;
        c0.gridheight = 1;
        c0.weightx = 0;
        c0.weighty = 0;
        c0.fill = GridBagConstraints.NONE;
        c0.anchor = GridBagConstraints.NORTH;
        c0.insets = new Insets(10, 0, 80, 0);
        this.add(buttonStart, c0);
    }
    public void paintComponent(Graphics g){
        g.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    class StartButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //連接上server後 start才有用
            if(LocalUIData.isConnected && jTextFieldName.getText()!=null){
                //拿名字
                localUIData.stringName = jTextFieldName.getText();
                localUIData.jLabelName.setText(localUIData.stringName + ":");
                if(localUIData.stringName.equals("")){
                    localUIData.jLabelName.setText("Snake:");
                }
                //告訴server已經開始了
                synchronized (dynamicUIData) {
                    dynamicUIData.booIsStart.set(localUIData.intPosition, true);
                    try {
                        LocalUIData.doOutToServer.reset();
                        LocalUIData.doOutToServer.writeObject(dynamicUIData);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Start button do out to server error.");
                    }
                }
                //切換到下一個畫面
                localUIData.card.show(localUIData.pane, "panelPlay");
                localUIData.playPanel.setFocusable(true);
                localUIData.playPanel.requestFocus();

                Timer timerStartSnake = new Timer();
                TimerTask timerTaskStartSnake = new TimerTask() {
                    @Override
                    public void run() {
                        if(dynamicUIData.booIsStart.get(0) && dynamicUIData.booIsStart.get(1)){
                            try{Thread.sleep(2000);} catch(Exception e){}
                            //localUIData.playPanel.startSnake();
                            Thread threadStartSnake = new Thread(localUIData.playPanel);
                            threadStartSnake.start();
                            timerStartSnake.cancel();
                            timerStartSnake.purge();
                        }
                    }
                };
                timerStartSnake.schedule(timerTaskStartSnake, 0, 10);
            }
            else if(!LocalUIData.isConnected){
                JOptionPane.showMessageDialog(null, "You have to connect first!");
            }
            else{
                JOptionPane.showMessageDialog(null, "You have to enter your name!");
            }
        }
    }

    class ConnectButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //按下連接後才開始連接到Server
            String stringIP = jTextFieldIP.getText();
            localUIData.client.connect(stringIP);
        }
    }
}

class JTextFieldLimit extends PlainDocument {
    private int limit;
    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null)
            return;
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}