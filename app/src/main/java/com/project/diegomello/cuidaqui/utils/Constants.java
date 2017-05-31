package com.project.diegomello.cuidaqui.utils;

/**
 * Created by DiegoMello on 2/1/2017.
 */

public class Constants {
    public static final Integer CALL_STATUS_INITIALIZATION = 0;     //0 : initialization code for remote control
    public static final Integer CALL_STATUS_WATING_TO_SERVE = 1;     //1 : waiting to be served
    public static final Integer CALL_STATUS_ON_THE_WAY = 2;     //2 : Someone is o the way
    public static final Integer CALL_STATUS_SERVED = 3;     //3 : already served

    public static String DISCOMFORT_CALL = "DISCOMFORT_CALL";
    public static String BATHROOM_CALL = "BATHROOM_CALL";
    public static String WATER_CALL = "WATER_CALL";
    public static String EMERGENCY_CALL = "EMERGENCY_CALL";


    public static String REGISTRATION_ID = "REGISTRATION_ID";

    public static String PATIENT_SHARED_PREF = "PATIENT_SHARED_PREF";
    public static String PATIENT_SHARED_PREF_NAME_STRING = "PATIENT_SHARED_PREF_NAME_STRING";

    public static String PATIENT_CALL_COUNTER_EMERGENCY = "PATIENT_CALL_COUNTER_EMERGENCY";
    public static String PATIENT_CALL_COUNTER_BATHROOM = "PATIENT_CALL_COUNTER_BATHROOM";
    public static String PATIENT_CALL_COUNTER_WATER = "PATIENT_CALL_COUNTER_WATER";
    public static String PATIENT_CALL_COUNTER_DISCOMFORT = "PATIENT_CALL_COUNTER_DISCOMFORT";
}
