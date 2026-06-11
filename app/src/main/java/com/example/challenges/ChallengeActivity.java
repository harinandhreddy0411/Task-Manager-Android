package com.example.challenges;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ChallengeActivity extends AppCompatActivity {

    TextView challengeText;
    Button generateBtn;

    String[] challenges = {
            "Do 10 pushups",
            "Drink 2 liters of water",
            "Read 10 pages of a book",
            "Meditate for 5 minutes"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        challengeText = findViewById(R.id.challengeText);
        generateBtn = findViewById(R.id.generateBtn);

        generateBtn.setOnClickListener(v -> {
            Random random = new Random();
            int index = random.nextInt(challenges.length);
            challengeText.setText(challenges[index]);
        });
    }
}