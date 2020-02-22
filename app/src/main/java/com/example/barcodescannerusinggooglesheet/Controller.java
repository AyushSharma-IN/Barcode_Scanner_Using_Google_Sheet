package com.example.barcodescannerusinggooglesheet;

import android.util.Log;

import androidx.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Controller {
    public static final String TAG = "TAG";

    public static final String WAURL="Enter Your Script Launched URL";
    private static Response response;

    public static JSONObject read() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=read")
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "Receiving Null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject insert(String barcode) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=insert&barcode="+barcode)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "Receiving Null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject deleteData(String id) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=delete&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Receiving Null " + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject deleteAll() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(WAURL+"action=deleteAll")
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Receiving Null " + e.getLocalizedMessage());
        }
        return null;
    }
}