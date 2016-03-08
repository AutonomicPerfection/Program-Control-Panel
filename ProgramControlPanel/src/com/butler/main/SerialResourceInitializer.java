package com.butler.main;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.butler.utils.OutputPanel;



public class SerialResourceInitializer implements ResourceInitializer {
	JPanel gui = new JPanel();
	JLabel label = new JLabel("Serial Status");
	OutputPanel output = new OutputPanel(50, 50);
	
	
	public SerialResourceInitializer() {
		
	}
	
	
	
	@Override
	public void initializeResource(int index) {
		SystemVariables.addResource(index, new SerialResource(output.out));
	}
	
	public JPanel createGui() {
		gui.add(label);
		gui.add(output);
		return gui;
	}
		
}
