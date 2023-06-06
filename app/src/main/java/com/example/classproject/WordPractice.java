package com.example.classproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.service.RecordService;

import java.util.ArrayList;

public class WordPractice extends AppCompatActivity {
    private static final String TAG = "WordPractice";
    private static final int PERMISSION = 1;
    private RecordService service;
    private Button backBtn, recordBtn;
    private TextView sttText;
    private boolean isBind = false;
    //private String
    private ArrayList<String> sttResult = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_word);

        checkPermission();

        backBtn = (Button) findViewById(R.id.backBtn);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        sttText = (TextView) findViewById(R.id.sttText);

        backBtn.setOnClickListener(click);
        recordBtn.setOnClickListener(click);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "시작");
        Intent serviceIntent = new Intent(WordPractice.this, RecordService.class);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "삭제");
        unbindService(conn);
        isBind = false;
    }


    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    Intent backIntent = new Intent(WordPractice.this, WordDetail.class);
                    startActivity(backIntent);
                    break;
                case R.id.recordBtn:
                    if (isBind) {
                        Log.d(TAG, "되나?");
                        sttResult = service.getSttResult();
                    }
                    break;
            }
            if (sttResult != null) {
                Log.d(TAG, "제발");
                for(int i = 0; i < sttResult.size(); i++) {
                    sttText.setText(sttResult.get(i));
                }
            }
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RecordService.LocalBinder binder = (RecordService.LocalBinder) iBinder;
            service = binder.getService();
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    };

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(WordPractice.this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(WordPractice.this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(WordPractice.this, new String[] {
                        android.Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO
                }, PERMISSION);
            }
        }
    }
}
