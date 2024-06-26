package main;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;
//package Chess.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class PanelGame extends JPanel implements Runnable {

    Computer Comp = new Computer();    // Computer object to make computer moves

    // Scanner Object
    Scanner scan = new Scanner(System.in);

//Comp = new Computer(); // Computer object

    // * declaring the width as final and bit larger than the height so that we have space to
    // * show game statistics  at the side of 800*800 board
    public static final int WIDTH = 990;
    public static final int HEIGHT = 688; //* Final HEIGHT of 800
    final int FPS = 60; // * no of screen refreshes per second to be made

    Thread gamThread;

    Board board = new Board();
    Mouse mouse = new Mouse();

    // * Color of the pieces
    public static final int WHITE = 1;
    public static final int BLACK = 0;

    public static final int PVsC = 0;
    public static final int PVsP = 1;
    public static int MODE;
    public static int compColor;   // color for the computer
    public static int UserColor;   // color for the computer
    int currentColor = UserColor;    // Color to check whose turn is it

    int movesRow;
    String movesCol;
    String initials;

    // * Boolean values for the piece to move
    boolean canMove;        // check if the piece is able to move
    boolean validSquare;    // check if the square is valid over which the active piece is hovering
    boolean promotion;    // check if the pawn can be promoted
    boolean gameOver;        // check if checkmate
    boolean stalemate;    // check stalemate
    public static boolean compLose = false;


    // * Pieces on the board
    public static ArrayList<ChessPieces> simpieces = new ArrayList<>();
    // * pieces would be used for backup to reverse the moves the player made
    public static ArrayList<ChessPieces> pieces = new ArrayList<>();

    // Array list to display piece that pawn can be promoted to
    ArrayList<ChessPieces> promoPiece = new ArrayList<>();
   public static ArrayList<String> movePlayed = new ArrayList<>(); // to store the moves that have been played

    ChessPieces activePiece, checkingP, compPiece; // The piece on which the mouse is pressed
    public static ChessPieces castlingPiece; // used for castling of kind and rook

    private JButton homeButton;
    private int textCount =0;
