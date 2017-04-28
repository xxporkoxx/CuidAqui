package com.project.diegomello.cuidaqui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.project.diegomello.cuidaqui.utils.Constants;

import java.util.List;

/**
 * Created by DiegoMello on 2/1/2017.
 */

public class CallsAdapter extends ArrayAdapter<CallItem> {

    private Context mContext;
    private List<CallItem> callItems;
    private LayoutInflater mInflater;
    private ViewHolder mViewHolder;

    private DatabaseReference waterRef;
    private DatabaseReference bathroomRef;
    private DatabaseReference discomfortRef;
    private DatabaseReference emergency_ref;

    public CallsAdapter(Context context, List<CallItem> objects, DatabaseReference waterRef,DatabaseReference bathroomRef,DatabaseReference discomfortRef,DatabaseReference emergency_ref) {
        super(context, 0, objects);
        this.mContext = context;
        this.callItems = objects;
        this.waterRef = waterRef;
        this.bathroomRef = bathroomRef;
        this.discomfortRef = discomfortRef;
        this.emergency_ref = emergency_ref;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_call, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.callLinearLayout = (LinearLayout) convertView.findViewById(R.id.list_item_call_linearLayout);
            mViewHolder.callIconImageView = (ImageView) convertView.findViewById(R.id.list_item_call_icon_image);
            mViewHolder.callNameTextView = (TextView) convertView.findViewById(R.id.list_item_call_name_textView);
            mViewHolder.callStatusTextView = (TextView) convertView.findViewById(R.id.list_item_call_status_textView);
            mViewHolder.callActionButton = (Button) convertView.findViewById(R.id.list_item_call_action_button);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }

        CallItem item = callItems.get(position);
        final String callType = item.getCallType();
        final Integer status = item.getStatus();
        int linearLayoutColor = 0;
        int nameAndStatustextColor = R.color.white;

        if (callType.equals(Constants.WATER_CALL)) {
            mViewHolder.callIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_thirsty_36dp));
            linearLayoutColor = R.color.thirstyBlue;
        }
        else if(callType.equals(Constants.BATHROOM_CALL)){
            mViewHolder.callIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_bathroom_36dp));
            linearLayoutColor = R.color.bathroomGreen;
        }
        else if(callType.equals(Constants.DISCOMFORT_CALL)){
            mViewHolder.callIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_assistence_36dp));
            linearLayoutColor = R.color.assistenceYellow;
            nameAndStatustextColor = R.color.black;
        }
        else if(callType.equals(Constants.EMERGENCY_CALL)){
            mViewHolder.callIconImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_emergency_36dp));
            linearLayoutColor = R.color.sosRed;
        }

        mViewHolder.callLinearLayout.setBackgroundColor(mContext.getResources().getColor(linearLayoutColor));
        mViewHolder.callNameTextView.setTextColor(mContext.getResources().getColor(nameAndStatustextColor));
        mViewHolder.callStatusTextView.setTextColor(mContext.getResources().getColor(nameAndStatustextColor));

        if (status.equals(Constants.CALL_STATUS_INITIALIZATION)) {
            //mViewHolder.callStatusTextView.setText("");
        }
        else if(status.equals(Constants.CALL_STATUS_WATING_TO_SERVE)){
            mViewHolder.callStatusTextView.setText(R.string.call_waiting_to_serve_text);
            mViewHolder.callActionButton.setText(R.string.call_waiting_button_text);
        }
        else if(status.equals(Constants.CALL_STATUS_ON_THE_WAY)){
            mViewHolder.callStatusTextView.setText(R.string.call_on_the_way_text);
            mViewHolder.callActionButton.setText(R.string.call_on_the_way_button_text);
        }
        else if(status.equals(Constants.CALL_STATUS_SERVED)){
            mViewHolder.callStatusTextView.setText(R.string.call_served_text);
            mViewHolder.callActionButton.setText(R.string.call_served_button_text);
        }

        mViewHolder.callNameTextView.setText(item.getName());

        mViewHolder.callActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = null;
                if (callType.equals(Constants.WATER_CALL))
                    ref = waterRef;
                else if(callType.equals(Constants.BATHROOM_CALL))
                    ref = bathroomRef;
                else if(callType.equals(Constants.DISCOMFORT_CALL))
                    ref = discomfortRef;
                else if(callType.equals(Constants.EMERGENCY_CALL))
                    ref = emergency_ref;

                if(status.equals(Constants.CALL_STATUS_WATING_TO_SERVE))
                    ref.setValue(Constants.CALL_STATUS_ON_THE_WAY);
                else if(status.equals(Constants.CALL_STATUS_ON_THE_WAY))
                    ref.setValue(Constants.CALL_STATUS_SERVED);
                else if(status.equals(Constants.CALL_STATUS_SERVED)) {
                    ref.setValue(Constants.CALL_STATUS_INITIALIZATION);
                    callItems.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    public void refreshPatientName(String patientName){
        for(int i=0;i < callItems.size();i++){
            callItems.get(i).setPatientName(patientName);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        public LinearLayout callLinearLayout;
        public ImageView callIconImageView;
        public TextView callNameTextView;
        public TextView callStatusTextView;
        public Button callActionButton;
    }
}
