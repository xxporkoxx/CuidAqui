package com.project.diegomello.cuidaqui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.diegomello.cuidaqui.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ssid_EditText)
    EditText ssidEditText;

    @BindView(R.id.password_EditText)
    EditText passwordEditText;

    @BindView(R.id.config_Button)
    Button configButton;

    @BindView(R.id.calls_listView)
    ListView callListView;

    Context mContext;

    private ScanResult network;

    private FirebaseDatabase mDatabase;

    private DatabaseReference waterRef;
    private DatabaseReference bathroomRef;
    private DatabaseReference discomfortRef;
    private DatabaseReference emergencyRef;

    private CallsAdapter callAdapter;
    private ArrayList<CallItem> callListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        isGooglePlayServicesAvailable(this);
        mDatabase = FirebaseDatabase.getInstance();
        waterRef = mDatabase.getReference(Constants.WATER_CALL);
        bathroomRef = mDatabase.getReference(Constants.BATHROOM_CALL);
        discomfortRef = mDatabase.getReference(Constants.DISCOMFORT_CALL);
        emergencyRef = mDatabase.getReference(Constants.EMERGENCY_CALL);
        setupListView();
        passwordEditText.setVisibility(View.GONE);
        ssidEditText.setVisibility(View.GONE);
        configButton.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGooglePlayServicesAvailable(this);
        //TODO implement wi fi configutration on app
        //checkForWifi();

        firebaseSync();
    }

    private void setupListView(){
        callListItems = new ArrayList<>();
        callAdapter = new CallsAdapter(mContext,callListItems,waterRef,bathroomRef,discomfortRef, emergencyRef);
        callListView.setAdapter(callAdapter);
    }

    private void firebaseSync(){
        waterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                listNotification(status,Constants.WATER_CALL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERRORFIREBASE", "Failed to read value.", databaseError.toException());
            }
        });
        bathroomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                listNotification(status,Constants.BATHROOM_CALL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERRORFIREBASE", "Failed to read value.", databaseError.toException());
            }
        });
        discomfortRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                listNotification(status,Constants.DISCOMFORT_CALL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERRORFIREBASE", "Failed to read value.", databaseError.toException());
            }
        });
        emergencyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                listNotification(status,Constants.EMERGENCY_CALL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERRORFIREBASE", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void listNotification(Integer status, String callType){
        boolean alreadyNotified = false;
        int i;
        for(i=0; i < callListItems.size() ;i++) {
            if (callListItems.get(i).getCallType().equals(callType)){
                alreadyNotified=true;
                break;
            }
        }
        //Toast.makeText(mContext,"entrou",Toast.LENGTH_LONG).show();
        if(!status.equals(Constants.CALL_STATUS_INITIALIZATION) && !alreadyNotified){
            callListItems.add(new CallItem(callType,"Paciente",status));
        }
        else if((status.equals(Constants.CALL_STATUS_ON_THE_WAY)||status.equals(Constants.CALL_STATUS_SERVED)) && alreadyNotified){
            if(callListItems.get(i)!=null)
                callListItems.get(i).setStatus(status);
        }

        callAdapter.notifyDataSetChanged();
    }

    /*private void checkForWifi(){
        final WifiManager wifiManager = (WifiManager) this.getSystemService(mContext.WIFI_SERVICE);

        WifiInfo info = wifiManager.getConnectionInfo();

        final List<ScanResult> results = wifiManager.getScanResults();

        if (results != null) {
            for (int i=0 ; i < results.size(); i++) {
                //Tenta pelo BSSID e pelo SSID
                if(results.get(i).BSSID.equalsIgnoreCase("5e:cf:7f:2d:65:01") || results.get(i).SSID.equalsIgnoreCase("CuidAqui")){
                    network = results.get(i);
                    wifiListTextView.setText("Controle Encontrado digite as credênciais da rede para configurá-lo");

                    configButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Connect to remote control wifi
                            WifiConfiguration conf = new WifiConfiguration();
                            conf.SSID = "\"" + network.SSID + "\"";
                            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                            int networkID = wifiManager.addNetwork(conf);

                            wifiManager.disconnect();
                            wifiManager.enableNetwork(networkID, true);
                            wifiManager.reconnect();

                            //wifiCredentialsWebView.loadUrl("http://192.168.4.1");

                            //fazer a chamada http com as credenciais da rede e deposi desconectar e voltar para a rede antiga
                            //http://192.168.4.1/wifisave?s=Mello&p=39452119

                            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.4.1/").build();

                            final RemoteControlAPI remoteControlAPI = retrofit.create(RemoteControlAPI.class);

                            //GET REQUEST
                            Thread t = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        //check if connected!
                                            wifiManager.getConnectionInfo().getSupplicantState()!= SupplicantState.ASSOCIATED;
                                            //Wait to connect
                                            Thread.sleep(4000);


                                        Call<String> call = remoteControlAPI.sendWifiRemoteControlCredentials(ssidEditText.getText().toString(),passwordEditText.getText().toString());
                                        call.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                Log.d("TESTE","respondeu sucess");
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Log.d("TESTE","respondeu falha");
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            t.start();
                        }
                    });
                }
            }
        }
    }*/

    public boolean isGooglePlayServicesAvailable(final Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        isGooglePlayServicesAvailable(activity);
                    }
                }).show();
            }
            return false;
        }
        return true;
    }
}

