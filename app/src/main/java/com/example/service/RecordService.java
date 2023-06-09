package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class RecordService extends Service {
    public static final String ACTION_RESULT = "com.example.classproject.ACTION_RESULT";
    public static final String EXTRA_RESULT = "result";
    private static final String TAG = "Service";
    private final IBinder binder = new LocalBinder();
    private SpeechRecognizer speechRecognizer;
    private ArrayList<String> sttResult;
    public class LocalBinder extends Binder {
        public RecordService getService() {
            return RecordService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void sendResult(ArrayList<String> result) {
        Intent intent = new Intent(ACTION_RESULT);
        intent.putExtra(EXTRA_RESULT, result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public ArrayList<String> getSttResult() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(RecordService.this);
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d(TAG, "1");
                ArrayList<String> resultList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                sendResult(resultList);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        };
        speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        speechRecognizer.setRecognitionListener(listener);
        return sttResult;
    }
}
