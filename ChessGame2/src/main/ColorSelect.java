package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ColorSelect extends JFrame implements WindowListener, ActionListener {
    JButton black;
    JButton white;
    public static int userColor = 0; //0 is white, 1 is black
    public ColorSelect(){
        System.out.println("Color Wind");
        JPanel pan = new JPanel();
        black = new JButton("Black");
        black.addActionListener(this);
        white = new JButton("White");
        white.addActionListener(this);
        pan.add(black);
        pan.add(white);
        this.add(pan);
        this.setSize(300,300);
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
