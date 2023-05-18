package com.example.classproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class STTPage extends AppCompatActivity implements View.OnClickListener{
    private Button backBtn, sttBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_word);

        backBtn = (Button) findViewById(R.id.backBtn);
        sttBtn = (Button) findViewById(R.id.sttBtn);

        backBtn.setOnClickListener(this);
        sttBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                Intent intent1 = new Intent(getApplicationContext(), WordDetailPage.class);
                startActivity(intent1);
                break;
            case R.id.sttBtn:
                //api 사용 -> 서버 연결?
                break;
        }
    }
}
