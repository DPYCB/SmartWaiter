package com.dpycb.smartwaiter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextClock;
import android.widget.TextView;

import com.dpycb.smartwaiter.common.Common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WaitActivity extends AppCompatActivity {

    TextView textTimer;
    private long initialTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        textTimer = findViewById(R.id.textTimer);
        initialTime = Integer.parseInt(Common.currentRequest.getTime())*60000;
        new CountDownTimer(initialTime, 1000) {
            @Override
            public void onTick(long l) {
                initialTime = l;

                int mins = (int) initialTime / 60000;
                int secs = (int) initialTime % 60000 / 1000;
                String timeText = mins + ":";
                if (secs < 10) {
                    timeText += "0";
                }
                timeText += secs;
                textTimer.setText(timeText);
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WaitActivity.this, FinishActivity.class));
                finish();
            }
        }.start();
    }
}
