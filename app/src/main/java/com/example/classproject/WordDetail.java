package com.example.classproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class WordDetail extends AppCompatActivity implements View.OnClickListener {
    private Button backBtn, practiceBtn;
    private TextView tvData, wData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_word);

        int buttonId = getIntent().getIntExtra("buttonId", 0);

        backBtn = findViewById(R.id.backBtn);
        practiceBtn = findViewById(R.id.Btn);
        tvData = findViewById(R.id.textView);
        wData = findViewById(R.id.wordView);

        backBtn.setOnClickListener(this);
        practiceBtn.setOnClickListener(this);

        wData.setText(getIntent().getStringExtra("word"));
        tvData.setText(getIntent().getStringExtra("detail"));
    }

    @Override
    public void onClick(View v) {
        int buttonId = getIntent().getIntExtra("buttonId", 0);
        String word = getIntent().getStringExtra("word");
        String detail = getIntent().getStringExtra("detail");
        String voice = getIntent().getStringExtra("voice");

        Intent intent;
        switch (v.getId()) {
            case R.id.backBtn:
                intent = new Intent(this, WordListActivity.class);
                break;
            case R.id.Btn:
                intent = new Intent(this, WordPractice.class);
                intent.putExtra("buttonId", buttonId);
                intent.putExtra("word", word);
                intent.putExtra("detail", detail);
                intent.putExtra("voice", voice);
                break;
            default:
                return;
        }

        startActivity(intent);
    }
}
