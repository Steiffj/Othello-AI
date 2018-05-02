package heuristics;

import java.util.Arrays;
import java.util.List;

import components.Board;
import components.Color;
import components.Coordinate;

public class PieceTableHeuristic extends Heuristic {

	private final long[][] PIECE_TABLE = new long[8][8];
	private final List<Coordinate> upperLeftBuffer = Arrays.asList(new Coordinate(0,1), new Coordinate(1,0), new Coordinate(1,1));
	private final List<Coordinate> upperRightBuffer = Arrays.asList(new Coordinate(0,6), new Coordinate(1,7), new Coordinate(1,6));
	private final List<Coordinate> lowerLeftBuffer = Arrays.asList(new Coordinate(6,0), new Coordinate(7,1), new Coordinate(6,1));
	private final List<Coordinate> lowerRightBuffer = Arrays.asList(new Coordinate(6,7), new Coordinate(7,6), new Coordinate(6,6));
	
	public PieceTableHeuristic() {
		super();
		buildPieceTable();
		name = "Piece Table";
		equalizer = 1;
	}
	
	public PieceTableHeuristic(int weight) {
		super(weight);
		buildPieceTable();
		name = "Piece Table";
		equalizer = 1;
	}
	
	@Override
	public long gradeBoard(Color piece, Board board) {
		long score = 0L;
		Color[][] boardInnards = board.getContents();
		Coordinate coord;
		for (int row = 0; row < boardInnards.length; row++) {
			for (int col = 0; col < boardInnards[row].length; col++) {
				if (boardInnards[row][col] == piece) {
					coord = new Coordinate(row, col);
					if (nearestCornerOccupied(coord, board) == piece) {
						/*
						 *  The square currently being checked is a buffer square, and the player has already taken the corner it buffers
						 *  (This means taking those buffers is no longer bad/dangerous)
						 */
						score -= PIECE_TABLE[row][col];
					} else {
						score += PIECE_TABLE[row][col];	
					}
				}
			}
		}
		/*
		 * Don't use equalizer here; all other heuristics are being equalized to the piece table's order of magnitude.
		 * It won't matter anyway, since the piece table's equalizer is set to 1.
		 * (The values were originally doubles 100 times smaller than their current values, but were converted to longs to avoid the lack of precision doubles introduce.)
		 */
		return score * weight;	
	}
	
	@Override
	public long gradeBoardRaw(Color piece, Board board) {
		return gradeBoard(piece, board) / weight;	
	}
	
	private Color nearestCornerOccupied(Coordinate coord, Board board) {
		/*
		 * TODO This is a terrible way to check for this... maybe revisit in the future 
		 */
		Color[][] boardInnards = board.getContents();
		
		// Upper-left corner
		if (upperLeftBuffer.contains(coord)) {
			return boardInnards[0][0];
		} else if (upperRightBuffer.contains(coord)) {
			return boardInnards[0][7];
		} else if (lowerLeftBuffer.contains(coord)) {
			return boardInnards[7][0];
		} else if (lowerRightBuffer.contains(coord)) {
			return boardInnards[7][7];
		} else {
			return Color.EMPTY;
		}
	}
	
	/**
	 * Initializes the piece table with the tuned values discussed in:
	 * <a href="https://web.stanford.edu/class/cs221/2017/restricted/p-final/man4/final.pdf">Programming an Othello AI</a>
	 */
	private void buildPieceTable() {
		
		/*
		 * Take advantage of the assignment operator's right-associativity :) 
		 * There is a mathy way to do this, but, we decided to assign the values manually, since they aren't getting updated in this program
		 */
		
		// Weight corners
		PIECE_TABLE[0][0] =
		PIECE_TABLE[0][7] = 
		PIECE_TABLE[7][0] = 
		PIECE_TABLE[7][7] = 1616L;
		
		// Weight corner buffers 1/3
		PIECE_TABLE[0][1] = 
		PIECE_TABLE[0][6] = 
		PIECE_TABLE[7][1] = 
		PIECE_TABLE[7][6] = -303L;
		
		// Weight corner buffers 2/3
		PIECE_TABLE[1][0] =
		PIECE_TABLE[1][7] =
		PIECE_TABLE[6][0] = 
		PIECE_TABLE[6][7] = -412L; 
		
		// Weight corner buffers 3/3
		PIECE_TABLE[1][1] = 
		PIECE_TABLE[1][6] = 
		PIECE_TABLE[6][1] = 
		PIECE_TABLE[6][6] = -181L;
		
		// Weight edges 1/4
		PIECE_TABLE[0][2] = 
		PIECE_TABLE[0][5] = 
		PIECE_TABLE[7][2] = 
		PIECE_TABLE[7][5] = 99L;
		
		// Weight edges 2/4
		PIECE_TABLE[0][3] = 
		PIECE_TABLE[0][4] = 
		PIECE_TABLE[7][3] = 
		PIECE_TABLE[7][4] = 43L; 
		
		// Weight edges 3/4
		PIECE_TABLE[2][0] = 
		PIECE_TABLE[2][7] = 
		PIECE_TABLE[5][0] = 
		PIECE_TABLE[5][7] = 133L;
		
		// Weight edges 4/4
		PIECE_TABLE[3][0] = 
		PIECE_TABLE[3][7] = 
		PIECE_TABLE[4][0] = 
		PIECE_TABLE[4][7] = 63L;
		
		// Weight center 1/8
		PIECE_TABLE[2][1] = 
		PIECE_TABLE[2][6] = 
		PIECE_TABLE[5][1] = 
		PIECE_TABLE[5][6] = -4L;
		
		// Weight center 2/8
		PIECE_TABLE[3][1] = 
		PIECE_TABLE[3][6] = 
		PIECE_TABLE[4][1] = 
		PIECE_TABLE[4][6] = -18L;
		
		// Weight center 3/8
		PIECE_TABLE[1][2] = 
		PIECE_TABLE[1][5] = 
		PIECE_TABLE[6][2] = 
		PIECE_TABLE[6][5] = -8L;
		
		// Weight center 4/8
		PIECE_TABLE[2][2] = 
		PIECE_TABLE[2][5] = 
		PIECE_TABLE[5][2] = 
		PIECE_TABLE[5][5] = 51L; 
		
		// Weight center 5/8
		PIECE_TABLE[3][2] = 
		PIECE_TABLE[3][5] = 
		PIECE_TABLE[4][2] = 
		PIECE_TABLE[4][5] = -4L;
		
		// Weight center 6/8
		PIECE_TABLE[1][3] = 
		PIECE_TABLE[1][4] = 
		PIECE_TABLE[6][3] = 
		PIECE_TABLE[6][4] = -27L; 
		
		// Weight center 7/8
		PIECE_TABLE[2][3] = 
		PIECE_TABLE[2][5] = 
		PIECE_TABLE[5][3] = 
		PIECE_TABLE[5][5] = 7L;
		
		// Weight center 8/8
		PIECE_TABLE[3][3] = 
		PIECE_TABLE[3][4] = 
		PIECE_TABLE[4][3] = 
		PIECE_TABLE[4][4] = -1L;
	}
}
