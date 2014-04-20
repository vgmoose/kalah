package com.mancala;

public class SmartBot extends Player
{
	Player otherPlayer;
	// This is the RobotPlayer class which will act as a regular player

	public SmartBot(Mancala mancala) {
		super(mancala);
		setName("SmartBot");		
	}

	public void setOtherPlayer(Player other)
	{
		otherPlayer = other;
	}

	public void startTurn()
	{
		// scan ahead 10 turns to see what potential moves are the best
		minimax(mancala, Integer.MAX_VALUE, Integer.MIN_VALUE, true, 10);
		
	}
	
	public int minimax(Mancala boardState, int alpha, int beta, boolean player, int depth)
	{
		// decrease the depth
		depth --;
		
		// return the heuristic if we can't go deeper or the game is over
		if (depth == 0 || boardState.checkWin())
			return heuristic();
		
		if (player)
		{
			Mancala[] potentialMoves = new Mancala[6];
			populatePotentialMoves(potentialMoves, boardState);
			
			// for each move, set the alpha
			for (int x=0; x<6; x++)
			{
				alpha = Math.max(alpha, minimax(potentialMoves[x], alpha, beta, false, depth));
				
				// alpha beta prune if necessary
				if (beta <= alpha)
					break;
			}
			
			// return the alpha
			return alpha;
		}
		else
		{
			Mancala[] potentialMoves = new Mancala[6];
			populatePotentialMoves(potentialMoves, boardState);
			
			// for each move, set the alpha
			for (int x=0; x<6; x++)
			{
				beta = Math.min(beta, minimax(potentialMoves[x], alpha, beta, true, depth));
				
				// alpha beta prune if necessary
				if (beta <= alpha)
					break;
			}
			
			// return the beta
			return beta;
		}
	}

	private void populatePotentialMoves(Mancala[] potentialMoves, Mancala boardState) 
	{
//		potentialMoves = boardState.clone();
		
	}

	public int heuristic()
	{
		// get the scores of each player
		int my_score = this.bigPit.counters;
		int your_score = otherPlayer.bigPit.counters;

		// to store the counts of stones on each side of the field
		int my_stones = 0;
		int your_stones = 0;

		// sum up the stones in the six pits
		for (Pit b : this.smallPits)
			my_stones += b.counters;

		// sum the stones in the six pits
		for (Pit b : otherPlayer.smallPits)
			your_stones += b.counters;
		
		// depending on whose turn it is, this should be maximized or minimized
		return (my_score - your_score) + (my_stones - your_stones);
	}

}
