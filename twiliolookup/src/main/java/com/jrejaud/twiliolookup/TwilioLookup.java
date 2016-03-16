package com.jrejaud.twiliolookup;

import android.util.Base64;
import android.util.Log;

import com.jrejaud.twiliolookup.Networking.TwilioLookupResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jrejaud on 3/16/16.
 */
public class TwilioLookup {

    public interface OnResponseCallback{
        void onResponse(TwilioLookupResponse response);
        void onFailure(Throwable throwable);
    }

    private static final String BASE_URL = "https://lookups.twilio.com/v1/";
    private static String accountSid = null;
    private static String authToken = null;

    public static void setAuthorization(String accountSid, String authToken) {
        TwilioLookup.accountSid = accountSid;
        TwilioLookup.authToken = authToken;
    }

    public static void lookup(String phoneNumber, final OnResponseCallback onResponseCallback) {

        if (accountSid==null||authToken==null) {
            throw new RuntimeException("You need to set your accountSid and authToken via setAuthorization()");
        }

        //TODO move this to another place
        //Used to add basic authorization
        String username = accountSid;
        String password = authToken;

        String credentials = username + ":" + password;
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                //https://futurestud.io/blog/android-basic-authentication-with-retrofit
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization",basic)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        TwilioService twilioService = retrofit.create(TwilioService.class);

        Call<TwilioLookupResponse> response = twilioService.lookup(phoneNumber);

        response.enqueue(new Callback<TwilioLookupResponse>() {
            @Override
            public void onResponse(Call<TwilioLookupResponse> call, retrofit2.Response<TwilioLookupResponse> response) {
                onResponseCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<TwilioLookupResponse> call, Throwable t) {
                onResponseCallback.onFailure(t);
            }
        });
    }
}
