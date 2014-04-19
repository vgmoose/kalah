package com.mancala;

public class SmallPit extends Pit {

	protected SmallPit opposite;

	public SmallPit(Player owner) {
		this.owner = owner;
	}

	public int removeCounters() {
		int amount = counters;
		counters = 0;
		pot.removeBeans();
		return amount;
	}

	public void setOpposite(SmallPit pit) {
		opposite = pit;
	}

	public SmallPit getOpposite() {
		return opposite;
	}

	@Override
	public Pit getNext() {
		return next;
	}

	@Override
	public void moveCounters() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SmallPit.this.owner.startMovingBeans();
				int amount = removeCounters();
				Pit current = SmallPit.this;
				while (amount > 0) {
					current = current.getNext();
					current.addCounters(1);
					amount--;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (current instanceof SmallPit) {
					if (current.getCounters() == 1 && current.owner == SmallPit.this.owner) {
						SmallPit.this.owner.getBigPit().addCounters(
								((SmallPit) current).removeCounters()
										+ ((SmallPit) current).getOpposite()
												.removeCounters());
					}
					SmallPit.this.owner.endTurn();
					if (!SmallPit.this.owner.getMancala().checkWin()) {
						if (SmallPit.this.owner == SmallPit.this.owner.getMancala().getPlayerOne()) {
							SmallPit.this.owner.getMancala().getPlayerTwo().startTurn();
						} else {
							SmallPit.this.owner.getMancala().getPlayerOne().startTurn();
						}
					}
				} else {
					if (SmallPit.this.owner.getMancala().checkWin()) {
						SmallPit.this.owner.endTurn();
					}
				}
				SmallPit.this.owner.endMovingBeans();
			}		
		}).start();
	}

	@Override
	public void reset() {
		this.setCounters(4);
		this.pot.initBeans();
	}
}
