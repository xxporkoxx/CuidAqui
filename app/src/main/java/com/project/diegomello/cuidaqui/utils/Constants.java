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

    public static Integer EMERGENCY_CALL_NUMBER = 1;
    public static Integer ASSISTENCE_CALL_NUMBER = 3;
    public static Integer BATHROOM_CALL_NUMBER = 2;
    public static Integer WATER_CALL_NUMBER = 4;


    public static String REGISTRATION_ID = "REGISTRATION_ID";

    public static String PACIENT_SHARED_PREF = "PACIENT_SHARED_PREF";
    public static String PACIENT_SHARED_PREF_NAME_STRING = "PACIENT_SHARED_PREF_NAME_STRING";

    //REMEBER TO ALWAYS CHANGE THE LINK FOR PRODUCTION
    public static final String BASE_URL ="http://caee3158.ngrok.io";
    public static final String NEW_CALL_ON_SOCKET_CALLBACK = "NEW_CALL_ON_SOCKET_CALLBACK";
    public static final String CONNECT_CENTRAL_TO_SOCKET = "CONNECT_CENTRAL_TO_SOCKET";
    public static final String socketConnected = "socketConnected";
    public static final String SOLVE_CALL_SOCKET_CALLBACK = "SOLVE_CALL_SOCKET_CALLBACK";
    public static final String CONNECT_CENTRAL_SUCESS_EMIT= "CONNECT_CENTRAL_SUCESS_EMIT";
    public static final String CONNECT_CENTRAL_ERROR_EMIT = "CONNECT_CENTRAL_ERROR_EMIT";
    public static final String DISCONNECT_CENTRAL = "DISCONNECT_CENTRAL";

    //LIST VIEW SECTION TWO SEPARATORS TYPES
    public static final Integer SECTION_TWO_NORMAL_CALL_TYPE_ITEM = 0;
    public static final Integer SECTION_TWO_UNSOLVED_CALL_TYPE_DIVIDER_ITEM = 1;
    public static final Integer SECTION_TWO_SOLVED_CALL_TYPE_DIVIDER_ITEM = 2;
}
