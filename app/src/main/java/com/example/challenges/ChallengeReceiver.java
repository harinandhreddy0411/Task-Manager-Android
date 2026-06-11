package com.example.challenges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ChallengeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Receive the challenge text from broadcast
        String challenge = intent.getStringExtra("challenge_text");
        if (challenge != null) {
            // Show a Toast message (you can also log or do other actions)
            Toast.makeText(context, "New Challenge: " + challenge, Toast.LENGTH_SHORT).show();
        }
    }
}