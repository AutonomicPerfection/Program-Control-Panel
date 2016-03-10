package com.butler.utils;

import guru.ttslib.TTS;

public class SpeechSynthHelper extends TTS{
	private static SpeechSynthHelper sysTTS = null;

	public static SpeechSynthHelper getSysTTS() {
		return sysTTS;
	}
	
	public static TTS createSysTTS() {
		sysTTS = new SpeechSynthHelper();
		return sysTTS;
	}
	
	public static void setSysTTS(SpeechSynthHelper _tts) {
		sysTTS = _tts;
	}
	
	public static SpeechSynthHelper getAtlasTTS() {
		SpeechSynthHelper atlas = new SpeechSynthHelper();
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
	
	public void speakAsynch(String speech) {
		speakAsynch(this, speech);
	}
}
