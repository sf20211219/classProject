package com.example.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class RecordService extends Service {
    private static final String TAG = "Service";
    public static final String ACTION_STT = "com.example.classproject.ACTION_STT";
    public static final String ACTION_SCORE = "com.example.classproject.ACTION_SCORE";
    public static final String STT_RESULT = "sttResult";
    public static final String SCORE_RESULT = "scoreResult";
    private final IBinder binder = new LocalBinder();


    private static final String PREFS_NAME = "prefs";
    private boolean isRecording = false;
    private boolean forceStop = false;
    private int maxLenSpeech = 16000 * 45;
    private byte[] speechData = new byte[maxLenSpeech * 2];
    private int lenSpeech = 0;

    public class LocalBinder extends Binder {
        public RecordService getService() {
            return RecordService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    private void sendSTTResult(String result) {
        Log.d(TAG, "sttResult");
        Intent intent = new Intent(ACTION_STT);
        intent.putExtra(STT_RESULT, result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendScoreResult(String result) {
        Log.d(TAG, "scoreResult");
        Intent intent = new Intent(ACTION_SCORE);
        intent.putExtra(SCORE_RESULT, result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    public void getResult() {
        if (isRecording) {
            forceStop = true;
        } else {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            recordSpeech();
                        } catch (RuntimeException e) {
                            return;
                        }

                        Thread threadRecog = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getSttResult();
                                getScoreResult();
                            }
                        });
                        threadRecog.start();
                        try {
                            threadRecog.join(20000);
                            if (threadRecog.isAlive()) {
                                threadRecog.interrupt();
                            }
                        } catch (InterruptedException e) {
                            Log.d(TAG, "1");
                        }
                    }
                }).start();
            } catch (Throwable t) {
                isRecording = false;
            }
        }
    }

    private void recordSpeech() {
        try {
            int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            @SuppressLint("MissingPermission") AudioRecord audio = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            lenSpeech = 0;
            if (audio.getState() != AudioRecord.STATE_INITIALIZED) {
                throw new RuntimeException("error");
            } else {
                short[] inBuffer = new short[bufferSize];
                forceStop = false;
                isRecording = true;
                audio.startRecording();
                while (!forceStop) {
                    int ret = audio.read(inBuffer, 0, bufferSize);
                    for (int i = 0; i < ret; i++) {
                        if (lenSpeech >= maxLenSpeech) {
                            forceStop = true;
                            break;
                        }
                        speechData[lenSpeech * 2] = (byte) (inBuffer[i] & 0x00FF);
                        speechData[lenSpeech * 2 + 1] = (byte) ((inBuffer[i] & 0xFF00) >> 8);
                        lenSpeech++;
                    }
                }
                audio.stop();
                audio.release();
                isRecording = false;
            }
        } catch (Throwable t) {
            throw new RuntimeException(t.toString());
        }
    }

    private void getSttResult() {
        Log.d(TAG, "getStt");
        String sttApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition/";
        String accessKey = "";
        String languageCode = "korean";
        String audioContents = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioContents = Base64.getEncoder().encodeToString(speechData);
        }

        Retrofit sttRetrofit = new Retrofit.Builder()
                .baseUrl(sttApiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecognitionArgumentRequest argument = new RecognitionArgumentRequest();
        argument.setAudio(audioContents);
        argument.setLanguageCode(languageCode);

        RecognitionRequest request = new RecognitionRequest();
        request.setArgument(argument);

        SttAPI sttRetrofitAPI = sttRetrofit.create(SttAPI.class);
        Call<RecognitionArgumentResponse> call = sttRetrofitAPI.evaluteRecognition(accessKey, request);
        call.enqueue(new Callback<RecognitionArgumentResponse>() {
            @Override
            public void onResponse(Call<RecognitionArgumentResponse> call, Response<RecognitionArgumentResponse> response) {
                Log.d(TAG, "sttResponse");
                if (response.isSuccessful()) {
                    RecognitionArgumentResponse body = response.body();
                    if (body != null) {
                        Log.d(TAG, "sttResponse2");
                        RecognitionReturnObject returnObject = body.getReturnObject();
                        String result = returnObject.getRecognized();
                        sendSTTResult(result);
                    }
                }
            }
            @Override
            public void onFailure(Call<RecognitionArgumentResponse> call, Throwable t) {

            }
        });
    }

    public void getScoreResult() {
        Log.d(TAG, "getScore");
        String scoreApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/PronunciationKor/";
        String accessKey = "";
        String languageCode = "korean";
        String script = "단어";
        String audioContents = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioContents = Base64.getEncoder().encodeToString(speechData);
        }

        Retrofit scoreRetrofit = new Retrofit.Builder()
                .baseUrl(scoreApiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PronunciationArgumentRequest argument = new PronunciationArgumentRequest();
        argument.setAudio(audioContents);
        argument.setLanguageCode(languageCode);
        argument.setScript(script);

        PronunciationRequest request = new PronunciationRequest();
        request.setArgument(argument);


        ScoreAPI scoreRetrofitAPI = scoreRetrofit.create(ScoreAPI.class);


        Call<PronunciationArgumentResponse> call = scoreRetrofitAPI.evalutePronunciation(accessKey, request);
        call.enqueue(new Callback<PronunciationArgumentResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<PronunciationArgumentResponse> call, Response<PronunciationArgumentResponse> response) {
                Log.d(TAG, "scoreResponse");
                if (response.isSuccessful()) {
                    PronunciationArgumentResponse body = response.body();
                    if (body != null) {
                        Log.d(TAG, "scoreResponse2");
                        PronunciationReturnObject returnObject = body.getReturnObject();
                        String result = returnObject.getRecognized();
                        sendScoreResult(result);
                    }
                }
            }
            @Override
            @EverythingIsNonNull
            public void onFailure(Call<PronunciationArgumentResponse> call, Throwable t) {

            }
        });
    }
}
