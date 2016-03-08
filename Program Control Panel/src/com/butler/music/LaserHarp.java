package com.butler.music;

import java.util.Scanner;

import processing.core.PApplet;
import processing.serial.Serial;

public class LaserHarp extends PApplet {
	Serial laserPort;
	String[] portNames;
	public void setup() {
		background(0);
		surface.setTitle("Laser Harp");
		try {
			portNames = Serial.list();
		} catch (Throwable e) {
			println("No serial devices detected. Please attach the Laser Harp to a USB port.");
			while (portNames == null) {
				try {
					portNames = Serial.list();
				} catch(Throwable ex) {
					//Do nothing until a serial device is detected
				}
			}
		}
		
		println("Ports with serial devices connected:");
		printArray(portNames);
		Scanner scan = new Scanner(System.in);
		laserPort = new Serial(this, scan.nextInt());
		scan.close();
	}
	
	public void settings() {
		size(200, 200);
	}
	
	public static String getName() {
		return "Laser Harp";
	}
	
	public static void main(String[] args) {
		PApplet.main(LaserHarp.class.getName());
	}

}
