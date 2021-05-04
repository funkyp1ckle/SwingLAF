package com.funkypickle.SwingLF;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;

public class CustomLF extends BasicLookAndFeel {
	public static final ColorUIResource BACKGROUND = new ColorUIResource(30, 30, 30);
	public static final ColorUIResource TEXT = new ColorUIResource(200, 200, 200);
	public static final ColorUIResource UI_COMPONENT_ROLLOVER = new ColorUIResource(50, 50, 50);
	private static final long serialVersionUID = 5850079142893016105L;

	protected static ColorUIResource ACCENT_COLOR;
	protected static Icon ICON;

	public CustomLF() {
		this(new ColorUIResource(255, 255, 255));
	}

	public CustomLF(ColorUIResource accent) {
		this(accent, null);
	}

	public CustomLF(Icon i) {
		this(new ColorUIResource(255, 255, 255), i);
	}

	public CustomLF(ColorUIResource accent, Icon i) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		CustomLF.ACCENT_COLOR = accent;
		CustomLF.ICON = i;
	}

	@Override
	protected void initClassDefaults(UIDefaults table) {
		String packageName = "com.funkypickle.SwingLF.";
		String basicPackageName = "javax.swing.plaf.basic.";
		Object[] uiDefaults = { "ButtonUI", packageName + "CustomButtonUI", "CheckBoxUI",
				basicPackageName + "BasicCheckBoxUI", "ColorChooserUI", basicPackageName + "BasicColorChooserUI",
				"FormattedTextFieldUI", basicPackageName + "BasicFormattedTextFieldUI", "MenuBarUI",
				basicPackageName + "BasicMenuBarUI", "MenuUI", basicPackageName + "BasicMenuUI", "MenuItemUI",
				basicPackageName + "BasicMenuItemUI", "CheckBoxMenuItemUI",
				basicPackageName + "BasicCheckBoxMenuItemUI", "RadioButtonMenuItemUI",
				basicPackageName + "BasicRadioButtonMenuItemUI", "RadioButtonUI",
				basicPackageName + "BasicRadioButtonUI", "ToggleButtonUI", packageName + "CustomToggleButtonUI",
				"PopupMenuUI", basicPackageName + "BasicPopupMenuUI", "ProgressBarUI",
				basicPackageName + "BasicProgressBarUI", "ScrollBarUI", packageName + "CustomScrollBarUI",
				"ScrollPaneUI", packageName + "CustomScrollPaneUI", "SplitPaneUI",
				basicPackageName + "BasicSplitPaneUI", "SliderUI", packageName + "CustomSliderUI", "SeparatorUI",
				basicPackageName + "BasicSeparatorUI", "SpinnerUI", basicPackageName + "BasicSpinnerUI",
				"ToolBarSeparatorUI", basicPackageName + "BasicToolBarSeparatorUI", "PopupMenuSeparatorUI",
				basicPackageName + "BasicPopupMenuSeparatorUI", "TabbedPaneUI", basicPackageName + "BasicTabbedPaneUI",
				"TextAreaUI", packageName + "CustomTextAreaUI", "TextFieldUI", packageName + "CustomTextFieldUI",
				"PasswordFieldUI", basicPackageName + "BasicPasswordFieldUI", "TextPaneUI",
				basicPackageName + "BasicTextPaneUI", "EditorPaneUI", basicPackageName + "BasicEditorPaneUI", "TreeUI",
				basicPackageName + "BasicTreeUI", "LabelUI", basicPackageName + "BasicLabelUI", "ListUI",
				basicPackageName + "BasicListUI", "ToolBarUI", basicPackageName + "BasicToolBarUI", "ToolTipUI",
				basicPackageName + "BasicToolTipUI", "ComboBoxUI", basicPackageName + "BasicComboBoxUI", "TableUI",
				basicPackageName + "BasicTableUI", "TableHeaderUI", basicPackageName + "BasicTableHeaderUI",
				"InternalFrameUI", basicPackageName + "BasicInternalFrameUI", "DesktopPaneUI",
				basicPackageName + "BasicDesktopPaneUI", "DesktopIconUI", basicPackageName + "BasicDesktopIconUI",
				"FileChooserUI", basicPackageName + "BasicFileChooserUI", "OptionPaneUI",
				basicPackageName + "BasicOptionPaneUI", "PanelUI", basicPackageName + "BasicPanelUI", "ViewportUI",
				basicPackageName + "BasicViewportUI", "RootPaneUI", packageName + "CustomRootPaneUI", };

		table.putDefaults(uiDefaults);
	}

	@Override
	protected void initSystemColorDefaults(UIDefaults table) {
		String[] colors = { "desktop", "#005C5C", /* Color of the desktop background */
				"activeCaption", "#1e1e1e", /* Color for captions (title bars) when they are active. */
				"activeCaptionText", "#FFFFFF", /* Text color for text in captions (title bars). */
				"activeCaptionBorder", "#1e1e1e", /* Border color for caption (title bar) window borders. */
				"inactiveCaption", "#1e1e1e", /* Color for captions (title bars) when not active. */
				"inactiveCaptionText", "#FFFFFF", /* Text color for text in inactive captions (title bars). */
				"inactiveCaptionBorder", "#1e1e1e", /* Border color for inactive caption (title bar) window borders. */
				"window", "#1e1e1e", /* Default color for the interior of windows */
				"windowBorder", "#1e1e1e", /* ??? */
				"windowText", "#FFFFFF", /* ??? */
				"menu", "#1e1e1e", /* Background color for menus */
				"menuText", "#FFFFFF", /* Text color for menus */
				"text", "#1e1e1e", /* Text background color */
				"textText", "#FFFFFF", /* Text foreground color */
				"textHighlight", "#344ceb", /* Text background color when selected */
				"textHighlightText", "#FFFFFF", /* Text color when selected */
				"textInactiveText", "#4b4b4b", /* Text color when disabled */
				"control", "#1e1e1e", /* Default color for controls (buttons, sliders, etc) */
				"controlText", "#FFFFFF", /* Default color for text in controls */
				"controlHighlight", "#1e1e1e", /* Specular highlight (opposite of the shadow) */
				"controlLtHighlight", "#1e1e1e", /* Highlight color for controls */
				"controlShadow", "#808080", /* Shadow color for controls */
				"controlDkShadow", "#000000", /* Dark shadow color for controls */
				"scrollbar", "#4b4b4b", /* Scrollbar background (usually the "track") */
				"info", "#1e1e1e", /* ??? */
				"infoText", "#FFFFFF" /* ??? */
		};
		loadSystemColors(table, colors, false);
	}

	@Override
	public String getName() {
		return "funkypickle's L&F";
	}

	@Override
	public String getID() {
		return "funkypickle";
	}

	@Override
	public String getDescription() {
		return "funkypickle's L&F";
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}

	@Override
	public boolean getSupportsWindowDecorations() {
		return true;
	}

}
