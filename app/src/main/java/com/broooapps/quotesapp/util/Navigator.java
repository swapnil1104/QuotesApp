package com.broooapps.quotesapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.broooapps.quotesapp.view.MainActivity;

/**
 * Created by Swapnil Tiwari on 23/03/19.
 * swapnil.tiwari@box8.in
 */
public class Navigator {

    public static void mainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
