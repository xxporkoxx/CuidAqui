package com.project.diegomello.cuidaqui.models;

import com.google.gson.annotations.SerializedName;
import com.project.diegomello.cuidaqui.utils.Constants;

import java.sql.Date;

/**
 * Created by DiegoMello on 2/1/2017.
 */

public class CallItem implements Comparable<CallItem> {
    @SerializedName("_id")
    private String _id;

    private String name;

    @SerializedName("callstatus")
    private Integer callstatus;

    @SerializedName("calltype")
    private String callType;

    @SerializedName("created_at")
    private Date created_at;

    @SerializedName("updated_at")
    private Date updated_at;

    @SerializedName("call_solved_at")
    private Date call_solved_at;

    private int listItemType = Constants.SECTION_TWO_NORMAL_CALL_TYPE_ITEM;

    public CallItem(String callType,String name, Integer status){
        this.callType = callType;
        this.name =name;
        this.callstatus = status;
    }

    public CallItem(String id,String name,int listItemType,Date call_solved_at){
        this._id = id;
        this.name =name;
        this.call_solved_at = call_solved_at;
        this.listItemType = listItemType;
    }

    public Integer getCallstatus() {
        return callstatus;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallstatus(Integer callstatus) {
        this.callstatus = callstatus;
    }

    public String getName() {
        return name;
    }

    public void setPatientName(String name) {
        this.name = name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getCall_solved_at() {
        return call_solved_at;
    }

    public void setCall_solved_at(Date call_solved_at) {
        this.call_solved_at = call_solved_at;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int compareTo(CallItem callItem) {
        //if(this.call_solved_at == callItem.call_solved_at)
          //  return 0;
        if(this.call_solved_at ==null ){//Não foi resolvido
            return -1;
        }else{
            return 1;
        }
    }

    public int getListItemType() {
        return listItemType;
    }

    public void setListItemType(int listItemType) {
        this.listItemType = listItemType;
    }
}
