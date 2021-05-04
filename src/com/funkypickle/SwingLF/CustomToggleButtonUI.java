package com.funkypickle.SwingLF;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

public class CustomToggleButtonUI extends BasicToggleButtonUI implements ActionListener {
	private JToggleButton btn;
	private int pos = 0;

	public static ComponentUI createUI(JComponent c) {
		return new CustomToggleButtonUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		this.btn = ((JToggleButton) c);
		btn.addActionListener(this);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		c.setBounds(c.getX(), c.getY(), c.getWidth(), c.getWidth() / 2);
		Dimension d = c.getSize();

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(CustomLF.UI_COMPONENT_ROLLOVER.darker());
		g2.fillRoundRect(c.getX(), c.getY(), (int) d.getWidth(), (int) d.getHeight(), (int) d.getHeight(),
				(int) d.getHeight());
		g2.setColor(CustomLF.TEXT);
		btn.setBorder(new RoundedBorder(CustomLF.UI_COMPONENT_ROLLOVER.brighter(), (int) d.getHeight()));
		int xOffset = (int) (d.getWidth() / 2);
		if (pos == 0)
			g2.fillOval(c.getX(), c.getY(), xOffset, (int) d.getHeight());
		else if (pos == 1)
			g2.fillOval(c.getX() + xOffset, c.getY(), xOffset, (int) d.getHeight());
	}

	@Override
	public void paintIcon(Graphics g, AbstractButton b, Rectangle iconRect) {
		return;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (pos == 0)
			pos = 1;
		else if (pos == 1)
			pos = 0;
		btn.repaint();
	}
}
