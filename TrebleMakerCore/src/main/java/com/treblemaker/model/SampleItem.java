package com.treblemaker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SampleItem {
	
	@JsonProperty("bpm")
    private int bpm;
	
	@JsonProperty("filename")
    private String fileName;
	
	@JsonProperty("rootone")
    private String rootOne;
	
	@JsonProperty("roottwo")
    private String rootTwo; 
	
	public int getBpm() {
		return bpm;
	}
	public void setBpm(int bpm) {
		this.bpm = bpm;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRootOne() {
		return rootOne;
	}
	public void setRootOne(String rootOne) {
		this.rootOne = rootOne;
	}
	public String getRootTwo() {
		return rootTwo;
	}
	public void setRootTwo(String rootTwo) {
		this.rootTwo = rootTwo;
	} 
}
