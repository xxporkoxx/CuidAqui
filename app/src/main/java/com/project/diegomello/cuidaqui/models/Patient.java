package com.project.diegomello.cuidaqui.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DiegoMello on 5/30/2017.
 */

public class Patient {
    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private String name;

    @SerializedName("age")
    private Integer age;

    @SerializedName("gender")
    private char gender;

    @SerializedName("disease")
    private String disease;

    @SerializedName("patientdegree")
    private Integer patientdegree;

    @SerializedName("caretakers")
    private List<String> caretakers;

    @SerializedName("calls")
    private List<String> calls;

    Patient(String name, Integer age, char gender, String disease, Integer patientdegree, List<String> caretakers, List<String>calls){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.patientdegree = patientdegree;
        this.caretakers = caretakers;
        this.calls = calls;
    }

    public String getName() {
        return name;
    }


    public Integer getAge() {
        return age;
    }

    public char getGender() {
        return gender;
    }

    public List<String> getCaretakers() {
        return caretakers;
    }
    public boolean isCaretakersArrayEmpty(){
        if(caretakers.size()>0)
            return false;
        return true;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getCalls() {
        return calls;
    }

    public void setCalls(List<String> calls) {
        this.calls = calls;
    }
}
