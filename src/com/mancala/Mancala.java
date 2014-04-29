package com.mancala;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Mancala implements Serializable {
	private static final int PIT_COUNT = 6;

	GUI gui;
	private Player playerOne;
	private Player playerTwo;
	private HighScores highScores;
	public boolean cloned = false;

	protected boolean botVbot;

	enum Algorithm {
		HUMAN, RANDOM, SMART;
	}
	
	boolean forcePlayersFromArgs= false;
	Mancala.Algorithm type1, type2;

	int depth = 3;

	static ArrayList<int[]> colorBases;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mancala mancala = new Mancala();
		
		if (args.length >= 2)
		{
			mancala.forcePlayersFromArgs = true;
			
			mancala.type1 = (args[0].equals("smart")? Algorithm.SMART : Algorithm.HUMAN);
			mancala.type1 = (args[0].equals("random")? Algorithm.RANDOM : mancala.type1);
			
			mancala.type2 = (args[1].equals("smart")? Algorithm.SMART : Algorithm.HUMAN);
			mancala.type2 = (args[1].equals("random")? Algorithm.RANDOM : mancala.type2);
			
			if (args.length == 3)
				mancala.depth = Integer.parseInt(args[2]);
		}
		
		mancala.initGame();
	}

	public boolean checkWin() {
		if(playerOne.hasCounters() && playerTwo.hasCounters())
			return false;
		else {
			if(playerOne.hasCounters()) {
				for(SmallPit s : playerOne.getSmallPits()) {
					playerOne.getBigPit().addCounters(s.removeCounters());
				}
			} else if(playerTwo.hasCounters()) {
				for(SmallPit s : playerTwo.getSmallPits()) {
					playerTwo.getBigPit().addCounters(s.removeCounters());
				}
			}
		}

		if (!cloned)
		{
		if(playerOne.getScore() > playerTwo.getScore()) {
			highScores.addHighSCore(playerOne.getName(), playerOne.getScore());
			JOptionPane.showMessageDialog(null, playerOne.getName() + " won with " + playerOne.getScore() + " points.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		} else if(playerOne.getScore() < playerTwo.getScore()) {
			highScores.addHighSCore(playerTwo.getName(), playerTwo.getScore());
			JOptionPane.showMessageDialog(null, playerTwo.getName() + " won with " + playerTwo.getScore() + " points.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "It was a tie. " + playerOne.getName() + " and " + playerTwo.getName() + " both scored 24 points.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		}

		return true;
	}

	public void initGame() {
		colorBases = new ArrayList<int[]>();

		for (int x=0; x<36; x++)
		{
			int[] colors = {(int) (Math.random()*256), (int) (Math.random()*256), (int) (Math.random()*256), (int) (Math.random()*6), (int) (Math.random()*6)};
			colorBases.add(colors);
		}

//		initPlayers(Algorithm.HUMAN, Algorithm.HUMAN);
//		initPlayers(Algorithm.HUMAN, Algorithm.RANDOM);
//		initPlayers(Algorithm.RANDOM, Algorithm.RANDOM);
//		initPlayers(Algorithm.HUMAN, Algorithm.SMART);
		
		
		if (forcePlayersFromArgs)
			initPlayers(type1, type2);
		else
			initPlayers(Algorithm.RANDOM, Algorithm.SMART);

		gui = new GUI(this);

		playerOne.startTurn();
		highScores = new HighScores();
	}

	public void changeNames() {
		String nameOne = JOptionPane.showInputDialog(null, "Enter name for player one (max 10 characters)", "Welcome to Mancala", JOptionPane.QUESTION_MESSAGE);
		if(nameOne != null && nameOne.length() <= 10)
			playerOne.setName(nameOne.replaceAll("\t", " "));
		String nameTwo = JOptionPane.showInputDialog(null, "Enter name for player two (max 10 characters)", "Welcome to Mancala", JOptionPane.QUESTION_MESSAGE);
		if(nameTwo != null && nameTwo.length() <= 10)
			playerTwo.setName(nameTwo.replaceAll("\t", " "));
		gui.refreshBigPots();
	}

	public void resetGame() {
		colorBases = new ArrayList<int[]>();

		for (int x=0; x<36; x++)
		{
			int[] colors = {(int) (Math.random()*256), (int) (Math.random()*256), (int) (Math.random()*256), (int) (Math.random()*6), (int) (Math.random()*6)};
			colorBases.add(colors);
		}

		playerOne.reset();
		playerTwo.reset();
		playerOne.startTurn();
	}

	private void initPlayers(Algorithm algo1, Algorithm algo2) {

		if (algo1 == Algorithm.HUMAN)
			playerOne = new Player(this);
		else if (algo1 == Algorithm.RANDOM)
			playerOne = new RandomBot(this);
		else
			playerOne = new SmartBot(this);

		if (algo2 == Algorithm.HUMAN)
			playerTwo = new Player(this);
		else if (algo2 == Algorithm.RANDOM)
			playerTwo = new RandomBot(this);
		else
			playerTwo = new SmartBot(this);

		if (playerOne.getClass() == playerTwo.getClass())
		{
			playerOne.setName(playerOne.getName() + " Right");
			playerTwo.setName(playerTwo.getName() + " Left");
		}
		
		botVbot = (playerOne.getClass().getName().equals("Player") ||
					playerTwo.getClass().getName().equals("Player"));

		playerTwo.endTurn();
		initPits();
//		playerOne.startTurn();
	}

	private void initPits() {
		SmallPit[] playerOnePits = new SmallPit[6];
		SmallPit[] playerTwoPits = new SmallPit[6];

		//create normal pits (1-5) for player 1
		for(int i = 0; i < PIT_COUNT - 1; i++) {
			playerOnePits[i] = new SmallPit(playerOne);
			playerOnePits[i].setCounters(3);
		}

		//create normal pits (1-5) for player 2
		for(int i = 0; i < PIT_COUNT - 1; i++) {
			playerTwoPits[i] = new SmallPit(playerTwo);
			playerTwoPits[i].setCounters(3);
		}

		//create last pits for players 1 & 2
		LastPit playerOneLastPit = new LastPit(playerOne);
		LastPit playerTwoLastPit = new LastPit(playerTwo);
		playerOneLastPit.setCounters(3);
		playerTwoLastPit.setCounters(3);

		//set alternates for last pits
		playerOneLastPit.setAlternate(playerTwoPits[0]);
		playerTwoLastPit.setAlternate(playerOnePits[0]);

		//add these to normal pits
		playerOnePits[PIT_COUNT - 1] = playerOneLastPit;
		playerTwoPits[PIT_COUNT - 1] = playerTwoLastPit;

		//add opposite pits
		for(int i = 0; i < PIT_COUNT; i++) {
			playerOnePits[i].setOpposite(playerTwoPits[PIT_COUNT - 1 - i]);
			playerTwoPits[i].setOpposite(playerOnePits[PIT_COUNT - 1 - i]);
		}

		//create big pits
		BigPit playerOneBigPit = new BigPit(playerOne);
		BigPit playerTwoBigPit = new BigPit(playerTwo);
		playerOneBigPit.setCounters(0);
		playerTwoBigPit.setCounters(0);
		playerOneBigPit.setNext(playerTwoPits[0]);
		playerTwoBigPit.setNext(playerOnePits[0]);

		//add next pits for player 1
		for(int i = 0; i < PIT_COUNT; i++) {
			if(i < PIT_COUNT - 1) {
				playerOnePits[i].setNext(playerOnePits[i + 1]);
			} else {
				playerOnePits[i].setNext(playerOneBigPit);
			}
		}

		//add next pits for player 2
		for(int i = 0; i < PIT_COUNT; i++) {
			if(i < PIT_COUNT - 1) {
				playerTwoPits[i].setNext(playerTwoPits[i + 1]);
			} else {
				playerTwoPits[i].setNext(playerTwoBigPit);
			}
		}

		//assign small pits
		playerOne.setSmallPits(playerOnePits);
		playerTwo.setSmallPits(playerTwoPits);

		//assign big pits
		playerOne.setBigPit(playerOneBigPit);
		playerTwo.setBigPit(playerTwoBigPit);
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	public Player getPlayerOne() {
		return playerOne;
	}

	public Player getPlayerTwo() {
		return playerTwo;
	}

	public HighScores getHighSCores() {
		return highScores;
	}

	public void youAreAClone() {
		cloned = true;
//		gui = null;
		
	}

}
