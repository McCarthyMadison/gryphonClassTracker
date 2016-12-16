package com.example.madison.mapexample;

import java.util.ArrayList;

/**
 *  The is the classType Class. Determines what the table values can be.
 */

public class classType {

    // All of the different variables that each class needs to hold onto

    int id;

    String classname;
    String freq;
    String count;
    String currentcount;
    String interval;
    String currentinterval;
    String until;
    String days;
    String location;

    int su;
    int mo;
    int tu;
    int we;
    int th;
    int fr;
    int sa;

    int missed;
    int attended;
    int unsure;

    int starth;
    int startm;
    int endh;
    int endm;

    int nextdate;


    // Default constructor
    public classType(){

    }

    // Constructor with all variables
    public classType(String classname,
            String freq,
            String count,
            String currentcount,
            String interval,
            String currentinterval,
            String until,
            String days,
            String location,
            int su,
            int mo,
            int tu,
            int we,
            int th,
            int fr,
            int sa,
            int missed,
            int attended,
            int unsure,
            int starth,
            int startm,
            int endh,
            int endm,
            int nextdate){
        this.classname = classname;
        this.freq = freq;
        this.count = count;
        this.currentcount = currentcount;
        this.interval = interval;
        this.currentinterval = currentinterval;
        this.until = until;
        this.days = days;
        this.location = location;

        this.su = su;
        this.mo = mo;
        this.tu = tu;
        this.we = we;
        this.th = th;
        this.fr = fr;
        this.sa = sa;

        this.missed = missed;
        this.attended = attended;
        this.unsure = unsure;

        this.starth = starth;
        this.startm = startm;
        this.endh = endh;
        this.endm = endm;

        this.nextdate = nextdate;
    }

    // Functionality to add a class ready to be inserted into the Class Table
    public static ArrayList<classType> addContact(String classname,
                                                  String freq,
                                                  String count,
                                                  String currentcount,
                                                  String interval,
                                                  String currentinterval,
                                                  String until,
                                                  String days,
                                                  String location,
                                                  int su,
                                                  int mo,
                                                  int tu,
                                                  int we,
                                                  int th,
                                                  int fr,
                                                  int sa,
                                                  int missed,
                                                  int attended,
                                                  int unsure,
                                                  int starth,
                                                  int startm,
                                                  int endh,
                                                  int endm,
                                                  int nextdate){
        ArrayList<classType> ct = new ArrayList<classType>();
        ct.add(new classType(classname, freq, count, currentcount, interval, currentinterval, until, days, location, su, mo, tu, we, th, fr, sa, missed, attended, unsure, starth, startm, endh, endm, nextdate));
        return ct;
    }
}
