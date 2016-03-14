package com.butler.robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import com.butler.main.Resource;
import com.butler.utils.SerialHelper;
import com.butler.utils.SpeechSynthHelper;

import processing.core.PApplet;
import processing.serial.Serial;

public class RobotArm extends PApplet implements KeyListener{
	private volatile Serial arm;
	private RobotArmGui gui;
	private boolean isManualOverride = true;
	private  int timesAutoRun = 0;
	private volatile boolean shouldAutopilot = false;
	private SpeechSynthHelper atlas;

	private final String[] stepModes = new String[] {
			"Full", "Half", "Quarter", "Eighth"
	};

	private int currentStepModeIndex = 0;
	public void setup() {
		surface.setVisible(false);
		atlas = SpeechSynthHelper.getAtlasTTS();
		gui = new RobotArmGui(this);
		new Thread(() -> {
			while (true) {
				if (shouldAutopilot) autopilot();
				delay(50);
			}
		}).start();
		//arm = SerialHelper.newSerial("RobotArmSciOly2016", true);
		arm = new SerialHelper("RobotArmSciOly2016", true);
		arm.bufferUntil('\n');
	}

	public void settings() {
		size(500, 500);
	}

	public void waitForReply(Serial port) {
		while (!(port.available() > 0)) {
			delay(10);
		}
		//String receivedString = port.readStringUntil('\n').trim();
		String receivedString = SerialHelper.readStringUntil(arm, "\n").trim();
		String[] params = receivedString.split(":");
		if (params[0].equalsIgnoreCase("Reply")) {
			if (params[1].equalsIgnoreCase("OK")) {
				System.out.println("Ready for next request");
				return;
			} else System.err.println("Error from connected device:\n" + params[2]);
		}
	}

	public void draw() {
		
	}

	public static String getName() {
		return "Robotic Arm";
	}


	public static void main(String[] args) {
		PApplet.main(RobotArm.class.getName());
	}


	public static void setResources(Resource[] resources) {
		if (resources[0] == null) {
			System.err.println("No resources!");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (isManualOverride) {
			if (e.getKeyCode() == KeyEvent.VK_UP) 
				moveArmAsynch(10, 0, 0, 0, 0, false);
			else if(e.getKeyCode() == KeyEvent.VK_DOWN) 
				moveArmAsynch(-10,0,0,0,0,false);
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				moveArmAsynch(0,10,0,0,0,false);
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
				moveArmAsynch(0,-10,0,0,0,false);
			else if(e.getKeyChar() == 'w')
				moveArmAsynch(0,0,10,0,0,false);
			else if(e.getKeyChar() == 's')
				moveArmAsynch(0,0,-10,0,0,false);
			else if(e.getKeyChar() == 'a')
				moveArmAsynch(0,0,0,10,0,false);
			else if(e.getKeyChar() == 'd')
				moveArmAsynch(0,0,0,-10,0,false);
			else if(e.getKeyChar() == 'r')
				moveArmAsynch(0,0,0,0,10,false);
			else if(e.getKeyChar() == 't')
				moveArmAsynch(0,0,0,0,-10,false);
		} 
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			setManualOverride(!isManualOverride); //Sets isManualOverride to opposite
		else if(e.getKeyChar() == 'm') {
			currentStepModeIndex++;
			if (currentStepModeIndex >= stepModes.length) currentStepModeIndex = 0;
			setStepMode(stepModes[currentStepModeIndex], true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void setManualOverride(boolean val) {
		isManualOverride = val;
		if (isManualOverride) {
			System.out.println("Manual override engaged");
			SpeechSynthHelper.speakAsynch(atlas, "Manual control mode engaged");
			gui.overrideStatus.setText("Mode: Manual Control");
			gui.repaint();
			shouldAutopilot = false;
		}else { 
			System.out.println("Autopilot engaged");
			SpeechSynthHelper.speakAsynch(atlas, "Autopilot mode engaged");
			gui.overrideStatus.setText("Mode: Autopilot");
			shouldAutopilot = true;
		}
	}
	
	public boolean isManualOverride() {
		return isManualOverride;
	}

	public void resetAutopilot() {
		while (shouldAutopilot){
			delay (50);
		}
		System.out.println("Resetting autopilot counter");
		SpeechSynthHelper.speakAsynch(atlas, "Resetting autopilot counter");
		setAutopilotCounter(0);
	}

	public void autopilot() {
		setStepMode("Full", false);
		switch (timesAutoRun) {
		case 0:
			moveArm(0, -180, 0, 0, 0, true);
			moveArm(0, 180, 0, 0, 0, true);
			break;
		}
		setAutopilotCounter(timesAutoRun + 1);
		setManualOverride(true);
	}
	
	public void setAutopilotCounter(int count) {
		timesAutoRun = count;
		gui.autopilotCounterStatus.setText("Autopilot sequence: " + timesAutoRun);
	}

	public void moveArmAsynch(int x, int y, int z, int a, int b, boolean c) {
		new Thread(() -> {
			moveArm(x, y, z, a, b, c);
		}).start();
	}
	public void moveArm(int x, int y, int z, int a, int b, boolean c) {
		System.out.println("Rotating Robot Arm to coordinates "+ x + "," + y + "," + z + "," + a + "," + b);
		if (arm != null) {
			int yCalc = (int) (((y / 360) * 3) / 3.275) * 1000;
			arm.write("Command:Rotate:" + x + "," + yCalc + "," + z + "," + a + "," + b + '\n' + "Command:SetScraperOn:" + c + '\n');
			waitForReply(arm);
		} else {
			System.out.println("Warning: No serial device connected. Commands won't be sent until a serial device is connected");
		}
	}

	public void setStepMode(String mode, boolean shouldSpeak) {
		gui.stepModeStatus.setText("Step Mode: " + mode);
		gui.stepModeStatus.repaint();
		currentStepModeIndex = new ArrayList<String>(Arrays.asList(stepModes)).indexOf(mode);
		if (arm != null) {
			System.out.println("Stepping mode set to " + mode.toLowerCase() + " step mode");
			if (shouldSpeak)SpeechSynthHelper.speakAsynch(atlas, "Stepping mode set to " + mode + " step mode");
			if (mode.equalsIgnoreCase("Full")) {
				arm.write("Command:StepMode:0,0");
				waitForReply(arm);
			} else if(mode.equalsIgnoreCase("Half")) {
				arm.write("Command:StepMode:1,0");
				waitForReply(arm);
			} else if(mode.equalsIgnoreCase("Quarter")) {
				arm.write("Command:StepMode:0,1");
				waitForReply(arm);
			} else if(mode.equalsIgnoreCase("Eighth")) {
				arm.write("Command:StepMode:1,1");
				waitForReply(arm);
			}
		} else {
			System.out.println("Error: no serial devices connected. Cannot set step mode without a connected serial device");
			//SpeechSynthHelper.speakAsynch(atlas, "Error: no serial devices connected");
			atlas.speakAsynch("Error: no serial devices connected");
		}
	}
}
