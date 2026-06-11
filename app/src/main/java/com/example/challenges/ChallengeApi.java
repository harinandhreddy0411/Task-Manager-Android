package com.example.challenges;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ChallengeApi {
    @GET("f47d8b28-4d92-4e63-8df1-c6d9117f2cbb")
    Call<List<Challenge>> getChallenges();
}