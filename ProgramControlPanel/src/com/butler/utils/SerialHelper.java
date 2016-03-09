package com.butler.utils;

import java.io.PrintStream;
import java.util.Scanner;

import com.butler.main.Resource;

import guru.ttslib.TTS;
import processing.core.PApplet;
import processing.serial.Serial;

public class SerialHelper implements Resource {
	private static PrintStream out = System.out;
	private static PApplet parent = new PApplet();
	private static TTS atlas;
	public static void setOut(PrintStream _out) {
		out = _out;
	}

	public static Serial newSerial() {
		String[] portNames = null;
		try {
			portNames = Serial.list();
		} catch (Exception e) {

		}
		if (portNames.length == 0 || portNames == null) {
			out.println("No serial devices detected. Please attach a device to a USB port.");
			while (portNames.length == 0 || portNames == null) {
				try {
					portNames = Serial.list();
				} catch(Throwable ex) {
					//Do nothing until a serial device is detected
				}
			}
		}
		out.println("Ports with serial devices connected:");
		for (String name : portNames) {
			out.println(name);
		}
		Scanner scan = new Scanner(System.in);
		String selectedPort = portNames[scan.nextInt()];
		scan.close();
		return new Serial(parent, selectedPort);
	}


	public static Serial newSerial(String targetName) {
		return newSerial(targetName, false);
	}
	
	public static Serial newSerial(String targetName, boolean shouldSpeak) {
		if (atlas == null) atlas = SpeechSynthHelper.getAtlasTTS();
		if (shouldSpeak) SpeechSynthHelper.speakAsynch(atlas, "Searching for serial devices");
		String[] portNames = null;
		try {
			portNames = Serial.list();
		} catch (Throwable e) {
			System.err.println("Serial error: " + e.getMessage());
		}
		if (portNames == null || portNames.length == 0) {
			out.println("No serial devices detected. Please attach a device to a USB port.");
			if (shouldSpeak) SpeechSynthHelper.speakAsynch(atlas, "Error: No serial devices detected");
			while (portNames == null || portNames.length == 0) {
				try {
					portNames = Serial.list();
				} catch(Throwable ex) {
					//Do nothing until a serial device is detected
				}
			}
		}
		out.println("Number of serial devices registered: " + portNames.length);
		out.println("Ports with serial devices registered:");
		for (String name : portNames) {
			out.println(name);
		}
		String deviceName = "";
		Serial port = null;
		long beginTime = System.currentTimeMillis();
		boolean hasErrored = false;
		for (int i = 0; !deviceName.equalsIgnoreCase(targetName); i++) {
			parent.delay(500);
			if (i >= portNames.length)
				i = 0;
			try {
				port = new Serial(parent, portNames[i]);
				if (shouldSpeak) SpeechSynthHelper.speakAsynch(atlas, "Identifying connected device on port " + portNames[i]);
				parent.delay(2 * 1000);
				port.write("Request:ID\n");
				//parent.delay(500);
				String gotString = readStringUntil(port, "\n").trim();
				//out.println("String got from device: " + gotString);
				//out.println("Does string got from device equal null: " + (gotString == null));
				deviceName = gotString.split(":")[2];
				out.println("Device name on port " + portNames[i] + ": " + deviceName);
				//out.println("Target name equals: " + targetName);
				//out.println("Does device name equal target name: " + deviceName.equalsIgnoreCase(targetName));
			} catch (Exception e) {
				if (!e.getMessage().split(":")[1].trim().equalsIgnoreCase("Port not found"))
					System.err.println("Serial error: " + e.getMessage());
				else {
					if ((System.currentTimeMillis() - beginTime >= 3 * 1000) && !hasErrored) {
						out.println("No serial devices detected. Please attach a device to a USB port.");
						if (shouldSpeak) SpeechSynthHelper.speakAsynch(atlas, "Error: No serial devices detected");
						hasErrored = true;
					}
				}
			}
		}
		if (shouldSpeak) SpeechSynthHelper.speakAsynch(atlas, "Device intialized.");
		return port;
	}
	
	public static String readStringUntil(Serial port, String delimiter) {
		String returnString = "";
		for (;!returnString.endsWith(delimiter);) {
			parent.delay(10);
			while (!(port.available() > 0));
			returnString += port.readString();
		}
		return returnString;
	}
}
