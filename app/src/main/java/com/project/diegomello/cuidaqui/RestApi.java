package com.project.diegomello.cuidaqui;

import com.project.diegomello.cuidaqui.models.CallItem;
import com.project.diegomello.cuidaqui.models.Patient;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;
import retrofit2.Call;

/**
 * Created by DiegoMello on 1/31/2017.
 */

public interface RestApi {
    @GET("wifisave")
    Call<String> sendWifiRemoteControlCredentials(@Query("ssid") String ssid, @Query("password") String password);

    @retrofit2.http.GET("patients/")
    Call<ArrayList<Patient>> getPatientsFromApi();

    @retrofit2.http.GET("calls/")
    Call<ArrayList<CallItem>> getCallsFromApi();
}
