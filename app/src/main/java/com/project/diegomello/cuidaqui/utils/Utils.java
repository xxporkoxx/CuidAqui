package com.project.diegomello.cuidaqui.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by DiegoMello on 3/29/2017.
 */

public class Utils {

    public static Context mContext;

    public static void setContext(Context context) {

        mContext = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
