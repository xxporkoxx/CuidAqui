package com.project.diegomello.cuidaqui.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by DiegoMello on 3/29/2017.
 */

public class Utils {

    public static Context mContext;

    public static void setContext(Context context) {

        mContext = context;
    }

    public static String returnFirebaseKey() {
        //FirebaseKey Mockada para realizar os testes nos emuladores do android
        String firebaseKey;
        if (FirebaseInstanceId.getInstance().getToken() != null)
            return FirebaseInstanceId.getInstance().getToken();
        else
            return Constants.FIREBASE_FAKE_API_FOR_EMULATOR;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
