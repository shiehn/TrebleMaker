package com.treblemaker.model.progressions;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgressionDTO {

	private List<ProgressionUnit> Structure = new ArrayList<>();

	private List<Integer> ArpeggioHiTemplateVerse;
	private List<Integer> ArpeggioHiTemplateBridge;
	private List<Integer> ArpeggioHiTemplateChorus;

	private List<Integer> ArpeggioMidTemplateVerse;
	private List<Integer> ArpeggioMidTemplateBridge;
	private List<Integer> ArpeggioMidTemplateChorus;

	private List<Integer> ArpeggioLowTemplateVerse;
	private List<Integer> ArpeggioLowTemplateBridge;
	private List<Integer> ArpeggioLowTemplateChorus;

	public ProgressionDTO(){
	}

	@JsonProperty("Structure")
	public List<ProgressionUnit> getStructure() {
		return Structure;
	}

	@JsonProperty("Structure")
	public void setStructure(List<ProgressionUnit> structure) {
		Structure = structure;
	}

	public List<Integer> getArpeggioHiTemplateVerse() {
		return ArpeggioHiTemplateVerse;
	}

	public void setArpeggioHiTemplateVerse(List<Integer> arpeggioHiTemplateVerse) {
		ArpeggioHiTemplateVerse = arpeggioHiTemplateVerse;
	}

	public List<Integer> getArpeggioMidTemplateVerse() {
		return ArpeggioMidTemplateVerse;
	}

	public void setArpeggioMidTemplateVerse(List<Integer> arpeggioMidTemplateVerse) {
		ArpeggioMidTemplateVerse = arpeggioMidTemplateVerse;
	}

	public List<Integer> getArpeggioMidTemplateBridge() {
		return ArpeggioMidTemplateBridge;
	}

	public void setArpeggioMidTemplateBridge(List<Integer> arpeggioMidTemplateBridge) {
		ArpeggioMidTemplateBridge = arpeggioMidTemplateBridge;
	}

	public List<Integer> getArpeggioMidTemplateChorus() {
		return ArpeggioMidTemplateChorus;
	}

	public void setArpeggioMidTemplateChorus(List<Integer> arpeggioMidTemplateChorus) {
		ArpeggioMidTemplateChorus = arpeggioMidTemplateChorus;
	}

	public List<Integer> getArpeggioLowTemplateVerse() {
		return ArpeggioLowTemplateVerse;
	}

	public void setArpeggioLowTemplateVerse(List<Integer> arpeggioLowTemplateVerse) {
		ArpeggioLowTemplateVerse = arpeggioLowTemplateVerse;
	}

	public List<Integer> getArpeggioLowTemplateBridge() {
		return ArpeggioLowTemplateBridge;
	}

	public void setArpeggioLowTemplateBridge(List<Integer> arpeggioLowTemplateBridge) {
		ArpeggioLowTemplateBridge = arpeggioLowTemplateBridge;
	}

	public List<Integer> getArpeggioLowTemplateChorus() {
		return ArpeggioLowTemplateChorus;
	}

	public void setArpeggioLowTemplateChorus(List<Integer> arpeggioLowTemplateChorus) {
		ArpeggioLowTemplateChorus = arpeggioLowTemplateChorus;
	}

	public List<Integer> getArpeggioHiTemplateBridge() {
		return ArpeggioHiTemplateBridge;
	}

	public void setArpeggioHiTemplateBridge(List<Integer> arpeggioHiTemplateBridge) {
		ArpeggioHiTemplateBridge = arpeggioHiTemplateBridge;
	}

	public List<Integer> getArpeggioHiTemplateChorus() {
		return ArpeggioHiTemplateChorus;
	}

	public void setArpeggioHiTemplateChorus(List<Integer> arpeggioHiTemplateChorus) {
		ArpeggioHiTemplateChorus = arpeggioHiTemplateChorus;
	}
}
