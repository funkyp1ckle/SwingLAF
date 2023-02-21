package com.funkypickle.SwingLF;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {
	private boolean rolloverEnabled = true;
	private boolean clickEnabled = true;

	private boolean isRollingOver;
	private boolean isClicked;

	private Color color;

	public static ComponentUI createUI(JComponent c) {
		return new CustomScrollBarUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (rolloverEnabled) {
					isRollingOver = true;
					scrollbar.repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (rolloverEnabled) {
					isRollingOver = false;
					scrollbar.repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (clickEnabled) {
					isClicked = true;
					scrollbar.repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (clickEnabled) {
					isClicked = false;
					scrollbar.repaint();
				}
			}
		});
		this.color = CustomLF.UI_COMPONENT_ROLLOVER.darker();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createZeroButton();
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createZeroButton();
	}

	@Override
	public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
			return;
		}

		int w = thumbBounds.width;
		int h = thumbBounds.height;

		if (isClicked) {
			g.setColor(color.brighter().brighter().brighter());
		} else if (isRollingOver) {
			g.setColor(color.brighter().brighter());
		} else {
			if (scrollbar.isOpaque())
				g.setColor(color.brighter());
			else
				g.setColor(CustomLF.BACKGROUND);
		}

		g.fillRoundRect(thumbBounds.x, thumbBounds.y, w, h, 10, 10);
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		g.setColor(CustomLF.BACKGROUND);
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

		if (trackHighlight == DECREASE_HIGHLIGHT) {
			paintDecreaseHighlight(g);
		} else if (trackHighlight == INCREASE_HIGHLIGHT) {
			paintIncreaseHighlight(g);
		}
	}

	private static JButton createZeroButton() {
		JButton jbutton = new JButton();
		jbutton.setPreferredSize(new Dimension(0, 0));
		jbutton.setMinimumSize(new Dimension(0, 0));
		jbutton.setMaximumSize(new Dimension(0, 0));
		return jbutton;
	}
}
