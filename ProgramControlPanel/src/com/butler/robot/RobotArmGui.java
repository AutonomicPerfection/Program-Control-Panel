package com.butler.robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.butler.utils.ConsolePanel;
import com.butler.utils.TextFieldInput;
import com.butler.utils.TextInputEvent;
import com.butler.utils.TextInputListener;

public class RobotArmGui extends JFrame implements TextInputListener, ActionListener{
	private RobotArm parent;
	private ConsolePanel console;
	private TextFieldInput input;
	private JButton restart = new JButton("Reset Autopilot");
	
	public JPanel controlPanel = new JPanel();
	public JLabel stepModeStatus = new JLabel("Step Mode: Full");
	public JLabel overrideStatus = new JLabel("Mode: Manual Control");
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
		
		input = new TextFieldInput("Desired movement vector:    ", 20);
		input.addTextInputListener(this);
		
		overrideStatus.setPreferredSize(new Dimension(50, 50));
		
		setSize(700, 500);
		setLayout(new BorderLayout());
		add(overrideStatus, BorderLayout.NORTH);
		add(console, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
		add(controlPanel, BorderLayout.WEST);
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
		parent.moveArm(params[0], params[1], params[2], params[3], params[4], false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(restart)) {
			parent.resetAutopilot();
		}
	}
}
