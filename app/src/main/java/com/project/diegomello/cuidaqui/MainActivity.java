package com.project.diegomello.cuidaqui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.project.diegomello.cuidaqui.utils.Constants;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private SectionOneFragment mSectionOneFragment;
    private SectionTwoFragment mSectionTwoFragment;
    private SectionThreeFragment mSectionThreeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        isGooglePlayServicesAvailable(this);
        createTabViews();
    }

    protected void createTabViews(){
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        isGooglePlayServicesAvailable(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rename_patient) {
            buildRenamePersonDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void buildRenamePersonDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.MyAlertDialogStyle);
        alertDialog.setTitle("Renomear Paciente");
        alertDialog.setMessage("Escreva o nome do seu paciente:");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //lp.setMargins(10,0,10,0);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(android.R.drawable.ic_menu_edit);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       /* password = input.getText().toString();
                        if (password.compareTo("") == 0) {
                            if (pass.equals(password)) {
                                Toast.makeText(getApplicationContext(),
                                        "Password Matched", Toast.LENGTH_SHORT).show();
                                Intent myIntent1 = new Intent(view.getContext(),
                                        Show.class);
                                startActivityForResult(myIntent1, 0);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }*/

                        SharedPreferences settings = getSharedPreferences(Constants.PACIENT_SHARED_PREF, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        String patientName = input.getText().toString();
                        editor.putString(Constants.PACIENT_SHARED_PREF_NAME_STRING,patientName);
                        editor.commit();

                        Toast.makeText(mContext, patientName+" "+getResources().getString(R.string.rename_patient_sucesfull_toast_text),Toast.LENGTH_LONG).show();

                        mSectionOneFragment.refreshPatientName(patientName);
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a SectionOneFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return mSectionOneFragment = SectionOneFragment.newInstance(position);
                case 1:
                    return mSectionTwoFragment = SectionTwoFragment.newInstance(position);
                case 2:
                    return mSectionThreeFragment = SectionThreeFragment.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CHAMADAS";
                case 1:
                    return "MENSAL";
                case 2:
                    return "HISTÓRICO";
            }
            return null;
        }
    }
}

