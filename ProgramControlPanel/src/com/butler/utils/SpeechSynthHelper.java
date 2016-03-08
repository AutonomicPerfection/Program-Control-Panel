package com.butler.utils;

import guru.ttslib.TTS;

public class SpeechSynthHelper {
	private static TTS sysTTS = null;

	public static TTS getSysTTS() {
		return sysTTS;
	}
	
	public static TTS createSysTTS() {
		sysTTS = new TTS();
		return sysTTS;
	}
	
	public static void setSysTTS(TTS _tts) {
		sysTTS = _tts;
	}
	
	public static TTS getAtlasTTS() {
		TTS atlas = new TTS();
		atlas.setPitch(.05F);
		atlas.setPitchRange(100);
		atlas.setPitchShift(100);
		return atlas;
	}
	
	public static void speakAsynch(TTS tts, String speech) {
		Thread speechThread = new Thread(() -> {
			tts.speak(speech);
		});
		speechThread.setName("TTS Thread " + speechThread.getId());
		speechThread.start();
	}
}
