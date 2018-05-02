package game;

import java.util.HashMap;
import java.util.Scanner;

import components.Board;
import components.Color;
import components.Coordinate;
import components.OthelloBoard;
import components.Player;
import heuristics.MaxPiecesHeuristic;
import heuristics.MinMobilityHeuristic;
import heuristics.ParityHeuristic;
import heuristics.PieceTableHeuristic;
import heuristics.StabilityHeuristic;
import players.Human;
import players.HybridAI;
import players.ShallowMindAI;

public class App {
	public static HashMap<String, Player> avatars = new HashMap<String, Player>();
	public static Player p1;
	public static Player p2;
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		initPlayerList();
		choosePlayers();
		String mode = "";
		while (!mode.matches("[Rr][Ee]?[Gg]?[Uu]?[Ll]?[Aa]?[Rr]?|[Pp][Rr]?[Oo]?[Bb]?[Ll]?[Ee]?[Mm]?")) {
			System.out.print("\nChoose a game mode (Regular/Problem setup): ");
			mode = sc.nextLine();
		}
		
		GameInteractive game = new GameInteractive(p1, p2);
		if (mode.matches("[Rr][Ee]?[Gg]?[Uu]?[Ll]?[Aa]?[Rr]?")) {
			game.playTournament(3);
		} else if (mode.matches("[Pp][Rr]?[Oo]?[Bb]?[Ll]?[Ee]?[Mm]?")) {
			Board builtBoard = buildBoard();
			setTurnOrder();
			game.playGame(builtBoard);
		}
	}
	
	public static void initPlayerList() {
		avatars.put("p", new HybridAI("Pieces", Color.B, false, new MaxPiecesHeuristic(1)));
		avatars.put("m", new HybridAI("Mobility", Color.B, false, new MinMobilityHeuristic(1)));
		avatars.put("h1", new HybridAI("Hybrid", Color.B, false, new MaxPiecesHeuristic(75), new MinMobilityHeuristic(25)));
		avatars.put("h2", new HybridAI("Hybrid Curves", Color.B, false, new MaxPiecesHeuristic(40), new MinMobilityHeuristic(65), new PieceTableHeuristic(100)));
		
		avatars.put("shallow", new ShallowMindAI("Shallow Mind", Color.B, new PieceTableHeuristic(100), 
																		new MaxPiecesHeuristic(30), 
																		new MinMobilityHeuristic(50), 
																		new StabilityHeuristic(60), 
																		new ParityHeuristic(75)));
		
		avatars.put("shallow simple", new ShallowMindAI("Shallow Mind Simplified", Color.B, 
																							new MaxPiecesHeuristic(40)
																							));
		avatars.put("human", new Human("Opponent", Color.B));
	}
	
	public static void choosePlayers() {
		System.out.println("Player Options:");
		for (String name : avatars.keySet()) {
			System.out.print(name + "  ");
		}
		System.out.print("\n\n");
		
		System.out.print("Which avatar do you choose as player 1? ");
		String player1 = sc.nextLine();
		while(!avatars.containsKey(player1)) {
			System.out.print("Which avatar do you choose as player 1? ");
			player1 = sc.nextLine();
		}
		System.out.print("Which avatar do you choose as player 2? ");
		String player2 = sc.nextLine();
		while(!avatars.containsKey(player2)) {
			System.out.print("Which avatar do you choose as player 2? ");
			player2 = sc.nextLine();
		}
		
		p1 = avatars.get(player1);
		p2 = avatars.get(player2);
		p1.setColor(Color.B);
		p2.setColor(Color.W);
	}
	
	public static Board buildBoard() {
		Board board = new OthelloBoard();
		String command = "";
		System.out.println("\n" + board.toString());
		while(!command.matches("[Dd][Oo][Nn][Ee][.]*")) {
			System.out.print("\nEnter piece to place: ");
			command = sc.nextLine().replaceAll("\\s+|,|(|)", "").toUpperCase();
			
			if (command.matches("[a-zA-Z][1-" + board.getWidth() + "][" + Color.B + Color.W + "]")) {
				String[] parts = command.replaceAll("\\s+|,|(|)", "").toUpperCase().split("");
				String[] colLabels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(0, board.getWidth()).split("");
				int col = -1;
				for (int c = 0; c < colLabels.length; c++) {
					if (parts[0].equals(colLabels[c])) {
						col = c;
					}
				}
				int row = Integer.parseInt(parts[1]) - 1;
				Color c = Color.valueOf(parts[2]);
				
				if (board.place(c, new Coordinate(row, col))) {
					System.out.println("Result\n" + board.toString());
				} else {
					System.out.println("Invalid position or color.");
				}
			} else {
				System.out.println("Improperly-formatted command.");
			}
		}
		return board;
	}
	
	public static void setTurnOrder() {
		String order = "";
		while (!order.toUpperCase().matches("[T][" + Color.B + Color.W + "]")) {
			System.out.print("Choose turn order (\"TB\" or \"TW\"): ");
			order = sc.nextLine();
		}
		
		if(order.matches("TB")) {
			p1.setColor(Color.B);
			p2.setColor(Color.W);
		} else if (order.matches("TW")) {
			p1.setColor(Color.W);
			p2.setColor(Color.B);
		}
	}
}
