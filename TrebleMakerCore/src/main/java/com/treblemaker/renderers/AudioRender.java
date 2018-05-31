package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.renderers.interfaces.IAudioRender;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Component
public class AudioRender implements IAudioRender{

	@Autowired
	private AppConfigs appConfigs;

	public String output(InputStream inputStream) throws IOException {

		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;

		try {

			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
		} finally {
			br.close();
		}

		return sb.toString();
	}
 
	public void renderPart(String midiFilePath, String audioTargetPath, Spectrum spectrum, SynthTemplate template, Integer bpm) {
  
		midiFilePath = midiFilePath.replace("//", "\\");
		audioTargetPath = audioTargetPath.replace("//", "\\");

		String synthSetting = "";
		if (spectrum == Spectrum.HI) {
			synthSetting = template.getHiSynthNameAlt() + "," + template.getHiPresetAlt();
		}else if (spectrum == Spectrum.ALT_HI) {
			synthSetting = template.getHiSynthNameAlt() + "," + template.getHiPresetAlt();
		}else if (spectrum == Spectrum.MID) {
			synthSetting = template.getMidSynthName() + "," + template.getMidPreset();
		}else if (spectrum == Spectrum.ALT_MID) {
			synthSetting = template.getMidSynthName() + "," + template.getMidPresetAlt();
		} else if (spectrum == Spectrum.LOW) {
			synthSetting = template.getLowSynthName() + "," + template.getLowPreset();
		}else if (spectrum == Spectrum.ALT_LOW) {
			synthSetting = template.getLowSynthName() + "," + template.getLowPresetAlt();
		}

		String debugProcess = appConfigs.getMrsWatsonLocation() + " " + "--midi-file" + " " + midiFilePath + " " + "--tempo" + " " + Integer.toString(bpm) + " " + "--output" + " " + audioTargetPath + " " + "--plugin" + " " + appConfigs.getVSTPluginDir() + synthSetting;

		Application.logger.debug("LOG: **********");
		Application.logger.debug("LOG:" + debugProcess);
		Application.logger.debug("LOG: **********");

		ProcessBuilder process = new ProcessBuilder(appConfigs.getMrsWatsonLocation(), "--midi-file", midiFilePath, "--tempo", Integer.toString(bpm), "--output", audioTargetPath, "--plugin", appConfigs.getVSTPluginDir() + synthSetting);
		StringBuilder strBuild = new StringBuilder();
		/*NEW CODE*/
		Process processDuration = null;

		try {
			process.redirectErrorStream(true).start();

			try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
				String line;
				while ((line = processOutputReader.readLine()) != null) {

					strBuild.append(line + System.lineSeparator());
				}
				processDuration.waitFor();
			} catch (InterruptedException e) {
				Application.logger.debug("LOG:", e);
			}
			System.out.print("renderPart " + spectrum + " COMPLETE");
		} catch (IOException e) {
			Application.logger.debug("LOG:", e);
		}
	} 
}
