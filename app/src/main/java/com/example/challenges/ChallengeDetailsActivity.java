package com.example.challenges;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class ChallengeDetailsActivity extends AppCompatActivity {

    private TextView challengeDetailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        challengeDetailText = findViewById(R.id.challengeDetailText);

        // 1️⃣ Check if challenge was passed via Intent
        String challenge = getIntent().getStringExtra("challenge_text");

        // 2️⃣ Check for Deep Link (URL)
        if (challenge == null && getIntent().getData() != null) {
            Uri data = getIntent().getData(); // e.g., challenges://challenge/1
            if (data.getLastPathSegment() != null) {
                int index = Integer.parseInt(data.getLastPathSegment());
                List<String> defaultChallenges = Arrays.asList(
                        "Do 10 push-ups",
                        "Drink a glass of water",
                        "Take a 5-minute walk",
                        "Write a gratitude note",
                        "Stretch for 3 minutes"
                );
                if (index >= 0 && index < defaultChallenges.size()) {
                    challenge = defaultChallenges.get(index);
                }
            }
        }

        // Display challenge or fallback text
        if (challenge != null) {
            challengeDetailText.setText(challenge);
        } else {
            challengeDetailText.setText("Challenge not found!");
        }
    }
}