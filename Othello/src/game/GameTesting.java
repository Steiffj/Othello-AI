package game;

import components.Board;
import components.Color;
import components.Coordinate;
import components.OthelloBoard;
import components.Player;

public class GameTesting {
	
	private Board gameBoard;
	private Player p1;
	private Player p2;

	public GameTesting() {
		// TODO Auto-generated constructor stub
	}
	
	private void swapPlayerOrder() {
		Player temp = p1;
		p1 = p2;
		p2 = temp;
		p1.swapColor();
		p2.swapColor();
	}
	
	public void playTournament(int numRounds) {
		int currentRound = 0;
		while (currentRound++ < numRounds) {
			playGame();
			if (currentRound < numRounds) {
				swapPlayerOrder();
			}
		}
	}
	
	/**
	 * Plays one full game of Othello. 
	 * Set up the different Players here!
	 */
	public Player playGame() {
		gameBoard = new OthelloBoard();
		
		// Each iteration through the while-loop is one full turn
		while(!gameBoard.isGameOver()) {
			
			turn(p1, p2);
		}
		
		System.out.println("\n\n====== Game Over! ======\n\n" + gameBoard.toString());
		
		if (gameBoard.winner() == Color.EMPTY) {
			System.out.println("It's a draw!");
		} else {
			System.out.println("The Winner is " + 
					(gameBoard.winner() == p1.getColor() ? 
							p1.getName() + ", playing as " + p1.getColor() : 
								p2.getName() + ", playing as " + p2.getColor()));
		}
		System.out.println("\nFinal Score");
		System.out.println("  " + Color.B + " total: " + gameBoard.countPieces(Color.B));
		System.out.println("  " + Color.W + " total: " + gameBoard.countPieces(Color.W));
		
		return p1.getColor() == gameBoard.winner() ? p1 : p2;
	}
	
	/**
	 * 
	 * One turn in a game of Othello (one ply for each Player)
	 * 
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @param b The game Board
	 */
	private void turn(Player p1, Player p2) {
		ply(p1, gameBoard);
		ply(p2, gameBoard);
	}
	
	/**
	 * 
	 * A ply for a single Player in a game of Othello. The Player will either place a piece on the Board, or pass it there are no valid moves available.
	 * 
	 * @param p the Player whose ply it is
	 * @param b the game Board
	 */
	private void ply(Player p, Board b) {
		
		if (b.countValidMoves(p.getColor()) > 0) {
			// Call the current Player's makeMove() method, and attempt to update the game board with the results
			Coordinate playerMove;
			while (!b.set(p.getColor(), (playerMove = p.makeMove(b)))) {
				System.err.println("\n" + p.getName() + " attempted invalid move" + (playerMove == null ? "!" : (": " + playerMove.toString())) + "\nPress ENTER to continue");
			}
		} else {
			return;
		}
	}
}
