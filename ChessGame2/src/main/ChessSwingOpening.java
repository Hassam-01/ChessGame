package main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
//package chessswingopening;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;

public class ChessSwingOpening {
    private Clip clip; // Declare Clip as an instance variable
    private static JFrame frame;
    // Constructor

    public static void setPanel(JFrame frame){
        ChessSwingOpening.frame = frame;
    }
    public  ChessSwingOpening(JFrame frame) {

        this.frame = frame;

        this.frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (Main.MODE == 0) {
                    ColorSelect cs = new ColorSelect();
                    cs.setVisible(true);
                } else{
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
//                JFrame window = new JFrame("Chess Mate AI");
//
//                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ? setting game close option
//                window.setResizable(false); // ? Make resize false so can't be resized to avoid disruption in display
//
//                PanelGame panel = new PanelGame(Main.MODE); // ? making an instance of Game panel
//                window.add(panel); // ? Adding the panel to the window
//                window.pack(); // ? adjusts the size according to the game panel
//
//                window.setLocationRelativeTo(null); //*  null to make the window appear in the center
//                window.setVisible(true);
//
//                panel.launchGame();
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
        });
            // Create a JFrame
//        frame = new JFrame("Chess Interface");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a CardLayout container
        JPanel mainPanel = new JPanel(new CardLayout());
//        mainPanel.setLayout(new CardLayout());

        // Main view with the chess wallpaper and initial buttons
        JPanel mainView = createMainView(mainPanel);

        // Instructions view with Page2.png and new buttons
        JPanel instructionsView = createInstructionsView(mainPanel);

        // Moves description view with MovesDescription.png
        JPanel movesDescriptionView = createMovesDescriptionView(mainPanel);

        // Check description view with CheckDescription.png
        JPanel checkDescriptionView = createCheckDescriptionView(mainPanel);

        // Basic concepts description view with BasicConceptsDescription.png
        JPanel basicConceptsDescriptionView = createBasicConceptsDescriptionView(mainPanel);

        // En passant description view with EnPassant.png
        JPanel enPassantDescriptionView = createEnPassantDescriptionView(mainPanel);

        // Castling description view with Castling.png
        JPanel castlingDescriptionView = createCastlingDescriptionView(mainPanel);

        // Promotion description view with Promotion.png
        JPanel promotionDescriptionView = createPromotionDescriptionView(mainPanel);

        // Add views to the main panel
        mainPanel.add(mainView, "MainView");
        mainPanel.add(instructionsView, "InstructionsView");
        mainPanel.add(movesDescriptionView, "MovesDescriptionView");
        mainPanel.add(checkDescriptionView, "CheckDescriptionView");
        mainPanel.add(basicConceptsDescriptionView, "BasicConceptsDescriptionView");
        mainPanel.add(enPassantDescriptionView, "EnPassantDescriptionView");
        mainPanel.add(castlingDescriptionView, "CastlingDescriptionView");
        mainPanel.add(promotionDescriptionView, "PromotionDescriptionView");

        // Add the main panel to the frame
        frame.add(mainPanel);

        // Set frame size
//        frame.setSize(990, 688);

        // Set frame visibility
//        frame.setVisible(true);

        // Start playing background music
        playBackgroundMusic();
    }

    private void playBackgroundMusic() {
        try {
            // Load the background music file
            URL soundURL = getClass().getResource("/piece/BackgroundMusic.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Start playing the music in a loop
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private JPanel createMainView(JPanel mainPanel) {
        // Load the chess wallpaper image
        ImageIcon icon = new ImageIcon(getClass().getResource("/piece/Chess wallpaper.png"));
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(990, 688, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(scaledIcon);
        label.setHorizontalAlignment(JLabel.CENTER);

        // Create buttons with images
        ImageIcon playIcon = new ImageIcon(getClass().getResource("/piece/Play.png"));
        ImageIcon computerIcon = new ImageIcon(getClass().getResource("/piece/Computer.png"));
        ImageIcon instructionsIcon = new ImageIcon(getClass().getResource("/piece/Instructions.png"));

        JButton playButton = createCustomButton(playIcon);
        JButton computerButton = createCustomButton(computerIcon);
        JButton instructionsButton = createCustomButton(instructionsIcon);

        // Add action listeners to buttons
        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "InstructionsView");
            }
        });

        // this button returns 1 to the panelGame to set mode as player tp player
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle play button click
                Main.MODE = 1;
                frame.dispose();
                clip.stop();
                clip.close();
//                System.exit(0);

            }
        });

        // this button returns 0 to the PanelGame to set the mode as player vs computer
        computerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle computer button click
                Main.MODE = 0;
//                Main.check = true;
                frame.dispose();
//                new PanelGame(Main.MODE);
                clip.stop();
                clip.close();
