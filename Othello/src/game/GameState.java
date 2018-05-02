package game;

import components.Board;
import components.Color;

public class GameState {
	private int plyNum;
	private Color plyColor;
	private Board board;
	
	public GameState(int plyNum, Color plyColor, Board board) {
		this.plyNum = plyNum;
		this.plyColor = plyColor;
		this.board = board.clone();
	}

	public int getPlyNum() {
		return plyNum;
	}

	public Color getPlyColor() {
		return plyColor;
	}

	public Board getBoard() {
		return board.clone();
	}
}
