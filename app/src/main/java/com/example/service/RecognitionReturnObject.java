package com.example.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecognitionReturnObject implements Serializable {
    @SerializedName("recognized")
    private String recognized;

    public String getRecognized() {
        return recognized;
    }
    public void setRecognized(String recognized) {
        this.recognized = recognized;
    }

}
