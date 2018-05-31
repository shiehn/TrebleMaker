package com.treblemaker.selectors;

import com.treblemaker.Application;
import com.treblemaker.selectors.interfaces.IArpeggioSelector;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ArpeggioSelector implements IArpeggioSelector {

	public int SelectArpeggioSpeed(){
		 
		int arpeggioSpeed = 8;
		
		switch(generateNumber()){
		case 1:
			return arpeggioSpeed = 4; 
		case 2:
			return arpeggioSpeed = 8; 
		case 3:
			return arpeggioSpeed = 16; 
		}
		
		return arpeggioSpeed;
	}
	
	public int generateNumber(){
		 
		//TODO WILL ONLY CHOOSE 4 or 8!!!
		int num = new Random().nextInt(2) + 1;
		Application.logger.debug("LOG: RANDOM NUM = " + num);
		return num; 
	}
}
