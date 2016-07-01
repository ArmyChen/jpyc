package com.twopole.utils;

/**
 * Created by Administrator on 2016-06-07.
 */
public class Vari  {
    private static int speakTimeSpans = 5;

    public static int getSpeakTimeSpans() {
        if(speakTimeSpans == 0){
            speakTimeSpans = 5;
        }
        return speakTimeSpans;
    }

    public static void setSpeakTimeSpans(int speakTimeSpans) {
        Vari.speakTimeSpans = speakTimeSpans;
    }
}
