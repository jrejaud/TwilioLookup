package com.jrejaud.twiliolookup;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jrejaud on 3/16/16.
 */
public class TwilioLookup {
    
    public interface OnResponseCallback{
        void onValidResponse(TwilioLookupResponse response);
        void onInvalidResponse(Response response);
        void onFailure(Throwable throwable);
    }

    private static final String BASE_URL = "https://lookups.twilio.com/v1/";
    private static String accountSid = null;
    private static String authToken = null;

    private static TwilioService twilioService;

    /** Set your Twilio AccountSid and AuthToken */
    public static void setAuthorization(String accountSid, String authToken) {
        TwilioLookup.accountSid = accountSid;
        TwilioLookup.authToken = authToken;

        String credentials = accountSid+ ":" + authToken;
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

        twilioService = retrofit.create(TwilioService.class);
    }

    /** Use Twilio to lookup if a number is valid or invalid */
    public static void lookup(String phoneNumber, final OnResponseCallback onResponseCallback) {

        if (accountSid==null||authToken==null) {
            throw new RuntimeException("You need to set your accountSid and authToken via setAuthorization()");
        }

        Call<TwilioLookupResponse> response = twilioService.lookup(phoneNumber);

        response.enqueue(new Callback<TwilioLookupResponse>() {
            @Override
            public void onResponse(Call<TwilioLookupResponse> call, retrofit2.Response<TwilioLookupResponse> response) {
                //This is a valid phone number
                if (response.code()==200) {
                    onResponseCallback.onValidResponse(response.body());

                } else if (response.code()==404) {
                    //This is an invalid phone number
                    onResponseCallback.onInvalidResponse(response.raw());
                }
            }

            @Override
            public void onFailure(Call<TwilioLookupResponse> call, Throwable t) {
                onResponseCallback.onFailure(t);
            }
        });
    }
}
