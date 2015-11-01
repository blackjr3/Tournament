/**
 * @author Jonathan Black
 */
package domineering;

import connect4.Connect4State;
import domineering.DomineeringState;
import game.GameMove;
import game.GamePlayer;
import game.GameState;
import game.Util;


public abstract class BaseDomineeringPlayer extends GamePlayer {
	
	public static int ROWS = DomineeringState.ROWS;
	public static int COLS = DomineeringState.COLS;
	public static final int MAX_SCORE = (BaseDomineeringPlayer.COLS*BaseDomineeringPlayer.ROWS)*1000;//A very large number (only used for terminal states I think)
	
	public BaseDomineeringPlayer(String nname)
	{ super(nname, "Domineering");	}
	
	public static boolean isPossibleMove(DomineeringState brd,char who,int r1, int c1,int r2,int c2){
		if(r1<0|| r2<0 || c1<0 || c2<0){
			return false;
		}else if(r1>=ROWS || r2 >= ROWS || c1 >= COLS || c2 >= COLS){
			return false;
		}else if(who == 'H'){
			if(r1-r2 != 0){
				return false;
			} else if(Math.abs(c1-c2) != 1){
				return false;
			}else if(brd.board[r1][c1] != brd.emptySym && brd.board[r2][c2] != brd.emptySym){
				return false;
			}else{
				return true;
			}
		}else {
			if(c1-c2 != 0){
				return false;
			} else if(Math.abs(r1-r2) != 1){
				return false;
			}else if(brd.board[r1][c1] != brd.emptySym && brd.board[r2][c2] != brd.emptySym){
				return false;
			}else{
				return true;
			}
		}
	}
	/**
	 * A function that determines how many possible moves there are.
	 * @param brd board to evaluated
	 * @param who 'H' or 'A'
	 * @return amount of possible moves for a player
	 */
	public static int possibleMoves(DomineeringState brd, char who){
		//I don't think we are doing it right because I am not sure if moveOk()
		//Works when it is not the player's turn so we might need a seperate function
		//To find out if that move is vaild.
		int numPossibleMoves = 0;
		if(who == 'H'){
			for(int c = 0; c < brd.COLS;c++){
				for(int r = 0; r < brd.ROWS;r++){
					if(isPossibleMove(brd,'H',r,c,r,c+1)){
						numPossibleMoves++;
					}
				}
			}
		}else{
			for(int r = 0; r < brd.ROWS;r++){
				for(int c = 0; c < brd.COLS;c++){
					if(isPossibleMove(brd,'A',r,c,r+1,c)){
						numPossibleMoves++;
					}
				}
			}
		}
		return numPossibleMoves;
	}
	/**
	 * A function that determines how many possible moves there are and how many blocking moves.
	 * @param brd board to evaluated
	 * @param who 'H' or 'A'
	 * @return array size 2 with return[0]= # possible moves and return[1]= # blocking moves
	 */
	public static int[] possibleMovesAndBlockingMoves(DomineeringState brd, char who){
		//TODO: Check logic for checking if a move is valid.
		int[] numMoves = new int[2];
		if(who == 'H'){
			for(int c = 0; c < brd.COLS;c++){
				for(int r = 0; r < brd.ROWS;r++){
					
					if(brd.moveOK(new DomineeringMove(r,c,r+1,c))){
						numMoves[0]++;
						if(BaseDomineeringPlayer.blockingMove(brd, r, c, r+1, c, who))
							numMoves[1]++;
					}
				}
			}
		}else{
			for(int r = 0; r < brd.ROWS;r++){
				for(int c = 0; c < brd.COLS;c++){
					if(brd.moveOK(new DomineeringMove(r,c,r,c+1))){
						numMoves[0]++;
						if(BaseDomineeringPlayer.blockingMove(brd, r, c, r, c+1, who))
							numMoves[1]++;
					}
				}
			}
		}
		return null;
	}
	/**
	 * Function that determines if a move will block an opponents move
	 * @param brd The board being analyzed
	 * @param r1 
	 * @param c1
	 * @param r2
	 * @param c2 
	 * @param who 'H' or 'A'
	 * @return true is the the desired move is a blocking move.
	 */
	public static boolean blockingMove(DomineeringState brd, int r1, int c1,int r2,int c2, char who){
		//TODO: Write a function to determine if the move block's the opponent from making a move.
		return false;
	}
	
	/**
	 * An evaluation function that returns the score of the board based on who has more moves.
	 * A positive result is seen as a better score.
	 * @param brd The board being analyzed 
	 * @param who 'H' or 'A'
	 * @return the score for the board
	 */
	public static int evalBoard1(DomineeringState brd){
		//TODO: Make sure the who char is being used correctly.
		int score = possibleMoves(brd, Connect4State.homeSym) - possibleMoves(brd, Connect4State.awaySym);
		if (Math.abs(score) > MAX_SCORE) {
			System.err.println("Problem with eval");
			System.exit(0);
		}
		return score;
	}
	/**
	 * Evaluated the board based on the number of moves, and the number or moves that will block an opponents move.
	 * A blocking move is considered two times as valuable as a normal move.
	 * @param brd board being evaluated
	 * @param who 'H' or 'A'
	 * @return score of the board
	 */
	public static int eval2(DomineeringState brd, char who){
		//TODO: Make sure the who char is being used correctly.
		int[] homeMoves = BaseDomineeringPlayer.possibleMovesAndBlockingMoves(brd,'H');
		int[] awayMoves = BaseDomineeringPlayer.possibleMovesAndBlockingMoves(brd,'A');
		if(who == 'H'){
			return 2*(homeMoves[1]-awayMoves[1]) + (homeMoves[0]-awayMoves[0]);
		}else{
			return 2*(awayMoves[1]-homeMoves[1]) + (awayMoves[0]-homeMoves[0]);
		}
	}
	/**
	 * A selector function for the evaluation of the bored.
	 * Note: We might not need this in the end because we will always be using the best one we have. 
	 * I figured it would be nice to have in the beginning while we test the different ones.
	 * @param brd board being evaluated
	 * @param who player the score is based on.
	 * @param evalFunction selected which evaluation function to use.
	 * @return the score of the board.
	 */
	public static double evalBoard(DomineeringState brd,int evalFunction){
		switch(evalFunction){
		case 1:
			System.out.println("Using eval function 1");
			//return BaseDomineeringPlayer.eval1(brd, who);
		case 2:
			System.out.println("Using eval function 2");
			//return BaseDomineeringPlayer.eval2(brd, who);
		}
		System.out.println("Improper Eval Function used.");
		return 0;
	}
}
