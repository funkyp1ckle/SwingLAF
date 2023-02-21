package com.funkypickle.SwingLF;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class CustomTextFieldUI extends BasicTextFieldUI {
	public static ComponentUI createUI(JComponent c) {
		return new CustomTextFieldUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setBorder(new RoundedBorder(CustomLF.TEXT, 20));
		c.setBackground(CustomLF.BACKGROUND);
	}
}
