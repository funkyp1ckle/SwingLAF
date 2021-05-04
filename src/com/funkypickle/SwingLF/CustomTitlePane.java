package com.funkypickle.SwingLF;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;

@SuppressWarnings("serial")
public class CustomTitlePane extends JComponent {

	private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);

	/**
	 * PropertyChangeListener added to the JRootPane.
	 */
	private PropertyChangeListener propertyChangeListener;

	/**
	 * Action used to close the Window.
	 */
	private Action closeAction;

	/**
	 * Action used to iconify the Frame.
	 */
	private Action iconifyAction;

	/**
	 * Action to restore the Frame size.
	 */
	private Action restoreAction;

	/**
	 * Action to restore the Frame size.
	 */
	private Action maximizeAction;

	/**
	 * Button used to maximize or restore the Frame.
	 */
	private JButton toggleButton;

	/**
	 * Button used to maximize or restore the Frame.
	 */
	private JButton iconifyButton;

	/**
	 * Button used to maximize or restore the Frame.
	 */
	private JButton closeButton;

	private JLabel iconWithName;

	/**
	 * Icon used for toggleButton when window is normal size.
	 */
	private Icon maximizeIcon;

	/**
	 * Icon used for toggleButton when window is maximized.
	 */
	private Icon minimizeIcon;

	/**
	 * Listens for changes in the state of the Window listener to update the state
	 * of the widgets.
	 */
	private WindowListener windowListener;

	private ComponentListener componentListener;

	/**
	 * Window we're currently in.
	 */
	private Window window;

	/**
	 * JRootPane rendering for.
	 */
	private JRootPane rootPane;
	/**
	 * Buffered Frame.state property. As state isn't bound, this is kept to
	 * determine when to avoid updating widgets.
	 */
	private int state;

	/**
	 * MetalRootPaneUI that created us.
	 */
	private CustomRootPaneUI rootPaneUI;

	public CustomTitlePane(JRootPane root, CustomRootPaneUI customRootPaneUI) {
		this.rootPane = root;
		rootPaneUI = customRootPaneUI;

		state = -1;

		installSubcomponents();
		installDefaults();

		setLayout(createLayout());
	}

	private void installListeners() {
		if (window != null) {
			windowListener = createWindowListener();
			componentListener = createComponentListener();
			propertyChangeListener = createWindowPropertyChangeListener();
			window.addWindowListener(windowListener);
			window.addComponentListener(componentListener);
			window.addPropertyChangeListener(propertyChangeListener);
		}
	}

	/**
	 * Uninstalls the necessary listeners.
	 */
	private void uninstallListeners() {
		if (window != null) {
			window.removeWindowListener(windowListener);
			window.removePropertyChangeListener(propertyChangeListener);
		}
	}

	/**
	 * Returns the <code>WindowListener</code> to add to the <code>Window</code>.
	 */
	private WindowListener createWindowListener() {
		return new WindowHandler();
	}

	private ComponentListener createComponentListener() {
		return new WindowCompHandler();
	}

	/**
	 * Returns the <code>PropertyChangeListener</code> to install on the
	 * <code>Window</code>.
	 */
	private PropertyChangeListener createWindowPropertyChangeListener() {
		return new PropertyChangeHandler();
	}

	/**
	 * Returns the <code>JRootPane</code> this was created for.
	 */
	@Override
	public JRootPane getRootPane() {
		return rootPane;
	}

	/**
	 * Returns the decoration style of the <code>JRootPane</code>.
	 */
	private int getWindowDecorationStyle() {
		return getRootPane().getWindowDecorationStyle();
	}

	@Override
	public void addNotify() {
		super.addNotify();

		uninstallListeners();

		window = SwingUtilities.getWindowAncestor(this);
		if (window != null) {
			if (window instanceof Frame) {
				Frame frame = ((Frame) window);
				setState(frame.getExtendedState());
				Icon i = CustomLF.ICON;
				String name = frame.getTitle();
				if (name != null && i != null) {
					iconWithName = new JLabel(name, i, JLabel.LEFT);
				} else if (name != null) {
					iconWithName = new JLabel(name, JLabel.LEFT);
				} else if (i != null) {
					iconWithName = new JLabel(i, JLabel.LEFT);
				}
				add(iconWithName);
			} else {
				setState(0);
			}
			setActive(window.isActive());
			installListeners();
		}
	}

	@Override
	public void removeNotify() {
		super.removeNotify();

		uninstallListeners();
		window = null;
	}

	/**
	 * Adds any sub-Components contained in the <code>MetalTitlePane</code>.
	 */
	private void installSubcomponents() {
		int decorationStyle = getWindowDecorationStyle();
		if (decorationStyle == JRootPane.FRAME) {
			createActions();
			createButtons();
			add(iconifyButton);
			add(toggleButton);
			add(closeButton);
		} else if (decorationStyle == JRootPane.PLAIN_DIALOG || decorationStyle == JRootPane.INFORMATION_DIALOG
				|| decorationStyle == JRootPane.ERROR_DIALOG || decorationStyle == JRootPane.COLOR_CHOOSER_DIALOG
				|| decorationStyle == JRootPane.FILE_CHOOSER_DIALOG || decorationStyle == JRootPane.QUESTION_DIALOG
				|| decorationStyle == JRootPane.WARNING_DIALOG) {
			createActions();
			createButtons();
			add(closeButton);
		}
	}

	/**
	 * Installs the fonts and necessary properties on the MetalTitlePane.
	 */
	private void installDefaults() {
		setFont(UIManager.getFont("InternalFrame.titleFont", getLocale()));
	}

	/**
	 * Closes the Window.
	 */
	private void close() {
		Window window = getWindow();

		if (window != null) {
			window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
		}
	}

	/**
	 * Iconifies the Frame.
	 */
	private void iconify() {
		Frame frame = getFrame();
		if (frame != null) {
			frame.setExtendedState(state | Frame.ICONIFIED);
		}
	}

	/**
	 * Maximizes the Frame.
	 */
	private void maximize() {
		Frame frame = getFrame();
		if (frame != null) {
			frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
		}
	}

	/**
	 * Restores the Frame size.
	 */
	private void restore() {
		Frame frame = getFrame();

		if (frame == null) {
			return;
		}

		if ((state & Frame.ICONIFIED) != 0) {
			frame.setExtendedState(state & ~Frame.ICONIFIED);
		} else {
			frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
		}
	}

	/**
	 * Create the <code>Action</code>s that get associated with the buttons and menu
	 * items.
	 */
	private void createActions() {
		closeAction = new CloseAction();
		if (getWindowDecorationStyle() == JRootPane.FRAME) {
			iconifyAction = new IconifyAction();
			restoreAction = new RestoreAction();
			maximizeAction = new MaximizeAction();
		}
	}

	static int getInt(Object key, int defaultValue) {
		Object value = UIManager.get(key);

		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		}
		if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException nfe) {
			}
		}
		return defaultValue;
	}

	/**
	 * Returns a <code>JButton</code> appropriate for placement on the TitlePane.
	 */
	private static JButton createTitleButton() {
		JButton button = new JButton();
		if (button.getUI() instanceof CustomButtonUI) {
			CustomButtonUI btnUI = ((CustomButtonUI) button.getUI());
			btnUI.setClickEnabled(false);
			btnUI.setRolloverEnabled(false);
		}
		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setOpaque(false);
		return button;
	}

	/**
	 * Creates the Buttons that will be placed on the TitlePane.
	 */
	private void createButtons() {
		closeButton = createTitleButton();
		closeButton.setAction(closeAction);
		closeButton.setText(null);
		closeButton.putClientProperty("paintActive", Boolean.TRUE);
		closeButton.setBorder(handyEmptyBorder);
		closeButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Close");
		closeButton.setIcon(new ImageIcon("res/close.png"));

		if (getWindowDecorationStyle() == JRootPane.FRAME) {
			maximizeIcon = new ImageIcon("res/maximize.png");
			minimizeIcon = new ImageIcon("res/minimize.png");

			iconifyButton = createTitleButton();
			iconifyButton.setAction(iconifyAction);
			iconifyButton.setText(null);
			iconifyButton.putClientProperty("paintActive", Boolean.TRUE);
			iconifyButton.setBorder(handyEmptyBorder);
			iconifyButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Iconify");
			iconifyButton.setIcon(minimizeIcon);

			toggleButton = createTitleButton();
			toggleButton.setAction(restoreAction);
			toggleButton.putClientProperty("paintActive", Boolean.TRUE);
			toggleButton.setBorder(handyEmptyBorder);
			toggleButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Maximize");
			toggleButton.setIcon(maximizeIcon);
		}
	}

	/**
	 * Returns the <code>LayoutManager</code> that should be installed on the
	 * <code>MetalTitlePane</code>.
	 */
	private LayoutManager createLayout() {
		return new TitlePaneLayout();
	}

	/**
	 * Updates state dependent upon the Window's active state.
	 */
	private void setActive(boolean isActive) {
		Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;

		closeButton.putClientProperty("paintActive", activeB);
		if (getWindowDecorationStyle() == JRootPane.FRAME) {
			iconifyButton.putClientProperty("paintActive", activeB);
			toggleButton.putClientProperty("paintActive", activeB);
		}
		// Repaint the whole thing as the Borders that are used have
		// different colors for active vs inactive
		getRootPane().repaint();
	}

	/**
	 * Sets the state of the Window.
	 */
	private void setState(int state) {
		setState(state, false);
	}

	/**
	 * Sets the state of the window. If <code>updateRegardless</code> is true and
	 * the state has not changed, this will update anyway.
	 */
	private void setState(int state, boolean updateRegardless) {
		Window w = getWindow();

		if (w != null && getWindowDecorationStyle() == JRootPane.FRAME) {
			if (this.state == state && !updateRegardless) {
				return;
			}
			Frame frame = getFrame();

			if (frame != null) {
				JRootPane rootPane = getRootPane();

				if (((state & Frame.MAXIMIZED_BOTH) != 0)
						&& (rootPane.getBorder() == null || (rootPane.getBorder() instanceof UIResource))
						&& frame.isShowing()) {
					rootPane.setBorder(null);
				} else if ((state & Frame.MAXIMIZED_BOTH) == 0) {
					// This is a croak, if state becomes bound, this can
					// be nuked.
					rootPaneUI.installBorder(rootPane);
				}
				if (frame.isResizable()) {
					if ((state & Frame.MAXIMIZED_BOTH) != 0) {
						updateToggleButton(restoreAction, maximizeIcon);
						maximizeAction.setEnabled(false);
						restoreAction.setEnabled(true);
					} else {
						updateToggleButton(maximizeAction, maximizeIcon);
						maximizeAction.setEnabled(true);
						restoreAction.setEnabled(false);
					}
					if (toggleButton.getParent() == null || iconifyButton.getParent() == null) {
						add(toggleButton);
						add(iconifyButton);
						revalidate();
						repaint();
					}
					toggleButton.setText(null);
				} else {
					maximizeAction.setEnabled(false);
					restoreAction.setEnabled(false);
					if (toggleButton.getParent() != null) {
						remove(toggleButton);
						revalidate();
						repaint();
					}
				}
			} else {
				// Not contained in a Frame
				maximizeAction.setEnabled(false);
				restoreAction.setEnabled(false);
				iconifyAction.setEnabled(false);
				remove(toggleButton);
				remove(iconifyButton);
				revalidate();
				repaint();
			}
			closeAction.setEnabled(true);
			this.state = state;
		}
	}

	/**
	 * Updates the toggle button to contain the Icon <code>icon</code>, and Action
	 * <code>action</code>.
	 */
	private void updateToggleButton(Action action, Icon icon) {
		toggleButton.setAction(action);
		toggleButton.setIcon(icon);
		toggleButton.setText(null);
	}

	/**
	 * Returns the Frame rendering in. This will return null if the
	 * <code>JRootPane</code> is not contained in a <code>Frame</code>.
	 */
	private Frame getFrame() {
		Window window = getWindow();

		if (window instanceof Frame) {
			return (Frame) window;
		}
		return null;
	}

	/**
	 * Returns the <code>Window</code> the <code>JRootPane</code> is contained in.
	 * This will return null if there is no parent ancestor of the
	 * <code>JRootPane</code>.
	 */
	private Window getWindow() {
		return window;
	}

	/**
	 * Renders the TitlePane.
	 */
	@Override
	public void paintComponent(Graphics g) {
		// As state isn't bound, we need a convenience place to check
		// if it has changed. Changing the state typically changes the
		if (getFrame() != null) {
			setState(getFrame().getExtendedState());
		}
		int width = getWidth();
		int height = getHeight();

		g.setColor(CustomLF.BACKGROUND.darker());
		g.fillRect(0, 0, width, height);

		g.setColor(CustomLF.BACKGROUND.brighter());
		g.drawLine(0, height - 1, width, height - 1);
		g.drawLine(0, 0, 0, 0);
		g.drawLine(width - 1, 0, width - 1, 0);
	}

	/**
	 * Actions used to <code>close</code> the <code>Window</code>.
	 */
	private class CloseAction extends AbstractAction {
		public CloseAction() {
			super(UIManager.getString("MetalTitlePane.closeTitle", getLocale()));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
	}

	/**
	 * Actions used to <code>iconfiy</code> the <code>Frame</code>.
	 */
	private class IconifyAction extends AbstractAction {
		public IconifyAction() {
			super(UIManager.getString("MetalTitlePane.iconifyTitle", getLocale()));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			iconify();
		}
	}

	/**
	 * Actions used to <code>restore</code> the <code>Frame</code>.
	 */
	private class RestoreAction extends AbstractAction {
		public RestoreAction() {
			super(UIManager.getString("MetalTitlePane.restoreTitle", getLocale()));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			restore();
		}
	}

	/**
	 * Actions used to <code>restore</code> the <code>Frame</code>.
	 */
	private class MaximizeAction extends AbstractAction {
		public MaximizeAction() {
			super(UIManager.getString("MetalTitlePane.maximizeTitle", getLocale()));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			maximize();
		}
	}

	private class TitlePaneLayout implements LayoutManager {
		@Override
		public void addLayoutComponent(String name, Component c) {
		}

		@Override
		public void removeLayoutComponent(Component c) {
		}

		@Override
		public Dimension preferredLayoutSize(Container c) {
			int height = 20;
			return new Dimension(height, height);
		}

		@Override
		public Dimension minimumLayoutSize(Container c) {
			return preferredLayoutSize(c);
		}

		@Override
		public void layoutContainer(Container c) {
			boolean leftToRight = (window == null) ? getRootPane().getComponentOrientation().isLeftToRight()
					: window.getComponentOrientation().isLeftToRight();

			int w = getWidth();
			int x = 5;
			int y = 0;
			int spacing;
			int buttonHeight = 20;
			int buttonWidth = 20;

			if (iconWithName != null) {

				iconWithName.setBounds(x, y, 100, buttonHeight);
			}
			x = leftToRight ? w : 0;
			spacing = 10;
			x += leftToRight ? -spacing - buttonWidth : spacing;
			if (closeButton != null) {
				closeButton.setBounds(x, y, buttonWidth, buttonHeight);
			}

			if (!leftToRight)
				x += buttonWidth;

			if (getWindowDecorationStyle() == JRootPane.FRAME) {
				if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
					if (toggleButton.getParent() != null) {
						x += leftToRight ? -spacing - buttonWidth : spacing;
						toggleButton.setBounds(x, y, buttonWidth, buttonHeight);
						if (!leftToRight) {
							x += buttonWidth;
						}
					}
				}

				if (iconifyButton != null && iconifyButton.getParent() != null) {
					x += leftToRight ? -spacing - buttonWidth : spacing;
					iconifyButton.setBounds(x, y, buttonWidth, buttonHeight);
					if (!leftToRight) {
						x += buttonWidth;
					}
				}
			}
		}
	}

	/**
	 * PropertyChangeListener installed on the Window. Updates the necessary state
	 * as the state of the Window changes.
	 */
	private class PropertyChangeHandler implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent pce) {
			String name = pce.getPropertyName();

			// Frame.state isn't currently bound.
			if ("resizable".equals(name) || "state".equals(name)) {
				Frame frame = getFrame();

				if (frame != null) {
					setState(frame.getExtendedState(), true);
				}
				if ("resizable".equals(name)) {
					getRootPane().repaint();
				}
			} else if ("title".equals(name)) {
				repaint();
			} else if ("componentOrientation" == name) {
				revalidate();
				repaint();
			}
		}

	}

	/**
	 * WindowListener installed on the Window, updates the state as necessary.
	 */
	private class WindowHandler extends WindowAdapter {
		@Override
		public void windowOpened(WindowEvent e) {
			window.setShape(new RoundRectangle2D.Float(0, 0, window.getWidth(), window.getHeight(), 5, 5));
		}

		@Override
		public void windowActivated(WindowEvent ev) {
			setActive(true);
		}

		@Override
		public void windowDeactivated(WindowEvent ev) {
			setActive(false);
		}
	}

	private class WindowCompHandler extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			window.setShape(new RoundRectangle2D.Float(0, 0, window.getWidth(), window.getHeight(), 5, 5));
		}
	}
}