package com.butler.utils;

import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.JPanel;

public class StatusPanel extends JPanel {
	
	private Hashtable<String, StatusLabel> statusTable = new Hashtable<String, StatusLabel>();
	
	public StatusPanel(int rows, int columns) {
		setLayout(new GridLayout(rows, columns));
	}
	
	public StatusPanel(int rows, int columns, Hashtable<String, StatusLabel> newStatusTable) {
		new StatusPanel(rows, columns);
		setStatusLabels(newStatusTable);
	}
	
	public void setStatusLabels(Hashtable<String, StatusLabel> newTable) {
		statusTable = newTable;
		removeAll();
		for (Entry<String, StatusLabel> currentEntry : statusTable.entrySet()) {
			add(currentEntry.getValue());
		}
	}
	
	public void addStatusLabel(StatusLabel newLabel) {
		statusTable.put(newLabel.getName(), newLabel);
		add(newLabel);
	}
	
	public void removeLabel(StatusLabel target) {
		remove(target);
		statusTable.remove(target.getName());
	}
	
	public StatusLabel getStatusLabel(String name) {
		return statusTable.get(name);
	}
	
}
