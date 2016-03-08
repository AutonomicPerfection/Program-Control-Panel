package com.butler.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.butler.utils.ConsolePanel;

//import guru.ttslib.TTS;

@SuppressWarnings("serial")
public class ControlPanelFrame extends JFrame{
	ProgramHub parent;
	JScrollPane programScroller;
	JPanel programSelectorPanel = new JPanel();
	ArrayList<ProgramSelectionButton> buttonList = new ArrayList<ProgramSelectionButton>();
	//TTS tts;
	private boolean shouldSpeak = true;
	public ControlPanelFrame(ProgramHub parent) {
		super("Future Technologies Program Hub");
		//tts = new TTS();
		//tts.setPitchShift((float) 2.05);
		//tts.setPitch((float) 0.1);
		setLayout(new GridLayout(0, 1));
		add(new ConsolePanel(5, 30));
		this.parent = parent;
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		if (this.parent.programList.size() == 0) {
			System.err.println("No programs found. Add them to the Programs.properties file in Resources.");
		} else if (this.parent.programList.size() < 4)
			programSelectorPanel.setLayout(new GridLayout(0, this.parent.programList.size()));
		else
			programSelectorPanel.setLayout(new GridLayout(0, 4));
		for (int i = 0; i < this.parent.programList.size(); i++) {
			Method getName = null;
			try {
				getName = this.parent.programList.get(i).getMethod("getName", (Class<?>[]) null);
			} catch (Exception e) {
				
			}
			try {
				buttonList.add(new ProgramSelectionButton(this, this.parent.programList.get(i).getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			buttonList.get(i).setPreferredSize(new Dimension(120, 120));
			programSelectorPanel.add(buttonList.get(i));
		}
		programScroller = new JScrollPane(programSelectorPanel);
		programScroller.setBorder(BorderFactory.createEmptyBorder(0, 10, 100, 10));
		add(programScroller);
		setVisible(true);
		println("Welcome to the Future Technologies Program Hub.\n"
				+ "Click on any button below to launch the corresponding program.");
	}
	
	public boolean isSpeakOn() {
		return shouldSpeak;
	}
	
	public void setSpeakOn(boolean value) {
		shouldSpeak = value;
	}
	
	public void println(Object message) {
		System.out.println(message);
		//if (shouldSpeak)
			//tts.speak(message.toString());
	}
	
}
