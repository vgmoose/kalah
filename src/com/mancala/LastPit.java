package com.mancala;

public class LastPit extends SmallPit {
	
	private Pit alternate;
	
	public LastPit(Player owner) {
		super(owner);
	}
	
	public void setAlternate(Pit pit) {
		alternate = pit;
	}

	@Override
	public Pit getNext() {
		if(owner.isTurn())
			return next;
		else
			return alternate;
	}

}
