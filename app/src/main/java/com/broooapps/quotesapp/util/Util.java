package com.broooapps.quotesapp.util;

/**
 * Created by Swapnil Tiwari on 05/04/19.
 * swapnil.tiwari@box8.in
 */
public class Util {

    public static double getRandomDoubleBetweenRange(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

}
