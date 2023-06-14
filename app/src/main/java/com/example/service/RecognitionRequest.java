package com.example.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecognitionRequest implements Serializable {
    @SerializedName("argument")
    private RecognitionArgumentRequest argument;

    public RecognitionArgumentRequest getArgument() {
        return argument;
    }
    public void setArgument(RecognitionArgumentRequest argument) {
        this.argument = argument;
    }
}
