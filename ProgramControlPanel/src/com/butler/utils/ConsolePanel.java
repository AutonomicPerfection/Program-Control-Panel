package com.butler.utils;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConsolePanel extends OutputPanel{	
	public ConsolePanel(int width, int height) {
		super(width, height);
		outputArea.setLineWrap(true);
		outputArea.setEditable(false);
		System.setOut(out);
	}
}
