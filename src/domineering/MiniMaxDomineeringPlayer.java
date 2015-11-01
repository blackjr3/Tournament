/**
 * 
 */
package domineering;

import game.GameMove;
import game.GamePlayer;
import game.GameState;
import game.Util;

/**
 * @author Jonathan Black
 *
 */
public class MiniMaxDomineeringPlayer extends BaseDomineeringPlayer {
	public final int MAX_DEPTH = 56;
	public int depthLimit;
	public ScoredDomineeringMove[] mvStack;
	
	protected class ScoredDomineeringMove extends DomineeringMove{
		double score;
		public ScoredDomineeringMove(int r1,int c1,int r2,int c2, double s){
			super(r1,c1,r2,c2);
			this.score = s;
		}
		public void set(int r1,int c1,int r2,int c2, double s){
			super.row1 = r1;
			super.row2 = r2;
			super.col1 = c1;
			super.col2 = c2;
			this.score = s;
		}
		public void set(DomineeringMove dMove){
			super.row1 = dMove.row1;
			super.row2 = dMove.row2;
			super.col1 = dMove.col1;
			super.col2 = dMove.col2;
			this.score = 0;
		}
		public void set(DomineeringMove dMove, double s){
			super.row1 = dMove.row1;
			super.row2 = dMove.row2;
			super.col1 = dMove.col1;
			super.col2 = dMove.col2;
			this.score = s;
		}
	}
	/**
	 * Constructor 
	 * @param nname name of the player(I think?)
	 * @param d the depth limit.
	 */
	public MiniMaxDomineeringPlayer(String nname, int d) {
		super(nname);
		this.depthLimit = d;
	}
	/**
	 * Constructor. Sets depthLimit to MAX_DEPTH.
	 * @param nname name of the player(I think?)
	 */
	public MiniMaxDomineeringPlayer(String nname){
		super(nname);
		this.depthLimit = this.MAX_DEPTH;
	}
	/**
	 * Shuffles the moves for random move ordering.
	 */
	protected static void shuffle(DomineeringMove[] ary){
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			int spot = Util.randInt(i, len - 1);
			DomineeringMove tmp = (DomineeringMove) ary[i].clone();
			ary[i] = (DomineeringMove) ary[spot].clone();
			ary[spot] = (DomineeringMove) tmp.clone();
		}
	}
	/**
	 * Initializes the stack of Moves.
	 */
	public void init(){
		mvStack = new ScoredDomineeringMove[MAX_DEPTH];
		for(int i = 0; i < MAX_DEPTH;i++){
			mvStack[i] = new ScoredDomineeringMove(0,0,0,0,0);
		}
		//System.out.println("mvStack has been initizaed with size: " + mvStack.length);
	}
	
	/**
	 * 
	 * @param brd current state of the board
	 * @param who which play'er moves to get
	 * @return
	 */
	public DomineeringMove[] getPossibleMoves(DomineeringState brd,char who){
		//TODO: Check logic for checking if a move is valid.
		int numPossibleMoves = 0;
		DomineeringMove[] tempMoves = new DomineeringMove[56];
		if(who == 'H'){
			for(int c = 0; c < brd.COLS;c++){
				for(int r = 0; r < brd.ROWS;r++){
					DomineeringMove tempMove = new DomineeringMove(r,c,r,c+1);
					//System.out.println("Home move being considered: "+ tempMove.toString());
					if(brd.moveOK(tempMove)){
						//System.out.println("Move okay!");
						tempMoves[numPossibleMoves] = tempMove;
						numPossibleMoves++;
					}
				}
			}
		}else{
			for(int r = 0; r < brd.ROWS;r++){
				for(int c = 0; c < brd.COLS;c++){
					DomineeringMove tempMove = new DomineeringMove(r,c,r+1,c);
					if(brd.moveOK(tempMove)){
						tempMoves[numPossibleMoves] = tempMove;
						numPossibleMoves++;
					}
				}
			}
		}
		DomineeringMove[] moves = new DomineeringMove[numPossibleMoves];
		System.arraycopy(tempMoves, 0, moves, 0, numPossibleMoves);
		return moves;
	}
	/**
	 * Determines if a board represents a completed game. If it is, the
	 * Evaluation values for these boards is recorded (i.e., 0 for a draw +X,
	 * For a HOME win and -X for an AWAY win.
	 * 
	 * @param brd domineering board to be examined
	 * @param mv where to place the score information; column is irrelevant
	 * @return true if the board is a terminal state
	 */
	protected boolean terminalValue(GameState brd, ScoredDomineeringMove mv) {
		GameState.Status status = brd.getStatus();
		boolean isTerminal = true;

		if (status == GameState.Status.HOME_WIN) {
			mv.set(0, 0, 0, 0, MAX_SCORE);
		} else if (status == GameState.Status.AWAY_WIN) {
			mv.set(0, 0, 0, 0, -MAX_SCORE);
		} else if (status == GameState.Status.DRAW) {
			mv.set(0, 0, 0, 0, 0);
		} else {
			isTerminal = false;
		}
		return isTerminal;
	}
	/**
	 * Performs the a depth limited minimax algorithm. It leaves it's move
	 * Recommendation at mvStack[currDepth].
	 * @param brd current board state
	 * @param currDepth current depth in the search
	 */
	private void minimax(DomineeringState brd, int currentDepth) {
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean isTerminal = terminalValue(brd, mvStack[currentDepth]);

		if (isTerminal) {
			;
		} else if (currentDepth == depthLimit) {
			//TODO: rewrite this part statement the home is wrong 
			mvStack[currentDepth].set(0,0,0,0, super.evalBoard1(brd));
		} else{
			//Connect4 uses a temp move but we have a data 
			//structure of domineeringmoves 
			//so I don't think this is needed
			//ScoredDomineeringMove tempMv = new ScoredDomineeringMove(0,0,0,0,0);
			
			double bestScore = (brd.getWho() == GameState.Who.HOME ? Double.NEGATIVE_INFINITY
					: Double.POSITIVE_INFINITY);
			
			ScoredDomineeringMove bestMove = mvStack[currentDepth];
			ScoredDomineeringMove nextMove = mvStack[currentDepth + 1];
			
			bestMove.set(0,0,0,0, bestScore);
			GameState.Who currTurn = brd.getWho();
			
			//Store the moves as DomineeringMove[numMoves]
			DomineeringMove[] possibleMoves;
			if(toMaximize){//home
				possibleMoves = this.getPossibleMoves(brd, 'H');
			}else{//away
				possibleMoves = this.getPossibleMoves(brd, 'A');
			}
			int numPossibleMoves = possibleMoves.length;
			shuffle(possibleMoves); //Random move ordering
			
			//A loop that goes through the possible moves
			for(int i = 0; i < numPossibleMoves;i++){
				//If we have the possible moves in a data structure
				//Then we can just go through them if not we will need
				//To make sure they are a vaild move
				if(brd.moveOK(possibleMoves[i])){
					//make the move.
					brd.makeMove(possibleMoves[i]);

					//Recursively call the minimax function
					this.minimax(brd, currentDepth+1);
					
					brd.board[possibleMoves[i].row1][possibleMoves[i].col1] = DomineeringState.emptySym;
					brd.board[possibleMoves[i].row2][possibleMoves[i].col2] = DomineeringState.emptySym;
					brd.numMoves--;
					brd.status = GameState.Status.GAME_ON;
					brd.who = currTurn;
					
					//See if this move path is better then the last
					if (toMaximize && nextMove.score > bestMove.score) {
						bestMove.set((DomineeringMove)possibleMoves[i].clone(), nextMove.score);
					} else if (!toMaximize && nextMove.score < bestMove.score) {
						bestMove.set((DomineeringMove)possibleMoves[i].clone(), nextMove.score);
					}
				}
			}
		}
	}

	/**
	 * Returns the best best move based off the depth limited minimax algorithm.
	 */
	public GameMove getMove(GameState brd, String lastMv) {
		//long startTime = System.nanoTime();
		minimax((DomineeringState)brd,0);
		//System.out.println("The time it took to calculate move " +brd.numMoves+ ": "+(System.nanoTime()-startTime));
		return mvStack[0];
	}
	
	public static void main(String[] args) {
		int depth = 4;
		int evalF = 1;
		GamePlayer p = new MiniMaxDomineeringPlayer("MM Domineering F1 " + depth, depth);
		p.compete(args);
	}

}
