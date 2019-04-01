package com.broooapps.quotesapp.network;

import com.broooapps.quotesapp.model.Quote;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public interface FirebaseService {

    @GET("quotes.json")
    Call<HashMap<String, Quote>> fetchQuotes();
}
