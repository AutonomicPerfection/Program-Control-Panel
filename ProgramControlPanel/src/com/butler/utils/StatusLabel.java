package com.butler.utils;

import javax.swing.JLabel;

public class StatusLabel extends JLabel {
	
	private String defaultValue;
	private String name;
	private String currentStatus;
	
	
	public StatusLabel(String _name, String _defaultValue) {
		super(_name + ": " + _defaultValue);
		defaultValue = _defaultValue;
		name = _name;
	}
	
	public void setStatus(String newStatus) {
		setText(name + ": " + newStatus);
		currentStatus = newStatus;
	}
	
	public String getStatus() {
		return currentStatus;
	}
	
	public String getName() {
		return name;
	}
	
	public void resetStatus() {
		setText(name + ": " + defaultValue);
	}
}
