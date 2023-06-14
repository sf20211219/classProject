package com.example.classproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private Button backBtn, testBtn, testEnd, rightBtn, leftBtn;
    private TextView number, textView;
    private ImageView imageView;
    private int count = 1;
    private static final String TAG = "TestActivity";

    private Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private List<Integer> shuffledList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        backBtn = findViewById(R.id.backBtn);
        testBtn = findViewById(R.id.testBtn);
        testEnd = findViewById(R.id.testEnd);
        number = findViewById(R.id.num);
        textView = findViewById(R.id.textView);
        rightBtn = findViewById(R.id.rightBtn);
        leftBtn = findViewById(R.id.leftBtn);
        imageView = findViewById(R.id.imageView);

        shuffledList = Arrays.asList(array);
        Collections.shuffle(shuffledList);

        backBtn.setOnClickListener(this);
        testBtn.setOnClickListener(this);
        testEnd.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);

        updateNumber();
        updateButtonVisibility();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.testBtn:
                String voiceResult = "음성인식 결과";  // 음성인식 결과를 가져옴
                String url = "http://서버주소/voice-result";
                sendRequestToServer(url, voiceResult);
                break;
            case R.id.testEnd:
                int index = count - 1;
                String[] words = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    int wordIndex = (i + index + 1 + array.length) % array.length;
                    words[i] = String.valueOf(shuffledList.get(wordIndex));
                }

                Intent intent = new Intent(getApplicationContext(), TestResult.class);
                intent.putExtra("words", words);
                startActivity(intent);
                break;
            case R.id.rightBtn:
                count++;
                if (count > array.length) {
                    count = 1;
                }
                updateNumber();
                updateButtonVisibility();
                break;
            case R.id.leftBtn:
                count--;
                if (count < 1) {
                    count = array.length;
                }
                updateNumber();
                updateButtonVisibility();
                break;
        }
    }

    private void updateNumber() {
        String countStr = String.format("%02d", count);
        number.setText(countStr);

        int index = count - 1;
        String url = "http://서버주소/btn" + shuffledList.get(index);
        sendRequestToServer(url, null);
    }

    private void sendRequestToServer(final String url, final String voiceResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL serverUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    if (voiceResult != null) {
                        byte[] postData = voiceResult.getBytes();
                        connection.getOutputStream().write(postData);
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String word = jsonObject.getString("word");
                            String imgUrl = jsonObject.getString("img_url");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(word);
                                    Glide.with(TestActivity.this).load(imgUrl).into(imageView);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e(TAG, "Server Request Failed. Response Code: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateButtonVisibility() {
        leftBtn.setVisibility(count == 1 ? View.INVISIBLE : View.VISIBLE);
        rightBtn.setVisibility(count == array.length ? View.INVISIBLE : View.VISIBLE);
        testEnd.setVisibility(count == array.length ? View.VISIBLE : View.INVISIBLE);
    }
}
