package com.butler.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ProgramSelectionButton extends JButton implements ActionListener{
	ControlPanelFrame frame;
	public ProgramSelectionButton(ControlPanelFrame _frame, String programName) {
		super(programName);
		frame = _frame;
		addActionListener(this);
		setToolTipText(programName);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0){
		if (frame.parent.programResources.get(frame.buttonList.indexOf(this)) != null) {
			Method setResources = null;
			Class<?> programClass = frame.parent.programList.get(frame.buttonList.indexOf(this));
			try {
				setResources = programClass.getMethod("setResources", Resource[].class);
				setResources.invoke(null, new Object[] {frame.parent.programResources.get(frame.buttonList.indexOf(this))});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Start the program
		Method mainMethod = null;
		try {
			mainMethod = frame.parent.programList.get(frame.buttonList.indexOf(this)).getMethod("main", String[].class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mainMethod.invoke(null, new Object[] {new String[]{""}});
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