//                System.exit(0);
            }
        });

        // Create a JPanel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(computerButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(instructionsButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Create a layered pane to layer components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(990, 688));

        // Add background label and button panel to layered pane
        layeredPane.add(label, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        // Create and add dancing welcome label
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(50, 20, 200, 50);
        layeredPane.add(welcomeLabel, JLayeredPane.PALETTE_LAYER);
        animateText(welcomeLabel); // Start animation

        // Set button panel position
        int buttonPanelX = (990 - buttonPanel.getPreferredSize().width) / 2;
        int buttonPanelY = (688 - buttonPanel.getPreferredSize().height) / 2;
        buttonPanel.setBounds(buttonPanelX, buttonPanelY, buttonPanel.getPreferredSize().width, buttonPanel.getPreferredSize().height);
        label.setBounds(0, 0, 990, 688);

        JPanel mainView = new JPanel(new BorderLayout());
        mainView.add(layeredPane, BorderLayout.CENTER);

        return mainView;
    }

    // Method to animate text
    private void animateText(JLabel label) {
        Timer timer = new Timer(500, new ActionListener() {
            int currentIndex = 0;
            char[] text = label.getText().toCharArray();
            Color[] colors = new Color[text.length];

            {
                for (int i = 0; i < text.length; i++) {
                    colors[i] = Color.BLACK;
                }
                colors[0] = Color.WHITE;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < text.length) {
                    colors[currentIndex] = Color.WHITE;
                    label.setForeground(new Color(255, 255, 255)); // White color
                    label.setText(buildString(text, colors));
                    currentIndex++;
                } else {
                    currentIndex = 0;
                    for (int i = 0; i < text.length; i++) {
                        colors[i] = Color.BLACK;
                    }
                    colors[0] = Color.WHITE;
                    label.setForeground(new Color(255, 255, 255)); // White color
                    label.setText(buildString(text, colors));
                }
            }

            private String buildString(char[] text, Color[] colors) {
                StringBuilder sb = new StringBuilder("<html>");
                for (int i = 0; i < text.length; i++) {
                    sb.append("<font color=");
                    sb.append(getHexColor(colors[i]));
                    sb.append(">");
                    sb.append(text[i]);
                    sb.append("</font>");
                }
                sb.append("</html>");
                return sb.toString();
            }

            private String getHexColor(Color color) {
                String hex = Integer.toHexString(color.getRGB() & 0xffffff);
                if (hex.length() < 6) {
                    hex = "000000".substring(0, 6 - hex.length()) + hex;
                }
                return "#" + hex;
            }
        });
        timer.start();
    }

    private JPanel createInstructionsView(JPanel mainPanel) {
        // Load the Page2 image
        ImageIcon newIcon = new ImageIcon(getClass().getResource("/piece/Page2.png"));
        ImageIcon scaledNewIcon = new ImageIcon(newIcon.getImage().getScaledInstance(990, 688, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(scaledNewIcon);
        label.setHorizontalAlignment(JLabel.CENTER);

        // Create buttons with images
        ImageIcon specialMovesIcon = new ImageIcon(getClass().getResource("/piece/SpecialMoves.png"));
        ImageIcon checkIcon = new ImageIcon(getClass().getResource("/piece/Check.png"));
        ImageIcon movesIcon = new ImageIcon(getClass().getResource("/piece/Moves.png"));
        ImageIcon basicConceptsIcon = new ImageIcon(getClass().getResource("/piece/BasicConcepts.png"));

        JButton specialMovesButton = createCustomButton(specialMovesIcon);
        JButton checkButton = createCustomButton(checkIcon);
        JButton movesButton = createCustomButton(movesIcon);
        JButton basicConceptsButton = createCustomButton(basicConceptsIcon);

        // Add action listeners to buttons
        specialMovesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "EnPassantDescriptionView");
            }
        });

        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "CheckDescriptionView");
            }
        });

        movesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "MovesDescriptionView");
            }
        });

        basicConceptsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "BasicConceptsDescriptionView");
            }
        });

        // Create a return button
        JButton returnButton = createReturnButton(mainPanel, "MainView");

        // Create a JPanel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(specialMovesButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(checkButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(movesButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(basicConceptsButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Create a layered pane to layer components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(990, 688));

        // Add background label, button panel, and return button to layered pane
        layeredPane.add(label, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(returnButton, JLayeredPane.PALETTE_LAYER);

        int buttonPanelX = (990 - buttonPanel.getPreferredSize().width) / 2;
        int buttonPanelY = (688 - buttonPanel.getPreferredSize().height) / 2;

        buttonPanel.setBounds(buttonPanelX, buttonPanelY, buttonPanel.getPreferredSize().width, buttonPanel.getPreferredSize().height);
        label.setBounds(0, 0, 990, 688);
        returnButton.setBounds(5, 5, 20, 20);

        JPanel instructionsView = new JPanel(new BorderLayout());
        instructionsView.add(layeredPane, BorderLayout.CENTER);

        return instructionsView;
    }

    private JPanel createMovesDescriptionView(JPanel mainPanel) {
        return createDescriptionView(mainPanel, "/piece/MovesDescription.png", "InstructionsView", true);
    }

    private JPanel createCheckDescriptionView(JPanel mainPanel) {
        return createDescriptionView(mainPanel, "/piece/CheckDescription.png", "InstructionsView", true);
    }

    private JPanel createBasicConceptsDescriptionView(JPanel mainPanel) {
        return createDescriptionView(mainPanel, "/piece/BasicConceptsDescription.png", "InstructionsView", true);
    }

    private JPanel createEnPassantDescriptionView(JPanel mainPanel) {
        JPanel enPassantDescriptionView = createDescriptionView(mainPanel, "/piece/EnPassant.png", "InstructionsView", true);
        JButton nextButton = createNextButton(mainPanel, "CastlingDescriptionView");
        JLayeredPane layeredPane = (JLayeredPane) enPassantDescriptionView.getComponent(0);
        layeredPane.add(nextButton, JLayeredPane.PALETTE_LAYER);
        nextButton.setBounds(965, 5, 20, 20);
        return enPassantDescriptionView;
    }

    private JPanel createCastlingDescriptionView(JPanel mainPanel) {
        JPanel castlingDescriptionView = createDescriptionView(mainPanel, "/piece/Castling.png", "InstructionsView", true);
        JButton nextButton = createNextButton(mainPanel, "PromotionDescriptionView");
        JLayeredPane layeredPane = (JLayeredPane) castlingDescriptionView.getComponent(0);
        layeredPane.add(nextButton, JLayeredPane.PALETTE_LAYER);
        nextButton.setBounds(965, 5, 20, 20);
        return castlingDescriptionView;
    }

    private JPanel createPromotionDescriptionView(JPanel mainPanel) {
        return createDescriptionView(mainPanel, "/piece/Promotion.png", "InstructionsView", true);
    }

    private JPanel createDescriptionView(JPanel mainPanel, String imagePath, String returnView, boolean addReturnButton) {
        // Load the description image
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(990, 688, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(scaledIcon);
        label.setHorizontalAlignment(JLabel.CENTER);

        // Create a return button
        JButton returnButton = createReturnButton(mainPanel, returnView);

        // Create a layered pane to layer components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(990, 688));

        // Add background label and return button to layered pane
        layeredPane.add(label, JLayeredPane.DEFAULT_LAYER);
        if (addReturnButton) {
            layeredPane.add(returnButton, JLayeredPane.PALETTE_LAYER);
        }

        label.setBounds(0, 0, 990, 688);
        if (addReturnButton) {
            returnButton.setBounds(5, 5, 20, 20);
        }

        JPanel descriptionView = new JPanel(new BorderLayout());
        descriptionView.add(layeredPane, BorderLayout.CENTER);

        return descriptionView;
    }

    private JButton createCustomButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        Dimension originalSize = new Dimension(170, 50); // Original size of the button
        button.setPreferredSize(originalSize);
        button.setBorder(new EmptyBorder(30, 20, 30, 20)); // Increase the button's border to increase its size
        button.setContentAreaFilled(false); // Remove content area fill

        int targetWidth = (int) (originalSize.width * 1.3);
        int targetHeight = (int) (originalSize.height * 1.3);

        Timer timer = new Timer(20, null);
        timer.addActionListener(new ActionListener() {
            int currentWidth = originalSize.width;
            int currentHeight = originalSize.height;

            @Override
            public void actionPerformed(ActionEvent e) {
                int step = 5; // Step size for animation
                if (currentWidth < targetWidth && currentHeight < targetHeight) {
                    currentWidth += step;
                    currentHeight += step;
                    button.setPreferredSize(new Dimension(currentWidth, currentHeight));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                timer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setPreferredSize(originalSize); // Restore original size
                timer.stop();
            }
        });

        return button;
    }
    private JButton createReturnButton(JPanel mainPanel, String returnView) {
        ImageIcon returnIcon = new ImageIcon(getClass().getResource("/piece/Return.png"));
        Image returnImg = returnIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        returnIcon = new ImageIcon(returnImg);
        JButton returnButton = new JButton(returnIcon);
        returnButton.setPreferredSize(new Dimension(20, 20));
        returnButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        returnButton.setContentAreaFilled(false);
        returnButton.setFocusPainted(false);
        returnButton.setBorderPainted(false);

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, returnView);
            }
        });

        return returnButton;
    }

    private JButton createNextButton(JPanel mainPanel, String nextView) {
        ImageIcon nextIcon = new ImageIcon(getClass().getResource("/piece/Next.png"));
        Image nextImg = nextIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        nextIcon = new ImageIcon(nextImg);
        JButton nextButton = new JButton(nextIcon);
        nextButton.setPreferredSize(new Dimension(20, 20));
        nextButton.setBorder(new EmptyBorder(0, 0, 5, 5));
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, nextView);
            }
        });

        return nextButton;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new ChessSwingOpening();
//            }
//        });
//    }
}
