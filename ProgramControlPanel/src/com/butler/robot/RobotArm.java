package com.butler.robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.butler.main.Resource;
import com.butler.utils.SerialHelper;
import com.butler.utils.SpeechSynthHelper;

import guru.ttslib.TTS;
import processing.core.PApplet;
import processing.serial.Serial;

public class RobotArm extends PApplet implements KeyListener{
	private volatile Serial arm;
	private RobotArmGui gui;
	private boolean isManualOverride = true;
	private  int timesAutoRun = 0;
	private volatile boolean shouldAutopilot = false;
	private TTS atlas;
	public void setup() {
		surface.setVisible(false);
		atlas = SpeechSynthHelper.getAtlasTTS();
		gui = new RobotArmGui(this);
		arm = SerialHelper.newSerial("RobotArmSciOly2016", true);
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
		if (shouldAutopilot) autopilot();
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
				moveArm(10, 0, 0, 0, 0, false);
			else if(e.getKeyCode() == KeyEvent.VK_DOWN) 
				moveArm(-10,0,0,0,0,false);
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				moveArm(0,10,0,0,0,false);
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
				moveArm(0,-10,0,0,0,false);
			else if(e.getKeyChar() == 'w')
				moveArm(0,0,10,0,0,false);
			else if(e.getKeyChar() == 's')
				moveArm(0,0,-10,0,0,false);
			else if(e.getKeyChar() == 'a')
				moveArm(0,0,0,10,0,false);
			else if(e.getKeyChar() == 'd')
				moveArm(0,0,0,-10,0,false);
			else if(e.getKeyChar() == 'r')
				moveArm(0,0,0,0,10,false);
			else if(e.getKeyChar() == 't')
				moveArm(0,0,0,0,-10,false);
		} 
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			setManualOverride(!isManualOverride); //Sets isManualOverride to opposite
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
			SpeechSynthHelper.speakAsynch(atlas, "Autopilot mode engaged");
			gui.overrideStatus.setText("Mode: Manual Control");
			gui.repaint();
			shouldAutopilot = false;
		}else { 
			System.out.println("Autopilot engaged");
			SpeechSynthHelper.speakAsynch(atlas, "Autopilot mode engaged");
			gui.overrideStatus.setText("Mode: Autopilot");
			gui.repaint();
			shouldAutopilot = true;
		}
	}
	
	public void autopilot() {
		switch (timesAutoRun) {
			case 0:
				moveArm(0, -180, 0, 0, 0, true);
				moveArm(0, 180, 0, 0, 0, true);
			break;
		}
		timesAutoRun++;
		setManualOverride(true);
	}
	
	public void moveArm(int x, int y, int z, int a, int b, boolean c) {
		System.out.println("Rotating Robot Arm to coordinates "+ x + "," + y + "," + z + "," + a + "," + b);
		if (arm != null) {
			y = (int) ((y / 1.8) * 3.275);
			arm.write("Command:Rotate:" + x + "," + y + "," + z + "," + a + "," + b + '\n' + "Command:SetScraperOn:" + c + '\n');
			waitForReply(arm);
		} else {
			System.out.println("Warning: No serial device connected. Commands won't be sent until a serial device is connected");
		}
	}

}
