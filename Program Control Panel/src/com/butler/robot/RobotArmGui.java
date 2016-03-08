package com.butler.robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.butler.utils.ConsolePanel;

public class RobotArmGui extends JFrame {
	RobotArm parent;
	ConsolePanel console;
	public JLabel overrideStatus = new JLabel("Mode: Manual Control");
	public RobotArmGui(RobotArm _parent) {
		super("Robot Arm Gui");
		parent = _parent;
		console = new ConsolePanel(10, 30);
		console.outputArea.setLineWrap(true);
		console.outputArea.setEditable(false);
		setSize(500, 500);
		overrideStatus.setPreferredSize(new Dimension(50, 50));
		setLayout(new BorderLayout());
		add(overrideStatus, BorderLayout.NORTH);
		add(console, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		console.outputArea.addKeyListener(parent);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
