package com.horionDev.climbingapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import kotlin.Unit;

public class UnityParentActivity extends UnityPlayerActivity {
    protected void onCreate(Bundle savedInstanceState) {
        // Calls UnityPlayerActivity.onCreate()
        super.onCreate(savedInstanceState);
        // Prints debug message to Logcat
        Log.d("OverrideActivity", "onCreate called!");
        Intent intent = getIntent();
        String modelPath = intent.getStringExtra("MODEL_PATH");
        UnityPlayer.UnitySendMessage("ZoomObject", "ReceiveMessage", modelPath);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Vérifiez si l'utilisateur a touché l'écran
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Faites ce que vous voulez ici
            Log.d("CustomActivity", "Touch down detected!");
        }

        // Transmettez l'événement de toucher à Unity
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
            Log.d("CustomActivity", "Back button pressed!");
            finish();
    }

}
