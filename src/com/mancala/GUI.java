package com.mancala;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI implements Serializable{
	 Pot[] pots;
	 Pot bigPot1;
	 Pot bigPot2;
	private Mancala mancala;
	
	public GUI(Mancala mancala) {
		this.mancala = mancala;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("Kalah");
		frame.setSize(860, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((screen.width - 860) / 2, (screen.height - 320) / 2);
		frame.setVisible(true);
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		createPots();
		JPanel center = new JPanel(new GridLayout(2, 6));
		center.setOpaque(false);
		MyPanel panel = new MyPanel();
		panel.setLayout(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		addPots(center, panel);
		content.add(panel, BorderLayout.CENTER);
		createMenu(frame);
	}
	
	public void refreshBigPots() {
		bigPot1.refresh();
		bigPot2.refresh();
	}
	
	private void createMenu(JFrame frame) {
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);
		
		JMenuItem newGame = new JMenuItem("New Game");
		menu.add(newGame);
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!mancala.getPlayerOne().isMovingBeans() && !mancala.getPlayerTwo().isMovingBeans()) {
					mancala.resetGame();
				}
			}
		});
		
		JMenuItem changeNames = new JMenuItem("Change names");
		menu.add(changeNames);
		changeNames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mancala.changeNames();
			}
		});
		
		JMenuItem exit = new JMenuItem("Exit");
		menu.add(exit);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JMenu highScoresMenu = new JMenu("High Scores");
		menu.setMnemonic(KeyEvent.VK_S);
		JMenuItem highScores = new JMenuItem("Top 10");
		highScores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mancala.getHighSCores().show();
			}
		});
		highScoresMenu.add(highScores);
		
		JMenuItem resethighScores = new JMenuItem("Reset");
		resethighScores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mancala.getHighSCores().resetHighScores();
			}
		});
		highScoresMenu.add(resethighScores);
		menuBar.add(highScoresMenu);
		
		JMenu helpMenu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		JMenuItem help = new JMenuItem("Show Help");
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getGUI().showHelp();
			}
		});
		helpMenu.add(help);
		menuBar.add(helpMenu);
	}

	private void addPots(JPanel center, MyPanel panel) {
		panel.add(bigPot1, BorderLayout.EAST);
		panel.add(bigPot2, BorderLayout.WEST);
		for (int i = 11; i > 5; i--) {
			center.add(pots[i]);
		}
		
		for (int i = 0; i < 6; i++) {
			center.add(pots[i]);
		}
	}

	private void createPots() {
		bigPot1 = new BigPot();
		bigPot1.setPit(mancala.getPlayerOne().getBigPit());
		bigPot2 = new BigPot();
		bigPot2.setPit(mancala.getPlayerTwo().getBigPit());
		pots = new Pot[12];
		for (int i = 0; i < pots.length; i++) {
			if (i < 6) {
				pots[i] = new Pot(true);
				pots[i].setPit(mancala.getPlayerOne().getSmallPits()[i]);
			} else {
				pots[i] = new Pot(false);
				pots[i].setPit(mancala.getPlayerTwo().getSmallPits()[i - 6]);
			}
		}
	}
	
	public GUI getGUI() {
		return this;
	}
	
	private void showHelp() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("Help");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocation((screen.width / 2) - 200, (screen.height / 2) -200);
		
		JTextArea helpText = new JTextArea(createHelpText());
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		helpText.setEditable(false);
		JScrollPane scroll = new JScrollPane(helpText);
		scroll.setVerticalScrollBar(new JScrollBar());
		frame.add(scroll);
		frame.setVisible(true);
	}
	
	private String createHelpText() {
		StringBuilder sb = new StringBuilder();
		sb.append("How to play Mancala.\n\n");
		sb.append("1. The Mancala 'board' is made up of two rows of six holes, or pits, each.\n");
		sb.append("2. Four pieces are placed in each of the 12 holes. " +
				"The color of the pieces is red.\n");
		sb.append("3. Each player has a 'store'. For player one, it is to the right of the playing field. " +
				"For player two, it is to the left of the playing field.\n");
		sb.append("4. The game begins with player one clicking on any one of the holes on his side.\n");
		sb.append("5. Moving counter-clockwise, one piece is deposited in each hole until the pieces run out.\n");
		sb.append("6. If you run into your own store, one piece is deposited in it. " +
				"If you run into your opponent's store, it is skipped.\n");
		sb.append("7. If the last piece is dropped in your own store, you get a free turn.\n");
		sb.append("8. If the last piece is dropped in an empty hole on your side and there are pieces in the hole directly opposite, " +
				"you capture that piece and any pieces in the hole directly opposite.\n");
		sb.append("9. All captured pieces are placed in your store.\n");
		sb.append("10. If you do not finish in your 'store', it is the turn of the other player. " + 
				"The other player can click on any non-empty hole he/she controls to make the move.\n");
		sb.append("11. The game ends when all six spaces on one side of the Mancala board are empty.\n");
		sb.append("12. The player who still has pieces on his side of the board when the game ends captures all of those pieces.\n");
		sb.append("13. The pieces in each store are counted. The winner is the player with the most pieces.\n");
		
		return sb.toString();
	}
}
