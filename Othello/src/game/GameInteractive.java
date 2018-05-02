package game;

import java.util.HashMap;
import java.util.Scanner;

import components.Board;
import components.Color;
import components.Coordinate;
import components.OthelloBoard;
import components.Player;
import players.Human;

public class GameInteractive {
	private HashMap<Integer, GameState> moveHistory;
	private Board gameBoard;
	private Player p1;
	private Player p2;
	private Scanner sc;
	
	private int currentPly;
	
	public GameInteractive(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
		gameBoard = new OthelloBoard();
		moveHistory = new HashMap<Integer, GameState>();
		sc = new Scanner(System.in);
	}
	
	private void swapPlayerOrder() {
		Player temp = p1;
		p1 = p2;
		p2 = temp;
		p1.swapColor();
		p2.swapColor();
	}
	
	private int currentTurn() {
		return currentPly / 2 + 1;
	}
	
	public void setBoard(Board board) {
		gameBoard = board;
	}
	
	/**
	 * Plays a tournament of Othello.
	 * 
	 * @param numRounds the number of rounds to play in a tournament
	 */
	public void playTournament(int numRounds) {
		int currentRound = 0;
		while (currentRound++ < numRounds) {
			gameBoard = new OthelloBoard();
			System.out.println("\n\n====== Game " + currentRound + " of " + numRounds + " ======\n");
			playGame();
			
			if (currentRound < numRounds) {
				System.out.print("\nSwitch who goes first for the next game? (y/n): ");
				if (sc.nextLine().matches("[Yy][Ee]?[Ss]?")) {
					swapPlayerOrder();
				}
			}
		}
	}
	
	public Player playGame() {
		return playGame(gameBoard);
	}
	
	/**
	 * Plays one full game of Othello. 
	 * Set up the different Players here!
	 */
	public Player playGame(Board board) {
		gameBoard = board;
		currentPly = 0;
		moveHistory.put(currentPly, new GameState(currentPly, p1.getColor(), gameBoard));
		
		// Each iteration through the while-loop is one full turn
		while(!gameBoard.isGameOver()) {
			System.out.println("\n\n---== Turn " + currentTurn() + " ==---");
			System.out.println("  " + Color.B + " total: " + gameBoard.countPieces(Color.B));
			System.out.println("  " + Color.W + " total: " + gameBoard.countPieces(Color.W));
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
//		while(undo()) {
//			ply(p1, gameBoard);
//		}
		
		System.out.print("\n");
		
		ply(p2, gameBoard);
//		while(undo()) {
//			ply(p2, gameBoard);
//		}
	}
	
	/**
	 * 
	 * A ply for a single Player in a game of Othello. The Player will either place a piece on the Board, or pass it there are no valid moves available.
	 * 
	 * @param p the Player whose ply it is
	 * @param b the game Board
	 */
	private void ply(Player p, Board b) {
		
		System.out.println("\n" + b.toString(p.getColor()));
		System.out.println("- Playing on board above; result shown below -");
		
		if (b.countValidMoves(p.getColor()) > 0) {
			// Print list of valid moves
			System.out.print("Valid moves: ");
			int count = 0;
			for (Coordinate coord : b.getValidMoves(p.getColor())) {
				System.out.print(coord.toString() + "  ");
				if (++count % 6 == 0) {
					System.out.print("\n             ");
				}
			}
			
			System.out.print("\n");
			System.out.print(p.getName() + "\'s ply (" + p.getColor() + "): ");
			
			// Call the current Player's makeMove() method, and attempt to update the game board with the results
			Coordinate playerMove;
			while (!b.set(p.getColor(), (playerMove = p.makeMove(b)))) {
				System.out.println("\n" + p.getName() + " attempted invalid move" + (playerMove == null ? "!" : (": " + playerMove.toString())) + "\nPress ENTER to continue");
				sc.nextLine();
				System.out.print(p.getName() + "\'s ply (" + p.getColor() + "): ");
			}
			if (!(p instanceof Human)) {
				System.out.println(playerMove.toString());
			}
			
			System.out.println("\n  " + Color.B + " total: " + b.countPieces(Color.B));
			System.out.println("  " + Color.W + " total: " + b.countPieces(Color.W));
			currentPly++;
			moveHistory.put(currentPly, new GameState(currentPly, p2.getColor(), gameBoard));
		} else {
			System.out.print(p.getName() + "\'s ply (" + p.getColor() + "): ");
			System.out.println("\n" + p.getName() + " passes (no moves available).");
			
			System.out.println("\n  " + Color.B + " total: " + b.countPieces(Color.B));
			System.out.println("  " + Color.W + " total: " + b.countPieces(Color.W));
			currentPly++;
			moveHistory.put(currentPly, new GameState(currentPly, p2.getColor(), gameBoard));
			return;
		}
	}
	
	private boolean undo() {
		return undo(1);
	}
	
	private boolean undo(int numPlies) {
		System.out.print("\nPress any key to confirm ply, or type \"undo\" to undo ply: ");
		String confirm = sc.nextLine();
		if (confirm.matches("[Uu][Nn][Dd][Oo]")) {
			GameState backup = moveHistory.get(currentPly - numPlies);
			gameBoard = backup.getBoard();
			currentPly = backup.getPlyNum();
			return true;
		} else {
			return false;
		}
	}
}
