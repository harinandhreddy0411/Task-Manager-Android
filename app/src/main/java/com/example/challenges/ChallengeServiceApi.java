package com.example.challenges;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChallengeServiceApi {
    @GET("/challenges")  // Replace with your actual endpoint
    Call<List<String>> getChallenges();
}