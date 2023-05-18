package com.example.classproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TestPage extends AppCompatActivity implements View.OnClickListener {
    private Button backBtn, testBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        backBtn = (Button) findViewById(R.id.backBtn);
        testBtn = (Button) findViewById(R.id.testBtn);

        backBtn.setOnClickListener(this);
        testBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.testBtn:
                //음성인식
                break;
        }
    }
}
