package com.treblemaker.utils;

public class ArrangementUtils {

	public static String getArpeggioNoteLength(int speed) {
		switch (speed) {
		case 1:
			return "w";
		case 2:
			return "h";
		case 4:
			return "q";
		case 8:
			return "i";
		case 16:
			return "s";
		default:
			return "q";
		}
	}
}