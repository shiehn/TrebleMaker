package com.treblemaker.weighters;

import java.util.HashMap;

public class BaseWeighter {

    public HashMap<String, Integer> classValue = null;

    public final String CLASS_BAD = "r_0";
    public final String CLASS_OK = "r_1";
    public final String CLASS_GOOD = "r_2";

    public final int VALUE_BAD = 0;
    public final int VALUE_OK = 1;
    public final int VALUE_GOOD = 2;

    public int convertVerticalClassification(String verticalClass){

        if(classValue == null){
            classValue = new HashMap<>();

            classValue.put(CLASS_BAD, VALUE_BAD);
            classValue.put(CLASS_OK, VALUE_OK);
            classValue.put(CLASS_GOOD, VALUE_GOOD);
        }

        return classValue.get(verticalClass);
    }
}
