package com.broooapps.quotesapp.network;

/**
 * Created by Swapnil Tiwari on 25/03/19.
 * swapnil.tiwari@box8.in
 */


import android.app.Activity;
import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class OkHttpTask {
    public static final String RESPONSE = "OkHttp Response";
    public static final String REQUEST = "OkHttp Request";
    public static final String ERR = "OkHttp Err";


    public static final int CASE_UNAUTHORIZED = 498;
    public static int REQ_SYNC = 999;
    private static final String TAG = OkHttpTask.class.getSimpleName();
    private int mRequestCode;
    private long mConnectTimeout = 0;
    private long mReadTimeout = 0;
    private long mWriteTimeout = 0;

    private Call call;
    private FormBody formBody;
    private static OkHttpClient client;
    private Request.Builder requestBuilder;
    private Request request;

    private Context mContext;
    private OkHttpTaskListener mCallback;
    private RequestMethod mMethod;
    private String mUrl;
    private HashMap<String, String> mHeader = new HashMap<>();
    private String mDialogMessage;
    private boolean mShowDialog;
    private boolean mCancellable;
    private boolean mSynchronous;
    private boolean mUnauthorized;
    private String result = "";
    private String input;
    private RequestBody requestBody;
    private boolean isRestarted;
    public static boolean mShowLog = false;

    public static void getInstance(boolean enableLogging) {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        mShowLog = enableLogging;
    }

    private OkHttpTask(Builder builder) {
        this.mMethod = builder.method;
        this.mUrl = builder.url;
        this.mRequestCode = builder.requestCode;
        this.mContext = builder.context;
        this.mCallback = builder.callback;
        this.mHeader = builder.header;
        this.mShowDialog = builder.showDialog;
        this.mDialogMessage = builder.dialogMessage;
        this.mCancellable = builder.cancellable;
        this.mConnectTimeout = builder.connectTimeout;
        this.mWriteTimeout = builder.writeTimeout;
        this.mReadTimeout = builder.readTimeout;
        this.mSynchronous = builder.synchronous;

        if (builder.overrideTimeOut) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(mConnectTimeout, TimeUnit.SECONDS)
                    .writeTimeout(mWriteTimeout, TimeUnit.SECONDS)
                    .readTimeout(mReadTimeout, TimeUnit.SECONDS)
                    .build();
        }

        requestBuilder = new Request.Builder();
        Iterator<HashMap<String, String>> headerEntries = ((HashMap) mHeader).entrySet().iterator();
        while (headerEntries.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) headerEntries.next();
            requestBuilder.addHeader((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public String executeSync(String json) throws IOException, NetworkOnMainThreadException {
        mSynchronous = true;

        final MediaType JSON = MediaType.parse("application/json");

        return executeInternal(RequestBody.create(JSON, json));
    }

    public String executeSync(ArrayList<HashMap<String, String>> params) throws IOException, NetworkOnMainThreadException {
        mSynchronous = true;

        FormBody.Builder formBody = new FormBody.Builder();
        HashMap hashMap = params.get(0);
        Iterator<HashMap<String, String>> entries = hashMap.entrySet().iterator();

        while (entries.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) entries.next();
            formBody.add((String) entry.getKey(), (String) entry.getValue());
        }

        return executeInternal(formBody.build());
    }

    public void executeAsync(String json) throws IOException {
        input = json;
        final MediaType JSON
                = MediaType.parse("application/json");

        executeInternal(RequestBody.create(JSON, json));
    }

    public void executeAsync(ArrayList<HashMap<String, String>> params) throws IOException {
        FormBody.Builder formBody = new FormBody.Builder();
        HashMap hashMap = params.get(0);
        Iterator<HashMap<String, String>> entries = hashMap.entrySet().iterator();

        while (entries.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) entries.next();
            formBody.add((String) entry.getKey(), (String) entry.getValue());
        }

        executeInternal(formBody.build());
    }

    private String executeInternal(RequestBody requestBody) throws IOException {
        this.requestBody = requestBody;

        switch (mMethod) {
            case GET:
                request = requestBuilder
                        .url(mUrl)
                        .build();
                break;
            case PUT:
                request = requestBuilder
                        .url(mUrl)
                        .put(requestBody)
                        .build();
                break;
            case POST:
                request = requestBuilder
                        .url(mUrl)
                        .post(requestBody)
                        .build();
                break;
            case PATCH:
                request = requestBuilder
                        .url(mUrl)
                        .patch(requestBody)
                        .build();

                break;
            case DELETE:
                request = requestBuilder
                        .url(mUrl)
                        .delete()
                        .build();
                break;
        }

        if (mShowLog)
            Log.v(REQUEST,"Context: " + getClassName() + request.toString() +
                    " Headers: " + ((mHeader.toString() != "") ? mHeader.toString() : "No Headers Found") +
                    " Body: " + ((request != null) ? bodyToString(request) : "Empty body"));


        if (mSynchronous) {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (mShowLog) Log.v(RESPONSE, "Context: " + getClassName() + res);
            response.close();
            return res;
        } else {
            call = client.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mShowLog) Log.v(RESPONSE, "Context: " + getClassName() + ERR + e.getMessage());
                                if (mCallback != null) {
                                    mCallback.noAvailableInternetConnection("Please check your internet connection and try again", mRequestCode);
                                }
                            }
                        });
                    } else {
                        if (mCallback != null)
                            mCallback.noAvailableInternetConnection("Please check your internet connection and try again", mRequestCode);
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                    if (response.body() != null) {
                        result = response.body().string();
                    }
                    if (mShowLog) Log.v(RESPONSE, "Context: " + getClassName() + result);
                    if (checkUnauthorized(result) && !isRestarted) {
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mCallback != null) {
                                        mCallback.onTaskComplete(OkHttpTask.this, result, CASE_UNAUTHORIZED);
                                    }
                                }
                            });
                        } else {
                            if (mCallback != null)
                                mCallback.onTaskComplete(OkHttpTask.this, result, CASE_UNAUTHORIZED);
                        }
                    } else {
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mCallback != null) {
                                        mCallback.onTaskComplete(OkHttpTask.this, result, mRequestCode);
                                    }
                                }
                            });
                        } else {
                            if (mCallback != null)
                                mCallback.onTaskComplete(OkHttpTask.this, result, mRequestCode);
                        }
                    }
                }

            });
        }
        return "";
    }

    private boolean checkUnauthorized(String result) {
        try {
            JSONObject resultObject = new JSONObject(result);
            if (resultObject.getJSONObject("meta").get("code").equals(498)) {
                return true;
            }
        } catch (JSONException e) {
            if (mShowLog) Log.v(RESPONSE, "Context: " + getClassName() + ERR + e.getMessage());
        }
        return false;
    }


    public static IContext builder() {
        return new Builder();
    }

    public static class Builder implements IListener, IRequestCode, IUrl, IMethod, IHeaders, IContext,
            ISetDialogMessage, IShowDialog {
        private ArrayList<HashMap<String, String>> temp_param = new ArrayList<>();
        private boolean showDialog;
        private boolean synchronous;
        private boolean cancellable;
        private int requestCode;
        private Context context;
        private OkHttpTaskListener callback;
        private HashMap<String, String> header = new HashMap<>();
        private long writeTimeout = 20;
        private long connectTimeout = 20;
        private long readTimeout = 20;
        private RequestMethod method;
        private String url;
        private String dialogMessage;
        private boolean showLog = false;
        boolean overrideTimeOut = false;

        @Override
        public IUrl setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        public IMethod setUrl(String url) {
            this.url = url;
            return this;
        }

        @Override
        public IRequestCode setMethod(RequestMethod method) {
            this.method = method;
            return this;
        }

        @Override
        public IHeaders setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        @Override
        public IListener setHeader(HashMap<String, String> headers) {
            this.header = headers;
            return this;
        }

        @Override
        public Builder setCallback(OkHttpTaskListener callback) {
            this.callback = callback;
            return this;
        }


        public Builder setTempParams(ArrayList<HashMap<String, String>> temp_param) {
            this.temp_param = temp_param;
            return this;
        }

        public Builder setCancellable(boolean cancellable) {
            this.cancellable = cancellable;
            return this;
        }

        public Builder setTimeOutTimeout(long connectTimeout, long readTimeout, long writeTimeout) {
            overrideTimeOut = true;
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder setSynchronous(boolean synchronous) {
            this.synchronous = synchronous;
            return this;
        }

        public OkHttpTask build() {
            return new OkHttpTask(this);
        }

        @Override
        public ISetDialogMessage showDialog(boolean show) {
            this.showDialog = show;
            return this;
        }

        @Override
        public Builder setDialogMessage(String message) {
            this.dialogMessage = message;
            return this;
        }
    }

    public interface IContext {
        IUrl setContext(Context context);
    }

    public interface IUrl {
        IMethod setUrl(String url);
    }

    public interface IMethod {
        IRequestCode setMethod(RequestMethod method);
    }

    public interface IRequestCode {
        IHeaders setRequestCode(int requestCode);
    }

    public interface IHeaders {
        IListener setHeader(HashMap<String, String> headers);
    }

    public interface IListener {
        Builder setCallback(OkHttpTaskListener callback);
    }

    public interface IShowDialog {
        ISetDialogMessage showDialog(boolean show);
    }

    public interface ISetDialogMessage {
        Builder setDialogMessage(String message);
    }

    public void restartTask(HashMap<String, String> headers) {
        try {
            requestBuilder = new Request.Builder();
            Iterator<HashMap<String, String>> headerEntries = ((HashMap) headers).entrySet().iterator();
            while (headerEntries.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) headerEntries.next();
                requestBuilder.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
            isRestarted = true;
            executeInternal(requestBody);
        } catch (IOException e) {
            if (mShowLog) Log.v(RESPONSE,"Context: " + getClassName() +  ERR + e.getMessage());
        }
    }

    private static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException | NullPointerException e) {
            return " Empty body";
        }
    }

    private String getClassName() {
        if (mCallback != null) {
            return " " + mCallback.getClass().getSimpleName() + " ";
        } else {
            return " nil ";
        }
    }
}

