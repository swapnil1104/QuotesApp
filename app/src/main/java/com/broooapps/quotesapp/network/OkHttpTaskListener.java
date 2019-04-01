package com.broooapps.quotesapp.network;

/**
 * Created by Swapnil Tiwari on 25/03/19.
 * swapnil.tiwari@box8.in
 */
public interface OkHttpTaskListener {

    void onTaskComplete(OkHttpTask task, String result, int resultCode);

    void noAvailableInternetConnection(String result, int resultCode);

}
