package com.hcc.mods.chromahud;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class NumberUtil {
    public static double round(double in, double places) {
        if(places==0)
            return Math.round(in);
//        return in;
        return Math.round(in* 10.0*places)/ (10*places);
    }

}
