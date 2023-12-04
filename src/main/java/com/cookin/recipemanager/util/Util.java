package com.cookin.recipemanager.util;

public class Util {

    public static String[] parseOperationValue(String str){
        String[] parts = str.split(":", 2);
        if (parts.length != 2) {
            //return default operation 'eq'
            parts[0] = "cn";
            parts[1] = str;
        }
        return parts;
    }
}
