package com.project.diegomello.cuidaqui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.diegomello.cuidaqui.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.project.diegomello.cuidaqui.utils.Utils.mContext;

/**
 * Created by DiegoMello on 4/26/2017.
 */

public class SectionTwoFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private SharedPreferences patientSharedPrefs;

    @BindView(R.id.section_two_fragment_emergency_textView)
    TextView emergencyTextView;

    @BindView(R.id.section_two_fragment_bathroom_textView)
    TextView bathroomTextView;

    @BindView(R.id.section_two_fragment_discomfort_textView)
    TextView discomfortTextView;

    @BindView(R.id.section_two_fragment_water_textView)
    TextView waterTextView;


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
        patientSharedPrefs = mContext.getSharedPreferences(Constants.PATIENT_SHARED_PREF, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCallCounter();
    }

    public void refreshCallCounter(){
        emergencyTextView.setText(mContext.getResources().getText(R.string.section_two_emergency_call_counter_label_string)+" "+patientSharedPrefs.getInt(Constants.PATIENT_CALL_COUNTER_EMERGENCY,0));
        bathroomTextView.setText(mContext.getResources().getText(R.string.section_two_bathroom_call_counter_label_string)+" "+patientSharedPrefs.getInt(Constants.PATIENT_CALL_COUNTER_BATHROOM,0));
        discomfortTextView.setText(mContext.getResources().getText(R.string.section_two_discomfort_call_counter_label_string)+" "+patientSharedPrefs.getInt(Constants.PATIENT_CALL_COUNTER_DISCOMFORT,0));
        waterTextView.setText(mContext.getResources().getText(R.string.section_two_water_call_counter_label_string)+" "+patientSharedPrefs.getInt(Constants.PATIENT_CALL_COUNTER_WATER,0));
    }
}
