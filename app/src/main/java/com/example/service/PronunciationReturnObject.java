package com.example.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PronunciationReturnObject implements Serializable {
    @SerializedName("recognized")
    private String recognized;

    @SerializedName("score")
    private int score;

    public String getRecognized() {
        return recognized;
    }

    public void setRecognized(String recognized) {
        this.recognized = recognized;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
