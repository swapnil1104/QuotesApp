package com.broooapps.quotesapp.network;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public interface RetrofitListener {

    void onSuccess(int requestCode, int responseCode, String response);

    void onFailure(int requestCode, int responseCode, String response);

}
