package com.treblemaker.logger;

import com.treblemaker.Application;
import com.treblemaker.model.ProcessingState;

public class Log {

    public static String LoopGenerator = "loopgenerator";
    public static String HarmonicStructureGenerator = "harmonicstructuregenerator";

    public static void LogProcedureState(ProcessingState.ProcessingStates processingState, String currentClass)
    {
        switch (processingState){

            //FOUR PATTERN
            case C1_L1:
                if(HarmonicStructureGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING CHORD ONE FOR PATTERN FOUR %%%%%");
                }else if(LoopGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING LOOP ONE FOR PATTERN FOUR %%%%%");
                }

                return;
            case C2_C3_C4:
                if(HarmonicStructureGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING CHORD C2_C3_C4 FOR PATTERN FOUR %%%%%");
                }else if(LoopGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : NO LOOP FOR C2_C3_C4 FOR PATTERN FOUR %%%%%");
                }

                return;
            //END FOUR PATTERN

            case C1_L3:
                if(HarmonicStructureGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING CHORD ONE %%%%%");
                }else if(LoopGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING LOOP THREE %%%%%");
                }

                return;
            case C3_L1:
                if(HarmonicStructureGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING CHORD THREE %%%%%");
                }else if(LoopGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING LOOP ONE %%%%%");
                }

                return;
            case C2_L4:
                if(HarmonicStructureGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING CHORD TWO %%%%%");
                }else if(LoopGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING LOOP FOUR %%%%%");
                }

                return;
            case C4_L2:
                if(HarmonicStructureGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING CHORD FOUR %%%%%");
                }else if(LoopGenerator.equalsIgnoreCase(currentClass)){
                    Application.logger.debug("LOG: %%%% POSITION : SETTING LOOP TWO %%%%%");
                }

                return;
            case COMPLETE:
                    Application.logger.debug("LOG: %%%% POSITION : FINISHED SETTING CHORDS AND LOOPS %%%%%");

                return;
            default:
                return;
        }
    }
}
