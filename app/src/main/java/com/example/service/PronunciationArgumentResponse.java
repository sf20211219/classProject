package com.example.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PronunciationArgumentResponse implements Serializable {
    @SerializedName("request_id")
    private String requestId;

    @SerializedName("result")
    private int result;

    @SerializedName("return_object")
    private PronunciationReturnObject returnObject;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public PronunciationReturnObject getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(PronunciationReturnObject returnObject) {
        this.returnObject = returnObject;
    }
}
