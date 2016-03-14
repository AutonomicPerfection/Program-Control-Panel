package com.butler.robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.butler.utils.ConsolePanel;
import com.butler.utils.StatusLabel;
import com.butler.utils.StatusPanel;
import com.butler.utils.TextFieldInput;
import com.butler.utils.TextInputEvent;
import com.butler.utils.TextInputListener;

@SuppressWarnings("serial")
public class RobotArmGui extends JFrame implements TextInputListener, ActionListener, FocusListener {
	private RobotArm parent;
	private ConsolePanel console;
	private TextFieldInput input;
	private JButton restart = new JButton("Reset Autopilot");
	private JPanel cheatSheet = new JPanel();
	private JLabel xPlus = new JLabel("Up: Up Arrow Key");
	private JLabel xMinus = new JLabel("Down: Down Arrow Key");
	private JLabel yPlus = new JLabel("Right: Right Arrow Key");
	private JLabel yMinus = new JLabel("Left: Left Arrow Key");
	private JLabel zPlus = new JLabel("Forward: W");
	private JLabel zMinus = new JLabel("Backward: S");
	private JLabel aPlus = new JLabel("Drum Right: D");
	private JLabel aMinus = new JLabel("Drum Left: A");
	private JLabel bPlus = new JLabel("Drum Clockwise: R");
	private JLabel bMinus = new JLabel("Drum Counterclockwise: T");
	
	public JPanel controlPanel = new JPanel();
	public JLabel stepModeStatus = new JLabel("Step Mode: Full");
	public StatusLabel overrideStatus = new StatusLabel("Mode", "Manual Control (Keybindings)");
	public JLabel autopilotCounterStatus = new JLabel("Autopilot sequence: 0");
	
	public RobotArmGui(RobotArm _parent) {
		super("Robot Arm Gui");
		
		parent = _parent;
		
		GridLayout layout = new GridLayout(10, 2);
		layout.setHgap(20);
		layout.setVgap(20);
		restart.addActionListener(this);
		controlPanel.setLayout(layout);
		controlPanel.add(stepModeStatus);
		controlPanel.add(autopilotCounterStatus);
		controlPanel.add(restart);
		
		console = new ConsolePanel(10, 30);
		console.outputArea.addKeyListener(parent);
		console.outputArea.addFocusListener(this);
		
		input = new TextFieldInput("Desired movement vector:    ", 20);
		input.addTextInputListener(this);
		input.getTextField().addFocusListener(this);
		
		overrideStatus.setPreferredSize(new Dimension(50, 50));
		
		//Cheat sheet
		GridLayout cheatSheetLayout = new GridLayout(0, 2);
		cheatSheetLayout.setHgap(1);
		cheatSheetLayout.setVgap(1);
		cheatSheet.setLayout(cheatSheetLayout);
		cheatSheet.add(xPlus);
		cheatSheet.add(xMinus);
		cheatSheet.add(yMinus);
		cheatSheet.add(yPlus);
		cheatSheet.add(zPlus);
		cheatSheet.add(zMinus);
		cheatSheet.add(aMinus);
		cheatSheet.add(aPlus);
		cheatSheet.add(bPlus);
		cheatSheet.add(bMinus);
		cheatSheet.setBorder(BorderFactory.createEmptyBorder(0, 10, 30, 20));
		
		setSize(900, 500);
		setLayout(new BorderLayout());
		add(overrideStatus, BorderLayout.NORTH);
		add(console, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
		add(controlPanel, BorderLayout.WEST);
		add(cheatSheet, BorderLayout.EAST);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void input(TextInputEvent event) {
		String[] stringParams = event.getInput().split(",");
		int[] params = new int[5];
		for (int i = 0; i < stringParams.length; i++) {
			params[i] = Integer.parseInt(stringParams[i]);
		}
		if (stringParams.length < 5) {
			for (int i = stringParams.length; i < 5; i++) {
				params[i] = 0;
			}
		}
		parent.moveArmAsynch(params[0], params[1], params[2], params[3], params[4], false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(restart)) {
			parent.resetAutopilot();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		System.out.println("Focus gained from " + e.getComponent().toString());
		if (e.getComponent().equals(console.outputArea) && !parent.isManualOverride()) {
			overrideStatus.setStatus("Manual Control (Keybindings)");
		} else if (e.getComponent() && !parent.isManualOverride()) {
			overrideStatus.setStatus("Manual Control (Vector Input)");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
}
