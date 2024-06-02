package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovesPanel extends JPanel {
    // Cache the font object
    private final Font font = new Font("Book Antique", Font.PLAIN, 15);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);  // Set the font once

        List<String> moves = new ArrayList<>(PanelGame.movePlayed);  // Create a copy of the list

        int x = 10;
        int y = 20;
        boolean isBlue = true;

        for (String str : moves) {
            g2d.setColor(isBlue ? Color.BLUE : Color.YELLOW);
            g2d.drawString(str, x, y);
            x += 50;
            if (x > 180) {
                x = 10;
                y += 45;
            }
            isBlue = !isBlue;  // Alternate color
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
