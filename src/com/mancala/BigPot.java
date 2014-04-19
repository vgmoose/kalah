package com.mancala;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public class BigPot extends Pot {
	
	private static final long serialVersionUID = -9024340637590238213L;
	
	public BigPot() {
		super(true);
		this.setPreferredSize(new Dimension(120,300));
	}

	@Override
	protected void initBeans() {
		beans = Collections.synchronizedList(new ArrayList<Bean>());
		refresh();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(pit.owner.getName(), g);
		int textWidth = (int) rect.getWidth();
		g.drawString(pit.owner.getName(), (getWidth() - textWidth) / 2, 20);
	}
	
	@Override
	protected void createListener() {}

}
