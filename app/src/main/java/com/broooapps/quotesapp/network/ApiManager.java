package com.broooapps.quotesapp.network;

import android.content.Context;

import com.broooapps.quotesapp.model.Quote;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public class ApiManager {
    private static final String API_ENDPOINT = "https://us-central1-quotesapp-9f2b7.cloudfunctions.net/api2/";

    private static final String API_ENDPOINT_FB = "";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
//            .addConverterFactory(JacksonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            .build();


    private static FirebaseService firebaseService = retrofit.create(FirebaseService.class);

    private static FBFunctionsService functionsService = retrofit.create(FBFunctionsService.class);

    public static Call<HashMap<String, Quote>> fetchQuotes() {
        return firebaseService.fetchQuotes();
    }

    public static Call<List<Quote>> fetchRandNotes() {
        return functionsService.fetchRandQuotes();
    }

    public static Call<JSONArray> fetchQuotesJson() {
        return functionsService.fetchQuotesJson();
    }

    public static void fetchQuotesOkHttp(Context context, OkHttpTaskListener listener) {

        try {
            OkHttpTask task = OkHttpTask.builder()
                    .setContext(context)
                    .setUrl(API_ENDPOINT + "quotes")
                    .setMethod(RequestMethod.GET)
                    .setRequestCode(1000)
                    .setHeader(new HashMap<String, String>())
                    .setCallback(listener)
                    .build();

            task.executeAsync("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
