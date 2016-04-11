package com.fbo.remoteraspberry.util;

/**
 * Created by Fred on 30/06/2015.
 */
public class TypeConvertionUtils {

    public static String[] toStringArray(Object[] array) {
        String[] res = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i].toString();
        }
        return res;
    }
}
