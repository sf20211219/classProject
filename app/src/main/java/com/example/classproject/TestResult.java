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

public class TestResult extends AppCompatActivity {
    private Button backBtn;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10;
    private TextView result1, result2, result3, result4, result5, result6, result7, result8, result9, result10;
    private static final String TAG = "TestResult";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_test);

        backBtn = findViewById(R.id.backBtn);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);

        Intent intent = getIntent();
        String[] words = intent.getStringArrayExtra("words");

        if (words != null) {
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                String apiUrl = "http://192.168.64.2:3000/btn" + word;
                sendRequestToServer(apiUrl, i);
            }
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendRequestToServer(final String url, final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL serverUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
                    connection.setRequestMethod("POST");

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        String responseString = response.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);

                            String word = jsonObject.getString("word");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView[] textViews = {
                                            textView1, textView2, textView3, textView4, textView5,
                                            textView6, textView7, textView8, textView9, textView10
                                    };
                                    if (index < textViews.length) {
                                        TextView textView = textViews[index];
                                        textView.setText(word);
                                    }
                                }
                            });
                            Log.d(TAG, "Word: " + word);
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
}
