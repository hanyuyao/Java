package snake.common.panel;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    AllData data;
    Image end, snake;
    public EndPanel(AllData allData) {
        setPreferredSize(new Dimension(759, 925));

        data = allData;

        end = new ImageIcon("end.png").getImage();
        snake = new ImageIcon("icon.png").getImage();

        this.setLayout(null);

        data.lebelScore.setFont(AllData.font2);
        data.lebelScore.setBounds(100, 50, 600, 200);
        data.lebelScore.setForeground(Color.yellow);
        this.add(allData.lebelScore);
    }
    public void paintComponent(Graphics g){
        g.drawImage(end, 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawImage(snake, 200, 200, this.getWidth(), this.getHeight(), null);
    }
}
