package com.example.service;

import android.speech.RecognizerIntent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecognitionArgumentResponse implements Serializable {
    @SerializedName("request_id")
    private String requestId;

    @SerializedName("result")
    private int result;

    @SerializedName("return_object")
    private RecognitionReturnObject returnObject;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getResult() {
        return result;
    }

    public RecognitionReturnObject getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(RecognitionReturnObject returnObject) {
        this.returnObject = returnObject;
    }
}
