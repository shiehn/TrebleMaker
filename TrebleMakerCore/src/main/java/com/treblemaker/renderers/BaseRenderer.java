package com.treblemaker.renderers;

import com.treblemaker.Application;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

@Component
public class BaseRenderer {

	public Boolean concatenateFiles(List<String> sourceFilesList, String destinationFileName) throws Exception {

		destinationFileName = correctFilePath(destinationFileName);

		try {
			Vector<AudioInputStream> inputStreams = new Vector<>();

			AudioFormat audioFormat = null;
			long frameLength = 0;

			for (String source : sourceFilesList) {
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(source));
				inputStreams.add(inputStream);

				audioFormat = inputStream.getFormat();
				frameLength = frameLength + inputStream.getFrameLength();
			}

			Enumeration<AudioInputStream> enumeration = inputStreams.elements();

			AudioInputStream appendedFiles =
					new AudioInputStream(
							new SequenceInputStream(enumeration),
							audioFormat,
							frameLength);

			Application.logger.debug("LOG: %%%%%%% RENDERED: " + destinationFileName);

			AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, new File(destinationFileName));
		} catch (Exception e) {
			Application.logger.debug("LOG:",e);
			return false;
		}

		return true;
	}

	private String correctFilePath(String destinationFileName){
		return destinationFileName.replace("\\","//" );
	}
}
