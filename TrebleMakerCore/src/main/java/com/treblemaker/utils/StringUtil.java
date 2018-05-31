package com.treblemaker.utils;

/**
 * Created by Steve on 2015-06-23.
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String value){

        if(value == null){
           return true;
        }

        if(value.equalsIgnoreCase("")){
            return true;
        }

        return false;
    }
}
