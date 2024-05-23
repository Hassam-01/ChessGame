Chess Mate AI

Welcome to Chess Mate AI! This Java-based chess game features both player-versus-player and player-versus-computer modes, complete with a graphical user interface, piece movement handling, and AI-driven computer     opponent logic. This README will guide you through the project's structure, setup, and components.

Running the Game

    To run the game, execute the Main class located in src/main/Main.java. This class initializes the game and sets up the necessary components for gameplay.  
    Detailed Component Overview

1. PanelGame
   
The PanelGame class contains the core logic of the chess game:

    Manages the game state (e.g., whose turn it is).
    Handles piece movements and implements chess rules (e.g., check, checkmate, stalemate).
    Renders the game board and pieces on the screen.

3. Chesspieces Package

This package includes individual classes for each type of chess piece (Bishop, King, Queen, Knight, Rook, Pawn). Each class:

    Defines valid moves based on chess rules.
    Loads and displays piece images.
    Tracks piece positions on the board.

3. Computer

The Computer class is responsible for the AI logic:

    Evaluates the board state to make decisions.
    Generates potential moves for the computer player.
    Uses the bestmoves package to determine the optimal move based on heuristics and evaluations.

4. Bestmoves Package

The bestmoves package, specifically the bestmoves class:

    Stores potential moves with evaluations.
    Compares all possible moves to determine the best one.
    Holds information about the move, involved piece, target location, and move rating.

5. ChessswingOpening and ColorSelect

These classes handle the user interface aspects:

    ChessswingOpening: Manages the initial login or splash screen.
    ColorSelect: Allows the user to choose their preferred color before starting the game.

Additional Classes

    Board: Manages the overall state and layout of the chessboard.
    Type: Defines the types of pieces and their properties.
    Mouse: Handles user input and interactions with the game board.

Resources

    The res package contains image assets for the game, ensuring each chess piece is visually represented on the board, contributing to a user-friendly and engaging experience.

Contributors

    AzkaAhmad754
    Muhammad-Shah-zaib
