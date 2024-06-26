package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class ColorSelect extends JFrame implements WindowListener, ActionListener {
    JButton black;
    JButton white;
    public ColorSelect(){
        ImageIcon backgroundIcon = null;
        try {
            Image backgroundImage = ImageIO.read(getClass().getResource("/piece/Wallpaper2.png"));
            backgroundIcon = new ImageIcon(backgroundImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then we create a background label
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new GridBagLayout()); // Use GridBagLayout to center buttons


        JPanel pan = new JPanel();
        black = new JButton("Black");
        black.addActionListener(this);
        white = new JButton("White");
        white.addActionListener(this);

        // Create panel for buttons with transparent background as we dont want the panel to block the image
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        black = new JButton("Black");

        black.setBackground(Color.BLACK);
        black.setForeground(Color.WHITE);
        white.setBackground(Color.WHITE);
        white.setForeground(Color.BLACK);

        black.addActionListener(this);
        white = new JButton("White");
        white.addActionListener(this);

        buttonPanel.add(black);
        buttonPanel.add(white);

        // Add the button panel to the background label
        background.add(buttonPanel, new GridBagConstraints());

        // Set the background label as the content pane
        this.setContentPane(background);

        this.setSize(200,200);
        this.setLocationRelativeTo(null);
        this.addWindowListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //0 is white, 1 is black
        if (e.getSource() == black){
            PanelGame.UserColor = 0;
            PanelGame.compColor = 1;
        } else if (e.getSource() == white) {
            PanelGame.UserColor = 1;
            PanelGame.compColor = 0;
        }

        this.dispose(); //go to window closed
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        //we open chess game after this object window is closed
        JFrame window = new JFrame("Chess Mate AI");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ? setting game close option
        window.setResizable(false); // ? Make resize false so can't be resized to avoid disruption in display

        PanelGame panel = new PanelGame(Main.MODE); // ? making an instance of Game panel
        window.add(panel); // ? Adding the panel to the window
        window.pack(); // ? adjusts the size according to the game panel

        window.setLocationRelativeTo(null); //*  null to make the window appear in the center
        window.setVisible(true);

        panel.launchGame();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
