package com.treblemaker.model;

import java.io.Serializable;

public class Transitions implements Serializable {

	private long Id;
	private String Genre;
	private int SubtleToExplicit;
	private boolean IsDrums;
	private boolean IsAmbience;
	private int Tempo;
	private int BarCount;
	private String TimeSignature;
	private String FileName;
	private String FilePath;
}
