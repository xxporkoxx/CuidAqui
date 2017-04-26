package com.project.diegomello.cuidaqui;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.diegomello.cuidaqui.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DiegoMello on 4/26/2017.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class SectionOneFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    @BindView(R.id.password_EditText)
    EditText ssidEditText;
    @BindView(R.id.ssid_EditText)
    EditText passwordEditText;
    @BindView(R.id.config_Button)
    Button configButton;
    @BindView(R.id.calls_listView)
    ListView callListView;
    @BindView(R.id.section_one_empty_list_imageView)
    ImageView emptyListImageView;

    Context mContext;

    private ScanResult network;

    private FirebaseDatabase mDatabase;

    private DatabaseReference waterRef;
    private DatabaseReference bathroomRef;
    private DatabaseReference discomfortRef;
    private DatabaseReference emergencyRef;

    private CallsAdapter callAdapter;
    private ArrayList<CallItem> callListItems;

    public SectionOneFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SectionOneFragment newInstance(int sectionNumber) {
        SectionOneFragment fragment = new SectionOneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.section_one_fragment, container, false);
        //callListView = (ListView) rootView.findViewById(R.id.calls_listView);
        //ssidEditText = (EditText) rootView.findViewById(R.id.ssid_EditText);
        //passwordEditText = (EditText) rootView.findViewById(R.id.password_EditText);
        //configButton = (Button) rootView.findViewById(R.id.config_Button);
        ButterKnife.bind(this, rootView);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();

        mDatabase = FirebaseDatabase.getInstance();
        waterRef = mDatabase.getReference(Constants.WATER_CALL);
        bathroomRef = mDatabase.getReference(Constants.BATHROOM_CALL);
        discomfortRef = mDatabase.getReference(Constants.DISCOMFORT_CALL);
        emergencyRef = mDatabase.getReference(Constants.EMERGENCY_CALL);
        passwordEditText.setVisibility(View.GONE);
        ssidEditText.setVisibility(View.GONE);
        configButton.setVisibility(View.GONE);
        setupListView();
        //TODO implement wi fi configutration on app
        //checkForWifi();

        firebaseSync();
    }

    private void setupListView(){
        callListItems = new ArrayList<>();
        callAdapter = new CallsAdapter(mContext,callListItems,waterRef,bathroomRef,discomfortRef, emergencyRef);
        callListView.setAdapter(callAdapter);
        callListView.setEmptyView(emptyListImageView);
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
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
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
}