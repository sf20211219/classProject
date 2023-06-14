package com.example.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PronunciationRequest implements Serializable {
    @SerializedName("argument")
    private PronunciationArgumentRequest argument;

    public PronunciationArgumentRequest getArgument() {
        return argument;
    }

    public void setArgument(PronunciationArgumentRequest argument) {
        this.argument = argument;
    }
}

