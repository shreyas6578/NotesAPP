package com.example.notesapp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LanguageToolService {

    @FormUrlEncoded
    @POST("v2/check")
    Call<GrammarResponse> checkGrammar(
            @Field("text") String text,  // The text to be checked
            @Field("language") String language // The language code (e.g., "en" for English)
    );

}
