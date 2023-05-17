package com.example.classproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WordDetailPage extends AppCompatActivity implements View.OnClickListener{
    private Button backBtn, Btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_word);

        backBtn = (Button) findViewById(R.id.backBtn);
        Btn = (Button) findViewById(R.id.Btn);

        backBtn.setOnClickListener(this);
        Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                Intent intent1 = new Intent(getApplicationContext(), WordPage.class);
                startActivity(intent1);
                break;
            case R.id.Btn:
                Intent intent2 = new Intent(getApplicationContext(), STTPage.class);
                startActivity(intent2);
                break;
        }
    }
}
