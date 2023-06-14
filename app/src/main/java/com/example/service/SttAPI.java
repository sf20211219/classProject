package com.example.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SttAPI {
    @POST("WiseASR/Recognition")
    Call<RecognitionArgumentResponse> evaluteRecognition(
            @Header("Authorization") String authorization,
            @Body RecognitionRequest request
    );
}
