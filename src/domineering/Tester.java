package domineering;

public class Tester {

	public static void main(String[] args) {
		
		
		
		DomineeringState brd = new DomineeringState();
		AlphaBetaDomineeringPlayer testPlayer = new AlphaBetaDomineeringPlayer("test player",20);
		System.out.println(brd.toString());
		
		testPlayer.init();
		for(int i = 0; i < 10; i++){
			System.out.println(testPlayer.getMove(brd, "").toString());;
		}
		
		
		/*
		//Creating a board
		brd.makeMove(new DomineeringMove(0,0,0,1));
		brd.makeMove(new DomineeringMove(1,0,2,0));
		brd.makeMove(new DomineeringMove(0,2,0,3));
		brd.makeMove(new DomineeringMove(1,1,2,1));
		System.out.println(brd.toString());
		
		
		/*
		//testing the alphaBeta search
		testPlayer.init();
		testPlayer.getMove(brd, (new DomineeringMove(1,1,2,1)).toString());
		*/
		
		/*
		//testing max number of moves:
		int homeMoves = testPlayer.possibleMoves(brd, 'H');
		int awayMoves = testPlayer.possibleMoves(brd, 'A');
		System.out.printf("Home Moves: %d \nAway Moves: %d \n",homeMoves,awayMoves);
		*/
		
		
		/*
		//Testing the getPossibleMoves
		DomineeringMove[] homeMoves = testPlayer.getPossibleMoves(brd, 'H');
		brd.makeMove(homeMoves[0]);
		DomineeringMove[] awayMoves = testPlayer.getPossibleMoves(brd, 'A');
		System.out.println(brd.toString());
		for(int i = 0; i < homeMoves.length;i++){
			//System.out.println("HomemoveSize: " + homeMoves.length);
			System.out.println("Home move "+ i+ ": " +homeMoves[i]);
		}
		for(int i = 0; i < awayMoves.length;i++){
			System.out.println("Away move "+ i+ ": " +awayMoves[i]);
		}
		System.out.println(brd.toString());
		*/
	}

}
