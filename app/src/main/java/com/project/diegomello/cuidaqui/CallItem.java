package com.project.diegomello.cuidaqui;

/**
 * Created by DiegoMello on 2/1/2017.
 */

public class CallItem {
    private String name;
    private Integer status;
    private String callType;

    CallItem(String callType,String name, Integer status){
        this.callType = callType;
        this.name =name;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCallType() {
        return callType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
