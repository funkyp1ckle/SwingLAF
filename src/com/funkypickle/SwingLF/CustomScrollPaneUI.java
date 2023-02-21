package com.funkypickle.SwingLF;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class CustomScrollPaneUI extends BasicScrollPaneUI {
	public static ComponentUI createUI(JComponent c) {
		return new CustomScrollPaneUI();
	}
}
