package com.mancala;

public class SmartBot extends Player
{
	// This is the RobotPlayer class which will act as a regular player

	int heuristicPitIndex = -1;
	
	public SmartBot(Mancala mancala) {
		super(mancala);
		setName("SmartBot");	
	}

	public void startTurn()
	{
		// prevent cycles from the simulation
		if (mancala.cloned)
			return;
					
		super.startTurn();
		
		int valDouble = -1;
		
		// check for a double move and take it if available
		for (int x=0; x<6; x++)
			if (this.smallPits[x].counters == (6-x))
				valDouble = x;
				
		if (valDouble >= 0)
			heuristicPitIndex = valDouble;
		else
		{
				
		// scan ahead 10 turns to see what potential moves are the best
		int val = minimax(mancala, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 3);
		
		// print out the heuristic
		System.out.println("pit #" + heuristicPitIndex + ", optimal value: " + val);
		}
		
		// make the move!
		this.smallPits[heuristicPitIndex].moveCountersHighlight();
	}
	
	public int minimax(Mancala boardState, int alpha, int beta, boolean player, int depth)
	{
		// decrease the depth
		depth --;
		
		// return the heuristic if we can't go deeper or the game is over
		if (depth == 0 || boardState.checkWin())
			return heuristic(boardState);
		
		if (player)
		{
			Mancala[] potentialMoves = new Mancala[6];
			populatePotentialMoves(potentialMoves, boardState);
			
			// for each move, set the alpha
			for (int x=0; x<6; x++)
			{
				if (potentialMoves[x] == null)
					continue;
				
				int oa = alpha;
				alpha = Math.max(alpha, minimax(potentialMoves[x], alpha, beta, false, depth));

				if (alpha != oa)
					heuristicPitIndex = x;
//					System.out.println("[" + depth + " : alpha] Choosing pit #" + (x+1) + " gets " + alpha);
					
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
				if (potentialMoves[x] == null)
					continue;
				
				int oa = beta;
				beta = Math.min(beta, minimax(potentialMoves[x], alpha, beta, true, depth));
				
				if (alpha != oa)
					heuristicPitIndex = x;
//					System.out.println("[" + depth + " : beta] Choosing pit #" + (x+1) + " gets " + beta);
				
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
		
		// enact each potential move (where the pit isn't empty) as a new boardstate
		for (int x=0; x<6; x++)
		{
			Mancala newState;
			try {
				newState = (Mancala) ObjectCloner.deepCopy(boardState);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			SmallPit[] pits;
			
			// get the active player's array of pits
			if (newState.getPlayerOne().isTurn())
				pits = newState.getPlayerOne().smallPits;
			else
				pits = newState.getPlayerTwo().smallPits;
			
			// tell the new state to only simulate the move
			newState.youAreAClone();
			
			// moves are invalid if there are no counters
			if (pits[x].counters == 0)
				newState = null;
			
			// enact the move selected
			pits[x].moveCounters();
			
			
			// store the result of the move to the potential moves array
			potentialMoves[x] = newState;
		}		
	}

	public int heuristic(Mancala boardState)
	{
		Player activePlayer, otherPlayer;
		
		// find out who the active player is
		if (boardState.getPlayerOne().isTurn())
		{
			activePlayer = boardState.getPlayerOne();
			otherPlayer = boardState.getPlayerTwo();
		}
		else
		{
			activePlayer = boardState.getPlayerTwo();
			otherPlayer = boardState.getPlayerOne();
		}
		
		// get the scores of each player
		int my_score = activePlayer.bigPit.counters;
		int your_score = otherPlayer.bigPit.counters;

		// to store the counts of stones on each side of the field
		int my_stones = 0;
		int your_stones = 0;

		// sum up the stones in the six pits
		for (Pit b : activePlayer.smallPits)
			my_stones += b.counters;

		// sum the stones in the six pits
		for (Pit b : otherPlayer.smallPits)
			your_stones += b.counters;
		
		// depending on whose turn it is, this should be maximized or minimized
		return (my_score - your_score) + (my_stones - your_stones);
	}

}
