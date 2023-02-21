package com.funkypickle.SwingLF;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

public class CustomSliderUI extends BasicSliderUI {

	private int TRACK_HEIGHT = 6;
	private int TRACK_WIDTH = 6;
	private int TRACK_ARC = 2;
	private Dimension THUMB_SIZE = new Dimension(TRACK_HEIGHT * 2, TRACK_HEIGHT * 2);
	private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();

	public CustomSliderUI(JSlider b) {
		super(b);
	}

	public static ComponentUI createUI(JComponent c) {
		return new CustomSliderUI((JSlider) c);
	}

	@Override
	protected void calculateTrackRect() {
		super.calculateTrackRect();
		if (isHorizontal()) {
			trackRect.y = trackRect.y + (trackRect.height - TRACK_HEIGHT) / 2;
			trackRect.height = TRACK_HEIGHT;
		} else {
			trackRect.x = trackRect.x + (trackRect.width - TRACK_WIDTH) / 2;
			trackRect.width = TRACK_WIDTH;
		}
		trackShape.setRoundRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height, TRACK_ARC, TRACK_ARC);
	}

	@Override
	protected void calculateThumbLocation() {
		super.calculateThumbLocation();
		if (isHorizontal()) {
			thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
		} else {
			thumbRect.x = trackRect.x + (trackRect.width - thumbRect.width) / 2;
		}
	}

	@Override
	protected Dimension getThumbSize() {
		return THUMB_SIZE;
	}

	private boolean isHorizontal() {
		return slider.getOrientation() == JSlider.HORIZONTAL;
	}

	@Override
	public void paintTrack(final Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		Shape clip = g2.getClip();

		boolean horizontal = isHorizontal();
		boolean inverted = slider.getInverted();

		// Paint track background.
		g2.setColor(CustomLF.UI_COMPONENT_ROLLOVER);
		g2.setClip(trackShape);
		trackShape.y += 1;
		g2.fill(trackShape);
		trackShape.y = trackRect.y;

		g2.setClip(clip);

		// Paint selected track.
		if (horizontal) {
			boolean ltr = slider.getComponentOrientation().isLeftToRight();
			if (ltr)
				inverted = !inverted;
			int thumbPos = thumbRect.x + thumbRect.width / 2;
			if (inverted) {
				g2.clipRect(0, 0, thumbPos, slider.getHeight());
			} else {
				g2.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
			}

		} else {
			int thumbPos = thumbRect.y + thumbRect.height / 2;
			if (inverted) {
				g2.clipRect(0, 0, slider.getHeight(), thumbPos);
			} else {
				g2.clipRect(0, thumbPos, slider.getWidth(), slider.getHeight() - thumbPos);
			}
		}
		g2.setColor(CustomLF.TEXT.darker());
		g2.fill(trackShape);
		g2.setClip(clip);
	}

	@Override
	public void paintThumb(final Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(CustomLF.TEXT);
		g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
	}

	@Override
	public void paintFocus(final Graphics g) {
	}
}
