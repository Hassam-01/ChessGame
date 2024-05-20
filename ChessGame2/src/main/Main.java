package main;
import javax.swing.*;

public class Main{
    public static int MODE = -1;
    public static boolean check;
    public static void main(String[] args) {
        JFrame window2 =  new JFrame("Chess Mate AI");

        window2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ? setting game close option
        window2.setResizable(false); // ? Make resize false so can't be resized to avoid disruption in display

        new ChessSwingOpening(window2);
        window2.dispose();
        window2.pack(); // ? adjusts the size according to the game panel
        window2.setLocationRelativeTo(null); //*  null to make the window appear in the center
        window2.setVisible(true);
    }
    // hello
}
