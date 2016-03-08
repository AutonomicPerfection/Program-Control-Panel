package com.butler.main;


import java.io.PrintStream;

import com.butler.utils.SerialHelper;

import processing.core.PApplet;
import processing.serial.Serial;

public class SerialResource implements Resource {
	
	public SerialResource() {
		
	}
	
	public SerialResource(PrintStream out) {
		SerialHelper.setOut(out);
	}
	
	public boolean isConsumable() {
		return false;
	}
	
	public Serial getSerial(String deviceID) {
		return SerialHelper.newSerial(deviceID);
	}
}
