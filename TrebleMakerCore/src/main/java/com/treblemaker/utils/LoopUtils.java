package com.treblemaker.utils;


public class LoopUtils {

	public static int getBarCount(int bpm, float sampleLength) throws Exception {

		float secondsInBar = getBeatsInSeconds(bpm, 4);

		return barsRequiredForLoop(secondsInBar, sampleLength);
	}

	public static int barsRequiredForLoop(float secondsInBar, float sampleLength) throws Exception {

		if(secondsInBar * 1 >= sampleLength){
			return 1;
		}else if(secondsInBar * 2 >= sampleLength){
			return 2;
		}else if(secondsInBar * 4 >= sampleLength){
			return 4;
		}else if(secondsInBar * 8 >= sampleLength){
			return 8;
		}

		throw new Exception("Sample length is to long");
	}

	public static boolean isPastBleedThreshold(float loopLength, float secondsInBar, float bleedThreshold){

		int numOfBarsInLoop = (int)Math.floor(loopLength / secondsInBar);

		float totalBleed = loopLength - (numOfBarsInLoop * secondsInBar);

		return (totalBleed > bleedThreshold);
	}

	public static int getTargetBarCount(int currentBarCount, boolean pastThreshold){

		switch(currentBarCount){

			case 0:
				return 1;
			case 1:
				if(!pastThreshold){
					return 1;
				}
				return 2;
			case 2:
				if(!pastThreshold) {
					return 2;
				}
				return 4;
			case 3:
				return 4;
			case 4:
				if(!pastThreshold) {
					return 4;
				}
				return 8;
			default:
				return 8;
		}
	}
	
	public static float getBarCountForLoop(float sampleLength, float secondsInBar){
		
		//TODO THIS SHOULD INCLUDE LONGER SAMPLES ETC ..
		
		if(sampleLength <= (secondsInBar * 2f)){
			return 2f;
		}

		return 4f;
	}
	
	public static float getBarGroupLengthInSeconds(float secondsInBar, float barCount){
		
		float floatLength = secondsInBar * barCount;
		return Math.round(floatLength);
	} 

	public static float getSecondsInBar(int bpm){
		return getBeatsInSeconds(bpm, 4);
	}

	public static double getSecondsInSixteethNote(int bpm){

		float secondsInBar = getSecondsInBar(bpm);
		return secondsInBar/16;
	}

	public static float getSecondsInBars(int bpm, int numberOfBars){
		float barInSeconds = getBeatsInSeconds(bpm, 4);
		return barInSeconds * numberOfBars;
	}

	public static float getMilliSecondsInBar(int bpm){

		//TODO DECLARE BPM GLOBALAY . .
		return getBeatsInSeconds(bpm, 4) * 1000;
	}
	
	public static float getBeatsInSeconds(int bpm, int numOfBeats){
		
		float secondsPerBeat = 60.0f / (float)bpm;
		return (float)numOfBeats * secondsPerBeat;
	}
} 