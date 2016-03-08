package com.butler.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OutputPanel extends JPanel{
	public JTextArea outputArea;
	public JScrollPane scroll;
	public PrintStream out;
	
	public OutputPanel(int width, int height) {
		super();
		outputArea = new JTextArea(width, height);
		out = new PrintStream(new TextAreaOutputStream(outputArea));
		scroll = new JScrollPane(outputArea);
		add(scroll);
	}
}
