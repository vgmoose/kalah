package com.mancala;

public abstract class Pit {
	
	protected int counters;
	protected Pit next;
	protected Player owner;
	protected Pot pot;
	
	abstract public Pit getNext();
	
	public void addCounters(final int number) {
		counters += number;
		pot.addBeans(number);
	}
	
	public int getCounters() {
		return counters;
	}
	
	public void setCounters(int amount) {
		counters = amount;
	}
	
	public void setPlayer(Player player) {
		this.owner = player;
	}
	
	public void setPot(Pot pot) {
		this.pot = pot;
	}
	
	public void setNext(Pit next) {
		this.next = next;
	}
	
	abstract public void moveCounters();
	abstract public void reset();

}
