package com.mancala;

import java.awt.Color;
import java.awt.GradientPaint;

public class Bean {
	private double relativeX;
	private double relativeY;
	int red = 255;
	int blue = 81;
	int green = 71;
	int modderX = 0;
	int modderY = 0;

	public Bean(double x, double y) {
		this.relativeX = x;
		this.relativeY = y;
	}

	public double getX() {
		return relativeX;
	}

	public void setX(double x) {
		this.relativeX = x;
	}

	public double getY() {
		return relativeY;
	}

	public void setY(double y) {
		this.relativeY = y;
	}
	
	public int[] getColorBase()
	{
		int[] colors = {red, green, blue, modderX, modderY};
		return colors;
	}
	
	public void setColorBase(int[] colors)
	{
		red = colors[0];
		green = colors[1];
		blue = colors[2];
		
		modderX = colors[3];
		modderY = colors[4];
	}
	
	public GradientPaint getColor(int x, int y)
	{
		return new GradientPaint(x + 2, y + 2, new Color(red, green, blue), x + 13, y + 13, new Color((red-116>0)? red-116 : 0, (green-72>0)? green-72 : 0, (blue-71>0)? blue-71 : 0), false);
	}

	public double distanceFrom(Bean bean, int width, int height) {
		double dx = 1.0 * (this.relativeX - bean.relativeX) * width;
		double dy = 1.0 * (this.relativeY - bean.relativeY) * height;
//		System.out.println(dx);
//		System.out.println(dy);
		double dist =  Math.sqrt(dx * dx + dy * dy);
//		System.out.println(dist);
		return dist;
	}

}
