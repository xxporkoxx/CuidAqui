package com.project.diegomello.cuidaqui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.diegomello.cuidaqui.models.CallItem;
import com.project.diegomello.cuidaqui.models.Patient;
import com.project.diegomello.cuidaqui.utils.Constants;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by DiegoMello on 8/10/2017.
 */

public class SectionTwoCallAdapter extends ArrayAdapter<CallItem> {

    private Context mContext;
    private ArrayList<Patient> patients;
    private ArrayList<CallItem> callItems;

    private ViewHolder mViewHolder;
    private LayoutInflater mInflater;

    private SimpleDateFormat dateFormat;


    public SectionTwoCallAdapter(Context context, ArrayList<Patient> patients, ArrayList<CallItem> callItems) {
        super(context, 0, callItems);
        this.mContext = context;
        this.callItems = callItems;
        organizeListView();
        this.patients = patients;
        this.dateFormat = new SimpleDateFormat("hh:mm aa");
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public void solveCallItem(CallItem solvedItem){
        Integer foundedItemIndex = null;
        for(int i=0; i<callItems.size() ;i++) {
            if(callItems.get(i).get_id().equals(solvedItem.get_id())){
                foundedItemIndex =i;
            }
        }
        if(foundedItemIndex!=null){
            callItems.remove(callItems.get(foundedItemIndex));
            callItems.add(solvedItem);
        }
    }

    public void receivedCallItem(CallItem receivedCallItem){
        callItems.add(1,receivedCallItem);
        notifyDataSetChanged();
    }

    private void organizeListView(){
        Collections.sort(this.callItems, new Comparator<CallItem>() {
            public int compare(CallItem c1, CallItem c2) {
                return c1.getCreated_at().compareTo(c2.getCreated_at());
            }
        });

        CallItem aux;
        for(int k=0;k<callItems.size() ;k++){
            for (int j=0;j<callItems.size();j++){
                if((callItems.get(k).compareTo(callItems.get(j)))==-1){
                    aux = callItems.get(k);
                    callItems.set(k,callItems.get(j));
                    callItems.set(j,aux);
                }
            }
        }

        //Inserting Separators
        for(int i=0;i<callItems.size();i++){
            if(i == 0){
                callItems.add(i,new CallItem("SEPARATOR","SEPARATOR",Constants.SECTION_TWO_UNSOLVED_CALL_TYPE_DIVIDER_ITEM,null));
            }else{
                if(callItems.get(i).getCall_solved_at()!=null && callItems.get(i-1).getCall_solved_at()==null){
                    callItems.add(i,new CallItem("SEPARATOR","SEPARATOR",Constants.SECTION_TWO_SOLVED_CALL_TYPE_DIVIDER_ITEM, new Date(10,10,10)));
                    i++;
                }
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CallItem item = callItems.get(position);

            if(convertView == null){
                convertView = mInflater.inflate(R.layout.list_item_call_section_two_call, parent, false);
                mViewHolder = new ViewHolder();
                mViewHolder.itemLinearLayout = (LinearLayout) convertView.findViewById(R.id.list_item_section_two_call_linearLayout);
                mViewHolder.itemPatientNameTextView = (TextView) convertView.findViewById(R.id.list_item_section_two_call_patient_name_textView);
                mViewHolder.itemPatientNecessityTextView = (TextView) convertView.findViewById(R.id.list_item_section_two_call_pacient_necessity_textView);
                mViewHolder.itemIconImageView = (ImageView) convertView.findViewById(R.id.list_item_section_two_call_icon_image);
                mViewHolder.itemTimeTextView = (TextView) convertView.findViewById(R.id.list_item_section_two_call_time_textView);
                mViewHolder.itemStatusTextView = (TextView) convertView.findViewById(R.id.list_item_call_status_TextView);
                convertView.setTag(mViewHolder);
            }else{
                mViewHolder = (ViewHolder)convertView.getTag();
            }

            if(item.getListItemType() == Constants.SECTION_TWO_NORMAL_CALL_TYPE_ITEM){
                final String callType = item.getCallType();
                final Integer status = item.getCallstatus();
                int linearLayoutColor = R.color.especific;
                int nameAndStatustextColor = R.color.white;
                String necessityString = "Ajuda";

                if (callType.equals(Constants.WATER_CALL_NUMBER.toString())) {
                    mViewHolder.itemIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_thirsty_36dp));
                    linearLayoutColor = R.color.thirstyBlue;
                    necessityString = "Água";
                }
                else if(callType.equals(Constants.BATHROOM_CALL_NUMBER.toString())){
                    mViewHolder.itemIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_bathroom_36dp));
                    linearLayoutColor = R.color.bathroomGreen;
                    necessityString = "Casa de Banho";
                }
                else if(callType.equals(Constants.ASSISTENCE_CALL_NUMBER.toString())){
                    mViewHolder.itemIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_assistence_36dp));
                    linearLayoutColor = R.color.assistenceYellow;
                    nameAndStatustextColor = R.color.black;
                    necessityString = "Auxílio";
                }
                else if(callType.equals(Constants.EMERGENCY_CALL_NUMBER.toString())){
                    mViewHolder.itemIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_emergency_36dp));
                    linearLayoutColor = R.color.sosRed;
                    necessityString = "Emergência";
                }

                mViewHolder.itemLinearLayout.setBackgroundColor(mContext.getResources().getColor(linearLayoutColor));
                mViewHolder.itemPatientNameTextView.setTextColor(mContext.getResources().getColor(nameAndStatustextColor));
                mViewHolder.itemPatientNecessityTextView.setTextColor(mContext.getResources().getColor(nameAndStatustextColor));
                mViewHolder.itemStatusTextView.setTextColor(mContext.getResources().getColor(nameAndStatustextColor));
                mViewHolder.itemTimeTextView.setTextColor(mContext.getResources().getColor(nameAndStatustextColor));

                mViewHolder.itemStatusTextView.setVisibility(View.VISIBLE);
                mViewHolder.itemTimeTextView.setVisibility(View.VISIBLE);
                mViewHolder.itemIconImageView.setVisibility(View.VISIBLE);
                mViewHolder.itemIconImageView.setVisibility(View.VISIBLE);

                if(status == Constants.CALL_STATUS_WATING_TO_SERVE){
                    mViewHolder.itemStatusTextView.setText("Aguardando");
                    mViewHolder.itemLinearLayout.setAlpha(1f);
                }
                if(status == Constants.CALL_STATUS_SERVED) {
                    mViewHolder.itemStatusTextView.setText("Resolvido");
                    mViewHolder.itemLinearLayout.setAlpha(0.2f);
                }

                mViewHolder.itemPatientNameTextView.setText(findPatientName(item));
                mViewHolder.itemPatientNecessityTextView.setText(necessityString);

                mViewHolder.itemTimeTextView.setText(dateFormat.format(item.getCreated_at())+" ~ "+((item.getCall_solved_at()==null) ? "Em aberto" : dateFormat.format(item.getCall_solved_at())));
            }
            else{
                mViewHolder.itemStatusTextView.setVisibility(View.GONE);
                mViewHolder.itemTimeTextView.setVisibility(View.GONE);
                mViewHolder.itemIconImageView.setVisibility(View.GONE);
                mViewHolder.itemIconImageView.setVisibility(View.GONE);
                mViewHolder.itemLinearLayout.setAlpha(1f);
                mViewHolder.itemPatientNameTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                mViewHolder.itemLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                if(item.getListItemType() == Constants.SECTION_TWO_SOLVED_CALL_TYPE_DIVIDER_ITEM)
                    mViewHolder.itemPatientNameTextView.setText("Chamadas Atendidas:");
                else
                    mViewHolder.itemPatientNameTextView.setText("Chamadas em espera:");
            }

        return convertView;
    }


    private String findPatientName(CallItem callItem){
        for(int i=0;i < patients.size();i++){
            Patient patient = patients.get(i);
            for (int j=0;j < patient.getCalls().size();j++){
                if ((callItem.get_id()).equals(patient.getCalls().get(j)))
                    return patient.getName();
            }
        }
        return "SemNome";
    }

    private static class ViewHolder{
        public LinearLayout itemLinearLayout;
        public ImageView itemIconImageView;
        public TextView itemPatientNameTextView;
        public TextView itemPatientNecessityTextView;
        public TextView itemTimeTextView;
        public TextView itemStatusTextView;
    }

}
