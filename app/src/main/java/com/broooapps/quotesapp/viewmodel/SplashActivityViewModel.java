package com.broooapps.quotesapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.sdsmdg.harjot.rotatingtext.models.Rotatable;


/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public class SplashActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> bgUrl;
    private MutableLiveData<String[]> rotatableText;


    public SplashActivityViewModel(@NonNull Application application) {
        super(application);

        bgUrl = new MutableLiveData<>();
        rotatableText = new MutableLiveData<>();
    }

    public MutableLiveData<String> getBgUrl() {
        bgUrl.postValue("https://images.unsplash.com/photo-1540508664702-7839b9d38075?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2755&q=80");
        return bgUrl;
    }

    public MutableLiveData<String[]> getRotatableText() {

        String[] arr = new String[3];
        arr[0] = "Motivating";
        arr[1] = "Inspiring";
        arr[2] = "Amazing";

        rotatableText.postValue(arr);
        return rotatableText;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
