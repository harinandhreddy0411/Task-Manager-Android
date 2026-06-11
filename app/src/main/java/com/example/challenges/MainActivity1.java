package com.example.challenges;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    private TextView challengeText, greetingText;
    private EditText nameInput;
    private Button startBtn, nextBtn, shareBtn;

    private List<String> challenges = new ArrayList<>();
    private int currentIndex = 0;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "secure_prefs";
    private static final String KEY_INDEX = "challenge_index";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        challengeText = findViewById(R.id.challengeText);
        greetingText = findViewById(R.id.greetingText);
        nameInput = findViewById(R.id.nameInput);
        startBtn = findViewById(R.id.startBtn);
        nextBtn = findViewById(R.id.nextChallengeBtn);
        shareBtn = findViewById(R.id.shareChallengeBtn);

        // Initialize EncryptedSharedPreferences
        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    this,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            Log.d("PrefsCheck", "EncryptedSharedPreferences initialized.");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Secure storage not available", Toast.LENGTH_LONG).show();
            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        }

        // Load username if saved
        if (sharedPreferences != null) {
            String savedName = sharedPreferences.getString(KEY_USERNAME, "");
            Log.d("PrefsCheck", "Loaded username: " + savedName);
            if (!savedName.isEmpty()) {
                nameInput.setText(savedName);
                greetingText.setText("Hello, " + savedName + "!");
            }
        }

        // Sample challenges
        challenges.add("Do 10 pushups");
        challenges.add("Drink 2 liters of water");
        challenges.add("Read 10 pages of a book");
        challenges.add("Go for a 15-minute walk");

        if (sharedPreferences != null) {
            currentIndex = sharedPreferences.getInt(KEY_INDEX, 0);
        }

        // Start foreground service
        Intent serviceIntent = new Intent(this, ChallengeService.class);
        ContextCompat.startForegroundService(this, serviceIntent);

        startBtn.setOnClickListener(v -> {
            String username = nameInput.getText().toString().trim();

            if (sharedPreferences != null) {
                sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
                Toast.makeText(this, "Saved username: " + username, Toast.LENGTH_SHORT).show();
            }

            greetingText.setText("Hello, " + username + "!");

            showChallenge();
        });

        nextBtn.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % challenges.size();

            if (sharedPreferences != null) {
                sharedPreferences.edit().putInt(KEY_INDEX, currentIndex).apply();
            }

            showChallenge();

            Intent broadcastIntent = new Intent("com.example.NEW_CHALLENGE");
            broadcastIntent.putExtra("challenge", challenges.get(currentIndex));
            sendBroadcast(broadcastIntent);
        });

        shareBtn.setOnClickListener(v -> shareChallenge());
    }

    private void showChallenge() {
        if (challenges.isEmpty()) {
            challengeText.setText("No challenges available");
        } else {
            challengeText.setText(challenges.get(currentIndex));
        }
    }

    private void shareChallenge() {
        try {
            String text = challengeText.getText().toString();
            File file = new File(getCacheDir(), "challenge.txt");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(text.getBytes());
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(
                            this,
                            "com.example.challenges.fileprovider",
                            file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Share Challenge"));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error sharing challenge", Toast.LENGTH_SHORT).show();
        }
    }
}