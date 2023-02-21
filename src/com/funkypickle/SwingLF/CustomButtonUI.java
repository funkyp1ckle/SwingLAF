package com.funkypickle.SwingLF;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class CustomButtonUI extends BasicButtonUI {
	private JButton btn;
	private RoundedBorder rb;
	private Color color;

	private boolean paintMonochrome = false;
	private boolean rolloverEnabled = true;
	private boolean clickEnabled = true;

	private boolean isRollingOver;
	private boolean isClicked;

	public static ComponentUI createUI(JComponent c) {
		return new CustomButtonUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		this.btn = ((JButton) c);
		c.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (rolloverEnabled) {
					isRollingOver = true;
					btn.repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (rolloverEnabled) {
					isRollingOver = false;
					btn.repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (clickEnabled) {
					isClicked = true;
					btn.repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (clickEnabled) {
					isClicked = false;
					btn.repaint();
				}
			}
		});
		this.rb = new RoundedBorder(CustomLF.UI_COMPONENT_ROLLOVER.brighter(), 20);
		this.color = CustomLF.UI_COMPONENT_ROLLOVER.darker();
		btn.setBorder(rb);
	}

	@Override
	public void paint(Graphics g, JComponent b) {
		Dimension d = b.getSize();
		if (isClicked) {
			g.setColor(color.brighter().brighter());
		} else if (isRollingOver) {
			g.setColor(color.brighter());
		} else {
			if (btn.isOpaque())
				g.setColor(color);
			else
				g.setColor(CustomLF.BACKGROUND);
		}
		g.fillRoundRect(btn.getX(), btn.getY(), (int) Math.ceil(d.getWidth()), (int) Math.ceil(d.getHeight()),
				rb.getRadius(), rb.getRadius());
		super.paint(g, b);
	}

	@Override
	public void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		if (btn.getIcon() != null && btn.getIcon() instanceof ImageIcon) {
			Dimension btnDimensions = btn.getSize();
			((ImageIcon) btn.getIcon()).setImage(scaleImage(((ImageIcon) btn.getIcon()).getImage(),
					(int) btnDimensions.getWidth(), (int) btnDimensions.getHeight()));
			super.paintIcon(g, c, iconRect);
			if (paintMonochrome) {
				btn.setIcon(convertToGrayScale((ImageIcon) btn.getIcon()));
			}
		} else {
			super.paintIcon(g, c, iconRect);
		}
	}

	private static Image scaleImage(Image img, int width, int height) {
		return img.getScaledInstance(width - (width / 4), height - (height / 4), Image.SCALE_SMOOTH);
	}

	private static ImageIcon convertToGrayScale(ImageIcon icon) {
		BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR_PRE);
		Graphics2D g2d = img.createGraphics();
		icon.paintIcon(null, g2d, 0, 0);
		g2d.dispose();

		ColorSpace grayColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(grayColorSpace, img.getColorModel().getColorSpace(), null);
		op.filter(img, img);

		return new ImageIcon(img);
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public boolean getRolloverEnabled() {
		return rolloverEnabled;
	}

	public void setRolloverEnabled(boolean b) {
		rolloverEnabled = b;
	}

	public boolean getClickEnabled() {
		return clickEnabled;
	}

	public void setClickEnabled(boolean b) {
		clickEnabled = b;
	}
}
