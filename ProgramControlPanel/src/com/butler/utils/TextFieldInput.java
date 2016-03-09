package com.butler.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.beans.property.adapter.ReadOnlyJavaBeanStringProperty;
import processing.core.PApplet;

public class TextFieldInput extends JPanel implements KeyListener, ActionListener{
	private JTextField txtField;
	private JButton enter = new JButton("Enter");
	private volatile String input = "";
	private boolean isInputConsumed = true; 
	private PApplet parent = new PApplet();
	private ArrayList<TextInputListener> listeners = new ArrayList<TextInputListener>();
	
	public TextFieldInput(int size) {
		super();
		txtField = new JTextField(size);
		txtField.addKeyListener(this);
		enter.addActionListener(this);
		add(txtField);
		add(enter);
	}
	
	public void addTextInputListener(TextInputListener _listener) {
		listeners.add(_listener);
	}
	
	public String readString() {
		while (isInputConsumed) parent.delay(10);
		isInputConsumed = true;
		return input;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyTyped(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			input = txtField.getText();
			isInputConsumed = false;
			for (TextInputListener current : listeners) {
				current.input(new TextInputEvent(this, input));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		input = txtField.getText();
		isInputConsumed = false;
		for (TextInputListener current : listeners) {
			current.input(new TextInputEvent(this, input));
		}
	}
}
