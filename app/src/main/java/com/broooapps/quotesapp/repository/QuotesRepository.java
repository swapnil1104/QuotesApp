package com.broooapps.quotesapp.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.util.JsonReader;
import android.util.Log;

import com.broooapps.quotesapp.model.Quote;
import com.broooapps.quotesapp.network.ApiManager;
import com.broooapps.quotesapp.network.FBFunctionsService;
import com.broooapps.quotesapp.network.FirebaseService;
import com.broooapps.quotesapp.network.OkHttpTask;
import com.broooapps.quotesapp.network.OkHttpTaskListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Swapnil Tiwari on 24/03/19.
 * swapnil.tiwari@box8.in
 */
public class QuotesRepository {
    private FBFunctionsService service;
    private Application application;
    private static QuotesRepository instance;
    MutableLiveData<List<Quote>> quoteMutableLiveData;


    public static QuotesRepository getInstance(Application application) {
        if (instance == null) {
            instance = new QuotesRepository(application);
        }

        return instance;
    }

    private QuotesRepository(Application application) {
        this.application = application;
        service = ApiManager.retrofit.create(FBFunctionsService.class);
    }

    public MutableLiveData<List<Quote>> fetchQuotes() {

        if (quoteMutableLiveData == null) {
            quoteMutableLiveData = new MutableLiveData<>();

            Log.v("Api call", "fetchQuotes");

            ApiManager.fetchQuotesOkHttp(application.getApplicationContext(), new OkHttpTaskListener() {
                @Override
                public void onTaskComplete(OkHttpTask task, String result, int resultCode) {
                    String jsonString = result;
                    Log.i("onResponse", jsonString);
                    List<Quote> quoteList = new Gson().fromJson(jsonString,
                            new TypeToken<List<Quote>>(){}.getType());

                    quoteMutableLiveData.postValue(quoteList);
                }

                @Override
                public void noAvailableInternetConnection(String result, int resultCode) {

                }
            });

            /*Call<HashMap<String, Quote>> quotesCall = ApiManager.fetchQuotes();
            quotesCall.enqueue(new Callback<HashMap<String, Quote>>() {
                @Override
                public void onResponse(Call<HashMap<String, Quote>> call, Response<HashMap<String, Quote>> response) {
                    List<Quote> quoteList = new ArrayList<>();
                    Iterator it = response.body().entrySet().iterator();
                    while (it.hasNext()) {
                        quoteList.add((Quote) ((Map.Entry) it.next()).getValue());
                    }
                    quoteMutableLiveData.postValue(quoteList);
                }

                @Override
                public void onFailure(Call<HashMap<String, Quote>> call, Throwable t) {

                }
            });*/
        }
        return quoteMutableLiveData;
    }

    public MutableLiveData<List<Quote>> refreshQuotes() {
        /*Call<HashMap<String, Quote>> quotesCall = ApiManager.fetchQuotes();
        quotesCall.enqueue(new Callback<HashMap<String, Quote>>() {
            @Override
            public void onResponse(Call<HashMap<String, Quote>> call, Response<HashMap<String, Quote>> response) {
                List<Quote> quoteList = new ArrayList<>();
                Iterator it = response.body().entrySet().iterator();
                while (it.hasNext()) {
                    quoteList.add((Quote) ((Map.Entry) it.next()).getValue());
                }
                quoteMutableLiveData.postValue(quoteList);
            }

            @Override
            public void onFailure(Call<HashMap<String, Quote>> call, Throwable t) {

            }
        });*/


        ApiManager.fetchQuotesOkHttp(application.getApplicationContext(), new OkHttpTaskListener() {
            @Override
            public void onTaskComplete(OkHttpTask task, String result, int resultCode) {
                String jsonString = result;
                Log.i("onResponse", jsonString);
                List<Quote> quoteList = new Gson().fromJson(jsonString,
                        new TypeToken<List<Quote>>(){}.getType());

                quoteMutableLiveData.postValue(quoteList);
            }

            @Override
            public void noAvailableInternetConnection(String result, int resultCode) {

            }
        });
        return quoteMutableLiveData;
    }
}
