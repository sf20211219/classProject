package com.example.classproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WordPage extends AppCompatActivity implements View.OnClickListener {
    private Button wordBtn[] = new Button[7];
    private Integer[] btnId = {
            R.id.wordBtn1, R.id.wordBtn2, R.id.wordBtn3, R.id.wordBtn4,
            R.id.wordBtn5, R.id.wordBtn6, R.id.wordBtn7
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_word);

        for(int i = 0; i <= 7; i++) {
            wordBtn[i] = (Button) findViewById(btnId[i]);
        }

        for(int i = 0; i < wordBtn.length; i++) {
            final int j = i;
            wordBtn[j].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        //버튼별 db 데이터 처리 화면 연결
    }
}
