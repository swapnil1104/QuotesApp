package com.broooapps.quotesapp.network;

import com.broooapps.quotesapp.model.Quote;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Swapnil Tiwari on 25/03/19.
 * swapnil.tiwari@box8.in
 */
public interface FBFunctionsService {

    @GET("/quotes")
    Call<List<Quote>> fetchRandQuotes();

    @GET("/quotes")
    Call<JSONArray> fetchQuotesJson();

}
