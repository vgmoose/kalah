package com.mancala;

public class Player {
	private String name;
	private boolean turn;
	private boolean movingBeans;
	private BigPit bigPit;
	private SmallPit[] smallPits;
	private Mancala mancala;
	
	public Player(Mancala mancala) {
		this.mancala = mancala;
		movingBeans = false;
	}
	
	public String getName() {
		return name;
	}
	
	public Mancala getMancala() {
		return mancala;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isTurn() {
		return turn;
	}
	
	public void startTurn() {
		this.turn = true;
	}
	
	public void endTurn() {
		this.turn = false;
	}
	
	public boolean isMovingBeans() {
		return movingBeans;
	}
	
	public void startMovingBeans() {
		this.movingBeans = true;
	}
	
	public void endMovingBeans() {
		this.movingBeans = false;
	}
	
	public int getScore() {
		return bigPit.getCounters();
	}
	
	public void setSmallPits(SmallPit[] pits) {
		smallPits = pits;
	}
	
	public SmallPit[] getSmallPits() {
		return smallPits;
	}
	
	public void setBigPit(BigPit pit) {
		bigPit = pit;
	}
	
	public BigPit getBigPit() {
		return bigPit;
	}
	
	public boolean hasCounters() {
		for(SmallPit s : smallPits) {
			if(s.getCounters() != 0)
				return true;
		}
		return false;
	}
	
	public void reset() {
		for(SmallPit s : smallPits) {
			s.reset();
		}
		bigPit.reset();
		this.endTurn();
	}

}
