package com.project.diegomello.cuidaqui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

import static com.project.diegomello.cuidaqui.utils.Utils.mContext;

/**
 * Created by DiegoMello on 4/26/2017.
 */

public class SectionTwoFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
    }

}
