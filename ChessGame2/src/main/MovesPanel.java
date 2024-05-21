package main;

import javax.swing.*;
import java.awt.*;
public class MovesPanel extends JPanel{
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        // Obtain the Graphics2D object
        int x = 10;
        int y = 20;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);

        for(String str : PanelGame.movePlayed){
            g2d.setFont(new Font("Book Antique", Font.PLAIN, 15));
            g2d.drawString( str, x , y);
            x += 50;
            if(x > 180){
                x = 10;
                y += 45;
            }
            if(g2d.getColor() == Color.BLUE)
                g2d.setColor(Color.YELLOW);
            else
                g2d.setColor(Color.BLUE);

        }
    }
    @Override

    public Dimension getPreferredSize() {
        // Calculate the preferred size based on the number of moves and font size
        int fontSize = 15;
        int lineHeight = fontSize + 10; // Add some padding
        int numLines = (PanelGame.movePlayed.size() + 1) / 2; // 2 moves per line
        int height = numLines * lineHeight;
        return new Dimension(250, height);

    }
}