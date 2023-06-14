package com.example.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecognitionArgumentRequest implements Serializable {
    @SerializedName("language_code")
    private String languageCode;

    @SerializedName("audio")
    private String audio;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
