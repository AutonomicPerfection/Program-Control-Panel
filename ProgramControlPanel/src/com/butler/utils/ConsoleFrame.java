package com.butler.utils;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ConsoleFrame extends JFrame {
	public ConsolePanel console;
	
	public ConsoleFrame() {
		super("Console");
		console = new ConsolePanel(20, 50);
		add(console);
		pack();
		setVisible(true);
	}
}
