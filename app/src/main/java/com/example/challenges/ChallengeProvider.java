package com.example.challenges;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChallengeProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.challenges.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/challenges");

    private List<String> challengeList = new ArrayList<>();

    @Override
    public boolean onCreate() {
        // Initialize default challenges
        challengeList.add("Do 10 push-ups");
        challengeList.add("Drink a glass of water");
        challengeList.add("Take a 5-minute walk");
        challengeList.add("Write a gratitude note");
        challengeList.add("Stretch for 3 minutes");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "challenge"});
        for (int i = 0; i < challengeList.size(); i++) {
            cursor.addRow(new Object[]{i, challengeList.get(i)});
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.example.challenges.challenge";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values != null && values.containsKey("challenge")) {
            challengeList.add(values.getAsString("challenge"));
            return ContentUris.withAppendedId(CONTENT_URI, challengeList.size() - 1);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        challengeList.clear();
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        if (values != null && values.containsKey("challenge") && selectionArgs != null && selectionArgs.length > 0) {
            int index = Integer.parseInt(selectionArgs[0]);
            if (index >= 0 && index < challengeList.size()) {
                challengeList.set(index, values.getAsString("challenge"));
                return 1;
            }
        }
        return 0;
    }
}