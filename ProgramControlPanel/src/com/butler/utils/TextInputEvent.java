package com.butler.utils;

import java.util.EventObject;

public class TextInputEvent extends EventObject{
	private String input;
	
	public TextInputEvent(Object source, String _input) {
		super(source);
		input = _input;
	}
	
	public String getInput() {
		return input;
	}
	
}