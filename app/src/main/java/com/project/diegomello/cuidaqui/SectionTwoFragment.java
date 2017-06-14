package com.project.diegomello.cuidaqui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.project.diegomello.cuidaqui.utils.Constants;
import com.project.diegomello.cuidaqui.utils.Utils;

import java.net.URISyntaxException;

import butterknife.ButterKnife;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
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
        Log.d("FIREBASEID",FirebaseInstanceId.getInstance().getToken());
        mSocket.on(Constants.socketConnected, mConnectCallBack = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socketConnected",(String)args[0]);
            }
        });
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
                Log.d("CONNECT_CENTRAL_SUCESS",(args[0]).toString());
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
            public void call(Object... args) {
                Log.d("NEW_CALL_ON_SOCKET",(args[0]).toString());
                ((MainActivity)Utils.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Utils.mContext,"RECEBEU",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        mSocket.on(Constants.SOLVE_CALL_SOCKET_CALLBACK, mSolveCallBack = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SOLVE_CALL_ON_SOCKET",(args[0]).toString());
                ((MainActivity)Utils.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Utils.mContext,"RECEBEU",Toast.LENGTH_LONG).show();
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
