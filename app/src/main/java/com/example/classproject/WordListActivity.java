package com.example.classproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class WordListActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[] wordBtn = new Button[10];
    private Button backBtn;
    private Integer[] btnId = {
            R.id.wordBtn1, R.id.wordBtn2, R.id.wordBtn3, R.id.wordBtn4, R.id.wordBtn5,
            R.id.wordBtn6, R.id.wordBtn7, R.id.wordBtn8, R.id.wordBtn9, R.id.wordBtn10
    };
    private static final String TAG = "ButtonOnClick";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_word);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        for (int i = 0; i < wordBtn.length; i++) {
            wordBtn[i] = findViewById(btnId[i]);
            wordBtn[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        int index = Arrays.asList(btnId).indexOf(buttonId);
        if (index != -1) {
            String url = "http://192.168.64.2:3000/btn" + (index + 1);
            String name = wordBtn[index].getText().toString();
            handleButtonClick(url, name, buttonId);
        } else if (buttonId == R.id.backBtn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void handleButtonClick(String url, String name, int buttonId) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                String requestBody = "{\"user_id\": \"" + url + "\", \"name\": \"" + name + "\"}";
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        Log.d(TAG, url);
                        try {
                            String word = new JSONObject(responseData).getString("word");
                            String voice = new JSONObject(responseData).getString("voice");
                            String detail = new JSONObject(responseData).getString("detail");
                            String videoUrl = new JSONObject(responseData).getString("videoUrl");

                            Log.d(TAG, videoUrl);
                            Intent intent = new Intent(WordListActivity.this, WordDetail.class);
                            intent.putExtra("buttonId", buttonId);
                            intent.putExtra("word", word);
                            intent.putExtra("detail", detail);
                            intent.putExtra("voice", voice);
                            intent.putExtra("videoData", videoUrl);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Log.d(TAG, "연결 안됨");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
