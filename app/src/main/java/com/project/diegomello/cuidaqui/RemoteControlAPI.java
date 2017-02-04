package com.project.diegomello.cuidaqui;

import retrofit.http.GET;
import retrofit.http.Query;
import retrofit2.Call;

/**
 * Created by DiegoMello on 1/31/2017.
 */

public interface RemoteControlAPI {
    @GET("wifisave")
    Call<String> sendWifiRemoteControlCredentials(@Query("ssid") String ssid, @Query("password") String password);
}
