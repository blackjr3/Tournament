/**
 * 
 */
package domineering;

import game.GameMove;
import game.GamePlayer;
import game.GameState;

/**
 * @author Jonathan Black 
 * Questions still to ask:
 */
// TODO: There is a timeout.
// TODO:
public class AlphaBetaDomineeringPlayer extends MiniMaxDomineeringPlayer {

	public AlphaBetaDomineeringPlayer(String nname, int d) {
		super(nname, d);
	}

	/**
	 * Performs alpha beta pruning.
	 * 
	 * @param brd
	 * @param currDepth
	 * @param alpha
	 * @param beta
	 */
	private void alphaBeta(DomineeringState brd, int currentDepth, double alpha, double beta) {
		//System.out.println("Current depth = " + currentDepth);
		//System.out.println("Depth Limit = " + depthLimit);
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean toMinimize = !toMaximize;

		boolean isTerminal = terminalValue(brd, mvStack[currentDepth]);

		if (isTerminal) {
			//System.out.println("Found a terminal board");
			;
		} else if (currentDepth >= depthLimit) {
			// TODO: Ask why the move is 0,0,0,0 or connect4move is 0
			//System.out.println("Reached depthLimit.");
			mvStack[currentDepth].set(0, 0, 0, 0, super.evalBoard1(brd));
		} else {
			// Connect4 uses a temp move but we have a data
			// structure of domineering moves
			// so I don't think this is needed
			// ScoredDomineeringMove tempMv = new
			// ScoredDomineeringMove(0,0,0,0,0);

			double bestScore = (brd.getWho() == GameState.Who.HOME ? Double.NEGATIVE_INFINITY
			        : Double.POSITIVE_INFINITY);

			ScoredDomineeringMove bestMove = mvStack[currentDepth];
			ScoredDomineeringMove nextMove = mvStack[currentDepth + 1];

			bestMove.set(0, 0, 0, 0, bestScore);
			GameState.Who currTurn = brd.getWho();

			// Store the moves as DomineeringMove[numMoves]
			// TODO: See if we need to make this
			// ScoredDomineeringMoves[numMoves]
			//System.out.println("Generating Possible Moves.");
			DomineeringMove[] possibleMoves;
			if (toMaximize) {// Home
				possibleMoves = this.getPossibleMoves(brd, 'H');
			} else { // Away
				possibleMoves = this.getPossibleMoves(brd, 'A');
			}
			int numPossibleMoves = possibleMoves.length;
			shuffle(possibleMoves); // Random move ordering
			//System.out.println("Generated " + numPossibleMoves+ " possible Moves.");
			for (int i = 0; i < numPossibleMoves; i++) {
				//System.out.println("Entering loop.");
				if (brd.moveOK(possibleMoves[i])) {
					//System.out.println("Testing move: " + possibleMoves[i].toString());
					// Connect4 uses a temporary move but I don't think we need it
					

					// Makes the move
					//System.out.println("Making move");
					brd.makeMove(possibleMoves[i]);

					// Recursively call the alphaBeta function
					//System.out.println("Entering Recusion.");
					this.alphaBeta(brd, currentDepth + 1, alpha, beta);

					// TODO: Make sure we clear the move completely.
					// Remove the move from the board
					brd.board[possibleMoves[i].row1][possibleMoves[i].col1] = DomineeringState.emptySym;
					brd.board[possibleMoves[i].row2][possibleMoves[i].col2] = DomineeringState.emptySym;
					brd.numMoves--;
					brd.status = GameState.Status.GAME_ON;
					brd.who = currTurn;

					// Check out the results, relative to what we've seen before
					if (toMaximize && nextMove.score > bestMove.score) {
						//System.out.println("Move is the best one so far.");
						bestMove.set((DomineeringMove) possibleMoves[i].clone(),nextMove.score);
					} else if (!toMaximize && nextMove.score < bestMove.score) {
						//System.out.println("Move is the best one so far.");
						bestMove.set((DomineeringMove) possibleMoves[i].clone(), nextMove.score);
					}
					// Update alpha and beta. Perform pruning, if possible.
					//System.out.println("Doing alpha beta pruning.");
					if (toMinimize) {
						beta = Math.min(bestMove.score, beta);
						if (bestMove.score <= alpha || bestMove.score == -MAX_SCORE) {
							return;
						}
					} else {
						alpha = Math.max(bestMove.score, alpha);
						if (bestMove.score >= beta || bestMove.score == MAX_SCORE) {
							return;
						}
					}

				}
			}
		}
	}

	public GameMove getMove(GameState brd, String lastMove) {
		//System.out.println("Getting the move.");
		alphaBeta((DomineeringState) brd, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		//System.out.print("Best move is: " + mvStack[0].toString() + " with score: ");
		//System.out.println(mvStack[0].score);
		return mvStack[0];
	}

	// Included in C4 alphaBeta not sure what it is used for.
	public static char[] toChars(String x) {
		char[] res = new char[x.length()];
		for (int i = 0; i < x.length(); i++)
			res[i] = x.charAt(i);
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int depth = 9;
		GamePlayer p = new AlphaBetaDomineeringPlayer("A-B Domineering F1 " + depth, depth);
		p.init();
		p.compete(args);

	}

}
