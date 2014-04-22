package com.mancala;

import java.io.Serializable;

public class LastPit extends SmallPit implements Serializable{
	
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
