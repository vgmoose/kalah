package com.mancala;

public class RandomBot extends Player {

	public RandomBot(Mancala mancala) {
		super(mancala);
		setName("RandomBot");
	}
	
	public void startTurn()
	{
		this.turn = true;
		int selected = 0;
		SmallPit chosenPit = null;
		
		// make a random move
		while (selected == 0)
		{
			 chosenPit = this.smallPits[(int) (Math.random()*this.smallPits.length)];
			 selected = chosenPit.counters;
		}

		if (chosenPit != null)
			chosenPit.moveCountersHighlight();
	}

}
