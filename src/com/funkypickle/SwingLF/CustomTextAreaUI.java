package com.funkypickle.SwingLF;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;

public class CustomTextAreaUI extends BasicTextAreaUI {
	public static ComponentUI createUI(JComponent c) {
		return new CustomTextAreaUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setBorder(new RoundedBorder(CustomLF.TEXT, 20));
		c.setBackground(CustomLF.BACKGROUND);
	}
}
