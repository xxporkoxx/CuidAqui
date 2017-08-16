package com.project.diegomello.cuidaqui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.diegomello.cuidaqui.models.CallItem;
import com.project.diegomello.cuidaqui.models.Patient;
import com.project.diegomello.cuidaqui.utils.Constants;
import com.project.diegomello.cuidaqui.utils.Utils;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.project.diegomello.cuidaqui.R.id.section_two_fragment_listView;
import static com.project.diegomello.cuidaqui.utils.Utils.mContext;

/**
 * Created by DiegoMello on 4/26/2017.
 */

public class SectionTwoFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private MediaPlayer mWaitingAtendenceSound;

    private Socket mSocket;
    private Emitter.Listener mConnectCallBack;
    private Emitter.Listener mNewCallBack;
    private Emitter.Listener mSolveCallBack;
    private Emitter.Listener mConnectCentralSucess;
    private Emitter.Listener mConnectCentralError;

    private ListView sectionTwoListView;
    private ProgressBar mProgressBar;

    private Gson mParser;

    private SectionTwoCallAdapter mSectionTwoCallAdapter;

    private RestApiAdapter mRestApiAdapter;


    public SectionTwoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SectionTwoFragment newInstance(int sectionNumber) {
        SectionTwoFragment fragment = new SectionTwoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.section_two_fragment, container, false);
        ButterKnife.bind(this, rootView);

        sectionTwoListView = (ListView) rootView.findViewById(section_two_fragment_listView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.section_two_fragment_progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_ATOP);

        mRestApiAdapter =  RestApiAdapter.getInstance();
        this.mParser=  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        retriveCallListFromAPI();
        return rootView;
    }

    public void refresh(){
        sectionTwoListView.setVisibility(View.GONE);
        retriveCallListFromAPI();
        sectionTwoListView.setVisibility(View.VISIBLE);
        mSectionTwoCallAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:  // it is going to refer the search id name in main.xml
                retriveCallListFromAPI();
                mSectionTwoCallAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void retriveCallListFromAPI(){
        mProgressBar.setVisibility(View.VISIBLE);
        sectionTwoListView.setVisibility(View.GONE);
        mRestApiAdapter.getCallsRestApi(new Callback<ArrayList<CallItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CallItem>> call, Response<ArrayList<CallItem>> response) {
                    if(response.isSuccessful()){
                        final ArrayList<CallItem> callitems = response.body();

                        mRestApiAdapter.getPatientsRestApi(new Callback<ArrayList<Patient>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Patient>> call, Response<ArrayList<Patient>> response) {
                                if (response.isSuccessful()) {
                                    ArrayList<Patient> patients = response.body();

                                    Log.d("RESPONSE", "REsposta");
                                    mSectionTwoCallAdapter = new SectionTwoCallAdapter(mContext, patients, callitems);
                                    sectionTwoListView.setAdapter(mSectionTwoCallAdapter);
                                    mProgressBar.setVisibility(View.GONE);
                                    sectionTwoListView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<Patient>> call, Throwable t) {
                                Log.d("RESPONSE", "Falhou");
                                mProgressBar.setVisibility(View.VISIBLE);
                                sectionTwoListView.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        Log.d("ERRORONRESPONSE", "Error message:" + response.message());
                        Toast.makeText(mContext, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                        mProgressBar.setVisibility(View.VISIBLE);
                        sectionTwoListView.setVisibility(View.GONE);
                    }
                }

            @Override
            public void onFailure(Call<ArrayList<CallItem>> call, Throwable t) {
                Log.d("ERRORONRESPONSE", "Error message:" + t.getMessage());
                t.printStackTrace();
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mWaitingAtendenceSound = MediaPlayer.create(mContext,R.raw.waiting_atendence_sound);
        mWaitingAtendenceSound.setLooping(true);
        try {
            mSocket = IO.socket(Constants.BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.on(Constants.socketConnected, mConnectCallBack = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socketConnected",(String)args[0]);
            }
        });
        Log.d("FIREBASEID",FirebaseInstanceId.getInstance().getToken());
//        mSocket.emit(Constants.CONNECT_CENTRAL_TO_SOCKET, (FirebaseInstanceId.getInstance().getToken()==null)?"fCaWbbhCtf8:APA91bEwH0_yM5pd6jujveDxHQUz8iN5Qye4d53HPNp95or-oARtspCtRq5tgv1xJ3a68QUmouzw7erggGR_KXTqmZog2j4UOx0kYqOLtUhl87N0_BJuzXh-4bH2vO0mQbgJX8q9Qcxf":FirebaseInstanceId.getInstance().getToken());
        mSocket.emit(Constants.CONNECT_CENTRAL_TO_SOCKET, FirebaseInstanceId.getInstance().getToken());
        mSocket.on(Constants.CONNECT_CENTRAL_SUCESS_EMIT, mConnectCentralSucess = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("CONNECT_CENTRAL_SUCESS",(args[0]).toString());
                ((MainActivity)Utils.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Utils.mContext,"Conectado ao Serviço",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        mSocket.on(Constants.CONNECT_CENTRAL_ERROR_EMIT, mConnectCentralError = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("CONNECT_CENTRAL_ERROR",(args[0]).toString());
                ((MainActivity)Utils.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Utils.mContext,"Falaha so Conectar ao Serviço",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        mSocket.on(Constants.NEW_CALL_ON_SOCKET_CALLBACK, mNewCallBack = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("NEW_CALL_ON_SOCKET",(args[0]).toString());
                ((MainActivity)Utils.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Utils.mContext,"Nova Chamada",Toast.LENGTH_LONG).show();
                        CallItem receivedcallItem = mParser.fromJson(args[0].toString(),CallItem.class);
                        mSectionTwoCallAdapter.receivedCallItem(receivedcallItem);
                    }
                });
            }
        });
        mSocket.on(Constants.SOLVE_CALL_SOCKET_CALLBACK, mSolveCallBack = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("SOLVE_CALL_ON_SOCKET",(args[0]).toString());
                ((MainActivity)Utils.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Utils.mContext,"Chamada Resolvida",Toast.LENGTH_LONG).show();
                        CallItem solvedcallItem = mParser.fromJson(args[0].toString(),CallItem.class);
                        mSectionTwoCallAdapter.solveCallItem(solvedcallItem);
                        mSectionTwoCallAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.emit(Constants.DISCONNECT_CENTRAL,FirebaseInstanceId.getInstance().getToken().trim());
        mSocket.disconnect();
        mSocket.off(Constants.NEW_CALL_ON_SOCKET_CALLBACK, mConnectCallBack);
        mSocket.off(Constants.NEW_CALL_ON_SOCKET_CALLBACK, mNewCallBack);
        mSocket.off(Constants.NEW_CALL_ON_SOCKET_CALLBACK, mSolveCallBack);
        mSocket.off(Constants.NEW_CALL_ON_SOCKET_CALLBACK, mConnectCentralSucess);
        mSocket.off(Constants.NEW_CALL_ON_SOCKET_CALLBACK, mConnectCentralError);
    }
}
