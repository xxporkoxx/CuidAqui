package com.project.diegomello.cuidaqui.managers;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.project.diegomello.cuidaqui.utils.Constants;

/**
 * Created by DiegoMello on 2/4/2017.
 */

public class FirebaseInstanceIDServiceManager extends FirebaseInstanceIdService {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    DatabaseReference registrationIDref;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FIREBASETOKEN", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);


    }

    public boolean sendRegistrationToServer(String refreshedToken){
        //TODO Check for internet connection if it does't work, maybe it can be the problem
        registrationIDref = mDatabase.getReference(Constants.REGISTRATION_ID);
        registrationIDref.setValue(refreshedToken,Constants.REGISTRATION_ID);

        return true;
    }
}
