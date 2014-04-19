package com.mancala;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class HighScores {
	
	private int highestScore;
	private int lowestScore = 0;
	private String lowestKey;
	private final String scoresFilePath =  "high_scores.txt";
	private final File scoresFile = new File(scoresFilePath);
	private JFrame frame;
	private HashMap<String, Integer> scores = new LinkedHashMap<String, Integer>();

	public HighScores() {
		initHighScores();
		refreshScores();
	}
	
	private void initHighScores() {
		this.frame = initHighScoresFrame();
		readHighScoresFromFile(scoresFile);
		initMinMax();
	}
	
	public void show() {
		initFrameContent();
		frame.setVisible(true);
	}
	
	private void initFrameContent() {
		String highScores = readHighScoresFromFile(scoresFile);
		JTextArea scoresText = new JTextArea(highScores);
		scoresText.setEditable(false);
		frame.add(scoresText);
	}
	
	private String readHighScoresFromFile(File file) {
		StringBuilder result = new StringBuilder();
		result.append("TOP 10 high scores: \n\n");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			int place = 1;
			while((line = reader.readLine()) != null) {
				String[] parts = line.split("	");
				scores.put(parts[0], Integer.parseInt(parts[1]));
				result.append(place + ". " + parts[0] + " - " + parts[1] +  "\n");
				place++;
			}
		} catch (Exception e) {
			System.out.println("read file: " + e.toString());
		}
		return result.toString();
	}
	
	
	private JFrame initHighScoresFrame() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame highScoresFrame = new JFrame("High Scores");
		highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		highScoresFrame.setSize(300, 400);
		highScoresFrame.setLocation((screen.width / 2) - 150, (screen.height / 2) -200);
		
		return highScoresFrame;
	}
	
	private void initMinMax() {
		int min = 100;
		if(scores.size() != 0) {
			for (String player : scores.keySet()) {
				int value = scores.get(player);
				if(value < min) {
					min = value;
					lowestKey = player;
				}
			}
		} else {
			min = 0;
		}
		if(scores.size() < 10) {
			lowestScore = 0;
		} else {
			lowestScore = min;
		}
	}

	public int getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(int highestScore) {
		this.highestScore = highestScore;
	}

	public int getLowestScore() {
		return lowestScore;
	}

	public void setLowestScore(int lowestScore) {
		this.lowestScore = lowestScore;
	}
	
	public void addHighSCore(String player, int score) {
		if(score > lowestScore) {
			if (!scores.containsKey(player) || scores.get(player) < score) {
				scores.put(player, score);
			}
			if(scores.size() > 10) {
				scores.remove(lowestKey);
			}
			refreshScores();
		}
	}
	
	public void resetHighScores() {
		scores.clear();
		refreshScores();
	}
	
	private void refreshScores() {
		scores = getSortedMap(scores);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(scoresFile));
			String line = "";
			for (String player : scores.keySet()) {
				line = player + "\t" + scores.get(player) + System.getProperty("line.separator");
				writer.write(line);
			}
			writer.flush();
		} catch (Exception e) {
			System.out.println("refresh: " + e.toString());
		}
		initHighScores();
	}
	
	public HashMap<String, Integer> getSortedMap(HashMap<String, Integer> hmap)
	{
		HashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		List<String> mapKeys = new ArrayList<String>(hmap.keySet());
		List<Integer> mapValues = new ArrayList<Integer>(hmap.values());
		
//		System.out.println(hmap);
		Collections.sort(mapValues, Collections.reverseOrder());
//		System.out.println(mapKeys);
//		System.out.println(mapValues);
//		System.out.println(hmap);
		
		for(Integer i : mapValues) {
//			System.out.println("Finding match for value " + i);
			for(String s : mapKeys) {
				if(hmap.get(s).equals(i)) {
//					System.out.println("Match found: " + s);
					map.put(s, i);
					hmap.remove(s);
					mapKeys.remove(s);
//					System.out.println(hmap);
					break;
				}
			}
		}
		
		return map;
	}
}
