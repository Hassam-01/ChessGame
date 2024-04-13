package main;
import javax.swing.JFrame;

public class Main{

    public static void main(String[] args) {
        JFrame window =  new JFrame("Chess Mate AI");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ? setting game close option
        window.setResizable(false); // ? Make resize false so can't be resized to avoid disruption in display
        
        
        PanelGame panel = new PanelGame(); // ? making an instance of Game panel
        window.add(panel); // ? Adding the panel to the window
        window.pack(); // ? adjusts the size according to the game panel
        
        window.setLocationRelativeTo(null); //*  null to make the window appear in the center
        window.setVisible(true);

        panel.launchGame();
    }
}
