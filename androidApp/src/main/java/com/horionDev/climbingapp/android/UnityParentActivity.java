package com.horionDev.climbingapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import kotlin.Unit;


public class UnityParentActivity extends UnityPlayerActivity {

    private String modelPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            Intent intent = getIntent();
            modelPath = intent.getStringExtra("MODEL_PATH");
            Log.d("UnityParentActivity", "Sending message to Unity after delay");
            UnityPlayer.UnitySendMessage("Importer", "ReceiveMessage", modelPath);
        }, 10000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("unity", "unity callback onStart");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("CustomActivity", "Touch down detected!");
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CustomActivity", "Back button pressed!");
        finish();
    }

}
