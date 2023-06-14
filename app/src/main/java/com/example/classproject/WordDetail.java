package com.example.classproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import android.util.Log;
import android.media.MediaPlayer;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WordDetail extends AppCompatActivity {
    private Button backBtn, startBtn, practiceBtn;
    private TextView tvData, wData;
    private VideoView vv;
    private boolean videoFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_word);

        backBtn = findViewById(R.id.backBtn);
        startBtn = findViewById(R.id.startBtn);
        practiceBtn = findViewById(R.id.Btn);
        tvData = findViewById(R.id.textView);
        wData = findViewById(R.id.wordView);
        vv = findViewById(R.id.vv);

        String num = getIntent().getStringExtra("num");
        String serverUrl = "http://서버주소/video/" + num + ".mp4";
        Uri uri = Uri.parse(serverUrl);
        vv.setVideoURI(uri);
        vv.requestFocus();

        String detailText = getIntent().getStringExtra("detail");
        String formattedJsonText = detailText.replace("\\n", "\n");

        wData.setText(getIntent().getStringExtra("word"));
        tvData.setText(formattedJsonText);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.start();
                startBtn.setVisibility(View.GONE);
            }
        });

        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPractice(serverUrl);
            }
        });

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.seekTo(0);
            }
        });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoFinished = true;
                practiceBtn.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.VISIBLE);
                vv.seekTo(0);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent(this, WordListActivity.class);
        startActivity(intent);
    }

    private void goToPractice(String serverUrl) {
        Intent intent = new Intent(this, WordPractice.class);
        intent.putExtra("buttonId", getIntent().getIntExtra("buttonId", 0));
        intent.putExtra("num", getIntent().getStringExtra("num"));
        intent.putExtra("word", getIntent().getStringExtra("word"));
        intent.putExtra("detail", getIntent().getStringExtra("detail"));
        intent.putExtra("voice", getIntent().getStringExtra("voice"));
        intent.putExtra("uriPath", serverUrl);
        startActivity(intent);
    }
}
