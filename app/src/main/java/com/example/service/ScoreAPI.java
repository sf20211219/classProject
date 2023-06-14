package com.example.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ScoreAPI {
    @POST("WiseASR/Pronunciation")
    Call<PronunciationArgumentResponse> evalutePronunciation(
            @Header("Authorization") String authorization,
            @Body PronunciationRequest request
    );
}
