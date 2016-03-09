package com.butler.robot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.butler.utils.ConsolePanel;
import com.butler.utils.TextFieldInput;
import com.butler.utils.TextInputEvent;
import com.butler.utils.TextInputListener;

public class RobotArmGui extends JFrame implements TextInputListener{
	private RobotArm parent;
	private ConsolePanel console;
	private TextFieldInput input;
	
	public JLabel overrideStatus = new JLabel("Mode: Manual Control");
	
	public RobotArmGui(RobotArm _parent) {
		super("Robot Arm Gui");
		parent = _parent;
		console = new ConsolePanel(10, 30);
		input = new TextFieldInput(20);
		input.addTextInputListener(this);
		setSize(500, 500);
		overrideStatus.setPreferredSize(new Dimension(50, 50));
		setLayout(new BorderLayout());
		add(overrideStatus, BorderLayout.NORTH);
		add(console, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		console.outputArea.addKeyListener(parent);
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
}
