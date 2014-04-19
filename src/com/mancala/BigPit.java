package com.mancala;


public class BigPit extends Pit {
	
	public BigPit(Player owner) {
		this.owner = owner;
	}

	@Override
	public Pit getNext() {
		return next;
	}

	@Override
	public void moveCounters() {
		throw new RuntimeException("Moved counters from big pot");
	}

	@Override
	public void reset() {
		this.setCounters(0);
		this.pot.initBeans();
	}

}
