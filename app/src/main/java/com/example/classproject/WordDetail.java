package com.example.classproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class WordDetail extends AppCompatActivity {
    private Button backBtn, practiceBtn;
    private TextView tvData, wData;
    private VideoView videoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_word);

        int buttonId = getIntent().getIntExtra("buttonId", 0);

        backBtn = findViewById(R.id.backBtn);
        practiceBtn = findViewById(R.id.Btn);
        tvData = findViewById(R.id.textView);
        wData = findViewById(R.id.wordView);
        videoData = findViewById(R.id.videoView);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPractice();
            }
        });

        wData.setText(getIntent().getStringExtra("word"));
        tvData.setText(getIntent().getStringExtra("detail"));

        //videoData.setVideoPath(pronunciationUrl);
        //videoData.start();
    }

    private void goBack() {
        Intent intent = new Intent(this, WordListActivity.class);
        startActivity(intent);
    }

    private void goToPractice() {
        int buttonId = getIntent().getIntExtra("buttonId", 0);
        String word = getIntent().getStringExtra("word");
        String detail = getIntent().getStringExtra("detail");
        String voice = getIntent().getStringExtra("voice");

        Intent intent = new Intent(this, WordPractice.class);
        intent.putExtra("buttonId", buttonId);
        intent.putExtra("word", word);
        intent.putExtra("detail", detail);
        intent.putExtra("voice", voice);
        startActivity(intent);
    }
}
