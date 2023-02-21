package com.funkypickle.SwingLF;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;

public class RoundedBorder implements Border {

	private int thickness;
	private int radius;
	private Color color;

	RoundedBorder(int radius) {
		this(Color.WHITE, radius);
	}

	RoundedBorder(Color c, int radius) {
		this(c, radius, 3);
	}

	RoundedBorder(Color c, int radius, int thickness) {
		this.thickness = thickness;
		this.radius = radius;
		this.color = c;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		int val = this.radius + this.thickness;
		return new Insets(val, val, val, val);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	public Color getColor() {
		return this.color;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int t) {
		this.thickness = t;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(color);
		g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
	}
}