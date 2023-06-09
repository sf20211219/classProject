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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.service.RecordService;

import java.util.ArrayList;

public class WordPractice extends AppCompatActivity {
    private static final String TAG = "WordPractice";
    private static final int PERMISSION = 1;
    private RecordService service;
    private Button backBtn, recordBtn;
    private TextView sttText, wData, tData;
    private boolean isBind = false;
    //private String
    private ArrayList<String> sttResult = null;

    private int buttonId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_word);

        checkPermission();

        Intent intent = getIntent(); // Intent 객체를 먼저 가져옴
        buttonId = intent.getIntExtra("buttonId", 0); // 전달받은 버튼 ID 저장

        backBtn = (Button) findViewById(R.id.backBtn);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        sttText = (TextView) findViewById(R.id.sttText);
        wData = (TextView) findViewById(R.id.wordView);
        tData = (TextView) findViewById(R.id.voiceTextView);
        String word = getIntent().getStringExtra("word");
        String voice = getIntent().getStringExtra("voice");

        wData.setText(word);
        tData.setText(voice);

        backBtn.setOnClickListener(click);
        recordBtn.setOnClickListener(click);

        LocalBroadcastManager.getInstance(this).registerReceiver(resultReceiver, new IntentFilter(RecordService.ACTION_RESULT));
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(WordPractice.this, RecordService.class);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    private BroadcastReceiver resultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> resultList = intent.getStringArrayListExtra(RecordService.EXTRA_RESULT);
            if (resultList != null) {
                for (int i = 0; i < resultList.size(); i++) {
                    sttText.setText(resultList.get(i));
                }
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(conn);
        isBind = false;
    }

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String word = getIntent().getStringExtra("word");
            String detail = getIntent().getStringExtra("detail");
            String voice = getIntent().getStringExtra("voice");
            switch (v.getId()) {
                case R.id.backBtn:
                    Intent backIntent = new Intent(WordPractice.this, WordDetail.class);
                    backIntent.putExtra("buttonId", buttonId);
                    backIntent.putExtra("word", word);
                    backIntent.putExtra("detail", detail);
                    backIntent.putExtra("voice", voice);
                    startActivity(backIntent);
                    break;
                case R.id.recordBtn:
                    if (isBind) {
                        sttResult = service.getSttResult();
                    }
                    break;
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