//    private JScrollPane scrollBar;

    MovesPanel MP = new MovesPanel();
    JScrollPane scrollBar = new JScrollPane(MP);



    // ! Constructor for PanelGame
    public PanelGame(int MODE) {

        PanelGame.MODE = MODE; // setting the mode
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); // ? defining the size of the panel
        setBackground(Color.black); // ? setting the background as black

        setLayout(null);

        scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollBar.setBounds(720, 350, 250, 200);
        add(scrollBar);

        homeButton = new JButton("Home");
        homeButton.setBounds(800, 600, 90, 30);
        homeButton.setBackground(Color.white);
        homeButton.setForeground(Color.black);
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current frame
                Window window = SwingUtilities.getWindowAncestor(PanelGame.this);
                if (window != null) {
                    window.dispose();
                    pieces.clear();
                    simpieces.clear();
                    movePlayed.clear();
                }
                Main.main(new String[0]);
            }
        });
        addMouseMotionListener(mouse); // Adding mouse motion listener
        addMouseListener(mouse);
        add(homeButton);

        // ? Setting the pieces on the board
        setPieces();

        copyArrayList(simpieces, pieces);
    }

    public void setPieces() {

        // ! White Pieces
        pieces.add(new Pawn(WHITE, 6, 0));
        pieces.add(new Pawn(WHITE, 6, 1));
        pieces.add(new Pawn(WHITE, 6, 2));
        pieces.add(new Pawn(WHITE, 6, 3));
        pieces.add(new Pawn(WHITE, 6, 4));
        pieces.add(new Pawn(WHITE, 6, 5));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 6, 7));
        pieces.add(new Rook(WHITE, 7, 0));
        pieces.add(new Knight(WHITE, 7, 1));
        pieces.add(new Bishop(WHITE, 7, 2));
        pieces.add(new Queen(WHITE, 7, 3));
        pieces.add(new King(WHITE, 7, 4));
        pieces.add(new Bishop(WHITE, 7, 5));
        pieces.add(new Knight(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 7, 7));


        // ! Black Pieces
        pieces.add(new Pawn(BLACK, 1, 0));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 1, 2));
        pieces.add(new Pawn(BLACK, 1, 3));
        pieces.add(new Pawn(BLACK, 1, 4));
        pieces.add(new Pawn(BLACK, 1, 5));
        pieces.add(new Pawn(BLACK, 1, 6));
        pieces.add(new Pawn(BLACK, 1, 7));
        pieces.add(new Rook(BLACK, 0, 7));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Bishop(BLACK, 0, 2));
        pieces.add(new Bishop(BLACK, 0, 5));
        pieces.add(new Knight(BLACK, 0, 1));
        pieces.add(new Knight(BLACK, 0, 6));
        pieces.add(new King(BLACK, 0, 4));
        pieces.add(new Queen(BLACK, 0, 3));


    }

    private void copyArrayList(ArrayList<ChessPieces> target, ArrayList<ChessPieces> source) {
        // ! Clearing if any null or unnecessary item in the target
        target.clear();

        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i)); // ! copying source to target
        }

    }

    public static void copyArrayListComp(ArrayList<ChessPieces> target, ArrayList<ChessPieces> source) {
        // ! Clearing if any null or unnecessary item in the target
        target.clear();

        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i)); // ! copying source to target
        }

    }


    public void launchGame() {
	// To start new thread
        gamThread = new Thread(this);
        gamThread.start(); // ? this methods invokes the run method being implemented from Runnable

    }

    @Override
    public void run() { // ? This method is used to make a game loop

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime(); // * used to find the elpased time
        long currentTime;

        while (gamThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta > 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
	    // there is seperate condition for computer pawn promotion
        if (MODE == PVsP || (MODE == PVsC && currentColor != compColor)) {

            if (promotion) {
                promoting();
            } else if (!gameOver && !stalemate) {

                if (mouse.pressed) {
                    // if the active piece is null means the player is not holding a piece and can pick a piece
                    if (activePiece == null) {
                        // checking which piece is the mouse hovering on
                        for (ChessPieces P : simpieces) {

                            if (P.color == currentColor && P.row == mouse.y / Board.SQUARE_SIZE && P.col == mouse.x / Board.SQUARE_SIZE) {
                                // when piece found setting it as active
                                activePiece = P;
                            }
                        }
                    } else {    // When the activepiece is not null means the player is already holding a piece so it can be moved

                        // method to move the piece
                        simulate();
                    }
                }

                if (mouse.pressed == false) {
                    if (activePiece != null) {
                        if (validSquare) {

                            // moved confirmed
                            // if the player makes the move copy the pieces to the backup arraylist
                            copyArrayList(pieces, simpieces);
                            activePiece.updatePosition();
                            if (castlingPiece != null)
                                castlingPiece.updatePosition();

                            if ((kingInCheck() && isCheckMate()) || compLose) {
                                gameOver = true;
                            } else if (isStaleMate() && !isCheckMate()) {
                                stalemate = true;
                            } else {
                                if (canPromote()) {
                                    promotion = true;
                                } else
                                    changePlayer();
                            }

                        } else {
                            copyArrayList(simpieces, pieces); // if the player cancels the move restore the orignal standings of the pieces
                            activePiece.resetPosition();
                            activePiece = null;
                        }
                    }
                }
            }

        } // if bracket of currentColor != compColor
        else {
            ComputerTurn(); // Computer's Turn
        }
    }

    // if(MODE == PVsC && currentColor != compColor)
    public void ComputerTurn() {
        Comp.choosePiece();     //calling computer to make a move
        compPiece = Comp.Cpiece(); // getting the computer's active piece

        compSimulate();

        copyArrayList(pieces, simpieces);

        compPiece.updatePosition();

        if (kingInCheck() && isCheckMate()) {
            gameOver = true;
        } else
            changePlayer();
    }

    private ChessPieces getKing(boolean opponent) {
        ChessPieces king = null;

        for (ChessPieces P : simpieces) {
            if (opponent) {
                if (P.type == Type.KING &&  P.color != currentColor) {
                    return P;
                }
            } else {
                if (P.type == Type.KING && P.color == currentColor) {
                    return P;
                }
            }
        }
        return king;
    }

    // opponent can capture king
    public boolean opponentCanCaptureKing() {
        ChessPieces king = getKing(false);

        for (ChessPieces P : simpieces) {
            if (P.color != king.color && P.canMove(king.col, king.row))
                return true;
        }
        return false;
    }


    // check checkmate method
    public boolean kingInCheck() {
        ChessPieces king = getKing(true); // passing true to get the opponent king

        if (MODE == PVsP || (MODE == PVsC && currentColor != compColor)) {

            if (activePiece.canMove(king.col, king.row)) {

                checkingP = activePiece;

                return true;

            }

        } else if (MODE == PVsC) {
            if (compPiece.canMove(king.col, king.row)) {
                checkingP = compPiece;
                return true;
            }
        }
        checkingP = null;
        return false;
    }


    // promoting method
    public void promoting() {
        // if the mouse is pressed we checked the location of the mouse and match the piece and choose it as the promoted version
        if (mouse.pressed) {
            for (ChessPieces P : promoPiece) {
                if (P.col == mouse.x / Board.SQUARE_SIZE && P.row == mouse.y / Board.SQUARE_SIZE) {
                    switch (P.type) {
                        case ROOK:
                            simpieces.add(new Rook(currentColor, activePiece.row, activePiece.col));
                            break;
                        case QUEEN:
                            simpieces.add(new Queen(currentColor, activePiece.row, activePiece.col));
                            break;
                        case BISHOP:
                            simpieces.add(new Bishop(currentColor, activePiece.row, activePiece.col));
                            break;
                        case KNIGHT:
                            simpieces.add(new Knight(currentColor, activePiece.row, activePiece.col));
                            break;
                        default:
                            break;
                    }

                    simpieces.remove(activePiece.getIndex()); // remove the pawn after promotion
                    copyArrayList(pieces, simpieces);            // copy the array to the backup array also
                    activePiece = null;                            // setting the active piece as null after selection
                    promotion = false;                            // when prmotion is done making it false
                    changePlayer();                                // when piece has been selected for promotion then the turn switches
                }
            }
        }
    }


    public void compSimulate() {

        canMove = false;
        validSquare = false;

        // updating the position of the piece according to the mouse movements
        compPiece.x = compPiece.getX(compPiece.col);
        compPiece.y = compPiece.getY(compPiece.row);

        compPiece.col = compPiece.getCol(compPiece.x);
        compPiece.row = compPiece.getRow(compPiece.y);

        canMove = true;

        if (compPiece.hittingPiece != null) {
            simpieces.remove(compPiece.hittingPiece.getIndex());
        }

//	 checkCastling();
        if (!illegalMove(compPiece) && !opponentCanCaptureKing())
            validSquare = true;

//	 	}

    }

    // responsible for the new position of the piece when dragged with the mouse
// this function is active when mouse is pressed
    public void simulate() {

        if (activePiece != null) {

            canMove = false;
            validSquare = false;

            // copying the backup pieces to the array list so that the pieces dont disappear when the player hovers the active piece over the pieces
            copyArrayList(simpieces, pieces);

            // Reset castling position
            if (castlingPiece != null) {
                castlingPiece.col = castlingPiece.precol;
                castlingPiece.x = castlingPiece.getX(castlingPiece.col);
                castlingPiece = null;

            }


            // updating the position of the piece according to the mouse movements
            activePiece.x = mouse.x - Board.HALF_SQUARE_SIZE;
            activePiece.y = mouse.y - Board.HALF_SQUARE_SIZE;

            activePiece.col = activePiece.getCol(activePiece.x);
            activePiece.row = activePiece.getRow(activePiece.y);

            if (activePiece.canMove(activePiece.col, activePiece.row)) {

                canMove = true;

                if (activePiece.hittingPiece != null) {
                    simpieces.remove(activePiece.hittingPiece.getIndex());
                }

                checkCastling();
                if (!illegalMove(activePiece) && !opponentCanCaptureKing())
                    validSquare = true;

            }
        }
    }


    //method to check if the pawn can be promoted
    public boolean canPromote() {
        if (activePiece.type == Type.PAWN)
            if (currentColor == WHITE && activePiece.row == 0 || currentColor == BLACK && activePiece.row == 7) {
                promoPiece.clear();

                // Adding pieces to the side of the chess board from where the player can choose;
                promoPiece.add(new Rook(currentColor, 2, 9));
                promoPiece.add(new Queen(currentColor, 3, 9));
                promoPiece.add(new Bishop(currentColor, 4, 9));
                promoPiece.add(new Knight(currentColor, 5, 9));

                return true;
            }
        return false;
    }

    // check stale mate which is a game draw condition
    public boolean isStaleMate() {
        // stale mate happens when the king is only left and it has no legal moves
        int count = 0; // count to check the number of pieces remaining

        for (ChessPieces P : simpieces) {
            if (P.color != currentColor) {
                count++;
            }
        }
        if (count == 1) {    // if only 1 piece is left that is the king
            if (!kingCanMove(getKing(true))) { // check if the king can make a legal move
                return true;
            }
        }

        return false;
    }


    // check check mate
    public boolean isCheckMate() {

        // check mate is check  for up down and left right, diagonal and not for knight because it can't be blocked

        ChessPieces king = getKing(true);

        if (kingCanMove(king) || checkingP == null) {
            return false;
        } else {
            // if the king can't move and is in the check position then still we got another chance
            // another piece can block the check

            // check the distance between the king and checking piece
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);

            if (colDiff == 0) {
                // attack vertically
                if (checkingP.row < king.row) {
                    // the attacking piece is above the king
                    for (int row = checkingP.row; row < king.row; row++) {
                        for (ChessPieces P : simpieces) {
                            if (P != king && P.color != currentColor && P.canMove(checkingP.col, row))
                                return false;
                        }
                    }
                }
                if (checkingP.row > king.row) {
                    // the attacking piece is below the king

                    for (int row = checkingP.row; row > king.row; row--) {
                        for (ChessPieces P : simpieces) {
                            if (P != king && P.color != currentColor && P.canMove(checkingP.col, row))
                                return false;
                        }
                    }
                }

            } else if (rowDiff == 0) {
                //attack horizontally

                if (checkingP.col < king.col) {
                    // attacking from left side
                    for (int col = checkingP.col; col < king.col; col++) {
                        for (ChessPieces P : simpieces) {
                            if (P != king && P.color != currentColor && P.canMove(col, checkingP.row))
                                return false;
                        }
                    }
                }

                if (checkingP.col > king.col) {
                    // attacking from the right
                    for (int col = checkingP.col; col > king.col; col--) {
                        for (ChessPieces P : simpieces) {
                            if (P != king && P.color != currentColor && P.canMove(col, checkingP.row))
                                return false;
                        }
                    }
                }

            } else if (colDiff == rowDiff) {
                // attack diagonally

                // from top
                if (checkingP.row < king.row) {

                    // top left
                    if (checkingP.col < king.col) {
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++) {
                            for (ChessPieces P : simpieces) {
                                if (P != king && P.color != currentColor && P.canMove(col, row))
                                    return false;
                            }
                        }
                    }
                    // top right
                    if (checkingP.col > king.col) {
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
                            for (ChessPieces P : simpieces) {
                                if (P != king && P.color != currentColor && P.canMove(col, row))
                                    return false;
                            }
                        }
                    }
                }

                // lower
                if (checkingP.row > king.row) {
                    // lower left
                    if (checkingP.col < king.col) {
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--) {
                            for (ChessPieces P : simpieces) {
                                if (P != king && P.color != checkingP.color && P.canMove(col, row)) {

                                    return false;
                                }

                            }
                        }
                    }
                    // lower right
                    if (checkingP.col > king.col) {
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--) {
                            for (ChessPieces P : simpieces) {
                                if (P != king && P.color != currentColor && P.canMove(col, row))
                                    return false;
                            }
                        }
                    }
                }

            }
        }


        return true;
    }

    public boolean kingCanMove(ChessPieces king) {

        // find any possible move of the king, if so return true else false
        if (isValidMove(king, -1, -1))
            return true;
        if (isValidMove(king, 0, -1))
            return true;
        if (isValidMove(king, -1, 0))
            return true;
        if (isValidMove(king, 1, 1))
            return true;
        if (isValidMove(king, -1, 1))
            return true;
        if (isValidMove(king, 1, -1))
            return true;
        if (isValidMove(king, 1, 0))
            return true;
        if (isValidMove(king, 0, 1))
            return true;


        return false;
    }

    public boolean isValidMove(ChessPieces king, int colPlus, int rowPlus) {

        boolean isValidMove = false;

        // making possible moves of the king
        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {    // hypothetically checking if the king can move there
            if (king.hittingPiece != null) {        // if the king is hitting a piece remove it
                simpieces.remove(king.hittingPiece.getIndex());
            }
            if (!illegalMove(king)) {        // if the piece is not illegal then set validMove as true
                isValidMove = true;
            }

        }
        // reset the removed piece
        king.resetPosition();
        copyArrayList(simpieces, pieces);

        return isValidMove;
    }


    // check castling
    public void checkCastling() {
        if (castlingPiece != null) {
            if (castlingPiece.col == 0)
                castlingPiece.col += 3;
            else if (castlingPiece.col == 7)
                castlingPiece.col -= 2;
            castlingPiece.x = castlingPiece.getX(castlingPiece.col);

        }
    }

    // Check kings illegal move
    public boolean illegalMove(ChessPieces piece) {
        // check if the piece received is king
        if (piece.type == Type.KING)
            for (ChessPieces P : simpieces) {    // scan the simpieces and check if the piece is not king it self and is opponent and can move to king's place
                if (P != piece && P.color != piece.color && P.canMove(piece.col, piece.row))
                    return true;
            }
        return false;
    }

    // method to change the turn after every move
    public void changePlayer() {

        if(MODE == PVsP || (MODE == PVsC && currentColor != compColor)){
        if (activePiece != null) {
            movesCol = getFile();
            movesRow = activePiece.row + 1;
            initials = getIntials(activePiece);
			if(movesCol != null) {
				String movesPlayed = initials +  movesCol + movesRow;
				movePlayed.add(movesPlayed);

			}
        }
        }else if(MODE == PVsC && currentColor == compColor && compPiece != null){
             movesCol = getFile();
             movesRow = compPiece.row + 1;
             initials = getIntials(compPiece);

            if(movesCol != null) {
                 String movesPlayed = initials +  movesCol + movesRow;
                 movePlayed.add(movesPlayed);
             }
         }

        // the turn is changed by switching the color
        if (currentColor == WHITE) {
            currentColor = BLACK;
            for (ChessPieces P : PanelGame.simpieces) {
                if (P.color == BLACK)
                    P.twostepped = false;
            }
        } else {
            currentColor = WHITE;

            // setting the twostepped to false when the turn changes

            // En passant is only valid for the move immediately after the opponent pawn makes a twoswtep move
            // so after the turn changes the twostepped boolean value is changed to false so that it can no longer
            // be captured

            for (ChessPieces P : PanelGame.simpieces) {
                if (P.color == WHITE)
                    P.twostepped = false;
            }
        }

        activePiece = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the board
        board.draw(g2);

        // Draw the pieces
        List<ChessPieces> copy = new ArrayList<>(simpieces);
        for (ChessPieces piece : copy) {
            piece.draw(g2);
        }

        // Draw possible move squares and handle active piece
        if (activePiece != null) {
            int squareX = activePiece.col * Board.SQUARE_SIZE;
            int squareY = activePiece.row * Board.SQUARE_SIZE;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

            if (canMove) {
                if (illegalMove(activePiece) || opponentCanCaptureKing()) {
                    g2.setColor(Color.GRAY);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillRect(squareX, squareY, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                activePiece.draw(g2);
            } else if (!activePiece.ownSquare(activePiece.col, activePiece.row) && activePiece.isValidSquare(activePiece.col, activePiece.row)) {
                g2.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red
                g2.fillRect(squareX, squareY, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        // Draw text indicating whose turn it is
        g2.setFont(new Font("Book Antique", Font.PLAIN, 30));
        g2.setColor(Color.WHITE);
        String turnText = (currentColor == WHITE) ? "White's Turn" : "Black's Turn";
        g2.drawString(turnText, 750, 50);

        // Draw check message if applicable
        if (checkingP != null) {
            g2.setColor(Color.RED);
            g2.drawString("King is in Check", 750, 160);
        }

        // Draw moves played
        drawMovesPlayed(g2);

        // Draw promotion choices
        if (promotion) {
            g2.drawString("Promote To:", 750, 150);
            for (ChessPieces piece : promoPiece) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        }

        // Draw game over messages
        if (gameOver || compLose) {
            g2.setColor(Color.GREEN);
            String gameOverMsg;
            if (MODE == PVsC) {
                gameOverMsg = (currentColor == compColor) ? "You Lose.. !" : "You Win..!";
            } else {
                gameOverMsg = (currentColor == WHITE) ? "White Wins" : "Black Wins";
            }
            g2.drawString(gameOverMsg, 750, 190);
        }

        // Draw stalemate message
        if (stalemate) {
            g2.setColor(Color.GREEN);
            g2.drawString("It's a Draw! Stalemate", 700, 200);
        }
    }


    // method to get which column the piece moved
    private String getFile() {

        if (activePiece != null || compPiece != null) {
            int column = (MODE == PVsP || currentColor != compColor)? activePiece.col: (MODE == PVsC && currentColor == compColor)? compPiece.col:-1;
            char file = switch (column) {
                case 0 -> 'a';
                case 1 -> 'b';
                case 2 -> 'c';
                case 3 -> 'd';
                case 4 -> 'e';
                case 5 -> 'f';
                case 6 -> 'g';
                case 7 -> 'h';
                default -> 'x';
            };
            String file1 = "" + file;
            return file1;
        }

        return "";
    }

	private void drawMovesPlayed(Graphics2D g2){
//    System.out.println("DRAW PANEL");
//        MovesPanel.textCount = this.textCount;
//        textCount++;
        MP.repaint();

        MP.setPreferredSize(new Dimension(250, MP.getPreferredSize().height));
        JScrollBar bar = scrollBar.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
        scrollBar.validate();
	}

    private String getIntials(ChessPieces P){

        switch(P.type){
            case Type.KING: return "K: ";
            case Type.PAWN: return "P: ";
            case Type.ROOK: return "R: ";
            case Type.QUEEN: return "Q: ";
            case Type.KNIGHT: return "Kn: ";
            case Type.BISHOP: return "B: ";
        }
        return "";
    }

}


